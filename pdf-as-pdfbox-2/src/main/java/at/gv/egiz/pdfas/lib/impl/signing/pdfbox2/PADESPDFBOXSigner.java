/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
package at.gv.egiz.pdfas.lib.impl.signing.pdfbox2;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDNumberTreeNode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.exception.ValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.DomXmpParser;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.messages.MessageResolver;
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings;
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.impl.ErrorExtractor;
import at.gv.egiz.pdfas.lib.impl.SignaturePositionImpl;
import at.gv.egiz.pdfas.lib.impl.configuration.SignatureProfileConfiguration;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.PDFBOXObject;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.positioning.Positioning;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.utils.PdfBoxUtils;
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderFilter;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import at.gv.egiz.pdfas.lib.impl.signing.IPdfSigner;
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureExtractor;
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureInterface;
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFStamper;
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFVisualObject;
import at.gv.egiz.pdfas.lib.impl.stamping.TableFactory;
import at.gv.egiz.pdfas.lib.impl.stamping.ValueResolver;
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2.PDFAsVisualSignatureProperties;
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2.PdfBoxVisualObject;
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2.StamperFactory;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import at.gv.egiz.pdfas.lib.impl.status.PDFObject;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction;
import at.knowcenter.wag.egov.egiz.pdf.TablePos;
import at.knowcenter.wag.egov.egiz.table.Table;
import iaik.x509.X509Certificate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PADESPDFBOXSigner implements IPdfSigner, IConfigurationConstants {



  @Override
  public void signPDF(PDFObject genericPdfObject, RequestedSignature requestedSignature,
      PDFASSignatureInterface genericSigner) throws PdfAsException {

    
    boolean isAdobeSigForm = false;
    
    if (!(genericPdfObject instanceof PDFBOXObject)) {
      throw new PdfAsException("PDF to signObject is of wrong type: " + genericPdfObject.getClass().getName());
      
    }

    if (!(genericSigner instanceof PDFASPDFBOXSignatureInterface)) {
      throw new PdfAsException("PDF signerObject is of wrong type:" + genericSigner.getClass().getName());
      
    }
    
    final PDFBOXObject pdfObject = (PDFBOXObject) genericPdfObject;
    final PDFASPDFBOXSignatureInterface signer = (PDFASPDFBOXSignatureInterface) genericSigner;

    PDDocument doc = null;
    SignatureOptions options = new SignatureOptions();
    try {
      doc = pdfObject.getDocument();
      

      // sign a PDF with an existing empty signature, as created by the
      // CreateEmptySignatureForm example.
      PDSignature signature = findExistingSignature(doc, getSignatureFieldNameConfig(pdfObject)); 
      if (signature == null) {
        // create signature dictionary
        signature = new PDSignature();
        
      } else {
        isAdobeSigForm = true;
        
      }

      // set basic signature parameters
      signature.setFilter(COSName.getPDFName(signer.getPDFFilter()));
      signature.setSubFilter(COSName.getPDFName(signer.getPDFSubFilter()));
      signature.setSignDate(Calendar.getInstance());
      log.debug("Signing @ " + signature.getSignDate().getTime().toString());
      
      // extract next QR-code placeholder, if exists 
      SignaturePlaceholderData nextPlaceholderData = PlaceholderFilter.checkPlaceholderSignatureLocation(
          pdfObject.getStatus(), pdfObject.getStatus().getSettings(), 
          pdfObject.getStatus().getSignParamter().getPlaceHolderId());

      if (nextPlaceholderData != null) {
        log.info("Placeholder data found.");
        signature.setLocation(nextPlaceholderData.getPlaceholderName());
                     
        
        // TODO: only over-write if requested
        if (nextPlaceholderData.getProfile() != null) {                    
          log.debug("Placeholder Profile set to: {}", nextPlaceholderData.getProfile());
          requestedSignature.setSignatureProfileID(nextPlaceholderData.getProfile());
          
        }
      }
      
                
      final SignatureProfileSettings signatureProfileSettings = 
          TableFactory.createProfile(requestedSignature.getSignatureProfileID(), pdfObject.getStatus().getSettings());
       
      // set signature name
      final ValueResolver resolver = new ValueResolver(requestedSignature, pdfObject.getStatus());
      final String signerName = resolver.resolve("SIG_SUBJECT", 
          signatureProfileSettings.getValue("SIG_SUBJECT"), signatureProfileSettings);
      signature.setName(signerName);

      // set signature reason
      String signerReason = signatureProfileSettings.getSigningReason() != null 
          ? signatureProfileSettings.getSigningReason() : "PAdES Signature";
      signature.setReason(signerReason);
      log.debug("Signing reason: " + signerReason);

      signer.setPDSignature(signature);

      
      if (signatureProfileSettings.isPDFA() || signatureProfileSettings.isPDFA3()) {
        signatureProfileSettings.setPDFAVersion(getPDFAVersion(doc));
        
      }

      // prepare basic signature options
      options.setPreferredSignatureSize(calculateBlankAreaForSignature(signatureProfileSettings));

            
      PDFAsVisualSignatureProperties properties = null;
      if (requestedSignature.isVisual()) {                
        log.info("Creating visual siganture block");
        
        final SignatureProfileConfiguration signatureProfileConfiguration = 
            pdfObject.getStatus().getSignatureProfileConfiguration(requestedSignature.getSignatureProfileID());
        final TablePos tablePos = prepareTablePosition(nextPlaceholderData, signatureProfileConfiguration,
            pdfObject.getStatus().getSignParamter().getSignaturePosition());
        final Table main = TableFactory.createSigTable(signatureProfileSettings, MAIN, pdfObject.getStatus(),
            requestedSignature);

        final IPDFStamper stamper = StamperFactory.createDefaultStamper(pdfObject.getStatus().getSettings());
        final IPDFVisualObject visualObject = stamper.createVisualPDFObject(pdfObject, main);

        final PositioningInstruction positioningInstruction = Positioning.determineTablePositioning(tablePos,
            doc, visualObject, pdfObject.getStatus().getSettings(), signatureProfileSettings);
        log.debug("Positioning: {}", positioningInstruction.toString());

        if (!isAdobeSigForm) {
          if (positioningInstruction.isMakeNewPage()) {
            final int last = doc.getNumberOfPages() - 1;
            final PDDocumentCatalog root = doc.getDocumentCatalog();
            final PDPage lastPage = root.getPages().get(last);
            root.getPages().getCOSObject().setNeedToBeUpdated(true);
            final PDPage p = new PDPage(lastPage.getMediaBox());
            p.setResources(new PDResources());
            p.setRotation(lastPage.getRotation());
            doc.addPage(p);
          }

          // handle rotated page
          final int targetPageNumber = positioningInstruction.getPage();
          log.debug("Target Page: " + targetPageNumber);
          final PDPage targetPage = doc.getPages().get(targetPageNumber - 1);
          final int rot = targetPage.getRotation();
          log.debug("Page rotation: " + rot);
          log.debug("resulting Sign rotation: " + positioningInstruction.getRotation());

          final SignaturePositionImpl position = new SignaturePositionImpl();
          position.setX(positioningInstruction.getX());
          position.setY(positioningInstruction.getY());
          position.setPage(positioningInstruction.getPage());
          position.setHeight(visualObject.getHeight());
          position.setWidth(visualObject.getWidth());
          requestedSignature.setSignaturePosition(position);
        }
                
        properties = new PDFAsVisualSignatureProperties(pdfObject.getStatus().getSettings(), pdfObject,
            (PdfBoxVisualObject) visualObject, positioningInstruction, signatureProfileSettings);
        properties.buildSignature();

        if (signatureProfileSettings.isPDFA() || signatureProfileSettings.isPDFA3()) {
          final PDDocumentCatalog root = doc.getDocumentCatalog();

          InputStream colorProfile = null;
          // colorProfile = this.getClass().getResourceAsStream("/icm/sRGB.icm");
          colorProfile = this.getClass().getResourceAsStream("/icm/sRGB Color Space Profile.icm");
          // Set output intents for PDF/A conformity//
          try {
            final PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);

            intent.setInfo("sRGB IEC61966-2.1");
            intent.setOutputCondition("sRGB IEC61966-2.1");
            intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
            intent.setRegistryName("http://www.color.org");
            final List<PDOutputIntent> oi = new ArrayList<>();
            oi.add(intent);
            root.setOutputIntents(oi);
            root.getCOSObject().setNeedToBeUpdated(true);

            log.info("added Output Intent");
          } catch (final Throwable e) {
            e.printStackTrace();
            throw new PdfAsException("Failed to add Output Intent", e);
            
          } finally {
            IOUtils.closeQuietly(colorProfile);
            
          }
        }
        
        options.setPage(positioningInstruction.getPage() - 1);
        options.setVisualSignature(properties.getVisibleSignature());
        
      }

      doc.addSignature(signature, signer, options);
      
      String sigFieldName = buildNextSignatureFieldName(doc, pdfObject);
      
      // this is not used for Adobe signature fields
      if (!isAdobeSigForm) {
        PDSignatureField signatureField = null;
        final PDAcroForm acroFormm = doc.getDocumentCatalog().getAcroForm();
        if (acroFormm != null) {
          @SuppressWarnings("unchecked")
          final List<PDField> fields = acroFormm.getFields();

          if (fields != null) {
            for (final PDField pdField : fields) {
              if (pdField != null) {
                if (pdField instanceof PDSignatureField) {
                  final PDSignatureField tmpSigField = (PDSignatureField) pdField;

                  if (tmpSigField.getSignature() != null
                      && tmpSigField.getSignature().getCOSObject() != null) {
                    if (tmpSigField.getSignature().getCOSObject()
                        .equals(signature.getCOSObject())) {
                      signatureField = (PDSignatureField) pdField;

                    }
                  }
                }
              }
            }
          } else {
            log.warn("Failed to name Signature Field! [Cannot find Field list in acroForm!]");
          }

          if (signatureField != null) {
            signatureField.setPartialName(sigFieldName);
          }
          if (properties != null) {
            signatureField.setAlternateFieldName(properties.getAlternativeTableCaption());
          } else {
            signatureField.setAlternateFieldName(sigFieldName);
          }
        } else {
          log.warn("Failed to name Signature Field! [Cannot find acroForm!]");
          
        }
      }

      PDSignatureField signatureField = null;
      final PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
      if (acroForm != null) {
        signatureField = (PDSignatureField) acroForm.getField(sigFieldName);
        
      }


      injectPdfUaContent(doc, signatureField, sigFieldName, signatureProfileSettings);
      
      performTechnicalSignature(doc, pdfObject, signatureProfileSettings);


      log.debug("Signature done!");
      
    } catch (final IOException e) {
      log.warn(MessageResolver.resolveMessage("error.pdf.sig.01"), e);
      throw new PdfAsException("error.pdf.sig.01", e);
    
    } catch (PDFASError e2) {
      log.warn(e2.getInfo());
      throw new PdfAsException("error.pdf.sig.01", e2);
      
    } finally {
      if (options != null) {
        if (options.getVisualSignature() != null) {
          try {
            options.getVisualSignature().close();
            options.close();
          } catch (IOException e) {
            log.debug("Failed to close VisualSignature!", e);
          }
        }
      }
      
      if (doc != null) {
        try {
          doc.close();
          // SignaturePlaceholderExtractor.getPlaceholders().clear();
        } catch (final IOException e) {
          log.debug("Failed to close COS Doc!", e);
          // Ignore
        }
      }
    }
  }

  private void performTechnicalSignature(PDDocument doc, PDFBOXObject pdfObject, 
      SignatureProfileSettings signatureProfileSettings) throws PdfAsException {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      synchronized (doc) {
        doc.saveIncremental(bos);
        final byte[] outputDocument = bos.toByteArray();
        pdfObject.setSignedDocument(outputDocument);
      }
      /* Check if resulting pdf is PDF-A conform */
      if (signatureProfileSettings.isPDFA()) {
        runPDFAPreflight(new ByteArrayDataSource(pdfObject.getSignedDocument()));
      }

    } catch (final IOException e1) {
      log.error("Can not save incremental update", e1);

    }
    
    System.gc();
    
  }

  private void injectPdfUaContent(PDDocument doc, PDSignatureField signatureField, String sigFieldName, 
      SignatureProfileSettings signatureProfileSettings) throws PdfAsException {
    try {
      log.info("Adding pdf/ua content .... ");
      final PDDocumentCatalog root = doc.getDocumentCatalog();
      final PDStructureTreeRoot structureTreeRoot = root.getStructureTreeRoot();
      if (structureTreeRoot != null) {
        log.info("Tree Root: {}", structureTreeRoot.toString());
        final List<Object> kids = structureTreeRoot.getKids();

        if (kids == null) {
          log.info("No kid-elements in structure tree Root, maybe not PDF/UA document");
        }

        PDStructureElement docElement = null;
        for (final Object k : kids) {
          if (k instanceof PDStructureElement) {
            docElement = (PDStructureElement) k;
            break;

          }
        }

        final PDStructureElement sigBlock = new PDStructureElement("Form", docElement);

        // create object dictionary and add as child element
        final COSDictionary objectDic = new COSDictionary();
        objectDic.setName("Type", "OBJR");

        objectDic.setItem("Pg", signatureField.getWidget().getPage());
        objectDic.setItem("Obj", signatureField.getWidget());

        final List<Object> l = new ArrayList<>();
        l.add(objectDic);
        sigBlock.setKids(l);
        sigBlock.setPage(signatureField.getWidget().getPage());

        sigBlock.setTitle("Signature Table");
        sigBlock.setParent(docElement);
        docElement.appendKid(sigBlock);

        // Create and add Attribute dictionary to mitigate PAC
        // warning
        final COSDictionary sigBlockDic = sigBlock.getCOSObject();
        final COSDictionary sub = new COSDictionary();

        sub.setName("O", "Layout");
        sub.setName("Placement", "Block");
        sigBlockDic.setItem(COSName.A, sub);
        sigBlockDic.setNeedToBeUpdated(true);

        // Modify number tree
        PDNumberTreeNode ntn = structureTreeRoot.getParentTree();
        if (ntn == null) {
          ntn = new PDNumberTreeNode(objectDic, null);
          log.info("No number-tree-node found!");
        }

        final COSArray ntnKids = (COSArray) ntn.getCOSObject().getDictionaryObject(COSName.KIDS);
        final COSArray ntnNumbers = (COSArray) ntn.getCOSObject().getDictionaryObject(COSName.NUMS);

        final int parentTreeNextKey = getParentTreeNextKey(structureTreeRoot);

        if (ntnNumbers == null && ntnKids != null) {// no number array, so continue with the kids array
          // create dictionary with limits and nums array
          final COSDictionary pTreeEntry = new COSDictionary();
          final COSArray limitsArray = new COSArray();
          // limits for exact one entry
          limitsArray.add(COSInteger.get(parentTreeNextKey));
          limitsArray.add(COSInteger.get(parentTreeNextKey));

          final COSArray numsArray = new COSArray();
          numsArray.add(COSInteger.get(parentTreeNextKey));
          numsArray.add(sigBlock);

          pTreeEntry.setItem(COSName.NUMS, numsArray);
          pTreeEntry.setItem(COSName.LIMITS, limitsArray);

          final PDNumberTreeNode newKidsElement = new PDNumberTreeNode(pTreeEntry, PDNumberTreeNode.class);

          ntnKids.add(newKidsElement);
          ntnKids.setNeedToBeUpdated(true);

        } else if (ntnNumbers != null && ntnKids == null) {

          final int arrindex = ntnNumbers.size();

          ntnNumbers.add(arrindex, COSInteger.get(parentTreeNextKey));
          ntnNumbers.add(arrindex + 1, sigBlock.getCOSObject());

          ntnNumbers.setNeedToBeUpdated(true);

          structureTreeRoot.setParentTree(ntn);

        } else if (ntnNumbers == null && ntnKids == null) {
          // document is not pdfua conform before signature creation
          throw new PdfAsException("error.pdf.sig.pdfua.1");
        } else {
          // this is not allowed
          throw new PdfAsException("error.pdf.sig.pdfua.1");
        }

        // set StructureParent for signature field annotation
        signatureField.getWidget().setStructParent(parentTreeNextKey);

        // Increase the next Key value in the structure tree root
        structureTreeRoot.setParentTreeNextKey(parentTreeNextKey + 1);

        // add the Tabs /S Element for Tabbing through annots
        final PDPage p = signatureField.getWidget().getPage();
        p.getCOSObject().setName("Tabs", "S");
        p.getCOSObject().setNeedToBeUpdated(true);

        // check alternative signature field name
        if (signatureField != null) {
          if (signatureField.getAlternateFieldName().equals("")) {
            signatureField.setAlternateFieldName(sigFieldName);
          }
        }

        ntn.getCOSObject().setNeedToBeUpdated(true);
        sigBlock.getCOSObject().setNeedToBeUpdated(true);
        structureTreeRoot.getCOSObject().setNeedToBeUpdated(true);
        objectDic.setNeedToBeUpdated(true);
        docElement.getCOSObject().setNeedToBeUpdated(true);
      }
    } catch (final Throwable e) {
      if (signatureProfileSettings.isPDFUA()) {
        log.error("Could not create PDF-UA conform document!");
        throw new PdfAsException("error.pdf.sig.pdfua.1", e);
        
      } else {
        log.info("Could not create PDF-UA conform signature");
      }
    }
    
  }

  private String buildNextSignatureFieldName(PDDocument doc, PDFBOXObject pdfObject) {
    String sigFieldName = getSignatureFieldNameConfig(pdfObject);   
    if (sigFieldName == null) {
      sigFieldName = "PDF-AS Signatur";
      
    }
    return sigFieldName + PdfBoxUtils.countSignatures(doc, sigFieldName);
    
  }

  private int calculateBlankAreaForSignature(SignatureProfileSettings signatureProfileSettings) {
    int signatureSize = 0x1000;
    try {
      final String reservedSignatureSizeString = signatureProfileSettings.getValue(SIG_RESERVED_SIZE);
      if (reservedSignatureSizeString != null) {
        signatureSize = Integer.parseInt(reservedSignatureSizeString);
      }
      log.debug("Reserving {} bytes for signature", signatureSize);
      
    } catch (final NumberFormatException e) {
      log.warn("Invalid configuration value: {} should be a number using 0x1000", SIG_RESERVED_SIZE);
      
    }
    
    return signatureSize;    
  }

  private TablePos prepareTablePosition(SignaturePlaceholderData nextPlaceholderData, 
      SignatureProfileConfiguration signatureProfileConfiguration, String profilePosParam) throws PdfAsException {

                            
    if (nextPlaceholderData != null && nextPlaceholderData.getTablePos() != null) {
      final float minWidth = signatureProfileConfiguration.getMinWidth();
      TablePos tablePos = nextPlaceholderData.getTablePos();         
      if (minWidth > 0) {
        if (tablePos.getWidth() < minWidth) {
          tablePos.width = minWidth;
          log.debug("Correcting placeholder with to minimum width {}", minWidth);
        }
      }
      log.debug("Placeholder Position set to: " + tablePos.toString());
      return tablePos;
      
    } else {
      TablePos signaturePos = null;
      final String signaturePosString = signatureProfileConfiguration.getDefaultPositioning();

      if (signaturePosString != null) {
        log.debug("using signature Positioning: " + signaturePosString);
        signaturePos = new TablePos(signaturePosString);
        
      }

      log.debug("using Positioning: " + profilePosParam);

      if (profilePosParam != null) {
        // Merge Signature Position
        return new TablePos(profilePosParam, signaturePos);        
      
      } else if (signaturePos != null){
        // Fallback to signature Position!
        return signaturePos;
        
      } else {
        return new TablePos();
        
      }
    }
  }

  private String getSignatureFieldNameConfig(PDFBOXObject pdfObject) {
    return pdfObject.getStatus().getSettings().getValue(SIGNATURE_FIELD_NAME);
  }

  private int getParentTreeNextKey(PDStructureTreeRoot structureTreeRoot) throws IOException {
    int nextKey = structureTreeRoot.getParentTreeNextKey();
    if (nextKey < 0) {
      final Map<Integer, COSObjectable> destNumberTreeAsMap = getNumberTreeAsMap(structureTreeRoot
          .getParentTree());
      if (destNumberTreeAsMap.isEmpty()) {
        nextKey = 0;

      } else {
        nextKey = Collections.max(destNumberTreeAsMap.keySet()) + 1;

      }
    }

    return nextKey;
  }

  /**
   * Check via PreFlightParser if PDF-Document is a valid PDFA1
   *
   * @param signedDocument: signed Document
   * @throws PdfAsException
   */
  private void runPDFAPreflight(final DataSource signedDocument) throws PdfAsException {
    PreflightDocument document = null;
    ValidationResult result = null;
    try {
      final PreflightParser parser = new PreflightParser(signedDocument);
      //
      // parser.parse(Format.PDF_A1B);
      parser.parse();
      document = parser.getPreflightDocument();
      document.validate();

      document.close();
      result = document.getResult();
      log.info("PDF-A Validation Result: " + result.isValid());

      if (result.getErrorsList().size() > 0) {
        log.error("The following validation errors occured for PDF-A validation");
      }

      for (final ValidationResult.ValidationError ve : result.getErrorsList()) {
        log.error("\t" + ve.getErrorCode() + ": " + ve.getDetails());
      }

      if (!result.isValid()) {
        log.info("The file is not a valid PDF-A document");
      }

    } catch (final SyntaxValidationException e) {
      log.error("The file is syntactically invalid.", e);
      throw new PdfAsException("Resulting PDF Document is syntactically invalid.");
    } catch (final ValidationException e) {
      log.error("The file is not a valid PDF-A document.", e);
    } catch (final IOException e) {
      log.error("An IOException (" + e.getMessage()
          + ") occurred, while validating the PDF-A conformance", e);
      throw new PdfAsException("Failed validating PDF Document IOException.");
    } catch (final RuntimeException e) {
      log.debug("An RuntimeException (" + e.getMessage()
          + ") occurred, while validating the PDF-A conformance", e);
      throw new PdfAsException("Failed validating PDF Document RuntimeException.");
    } finally {
      if (document != null) {
        IOUtils.closeQuietly(document);
      }
    }
  }

  @Override
  public PDFObject buildPDFObject(OperationStatus operationStatus) {
    return new PDFBOXObject(operationStatus);
  }

  @Override
  public PDFASSignatureInterface buildSignaturInterface(IPlainSigner signer, SignParameter parameters,
      RequestedSignature requestedSignature) {
    return new PdfboxSignerWrapper(signer, parameters, requestedSignature);
  }

  @Override
  public PDFASSignatureExtractor buildBlindSignaturInterface(X509Certificate certificate, String filter,
      String subfilter, Calendar date) {
    return new SignatureDataExtractor(certificate, filter, subfilter, date);
  }

  @Override
  public void checkPDFPermissions(PDFObject genericPdfObject) throws PdfAsException {
    if (!(genericPdfObject instanceof PDFBOXObject)) {
      // tODO:
      throw new PdfAsException();
    }

    final PDFBOXObject pdfObject = (PDFBOXObject) genericPdfObject;
    PdfBoxUtils.checkPDFPermissions(pdfObject.getDocument());
  }

  @Override
  public byte[] rewritePlainSignature(byte[] plainSignature) {
    final String signature = new COSString(plainSignature).toHexString();
    final byte[] pdfSignature = signature.getBytes();
    return pdfSignature;
  }

  @Override
  public Image generateVisibleSignaturePreview(SignParameter parameter,
      java.security.cert.X509Certificate cert,
      int resolution, OperationStatus status, RequestedSignature requestedSignature) throws PDFASError {
    try {

      final PDFBOXObject pdfObject = (PDFBOXObject) status.getPdfObject();
      final PDDocument origDoc = new PDDocument();

      origDoc.addPage(new PDPage(PDRectangle.A4));
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      origDoc.save(baos);
      baos.close();

      pdfObject.setOriginalDocument(new ByteArrayDataSource(baos.toByteArray()));

      final SignatureProfileSettings signatureProfileSettings = TableFactory
          .createProfile(requestedSignature.getSignatureProfileID(), pdfObject.getStatus().getSettings());

      // create Table describtion
      final Table main = TableFactory.createSigTable(signatureProfileSettings, MAIN, pdfObject.getStatus(),
          requestedSignature);

      final IPDFStamper stamper = StamperFactory.createDefaultStamper(pdfObject.getStatus().getSettings());

      final IPDFVisualObject visualObject = stamper.createVisualPDFObject(pdfObject, main);

      final SignatureProfileConfiguration signatureProfileConfiguration = pdfObject.getStatus()
          .getSignatureProfileConfiguration(requestedSignature.getSignatureProfileID());

      final String signaturePosString = signatureProfileConfiguration.getDefaultPositioning();
      PositioningInstruction positioningInstruction;
      if (signaturePosString != null) {
        positioningInstruction = Positioning.determineTablePositioning(new TablePos(signaturePosString),
            origDoc, visualObject, pdfObject.getStatus().getSettings(), signatureProfileSettings);
      } else {
        positioningInstruction = Positioning.determineTablePositioning(new TablePos(), origDoc,
            visualObject, pdfObject.getStatus().getSettings(), signatureProfileSettings);
      }

      origDoc.close();

      final SignaturePositionImpl position = new SignaturePositionImpl();
      position.setX(positioningInstruction.getX());
      position.setY(positioningInstruction.getY());
      position.setPage(positioningInstruction.getPage());
      position.setHeight(visualObject.getHeight());
      position.setWidth(visualObject.getWidth());

      requestedSignature.setSignaturePosition(position);

      final PDFAsVisualSignatureProperties properties = new PDFAsVisualSignatureProperties(
          pdfObject.getStatus().getSettings(), pdfObject, (PdfBoxVisualObject) visualObject,
          positioningInstruction, signatureProfileSettings);

      properties.buildSignature();
      PDDocument visualDoc;
      synchronized (PDDocument.class) {
        visualDoc = PDDocument.load(properties.getVisibleSignature());
      }


      final float stdRes = 72;
      final float targetRes = resolution;
      final float factor = targetRes / stdRes;

      final int targetPageNumber = 0;// TODO: is this always the case
      PDFRenderer pdfRenderer = new PDFRenderer(visualDoc);
      final BufferedImage outputImage = pdfRenderer.renderImageWithDPI(targetPageNumber, targetRes,
          ImageType.ARGB);

      visualDoc.close();
      pdfRenderer = null;
      
      final BufferedImage cutOut = new BufferedImage(
          (int) (position.getWidth() * factor),
          (int) (position.getHeight() * factor), 
          BufferedImage.TYPE_4BYTE_ABGR);

      final Graphics2D graphics = (Graphics2D) cutOut.getGraphics();

      graphics.drawImage(outputImage, 0, 0, cutOut.getWidth(), cutOut.getHeight(), 
          (int) (0 * factor),
          (int) (outputImage.getHeight() - (position.getHeight() + 1) * factor),
          (int) ((position.getWidth() + 2) * factor), 
          (int) (outputImage.getHeight() - (position.getHeight()) * factor + position.getHeight() * factor),
          null);
      return cutOut;
      
    } catch (final PdfAsException e) {
      log.warn("PDF-AS  Exception", e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    } catch (final Throwable e) {
      log.warn("Unexpected Throwable  Exception", e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    }
  }

  private String getPDFAVersion(PDDocument doc) {
    try {
      final PDDocumentCatalog cat = doc.getDocumentCatalog();
      final PDMetadata metadata = cat.getMetadata();

      if (metadata != null) {
        final DomXmpParser xmpParser = new DomXmpParser();
        final XMPMetadata xmpMetadata = xmpParser.parse(metadata.exportXMPMetadata());
        if (xmpMetadata != null) {
          final PDFAIdentificationSchema pdfaIdentificationSchema = xmpMetadata.getPDFIdentificationSchema();
          if (pdfaIdentificationSchema != null) {
            final Integer pdfaversion = pdfaIdentificationSchema.getPart();
            final String conformance = pdfaIdentificationSchema.getConformance();
            log.info("Detected PDF/A Version: {} - {}", pdfaversion, conformance);

            if (pdfaversion != null) {
              return String.valueOf(pdfaversion);
            }
          }
        }
      }
    } catch (final Throwable e) {
      log.warn("Failed to determine PDF/A Version!", e);
    }
    return null;
  }

  // Find an existing signature.
  private PDSignature findExistingSignature(PDDocument doc, String sigFieldName) {
    PDSignature signature = null;
    PDSignatureField signatureField;
    final PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
    if (acroForm != null) {
      signatureField = (PDSignatureField) acroForm.getField(sigFieldName);
      if (signatureField != null) {
        // retrieve signature dictionary
        signature = signatureField.getSignature();
        if (signature == null) {
          signature = new PDSignature();
          signatureField.getCOSObject().setItem(COSName.V, signature);
        } else {
          throw new IllegalStateException("The signature field " + sigFieldName + " is already signed.");
        }
      }
    }
    return signature;
    
  }
  
  static Map<Integer, COSObjectable> getNumberTreeAsMap(PDNumberTreeNode tree)
      throws IOException {
    Map<Integer, COSObjectable> numbers = tree.getNumbers();
    if (numbers == null) {
      numbers = new LinkedHashMap<>();
    } else {
      // must copy because the map is read only
      numbers = new LinkedHashMap<>(numbers);
    }
    final List<PDNumberTreeNode> kids = tree.getKids();
    if (kids != null) {
      for (final PDNumberTreeNode kid : kids) {
        numbers.putAll(getNumberTreeAsMap(kid));
      }
    }
    return numbers;
  }

}