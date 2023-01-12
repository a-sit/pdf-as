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
package at.gv.egiz.pdfas.lib.impl.pdfbox2.positioning;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.utils.PdfBoxUtils;
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFVisualObject;
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction;
import at.knowcenter.wag.egov.egiz.pdf.TablePos;
import at.knowcenter.wag.egov.egiz.pdfbox2.pdf.PDFUtilities;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA. User: afitzek Date: 8/29/13 Time: 4:30 PM To
 * change this template use File | Settings | File Templates.
 */
@Slf4j
public class Positioning {

  /**
   * The left/right margin.
   */
  public static final float SIGNATURE_MARGIN_HORIZONTAL = 50f;

  /**
   * The top/bottom margin.
   */
  public static final float SIGNATURE_MARGIN_VERTICAL = 20f;

  /**
   * Evalutates absolute positioning and prepares the PositioningInstruction for
   * placing the table.
   *
   * @param pos            The absolute positioning parameter. If null it is
   *                       sought in the profile definition.
   * @param signature_type The profile definition of the table to be written.
   * @param pdfDataSource  The pdf.
   * @param pdf_table      The pdf table to be written.
   * @param settings
   * @return Returns the PositioningInformation.
   * @throws PdfAsException F.e.
   */
  public static PositioningInstruction determineTablePositioning(
      TablePos pos, String signature_type, PDDocument pdfDataSource,
      IPDFVisualObject pdf_table, ISettings settings) throws PdfAsException {
    return adjustSignatureTableandCalculatePosition(pdfDataSource,
        pdf_table, pos, settings);
  }

  /**
   * Sets the width of the table according to the layout of the document and
   * calculates the y position where the PDFPTable should be placed.
   *
   * @param pdfDataSource The PDF document.
   * @param pdf_table     The PDFPTable to be placed.
   * @param settings
   * @return Returns the position where the PDFPTable should be placed.
   * @throws PdfAsException F.e.
   */
  public static PositioningInstruction adjustSignatureTableandCalculatePosition(
      final PDDocument pdfDataSource, IPDFVisualObject pdf_table,
      TablePos pos, ISettings settings) throws PdfAsException {
    PdfBoxUtils.checkPDFPermissions(pdfDataSource);
    final long numberOfExistingSignatures = getNumberOfExistingSignatures(pdfDataSource);

    // get pages of currentdocument
    final int doc_pages = pdfDataSource.getNumberOfPages();
    int page = doc_pages;
    boolean make_new_page = pos.isNewPage();


    if (!(pos.isNewPage() || pos.isPauto())) {
      // we should posit signaturtable on this page
      page = pos.getPage();
      if (page > doc_pages) {
        log.debug("Document is shorter than requested page for signature block. Adding new page ...");
        make_new_page = true;
        page = doc_pages;
        
      }
    }

    make_new_page = checkIfNewPageIsAllowed(make_new_page, numberOfExistingSignatures, settings);
    
    
    if(make_new_page && numberOfExistingSignatures!=0) {
      make_new_page = false;
      
    }
       
    
    final PDPage pdPage = pdfDataSource.getPage(page - 1);

    PDRectangle cropBox = pdPage.getCropBox();

    // fallback to MediaBox if Cropbox not available!

    if (cropBox == null) {
      cropBox = pdPage.getCropBox();
    }

    if (cropBox == null) {
      cropBox = pdPage.getCropBox();
    }

    // getPagedimensions
    // Rectangle psize = reader.getPageSizeWithRotation(page);
    // int page_rotation = reader.getPageRotation(page);

    // Integer rotation = pdPage.getRotation();
    // int page_rotation = rotation.intValue();

    final int rotation = pdPage.getRotation();

    log.debug("Original CropBox: " + cropBox.toString());

    cropBox = rotateBox(cropBox, rotation);

    log.debug("Rotated CropBox: " + cropBox.toString());

    final float page_width = cropBox.getWidth();
    final float page_height = cropBox.getHeight();

    log.debug("CropBox width: " + page_width);
    log.debug("CropBox heigth: " + page_height);

    // now we can calculate x-position
    float pre_pos_x = SIGNATURE_MARGIN_HORIZONTAL;
    if (!pos.isXauto()) {
      // we do have absolute x
      pre_pos_x = pos.getPosX();
    }
    // calculate width
    // center
    float pre_width = page_width - 2 * pre_pos_x;
    if (!pos.isWauto()) {
      // we do have absolute width
      pre_width = pos.getWidth();
      if (pos.isXauto()) { // center x
        pre_pos_x = (page_width - pre_width) / 2;
        
      }
    }
    
    final float pos_x = pre_pos_x;
    final float width = pre_width;

    // Signatur table dimensions are complete
    pdf_table.setWidth(width);
    pdf_table.fixWidth();

    final float table_height = pdf_table.getHeight();
    // now check pos_y
    float pos_y = pos.getPosY();

    // in case an absolute y position is already given OR
    // if the table is related to an invisible signature
    // there is no need for further calculations
    // (fixed adding new page in case of invisible signatures)
    if (!pos.isYauto() || table_height == 0) {
      // we do have y-position too --> all parameters but page ok
      if (make_new_page) {
        page++;
        
      }      
      return new PositioningInstruction(make_new_page, page, pos_x, pos_y, pos.rotation);
      
    }
    
    // pos_y is auto
    if (make_new_page) {            
      // ignore footer in new page
      page++;
      pos_y = page_height - SIGNATURE_MARGIN_VERTICAL;            
      return new PositioningInstruction(make_new_page, page, pos_x, pos_y, pos.rotation);
      
    }
    
    // up to here no checks have to be made if Tablesize and Pagesize are
    // fit
    // Now we have to getfreespace in page and reguard footerline   
    float pre_page_length = calculatePrePageLength(pdfDataSource, page, pdf_table, pos.getFooterLine(), settings);
   
    if (pre_page_length == Float.NEGATIVE_INFINITY) {
      // we do have an empty page or nothing in area above footerline     
      pos_y = page_height - SIGNATURE_MARGIN_VERTICAL;      
      return buildPostitionInfoOnSubpage(pdfDataSource, make_new_page, page, pos_x, pos_y, pos.rotation, 
          pos.getFooterLine(), table_height, pos, page_height, numberOfExistingSignatures, settings);
           
    } else {    
      // we do have text take SIGNATURE_MARGIN
      pos_y = page_height - pre_page_length - SIGNATURE_MARGIN_VERTICAL;
      return buildPostitionInfoOnSubpage(pdfDataSource, make_new_page, page, pos_x, pos_y, pos.rotation, 
          pos.getFooterLine(), table_height, pos, page_height, numberOfExistingSignatures, settings);
    
    }  
  }

