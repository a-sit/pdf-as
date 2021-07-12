package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractorConstants;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import java.util.ArrayList;
import java.util.List;

public class SignatureFieldsAndPlaceHolderExtractor {

    //Search for empty signature fields
    public static List<String> findEmptySignatureFields(PDDocument doc)
    {
        PDSignature signature;
        List<PDField> signatureField;
        List<String> signatureFieldNames = new ArrayList<>();
        PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
        if (acroForm != null) {
            signatureField = acroForm.getFields();
            for (PDField pdField : signatureField) {
                if(pdField instanceof PDSignatureField && pdField.getPartialName()!=null)
                {
                    signature = ((PDSignatureField) pdField).getSignature();
                    if(signature == null) signatureFieldNames.add(pdField.getPartialName());
                }
            }
        }
        return signatureFieldNames;
    }
    /*
    Needed by PDF-OVER
     */
    public static SignaturePlaceholderData getNextSignaturePlaceHolder(PDDocument doc) {
        try {
            SignaturePlaceholderExtractor signaturePlaceholderExtractor = new SignaturePlaceholderExtractor("1",
                PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED, doc);
            return signaturePlaceholderExtractor.extract(doc, "1",
                PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SignaturePlaceholderData getSignaturePlaceHolder(PDDocument doc, String placeholderId,
                                                                   int mode) {
        try {
            SignaturePlaceholderExtractor signaturePlaceholderExtractor = new SignaturePlaceholderExtractor( placeholderId,
                mode, doc);
            return signaturePlaceholderExtractor.extract(doc, placeholderId, mode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SignaturePlaceholderData> getSignaturePlaceHolderList(PDDocument doc, String placeholderId, int mode) {
        try {
            SignaturePlaceholderExtractor signaturePlaceholderExtractor = new SignaturePlaceholderExtractor( placeholderId,
                mode, doc);
            return signaturePlaceholderExtractor.extractList(doc, placeholderId, mode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
