package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractorConstants;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;

public class SignatureFieldsAndPlaceHolderExtractor {

  // Search for empty signature fields
  public static List<String> findEmptySignatureFields(PDDocument doc) {
    PDSignature signature;
    List<PDField> signatureField;
    final List<String> signatureFieldNames = new ArrayList<>();
    final PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
    if (acroForm != null) {
      signatureField = acroForm.getFields();
      for (final PDField pdField : signatureField) {
        if (pdField instanceof PDSignatureField && pdField.getPartialName() != null) {
          signature = ((PDSignatureField) pdField).getSignature();
          if (signature == null) {
            signatureFieldNames.add(pdField.getPartialName());
          }
        }
      }
    }
    return signatureFieldNames;
  }
  /*
   * Needed by PDF-OVER
   */

  /**
   * Returns the next unused signature placeholder
   *
   * @param doc The document to be searched for signature placeholders
   * @return The next unused signature placeholder or null in case there is none
   */
  public static SignaturePlaceholderData getNextUnusedSignaturePlaceHolder(PDDocument doc) {
    try {
      final String placeholderId = "1";
      final int mode = PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED;
      final SignaturePlaceholderExtractor signaturePlaceholderExtractor = new SignaturePlaceholderExtractor();
      return signaturePlaceholderExtractor.extract(doc, placeholderId, mode);

    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