  private static float calculatePrePageLength(PDDocument pdfDataSource, int page, IPDFVisualObject pdf_table, float footer_line, ISettings settings) {    
    try {
      return PDFUtilities.getMaxYPosition(pdfDataSource, page - 1, pdf_table,
          SIGNATURE_MARGIN_VERTICAL, footer_line, settings);
    
    } catch (final IOException e) {
      log.warn("Could not determine page length, using -INFINITY");
      return Float.NEGATIVE_INFINITY;
      
    }
  }

  private static boolean isFailOnLessSpaceEnabled(ISettings settings) {    
    String value = settings.getValue(IConfigurationConstants.SIG_BLOCK_LESS_SPACE_STOPPING_WITH_ERROR);    
    return Boolean.valueOf(value);
    
  }
  
  private static boolean checkIfNewPageIsAllowed(boolean make_new_page, long numberOfExistingSignatures, ISettings settings) throws PdfAsException {
    if(make_new_page && numberOfExistingSignatures!=0) {
      log.info("Signature-block would be need a new page, but new pages are not allowed on already signed documents.");
      if (isFailOnLessSpaceEnabled(settings)) {
        throw new PdfAsException("error.pdf.stamp.12");
        
      } else {
        log.info("Placing signature-block on last page without free-space checks ... ");
        return false;
        
      }
      
    } else {
      return make_new_page;
      
    }       
  }
  
  private static PositioningInstruction buildPostitionInfoOnSubpage(PDDocument pdfDataSource, boolean make_new_page, int page, float pos_x, 
      float pos_y, float rotation, float footer_line, float table_height, TablePos pos, float page_height, 
      long numberOfExistingSignatures, ISettings settings) throws PdfAsException {
    if (pos_y - footer_line <= table_height) {
      
      make_new_page = checkIfNewPageIsAllowed(true, numberOfExistingSignatures, settings);
      if (make_new_page) {        
        page++;
        
      }
            
      if (!pos.isPauto()) {
        // we have to correct pagenumber
        page = pdfDataSource.getNumberOfPages();
        
      }
      // no text --> SIGNATURE_BORDER
      pos_y = page_height - SIGNATURE_MARGIN_VERTICAL;
      
    }
    
    return new PositioningInstruction(make_new_page, page, pos_x, pos_y, pos.rotation);
    
  }

  private static long getNumberOfExistingSignatures(PDDocument pdfDataSource) {
    try {
      return pdfDataSource.getSignatureFields().stream()
          .filter(el -> el.getSignature() != null)
          .count();

    } catch (final IOException e) {
      log.warn("Can not extract existing Signatures from PDF. Use it as 0", e);
      return 0;

    }
  }

  private static PDRectangle rotateBox(PDRectangle cropBox, int rotation) {
    if (rotation != 0) {
      final Point2D upSrc = new Point2D.Float();

      upSrc.setLocation(cropBox.getUpperRightX(),
          cropBox.getUpperRightY());

      final Point2D llSrc = new Point2D.Float();
      llSrc.setLocation(cropBox.getLowerLeftX(), cropBox.getLowerLeftY());
      final AffineTransform transform = new AffineTransform();
      transform.setToIdentity();
      if (rotation % 360 != 0) {
        transform.setToRotation(Math.toRadians(rotation * -1), llSrc.getX(),
            llSrc.getY());
      }
      final Point2D upDst = new Point2D.Float();
      transform.transform(upSrc, upDst);

      final Point2D llDst = new Point2D.Float();
      transform.transform(llSrc, llDst);

      float y1 = (float) upDst.getY();
      float y2 = (float) llDst.getY();

      if (y1 > y2) {
        final float t = y1;
        y1 = y2;
        y2 = t;
      }

      if (y1 < 0) {
        y2 = y2 + -1 * y1;
        y1 = 0;
      }

      float x1 = (float) upDst.getX();
      float x2 = (float) llDst.getX();

      if (x1 > x2) {
        final float t = x1;
        x1 = x2;
        x2 = t;
      }

      if (x1 < 0) {
        x2 = x2 + -1 * x1;
        x1 = 0;
      }

      cropBox.setUpperRightX(x2);
      cropBox.setUpperRightY(y2);
      cropBox.setLowerLeftY(y1);
      cropBox.setLowerLeftX(x1);
    }
    return cropBox;
  }

}
