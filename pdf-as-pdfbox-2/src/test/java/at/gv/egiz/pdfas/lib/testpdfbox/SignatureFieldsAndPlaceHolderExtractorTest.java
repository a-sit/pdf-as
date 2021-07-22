package at.gv.egiz.pdfas.lib.testpdfbox;

import at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder.SignatureFieldsAndPlaceHolderExtractor;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.List;

public class SignatureFieldsAndPlaceHolderExtractorTest {

  public static void main(String[] args) {
    String dir = "/Users/amarsalek/Documents/pdf-as-4/pdf-as-pdfbox-2/src/test/resources/";

    getPlacHolders(dir+ "manySignFields.pdf");
    getPlacHolders(dir+ "manySignFields_signed4.pdf");
    getNextSignaturePlaceHolder(dir + "new_qr_2-2.pdf");
    getNextSignaturePlaceHolder(dir + "new_qr_2_signed.pdf");
    getNextSignaturePlaceHolder(dir + "new_qr_2_signed_signed.pdf");
    getNextSignaturePlaceHolder(dir + "new_qr_2_signed_signed_signed.pdf");
    //TODO convert into junit test
  }

  private static void getPlacHolders(String filePath) {
    try {
      PDDocument doc = PDDocument.load(new File(filePath));
      List<String> result = SignatureFieldsAndPlaceHolderExtractor.findEmptySignatureFields(doc);
      System.out.println(filePath + ": " + result);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void getNextSignaturePlaceHolder(String filePath) {
    try {
      PDDocument doc = PDDocument.load(new File(filePath));
      SignaturePlaceholderData result =
          SignatureFieldsAndPlaceHolderExtractor.getNextUnusedSignaturePlaceHolder(doc);
      System.out.println(filePath + ": " + result);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

}
