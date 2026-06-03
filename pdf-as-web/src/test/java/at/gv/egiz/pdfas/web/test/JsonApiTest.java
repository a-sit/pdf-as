package at.gv.egiz.pdfas.web.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tools.jackson.databind.json.JsonMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.Lombok;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(properties = {
    "management.endpoint.metrics.enabled=true",
    "management.endpoints.web.exposure.include=metrics"
})
@AutoConfigureMockMvc
public class JsonApiTest extends TestUtils.CanWatchOperationCount {
  @Autowired MockMvc mvc;
  @Autowired JsonMapper om;

  static {
    try {
      System.setProperty("pdf-as-web.conf",
          (new File(".").getCanonicalPath()) + "/src/test/resources/config/pdfas/pdf-as-web.properties");
    } catch (Throwable t) {
      throw Lombok.sneakyThrow(t);
    }
  }

  @Test
  @SneakyThrows
  public void sign_single_jks() {
    try (val watcher = OperationCountWatcher("operation:sign", "status:ok")) {
      final String pdf = Base64.getEncoder().encodeToString(
          IOUtils.toByteArray(JsonApiTest.class.getResourceAsStream("/data/enc_own.pdf")));

      final String signRequestID = UUID.randomUUID().toString();
      final String signRequest = om.writeValueAsString(
          Map.of(
              "requestID", signRequestID,
              "inputData", pdf,
              "parameters", Map.of(
                  "connector", "jks",
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

  @Test
  @SneakyThrows
  public void verify_single() {
    try (val watcher = OperationCountWatcher("operation:verify", "status:ok")) {
      final String pdf = Base64.getEncoder().encodeToString(
          IOUtils.toByteArray(JsonApiTest.class.getResourceAsStream("/data/dummy-pdf-signed.pdf")));

      final String verifyRequestID = UUID.randomUUID().toString();
      final String verifyRequest = om.writeValueAsString(
          Map.of(
              "requestID", verifyRequestID,
              "inputData", pdf,
              "verificationLevel", "intOnly"
          )
      );

      mvc.perform(
              post("/api/v2/verify")
                  .contentType(MediaType.APPLICATION_JSON)
                  .accept(MediaType.APPLICATION_JSON)
                  .content(verifyRequest)
          )
          .andExpect(status().isOk())
          .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.verifyResults").isArray())
          .andExpect(jsonPath("$.verifyResults.length()").value(1))
          .andExpect(jsonPath("$.verifyResults[0].requestID").value(verifyRequestID))
          .andExpect(jsonPath("$.verifyResults[0].error").isEmpty())
          .andExpect(jsonPath("$.verifyResults[0].signatureIndex").value(0))
          .andExpect(jsonPath("$.verifyResults[0].signedBy").value("CN=MOA-ID IDP (Test-Version),O=EGIZ,L=Graz,C=AT"));
    }
  }

  @Test
  @SneakyThrows
  public void openapi_docs_test() {
    mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.paths['/api/v2/sign/single']").exists())
        .andExpect(jsonPath("$.paths['/api/v2/sign/bulk']").exists())
        .andExpect(jsonPath("$.paths['/api/v2/sign/multiple']").exists())
        .andExpect(jsonPath("$.paths['/api/v2/sign/multiple/get-result']").exists())
        .andExpect(jsonPath("$.paths['/api/v2/verify']").exists());
  }
}
