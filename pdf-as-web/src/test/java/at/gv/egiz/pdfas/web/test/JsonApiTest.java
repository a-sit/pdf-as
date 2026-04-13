package at.gv.egiz.pdfas.web.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.Lombok;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "management.endpoint.metrics.enabled=true",
    "management.endpoints.web.exposure.include=metrics"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JsonApiTest {
  @Autowired MockMvc mvc;
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
  @SneakyThrows
  public void sign_single_jks() {
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
        new byte[]{'%','P','D','F','-'}, Arrays.copyOfRange(signedPDF, 0, 5));

    mvc.perform(
        get("/actuator/metrics/pdfas_requests")
            .param("tag", "operation:sign")
            .param("tag", "status:ok")
    )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.measurements[?(@.statistic == 'COUNT')].value").value(1.0));
  }

  @Test
  @SneakyThrows
  public void verify_single() {
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

    mvc.perform(
            get("/actuator/metrics/pdfas_requests")
                .param("tag", "operation:verify")
                .param("tag", "status:ok")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.measurements[?(@.statistic == 'COUNT')].value").value(1.0));
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
