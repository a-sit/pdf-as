package at.gv.egiz.pdfas.web.test;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RealTomcatTests {
  @LocalServerPort int port;

  @BeforeAll
  @SneakyThrows
  public static void classInitializer() {
    final String current = new java.io.File(".").getCanonicalPath();
    System.setProperty("pdf-as-web.conf",
        current + "/src/test/resources/config/pdfas/pdf-as-web.properties");
  }

  @BeforeAll
  public static void jceWorkaround() {
    System.setProperty("javax.net.ssl.trustStoreType", "JKS");
  }

  @Test
  @SneakyThrows
  public void fileErrorOnNoDocument() {
    byte[] pdf = IOUtils.toByteArray(RealTomcatTests.class.getResourceAsStream("/data/enc_own.pdf"));
    val boundary = "----TEST";
    val prefix = (
          "--"+boundary+"\r\nContent-Disposition: form-data; name=\"source\"\r\n\r\ninternal\r\n"+
              "--"+boundary+"\r\nContent-Disposition: form-data; name=\"connector\"\r\n\r\nmobilebku\r\n"+
              "--"+boundary+"\r\nContent-Disposition: form-data; name=\"pdf-file\"; filename=\"\"\r\nContent-Type: application/pdf\r\n\r\n"
      ).getBytes(StandardCharsets.UTF_8);
    val suffix = (
        "\r\n--"+boundary+"--\r\n"
      ).getBytes(StandardCharsets.UTF_8);
    val multipartBody = List.of(prefix, pdf, suffix);

    val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:"+port+"/Sign"))
        .header("Content-Type", "multipart/form-data; boundary="+boundary)
        .POST(HttpRequest.BodyPublishers.ofByteArrays(multipartBody))
        .build();

    val response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    assertEquals(200, response.statusCode());
    assertTrue("Should contain redirect to a-trust", response.body().contains("https-security-layer-request"));
  }
}
