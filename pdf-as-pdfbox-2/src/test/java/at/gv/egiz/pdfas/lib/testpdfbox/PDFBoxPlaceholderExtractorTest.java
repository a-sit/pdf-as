package at.gv.egiz.pdfas.lib.testpdfbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
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

  }



  private static List<String> getPlaceHolders(String filePath) throws IOException {
    try (final PDDocument doc = PDDocument.load(PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(
        filePath))) {
      return SignatureFieldsAndPlaceHolderExtractor.findEmptySignatureFields(doc);
    }
  }

  private static SignaturePlaceholderData getNextSignaturePlaceHolder(String filePath) throws IOException {
    try (final PDDocument doc = PDDocument.load(PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(
        filePath))) {
      return SignatureFieldsAndPlaceHolderExtractor.getNextUnusedSignaturePlaceHolder(doc);
    }
  }

}
