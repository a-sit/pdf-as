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
/**
 * <copyright> Copyright 2006 by Know-Center, Graz, Austria </copyright>
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
 */
package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorProcessor;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDPropBuild;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDPropBuildDataDict;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.util.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import at.gv.egiz.pdfas.common.exceptions.PDFIOException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.exceptions.PlaceholderExtractionException;
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractorConstants;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import at.knowcenter.wag.egov.egiz.pdf.TablePos;
import javassist.bytecode.stackmap.TypeData.ClassName;
import lombok.extern.slf4j.Slf4j;

/**
 * Extract all relevant information from a placeholder image.
 *
 * @author exthex
 *
 */
@Slf4j
public class SignaturePlaceholderExtractor extends PDFStreamEngine implements PlaceholderExtractorConstants {

  public static final String PREFIX = "PDF-AS_";
  
  private final List<SignaturePlaceholderData> placeholders = new ArrayList<>();
  private int currentPage = 0;

  protected SignaturePlaceholderExtractor() throws IOException, ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    super();

    final Properties properties = new Properties();
    properties.load(ClassName.class.getClassLoader().getResourceAsStream(
        "placeholder/pdfbox-reader-2.properties"));

    final Set<Entry<Object, Object>> entries = properties.entrySet();
    for (final Entry<Object, Object> entry : entries) {
      final String processorClassName = (String) entry.getValue();
      final Class<?> klass = Class.forName(processorClassName);
      final org.apache.pdfbox.contentstream.operator.OperatorProcessor processor =
          (OperatorProcessor) klass.newInstance();

      addOperator(processor);
      
    }
  }
    
  /**
   * Search the document for placeholder images and possibly included additional
   * info.<br/>
   * Searches only for the first placeholder page after page from top.
   *
   * @return available info from the first found placeholder.
   * @throws PdfAsException                 if the document could not be read.
   * @throws PlaceholderExtractionException if STRICT matching mode was requested
   *                                        and no suitable placeholder could be
   *                                        found.
   */
  public SignaturePlaceholderData extract(PDDocument doc,
      String placeholderId, int matchMode) throws PdfAsException {

    List<String> extistingSignatureNames = existingExistingSignatureNames(doc);
    
    
    int pageNr = 0;
    for (final PDPage page : doc.getPages()) {
      pageNr++;

      try {
        this.currentPage = pageNr;        
        if (page.getContents() != null && page.getResources() != null && page.getContentStreams() != null) {
          processPage(page); // TODO: pdfbox2 - right?

        }
        
        final SignaturePlaceholderData ret = matchPlaceholderPage(
            removeAlreadyUsePlaceholders(placeholders, extistingSignatureNames), placeholderId, matchMode);        
        if (ret != null) {
          return ret;
          
        }
        
      } catch (final IOException e1) {
        throw new PDFIOException("error.pdf.io.04", e1);
        
      } catch (final Throwable e) {
        throw new PDFIOException("error.pdf.io.04", e);
        
      }
    }
    
    if (placeholders.size() > 0) {
      final SignaturePlaceholderData ret = matchPlaceholderDocument(
          removeAlreadyUsePlaceholders(placeholders, extistingSignatureNames), placeholderId, matchMode);
      return ret;
      
    }
    // no placeholders found, apply strict mode if set
    if (matchMode == PLACEHOLDER_MATCH_MODE_STRICT) {
      throw new PlaceholderExtractionException("error.pdf.stamp.09");
      
    }
    return null;
  }

  @Override
  protected void processOperator(Operator operator, List<COSBase> arguments)
      throws IOException {
    final String operation = operator.getName();
    if (operation.equals("Do")) {
      final COSName objectName = (COSName) arguments.get(0);
      final PDXObject xobject = getResources().getXObject(objectName);
      if (xobject instanceof PDImageXObject) {
        try {
          final PDImageXObject image = (PDImageXObject) xobject;
          final SignaturePlaceholderData data = checkImage(image);
          if (data != null) {
            final PDPage page = getCurrentPage();
            final Matrix ctm = getGraphicsState()
                .getCurrentTransformationMatrix();
            int pageRotation = page.getRotation();
            pageRotation = pageRotation % 360;
            final double rotationInRadians = Math.toRadians(pageRotation);// (page.findRotation() * Math.PI) /
                                                                          // 180;

            final AffineTransform rotation = new AffineTransform();
            rotation.setToRotation(rotationInRadians);
            final AffineTransform rotationInverse = rotation
                .createInverse();
            final Matrix rotationInverseMatrix = new Matrix();
            rotationInverseMatrix
                .setFromAffineTransform(rotationInverse);
            final Matrix rotationMatrix = new Matrix();
            rotationMatrix.setFromAffineTransform(rotation);

            final Matrix unrotatedCTM = ctm
                .multiply(rotationInverseMatrix);

            float x = unrotatedCTM.getXPosition();
            final float yPos = unrotatedCTM.getYPosition();
            final float yScale = unrotatedCTM.getScaleY();
            float y = yPos + yScale;
            final float w = unrotatedCTM.getScaleX();

            log.debug("Page height: {}", page.getCropBox().getHeight());
            log.debug("Page width: {}", page.getCropBox().getWidth());

            if (pageRotation == 90) {
              y = page.getCropBox().getWidth() - y * -1;
            } else if (pageRotation == 180) {
              x = page.getCropBox().getWidth() + x;
              y = page.getCropBox().getHeight() - y * -1;
            } else if (pageRotation == 270) {
              x = page.getCropBox().getHeight() + x;
            }

            final String posString = "p:" + currentPage + ";x:" + Math.floor(x)
                + ";y:" + Math.ceil(y) + ";w:" + Math.ceil(w);
                       
            try {
              data.setTablePos(new TablePos(posString));
              data.setPlaceholderName(buildUniqueObjectName(objectName));
              log.debug("Found Placeholder at: {}", data.toString());
              placeholders.add(data);
              
            } catch (final PdfAsException e) {
              throw new IOException();
              
            }
          }
        } catch (final NoninvertibleTransformException e) {
          throw new IOException(e);
        }
      }
    } else {
      super.processOperator(operator, arguments);
    }
  }
  
  /**
   * Read placeholderId from signature.
   * 
   * @param signature Signature
   * @return placeholderId or <code>null</code> if there is no placeholderId.
   */
  private static String readPlaceHolderId(PDSignature signature) {
    PDPropBuild sigProps = getOrNew(signature);
    PDPropBuildDataDict appProps = getOrNew(sigProps);
    return appProps.getName() != null && appProps.getName().startsWith(PREFIX) 
        ? appProps.getName().substring(PREFIX.length())
        : appProps.getName();
    
  }
  
  /**
   * Set a placeholderId into signature directory.
   * 
   * @param signature Signature
   * @param placeholderId placeholderId
   */
  public static void setPlaceholderId(PDSignature signature, String placeholderId) {
    PDPropBuild sigProps = getOrNew(signature);    
    PDPropBuildDataDict appProps = getOrNew(sigProps);        
    appProps.setName(PREFIX + placeholderId);    
    sigProps.setPDPropBuildApp(appProps);
    signature.setPropBuild(sigProps);
    
  }
  
  /**
   * Builds unique placeholderId from PDF element.
   * 
   * @param name PDF element name
   * @return unique identifier
   */
  private String buildUniqueObjectName(COSName name)
  {
      COSDictionary dict = getResources().getCOSObject().getCOSDictionary(COSName.XOBJECT);
      if (dict == null)
      {
          return name.getName();
          
      }
      
      COSBase base = dict.getItem(name);
      if (base instanceof COSObject)
      {                            
          return name.getName() + "_" + String.valueOf(((COSObject) base).getObjectNumber());      
          
      }

      return name.getName();
      
  }
  
  private SignaturePlaceholderData matchPlaceholderPage(
      List<SignaturePlaceholderData> placeholders, String placeholderId, int matchMode) {
    log.debug("Searching requested placeholder:{} with matchMode:{} in single page ... ", placeholderId, matchMode);
    
    if (placeholders.size() == 0) {
      return null;
      
    }
    
    // check if find a placeholder with that ID    
    for (final SignaturePlaceholderData data : placeholders) {
      if (placeholderId != null && data.getId() != null 
          && matchPlaceHolderId(placeholderId, data.getId())) {
        return data;
        
      }
      
      if (matchMode != PLACEHOLDER_MATCH_MODE_SORTED 
          && placeholderId == null && data.getId() == null) {
        return data;
        
      }
    }
    
    return null;
  }
  
  private SignaturePlaceholderData matchPlaceholderDocument(
      List<SignaturePlaceholderData> placeholders, String placeholderId,
      int matchMode) throws PlaceholderExtractionException {

    log.debug("Searching requested placeholder:{} with matchMode:{} on any page ... ", placeholderId, matchMode);
    
    if (matchMode == PLACEHOLDER_MATCH_MODE_STRICT) {
      throw new PlaceholderExtractionException("error.pdf.stamp.09");
    }

    if (placeholders.size() == 0) {
      return null;
    }

    if (matchMode == PLACEHOLDER_MATCH_MODE_SORTED) {
      // sort all placeholders by the id string if all ids are null do nothing
      SignaturePlaceholderData currentFirstSpd = null;
      for (final SignaturePlaceholderData spd : placeholders) {
        if (spd.getId() != null) {
          if (currentFirstSpd == null) {
            currentFirstSpd = spd;
            log.debug("Setting new current ID: {}",
                currentFirstSpd.getId());
          } else {
            currentFirstSpd = placeHolderIdMatcher(currentFirstSpd, spd);

          }
        }
      }

      if (currentFirstSpd != null) {
        log.info("Running Placeholder sorted mode: using id: {}", currentFirstSpd.getId());
        return currentFirstSpd;
        
      } else {
        log.info(
            "Running Placeholder sorted mode: no placeholder with id found, fallback to first placeholder");
      }
    }

    for (final SignaturePlaceholderData spd : placeholders) {
      if (spd.getId() == null) {
        return spd;
      }
    }

    if (matchMode == PLACEHOLDER_MATCH_MODE_LENIENT) {
      return placeholders.get(0);
    }

    return null;
  }

  private boolean matchPlaceHolderId(String first, String second) {
    try {
      Integer firstIdInt = Integer.valueOf(first);
      Integer secondIdInt = Integer.valueOf(second);
      return firstIdInt == secondIdInt;
                  
    } catch (NumberFormatException e) {
      log.trace("Can not compare placeholderId's on integer level. Using String compare ... ");
      return first.equalsIgnoreCase(second);
      
    } 
    
  }
  
  private SignaturePlaceholderData placeHolderIdMatcher(SignaturePlaceholderData currentFirstSpd,
      SignaturePlaceholderData spd) {
    try {
      Integer currentIDInt = Integer.valueOf(currentFirstSpd.getId());
      Integer testIDInt = Integer.valueOf(spd.getId());
      
      if (testIDInt < currentIDInt) {
        log.debug("Setting new current ID: {}", testIDInt);
        return spd;
        
      } else {
        return currentFirstSpd;
        
      }            
    } catch (NumberFormatException e) {
      log.trace("Can not compare placeholderId's on integer level. Using String compare ... ");
      final String currentID = currentFirstSpd.getId();
      final String testID = spd.getId();
      log.debug("Testing placeholder current: {} compare to {}",
          currentID, testID);
      if (testID.compareToIgnoreCase(currentID) < 0) {      
        log.debug("Setting new current ID: {}",
            testID);      
        return spd;
        
      } else {
        return currentFirstSpd;
            
      }
    }       
  }


  
  private static final PDPropBuildDataDict getOrNew(PDPropBuild sigProps) {
    PDPropBuildDataDict props = sigProps.getApp();    
    return props != null ? props : new PDPropBuildDataDict();
  }
  
  private static final PDPropBuild getOrNew(PDSignature signature) {
    PDPropBuild sigProps = signature.getPropBuild();
    return sigProps != null ? sigProps : new PDPropBuild(); 
    
  }
  
  private List<String> existingExistingSignatureNames(PDDocument doc) {
    try {
      final List<PDSignature> pdSignatureList = doc.getSignatureDictionaries();      
      return pdSignatureList.stream()
        .map(el -> readPlaceHolderId(el))
        .filter(Objects::nonNull)        
        .collect(Collectors.toList());  
                 
    } catch (final IOException e) {
      log.warn("Can not parse signature dictionaries", e);
      return Collections.emptyList();
      
    }
  }
  
  private List<SignaturePlaceholderData> removeAlreadyUsePlaceholders(
      List<SignaturePlaceholderData> placeholders, List<String> existingPlaceholders) {
    if (placeholders != null) {
      List<SignaturePlaceholderData> result = placeholders.stream()
          .filter(el -> !existingPlaceholders.contains(el.getPlaceholderName()))
          .collect(Collectors.toList());             
      log.debug("Initial found #{} placeholders, but #{} removed because already used.",
          placeholders.size(), placeholders.size() - result.size());
      return result;
      
    } else {
      return Collections.emptyList();
      
    }
  }
  
  /**
   * Checks an image if it is a placeholder for a signature.
   *
   * @param image
   * @return
   * @throws IOException
   */
  private SignaturePlaceholderData checkImage(PDImageXObject image)
      throws IOException {
    final BufferedImage bimg = image.getImage();
    if (bimg == null) {
      String type = image.getSuffix();
      if (type != null) {
        type = type.toUpperCase() + " images";
      } else {
        type = "Image type";
      }
      log.info("Unable to extract image for QRCode analysis. "
          + type
          + " not supported. Add additional JAI Image filters to your classpath. Refer to https://jai.dev.java.net. Skipping image.");
      return null;
      
    }
    
    if (bimg.getHeight() < 10 || bimg.getWidth() < 10) {
      log.debug("Image too small for QRCode. Skipping image.");
      return null;
    }

    final LuminanceSource source = new BufferedImageLuminanceSource(bimg);
    final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    Result result;
    final long before = System.currentTimeMillis();
    try {
      final Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
      final Vector<BarcodeFormat> formats = new Vector<>();
      formats.add(BarcodeFormat.QR_CODE);
      hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
      result = new MultiFormatReader().decode(bitmap, hints);

      final String text = result.getText();
      String profile = null;
      String type = null;
      String sigKey = null;
      String id = null;
      if (text != null) {
        if (text.startsWith(QR_PLACEHOLDER_IDENTIFIER)) {

          final String[] data = text.split(";");
          if (data.length > 1) {
            for (int i = 1; i < data.length; i++) {
              final String kvPair = data[i];
              final String[] kv = kvPair.split("=");
              if (kv.length != 2) {
                log.debug("Invalid parameter in placeholder data: "
                    + kvPair);
              } else {
                if (kv[0]
                    .equalsIgnoreCase(SignaturePlaceholderData.ID_KEY)) {
                  id = kv[1];
                } else if (kv[0]
                    .equalsIgnoreCase(SignaturePlaceholderData.PROFILE_KEY)) {
                  profile = kv[1];
                } else if (kv[0]
                    .equalsIgnoreCase(SignaturePlaceholderData.SIG_KEY_KEY)) {
                  sigKey = kv[1];
                } else if (kv[0]
                    .equalsIgnoreCase(SignaturePlaceholderData.TYPE_KEY)) {
                  type = kv[1];
                }
              }
            }
          }
          return new SignaturePlaceholderData(profile, type, sigKey,
              id);
        } else {
          log.warn("QR-Code found but does not start with \""
              + QR_PLACEHOLDER_IDENTIFIER
              + "\". Ignoring QR placeholder.");
        }
      }
    } catch (final ReaderException re) {
      if (log.isDebugEnabled()) {
        log.debug("Could not decode - not a placeholder. needed: "
            + (System.currentTimeMillis() - before));
      }
      if (!(re instanceof NotFoundException)) {
        if (log.isInfoEnabled()) {
          log.info("Failed to decode image", re);
        }
      }
    } catch (final ArrayIndexOutOfBoundsException e) {
      if (log.isInfoEnabled()) {
        log.info("Failed to decode image. Probably a zxing bug", e);
      }
    }
    return null;
  }
}
