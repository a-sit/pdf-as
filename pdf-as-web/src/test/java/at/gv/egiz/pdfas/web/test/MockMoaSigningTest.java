package at.gv.egiz.pdfas.web.test;

import at.gv.e_government.reference.namespace.moa._20020822_.*;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner;
import at.gv.egiz.pdfas.lib.impl.configuration.ConfigurationImpl;
import at.gv.egiz.pdfas.moa.MOAConnector;
import at.gv.egiz.pdfas.sigs.pades.PAdESSignerKeystore;
import at.gv.egiz.pdfas.sigs.pkcs7detached.PKCS7DetachedSigner;
import at.gv.egiz.pdfas.web.config.PdfAsWebSpringConfiguration;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.servlets.ExternSignServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import iaik.x509.X509Certificate;
import jakarta.jws.WebService;
import jakarta.xml.ws.Endpoint;
import lombok.Lombok;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest(properties = {
    "management.endpoint.metrics.enabled=true",
    "management.endpoints.web.exposure.include=metrics"
})
@AutoConfigureMockMvc
public class MockMoaSigningTest {
  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;
  @Autowired PdfAsWebSpringConfiguration config;

  static {
    try {
      System.setProperty("pdf-as-web.conf",
          (new File(".").getCanonicalPath()) + "/src/test/resources/config/pdfas/pdf-as-web.properties");
    } catch (Throwable t) {
      throw Lombok.sneakyThrow(t);
    }
  }

  @BeforeClass
  public static void jceWorkaround() {
    System.setProperty("javax.net.ssl.trustStoreType", "JKS");
  }

  @WebService(
      serviceName = "SignatureCreationService",
      portName = "SignatureCreationPort",
      targetNamespace = "http://reference.e-government.gv.at/namespace/moa/20020822#",
      endpointInterface =
          "at.gv.e_government.reference.namespace.moa._20020822_.SignatureCreationPortType")
  class MockMoa implements AutoCloseable, SignatureCreationPortType {
    @SneakyThrows
    private static int freePort() {
      try (ServerSocket socket = new ServerSocket(0)) {
        return socket.getLocalPort();
      }
    }
    private static String azstring(int length) {
      return
          new Random().ints(97,123).limit(length)
              .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
              .toString();
    }
    public final int port = freePort();
    public final String endpointURL = "http://127.0.0.1:"+port+"/moa-spss/services/SignatureCreation";
    public final Endpoint endpoint =
        Endpoint.publish(endpointURL, this);
    public final String keyIdentifier = azstring(16);

    public final IPlainSigner signer;

    @SneakyThrows
    private Properties getBaseProperties() {
      try (InputStream in = new FileInputStream(config.getPdfAsWebConfPath())) {
        val props = new Properties();
        props.load(in);
        return props;
      }
    }

    @SneakyThrows
    private void injectProperties(Map<String, String> overlay) {
      val props = getBaseProperties();
      if (overlay != null) overlay.forEach(props::setProperty);
      try (val out = new ByteArrayOutputStream()) {
        props.store(out, "test config");
        try (val in = new ByteArrayInputStream(out.toByteArray())) {
          WebConfiguration.configure(in);
          PdfAsHelper.reloadConfig();
          PdfAsHelper.init();
        }
      }
    }

    @SneakyThrows
    public MockMoa() {
      try {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream is = MockMoaSigningTest.class.getResourceAsStream("/config/pdfas/test.p12")) {
          ks.load(is, "123456".toCharArray());
        }
        val alias = ks.aliases().nextElement();
        val privateKey = (PrivateKey) ks.getKey(alias, "123456".toCharArray());
        val certificate = new X509Certificate(ks.getCertificate(alias).getEncoded());
        signer = new PAdESSignerKeystore(privateKey, certificate);
      } catch (Exception e) {
        throw Lombok.sneakyThrow(e);
      }

      // inject ourselves into the configuration
      injectProperties(Map.of(
          "moal."+keyIdentifier+".enabled", "true",
          "moal."+keyIdentifier+".url", endpointURL,
          "moal."+keyIdentifier+".timeout", "5000",
          "moal."+keyIdentifier+".KeyIdentifier", "KG_TEST",
          "moal."+keyIdentifier+".Certificate",
            "base64:"+Base64.getEncoder().encodeToString(signer.getCertificate(null).getEncoded())
      ));
    }

