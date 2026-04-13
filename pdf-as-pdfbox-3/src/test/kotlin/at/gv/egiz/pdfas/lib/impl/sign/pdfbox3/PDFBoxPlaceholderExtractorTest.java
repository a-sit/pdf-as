package at.gv.egiz.pdfas.lib.impl.sign.pdfbox3;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Ignore;
import org.junit.Test;

import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBoxPlaceholderExtractor;
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
    assertEquals("Im0_1", getNextSignaturePlaceHolder("/data/Testdoc_Signatur.pdf").getPlaceholderName());
    assertEquals("Im0_2", getNextSignaturePlaceHolder("/data/own_Testdoc+Signatur-sign-sign.pdf").getPlaceholderName());
    assertEquals("Im0_2", getNextSignaturePlaceHolder("/data/own_Testdoc+Signatur-sign-sign-4_sign.pdf").getPlaceholderName());
    assertEquals("Im0", getNextSignaturePlaceHolder("/data/own_Testdoc+Signatur-sign-sign-4_sign-sign.pdf").getPlaceholderName());

  }
    
  @Test
  @Ignore
  @SneakyThrows
  public void placeHolderInAnnotation() {
    SignaturePlaceholderData listOfPlaceHolders = getNextSignaturePlaceHolder("/data/Test-sign.pdf");
    assertNotNull(listOfPlaceHolders);

  }

  private static List<String> getPlaceHolders(String filePath) throws IOException {
    try (final PDDocument doc = Loader.loadPDF(
        PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(filePath)
            .readAllBytes())) {
      return PDFBoxPlaceholderExtractor.findEmptySignatureFields(doc);
    }
  }

  private static SignaturePlaceholderData getNextSignaturePlaceHolder(String filePath) throws IOException {
    try (final PDDocument doc = Loader.loadPDF(
        PDFBoxPlaceholderExtractorTest.class.getResourceAsStream(filePath)
            .readAllBytes())) {
      return PDFBoxPlaceholderExtractor.getNextUnusedSignaturePlaceholder(doc);
    }
  }

}
