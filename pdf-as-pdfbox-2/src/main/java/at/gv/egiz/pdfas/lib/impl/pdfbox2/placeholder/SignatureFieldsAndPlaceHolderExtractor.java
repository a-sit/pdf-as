package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractorConstants;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import java.io.IOException;
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

    /**
     * Returns the next unused signature placeholder
     * @param doc The document to be searched for signature placeholders
     * @return The next unused signature placeholder or null in case there is none
     */
    public static SignaturePlaceholderData getNextUnusedSignaturePlaceHolder(PDDocument doc) {
        try {
            String placeholderId = "1";
            int mode = PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED;
            SignaturePlaceholderExtractor signaturePlaceholderExtractor = new SignaturePlaceholderExtractor( placeholderId,
                mode, doc);
            List<SignaturePlaceholderData> results = signaturePlaceholderExtractor.extractList(doc, placeholderId,
                mode);
            List<String> used = getExistingSignatureLocations(doc);
            //return first not used
            for(SignaturePlaceholderData result : results) {
                if(!used.contains(result.getPlaceholderName()))
                    return result;
            }
            return null;
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

    public static List<String> getExistingSignatureLocations(PDDocument doc) {
        List<String> existingLocations = new ArrayList<>();
        try {
            List <PDSignature> pdSignatureList =  doc.getSignatureDictionaries();
            if(pdSignatureList.size() != 0) {
                for(PDSignature sig : pdSignatureList) {
                    existingLocations.add(sig.getLocation());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingLocations;
    }
}
