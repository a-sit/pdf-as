package at.gv.egiz.pdfas.lib.testpdfbox;

import at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder.SignatureFieldsAndPlaceHolderExtractor;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignatureFieldsAndPlaceHolderExtractorTest {

  public String getPath(String resourceName) {
    ClassLoader classLoader = this.getClass().getClassLoader();
    File file = new File(classLoader.getResource(resourceName).getFile());
    String absolutePath = file.getAbsolutePath();

    System.out.println(absolutePath);
    return absolutePath;
  }

  @Test
  public void notSigned(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("new_qr_2-2.pdf"));
    Assert.assertEquals("Image5",result.getPlaceholderName());
  }
  @Test
  public void signedOnce(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed.pdf"));
    Assert.assertEquals("Image8",result.getPlaceholderName());
  }
  @Test
  public void signedTwice(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed_signed.pdf"));
    Assert.assertEquals(null,result);
  }
  @Test
  public void signedThrice(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed_signed_signed.pdf"));
    Assert.assertEquals(null,result);
  }

  @Test
  public void noPlaceHolder(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("manySignFields.pdf"));
    Assert.assertEquals(null,result);
  }
  @Test
  public void subsequentCalls(){
    SignaturePlaceholderData result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed_signed_signed.pdf"));
    Assert.assertEquals(null,result);

    result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed.pdf"));
    Assert.assertEquals("Image8",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2-2.pdf"));
    Assert.assertEquals("Image5",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2-2.pdf"));
    Assert.assertEquals("Image5",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2-2.pdf"));
    Assert.assertEquals("Image5",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed.pdf"));
    Assert.assertEquals("Image8",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed_signed_signed.pdf"));
    Assert.assertEquals(null,result);

    result = getNextSignaturePlaceHolder(getPath("new_qr_2-2.pdf"));
    Assert.assertEquals("Image5",result.getPlaceholderName());

    result = getNextSignaturePlaceHolder(getPath("new_qr_2_signed.pdf"));
    Assert.assertEquals("Image8",result.getPlaceholderName());
  }
  @Test
  public void notSignedAndNoFields(){
    List<String> result = getPlaceHolders(getPath("new_qr_2-2.pdf"));

    List<String> expectedResult = new ArrayList<>();
    Assert.assertEquals(expectedResult,result);
  }

  @Test
  public void notSignedFields(){
    List<String> result = getPlaceHolders(getPath("manySignFields.pdf"));

    List<String> expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_4", "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);
  }

  @Test
  public void signedOncePosition4FieldTest(){
    List<String> result = getPlaceHolders(getPath("manySignFields_signed4.pdf"));

    List<String> expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);
  }

  @Test
  public void multipleCallsFieldTest(){
    List<String> result = getPlaceHolders(getPath("manySignFields_signed4.pdf"));
    List<String> expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields_signed4.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_4", "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_4", "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields_signed4.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields_signed4.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

    result = getPlaceHolders(getPath("manySignFields.pdf"));
    expectedResult = Arrays.asList("Signature_0", "Signature_1", "Signature_2", "Signature_3",
        "Signature_4", "Signature_5", "Signature_6", "Signature_7");
    Assert.assertEquals(expectedResult,result);

  }

  private static List<String> getPlaceHolders(String filePath) {
    try {
      PDDocument doc = PDDocument.load(new File(filePath));
      List<String> results = SignatureFieldsAndPlaceHolderExtractor.findEmptySignatureFields(doc);
//      System.out.println(filePath + ": " + result);
      return results;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  public static SignaturePlaceholderData getNextSignaturePlaceHolder(String filePath) {
    try {
      PDDocument doc = PDDocument.load(new File(filePath));
      SignaturePlaceholderData result =
          SignatureFieldsAndPlaceHolderExtractor.getNextUnusedSignaturePlaceHolder(doc);
//      System.out.println(filePath + ": " + result);
      return result;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

}
