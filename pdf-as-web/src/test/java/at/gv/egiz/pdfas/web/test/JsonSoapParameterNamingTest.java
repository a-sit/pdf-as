package at.gv.egiz.pdfas.web.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import at.gv.egiz.pdfas.api.ws.PDFASSignParameters;
import at.gv.egiz.pdfas.api.ws.PDFASSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASVerifyRequest;
import at.gv.egiz.pdfas.api.ws.PdfasSignMultipleRequest;
import at.gv.egiz.pdfas.api.ws.PdfasSignMultipleResponse;
import at.gv.egiz.pdfas.api.ws.PdfasSignedDocument;
import at.gv.egiz.pdfas.api.ws.VerificationLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.Lombok;
import org.junit.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Verifies that the JSON API wire names match the SOAP/JAXB wire names.
 *
 * <p>The web documentation says that the JSON API parameters are identical to the SOAP parameters. These
 * tests lock that down for fields where the Java bean property name differs from the JAXB name, such as
 * {@code invoke-url}, {@code preprocessorArguments}, {@code configurationOverrides}, and {@code documents}.
 *
 * <p>Expected current-state behavior: this test fails until the SOAP DTOs either carry explicit Jackson
 * annotations or the web ObjectMapper is configured to honor JAXB annotations for JSON naming. Keep old JSON
 * names as aliases when implementing the fix so existing clients remain compatible.
 */
@RunWith(SpringRunner.class)
@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest(properties = {
    "management.endpoint.metrics.enabled=true",
    "management.endpoints.web.exposure.include=metrics"
})
public class JsonSoapParameterNamingTest {
  @Autowired ObjectMapper om;

  static {
    try {
      System.setProperty("pdf-as-web.conf",
          (new File(".").getCanonicalPath()) + "/src/test/resources/config/pdfas/pdf-as-web.properties");
    } catch (Throwable t) {
      throw Lombok.sneakyThrow(t);
    }
  }

  @Test
  public void signSingleUsesSoapParameterNamesForInputAndOutput() throws Exception {
    final byte[] input = "pdf".getBytes(StandardCharsets.UTF_8);
    final String json = "{"
        + "\"requestID\":\"req-1\","
        + "\"inputData\":\"" + Base64.getEncoder().encodeToString(input) + "\","
        + "\"verificationLevel\":\"intOnly\","
        + "\"parameters\":{"
        + "\"connector\":\"jks\","
        + "\"position\":\"x:auto;y:auto;w:auto;p:auto;f:0\","
        + "\"profile\":\"SIGNATURBLOCK_DE\","
        + "\"invoke-url\":\"https://example.invalid/success\","
        + "\"invoke-target\":\"_self\","
        + "\"invoke-error-url\":\"https://example.invalid/error\","
        + "\"transactionId\":\"tx-1\","
        + "\"keyIdentifier\":\"key-1\","
        + "\"qrCodeContent\":\"QR-CONTENT\","
        + "\"preprocessorArguments\":{\"propertyEntries\":[{\"key\":\"pre\",\"value\":\"one\"}]},"
        + "\"configurationOverrides\":{\"propertyEntries\":[{\"key\":\"cfg\",\"value\":\"two\"}]}"
        + "},"
        + "\"signatureBlockParameter\":{\"subject\":\"Test User\"}"
        + "}";

    final PDFASSignRequest request = om.readValue(json, PDFASSignRequest.class);

    assertEquals("req-1", request.getRequestID());
    assertArrayEquals(input, request.getInputData());
    assertEquals(VerificationLevel.INTEGRITY_ONLY, request.getVerificationLevel());
    assertEquals("Test User", request.getSignatureBlockParameters().get("subject"));

    final PDFASSignParameters parameters = request.getParameters();
    assertNotNull(parameters);
    assertEquals(PDFASSignParameters.Connector.JKS, parameters.getConnector());
    assertEquals("https://example.invalid/success", parameters.getInvokeURL());
    assertEquals("_self", parameters.getInvokeTarget());
    assertEquals("https://example.invalid/error", parameters.getInvokeErrorURL());
    assertEquals("tx-1", parameters.getTransactionId());
    assertEquals("key-1", parameters.getKeyIdentifier());
    assertEquals("QR-CONTENT", parameters.getQRCodeContent());
    assertEquals("one", parameters.getPreprocessor().getMap().get("pre"));
    assertEquals("two", parameters.getOverrides().getMap().get("cfg"));

    final JsonNode serialized = om.valueToTree(request);
    assertTrue(serialized.has("signatureBlockParameter"));
    assertFalse(serialized.has("signatureBlockParameters"));

    final JsonNode serializedParameters = serialized.get("parameters");
    assertJsonHasOnlySoapName(serializedParameters, "invoke-url", "invokeURL", "invokeUrl");
    assertJsonHasOnlySoapName(serializedParameters, "invoke-target", "invokeTarget");
    assertJsonHasOnlySoapName(serializedParameters, "invoke-error-url", "invokeErrorURL", "invokeErrorUrl");
    assertJsonHasOnlySoapName(serializedParameters, "preprocessorArguments", "preprocessor");
    assertJsonHasOnlySoapName(serializedParameters, "configurationOverrides", "overrides");
    assertJsonHasOnlySoapName(serializedParameters, "qrCodeContent", "QRCodeContent", "qrcodecontent");
    assertEquals("jks", serializedParameters.get("connector").asText());
  }

