package at.gv.egiz.pdfas.lib.testpdfbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Ignore;
import org.junit.Test;

import at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder.SignatureFieldsAndPlaceHolderExtractor;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import lombok.SneakyThrows;

public class PDFBoxPlaceholderExtractorTest {

  @Test
  @SneakyThrows
  public void nextPlaceholder() {
    SignaturePlaceholderData result = getNextSignaturePlaceHolder("/data/platzhalter_en_de_test.pdf");
    assertEquals("Im48", result.getPlaceholderName());
  }

  @Test
  @SneakyThrows
  public void allPlaceHolders() {
    List<String> listOfPlaceHolders = getPlaceHolders("/data/platzhalter_en_de_test.pdf");
    assertNotNull(listOfPlaceHolders);
    assertTrue(listOfPlaceHolders.isEmpty());

  }

  @Test
  @SneakyThrows
  public void nextPlaceholderDuplicateElements() {
    assertEquals("Im1", getNextSignaturePlaceHolder("/data/own_Testdoc+Signatur-sign-sign.pdf").getPlaceholderName());
    assertEquals("Im1", getNextSignaturePlaceHolder("/data/cmd_test-pdf-signed.pdf").getPlaceholderName());
    assertEquals("Im0_48", getNextSignaturePlaceHolder("/data/cmd_test-pdf-signed_2.pdf").getPlaceholderName());
    assertEquals("Im1_49", getNextSignaturePlaceHolder("/data/cmd_test-pdf-signed_3.pdf").getPlaceholderName());

  }
    
  @Test
  @Ignore
  @SneakyThrows
  public void placeHolderInAnnotation() {
    SignaturePlaceholderData listOfPlaceHolders = getNextSignaturePlaceHolder("/data/Test-sign.pdf");
    assertNotNull(listOfPlaceHolders);

  }
  
  private static List<String> getPlaceHolders(String filePath) throws IOException {
    final PDDocument doc = PDDocument.load(PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(
        filePath));
    final List<String> results = SignatureFieldsAndPlaceHolderExtractor.findEmptySignatureFields(doc);
    return results;

  }

  private static SignaturePlaceholderData getNextSignaturePlaceHolder(String filePath) throws IOException {
    final PDDocument doc = PDDocument.load(PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(
        filePath));
    final SignaturePlaceholderData result =
        SignatureFieldsAndPlaceHolderExtractor.getNextUnusedSignaturePlaceHolder(doc);
    return result;

  }

}