    @Override
    public CreateCMSSignatureResponseType createCMSSignature(CreateCMSSignatureRequest body) throws MOAFault {
      val signatureInfoList = body.getSingleSignatureInfo();
      Assertions.assertEquals(1, signatureInfoList.size());
      val signatureInfo = signatureInfoList.get(0);
      val dataObjectInfo = signatureInfo.getDataObjectInfo();
      Assertions.assertEquals("detached", dataObjectInfo.getStructure());
      val dataObject = dataObjectInfo.getDataObject();
      Assertions.assertEquals("application/pdf", dataObject.getMetaInfo().getMimeType());
      val content = dataObject.getContent().getBase64Content();
      Assertions.assertNotEquals(0, content.length);
      Assertions.assertEquals("KG_TEST", body.getKeyIdentifier());
      try {
        val cms = signer.sign(content, null, null, null);
        val response = new CreateCMSSignatureResponseType();
        response.getCMSSignatureOrErrorResponse().add(cms);
        return response;
      } catch (PdfAsException e) {
        throw new MOAFault("Failed to create detached CMS in fake MOA", e);
      }
    }

    @Override
    public CreateXMLSignatureResponseType createXMLSignature(CreateXMLSignatureRequest body) throws MOAFault {
      throw new IllegalStateException("We do not create XML signatures in this house.");
    }

    public void close() {
      endpoint.stop();
      // remove the injected overlay
      injectProperties(null);
    }
  }

  @Test
  @SneakyThrows
  public void signWithMockMOA() {
    try (val watcher = TestUtils.OperationCountWatcher(mvc, "operation:sign", "status:ok")) {
      try (MockMoa moa = new MockMoa()) {

        final String pdf = Base64.getEncoder().encodeToString(
            IOUtils.toByteArray(JsonApiTest.class.getResourceAsStream("/data/enc_own.pdf")));

        final String signRequestID = UUID.randomUUID().toString();
        final String signRequest = om.writeValueAsString(
            Map.of(
                "requestID", signRequestID,
                "inputData", pdf,
                "parameters", Map.of(
                    "connector", "moa",
                    "keyIdentifier", moa.keyIdentifier,
                    "transactionId", UUID.randomUUID().toString()
                )
            )
        );

        final String signResponse = mvc.perform(
                post("/api/v2/sign/single")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(signRequest)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.requestID").value(signRequestID))
            .andExpect(jsonPath("$.signedPDF").isNotEmpty())
            .andExpect(jsonPath("$.verificationResponse").exists())
            .andReturn().getResponse().getContentAsString();

        final byte[] signedPDF = Base64.getDecoder().decode(JsonPath.<String>read(signResponse, "$.signedPDF"));
        assertArrayEquals("Signed data looks PDF-ish (%PDF- header)",
            new byte[]{'%', 'P', 'D', 'F', '-'}, Arrays.copyOfRange(signedPDF, 0, 5));
      }
    }
  }

  @Test
  @SneakyThrows
  public void moaTimeout() {
    try (MockMoa moa = new MockMoa() {
      @Override
      @SneakyThrows
      public CreateCMSSignatureResponseType createCMSSignature(CreateCMSSignatureRequest body) throws MOAFault {
        // this will cause a timeout
        Thread.sleep(10 * 1000);
        throw new RuntimeException("unreachable");
      }
    }) {
      final String pdf = Base64.getEncoder().encodeToString(
          IOUtils.toByteArray(JsonApiTest.class.getResourceAsStream("/data/enc_own.pdf")));

      final String signRequestID = UUID.randomUUID().toString();
      final String signRequest = om.writeValueAsString(
          Map.of(
              "requestID", signRequestID,
              "inputData", pdf,
              "parameters", Map.of(
                  "connector", "moa",
                  "keyIdentifier", moa.keyIdentifier,
                  "transactionId", UUID.randomUUID().toString()
              )
          )
      );

      mvc.perform(
              post("/api/v2/sign/single")
                  .contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON)
                  .content(signRequest)
          )
          .andExpect(status().isOk())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.requestID").value(signRequestID))
          .andExpect(jsonPath("$.signedPDF").isEmpty())
          .andExpect(jsonPath("$.errorCode").value(11022));
    }
  }
}