  @Test
  public void signMultipleUsesSoapDocumentsNameForInputAndOutput() throws Exception {
    final byte[] input = "pdf-1".getBytes(StandardCharsets.UTF_8);
    final String json = "{"
        + "\"requestID\":\"multi-1\","
        + "\"transactionId\":\"tx-multi\","
        + "\"connector\":\"mobilebku\","
        + "\"invoke-url\":\"https://example.invalid/success\","
        + "\"invoke-target\":\"_top\","
        + "\"invoke-error-url\":\"https://example.invalid/error\","
        + "\"keyIdentifier\":\"kid\","
        + "\"preprocessorArguments\":{\"propertyEntries\":[{\"key\":\"pre\",\"value\":\"one\"}]},"
        + "\"configurationOverrides\":{\"propertyEntries\":[{\"key\":\"cfg\",\"value\":\"two\"}]},"
        + "\"signatureBlockParameter\":{\"subject\":\"Test User\"},"
        + "\"verificationLevel\":\"full\","
        + "\"documents\":[{"
        + "\"inputData\":\"" + Base64.getEncoder().encodeToString(input) + "\","
        + "\"fileName\":\"one.pdf\","
        + "\"position\":\"x:auto;y:auto\","
        + "\"qrCodeContent\":\"QR\","
        + "\"profile\":\"SIGNATURBLOCK_DE\""
        + "}]"
        + "}";

    final PdfasSignMultipleRequest request = om.readValue(json, PdfasSignMultipleRequest.class);

    assertEquals("multi-1", request.getRequestID());
    assertEquals("tx-multi", request.getTransactionId());
    assertEquals(PDFASSignParameters.Connector.MOBILEBKU, request.getConnector());
    assertEquals("https://example.invalid/success", request.getInvokeUrl());
    assertEquals("_top", request.getInvokeTarget());
    assertEquals("https://example.invalid/error", request.getInvokeErrorUrl());
    assertEquals("kid", request.getKeyIdentifier());
    assertEquals("one", request.getPreprocessor().getMap().get("pre"));
    assertEquals("two", request.getOverrides().getMap().get("cfg"));
    assertEquals("Test User", request.getSignatureBlockParameters().get("subject"));
    assertEquals(VerificationLevel.FULL_CERT_PATH, request.getVerificationLevel());
    assertEquals(1, request.getInput().size());
    assertArrayEquals(input, request.getInput().get(0).getInputData());
    assertEquals("one.pdf", request.getInput().get(0).getFileName());

    final JsonNode serialized = om.valueToTree(request);
    assertJsonHasOnlySoapName(serialized, "invoke-url", "invokeUrl");
    assertJsonHasOnlySoapName(serialized, "invoke-target", "invokeTarget");
    assertJsonHasOnlySoapName(serialized, "invoke-error-url", "invokeErrorUrl");
    assertJsonHasOnlySoapName(serialized, "preprocessorArguments", "preprocessor");
    assertJsonHasOnlySoapName(serialized, "configurationOverrides", "overrides");
    assertJsonHasOnlySoapName(serialized, "signatureBlockParameter", "signatureBlockParameters");
    assertJsonHasOnlySoapName(serialized, "documents", "input");
    assertEquals("mobilebku", serialized.get("connector").asText());

    final PdfasSignedDocument signedDocument = new PdfasSignedDocument();
    signedDocument.setFileName("one.pdf");
    signedDocument.setOutputData("signed".getBytes(StandardCharsets.UTF_8));

    final PdfasSignMultipleResponse response = new PdfasSignMultipleResponse();
    response.setRequestID("multi-1");
    response.setOutput(List.of(signedDocument));

    final JsonNode serializedResponse = om.valueToTree(response);
    assertJsonHasOnlySoapName(serializedResponse, "documents", "output");
    assertEquals("one.pdf", serializedResponse.get("documents").get(0).get("fileName").asText());
  }

  @Test
  public void verifyUsesSoapPreprocessorArgumentsNameForInputAndOutput() throws Exception {
    final byte[] input = "signed-pdf".getBytes(StandardCharsets.UTF_8);
    final String json = "{"
        + "\"requestID\":\"verify-1\","
        + "\"inputData\":\"" + Base64.getEncoder().encodeToString(input) + "\","
        + "\"verificationLevel\":\"intOnly\","
        + "\"signatureIndex\":0,"
        + "\"preprocessorArguments\":{\"propertyEntries\":[{\"key\":\"pre\",\"value\":\"one\"}]}"
        + "}";

    final PDFASVerifyRequest request = om.readValue(json, PDFASVerifyRequest.class);

    assertEquals("verify-1", request.getRequestID());
    assertArrayEquals(input, request.getInputData());
    assertEquals(VerificationLevel.INTEGRITY_ONLY, request.getVerificationLevel());
    assertEquals(Integer.valueOf(0), request.getSignatureIndex());
    assertEquals("one", request.getPreprocessor().getMap().get("pre"));

    final JsonNode serialized = om.valueToTree(request);
    assertJsonHasOnlySoapName(serialized, "preprocessorArguments", "preprocessor");
  }

  private static void assertJsonHasOnlySoapName(JsonNode node, String soapName, String... oldJsonNames) {
    assertTrue("Expected SOAP/JAXB JSON property " + soapName + " in " + node, node.has(soapName));
    for (String oldJsonName : oldJsonNames) {
      assertFalse("Did not expect legacy Java-bean JSON property " + oldJsonName + " in " + node,
          node.has(oldJsonName));
    }
  }
}
