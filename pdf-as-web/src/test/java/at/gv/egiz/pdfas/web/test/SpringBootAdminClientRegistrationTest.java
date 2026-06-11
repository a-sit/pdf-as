package at.gv.egiz.pdfas.web.test;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.sun.net.httpserver.HttpServer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = {
        "spring.boot.admin.client.enabled=true",
        "spring.boot.admin.client.period=250ms",
        "spring.boot.admin.client.instance.service-base-url=https://example.org/pdf-as-web",
    }
)
public class SpringBootAdminClientRegistrationTest {
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

  private static final MockAdminServer adminServer = new MockAdminServer();
  @AfterAll
  public static void stopServer() { adminServer.stop(); }
  @DynamicPropertySource
  public static void setAdminServerPath(DynamicPropertyRegistry registry) {
    registry.add("spring.boot.admin.client.url", adminServer::url);
  }

  private static final class MockAdminServer {
    private final HttpServer server;
    public final AtomicReference<String> method = new AtomicReference<>();
    public final AtomicReference<String> path = new AtomicReference<>();
    public final AtomicReference<String> body = new AtomicReference<>();
    public final CountDownLatch requestReceived = new CountDownLatch(1);
    @SneakyThrows
    MockAdminServer() {
      server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
      server.createContext("/instances", this::handleRequest);
      server.start();
    }

    public String url() { return "http://127.0.0.1:"+server.getAddress().getPort(); }

    public void stop() { server.stop(0); }

    private void handleRequest(HttpExchange exchange) throws IOException {
      method.set(exchange.getRequestMethod());
      path.set(exchange.getRequestURI().getPath());
      try (val buffer = new ByteArrayOutputStream()) {
        exchange.getRequestBody().transferTo(buffer);
        body.set(buffer.toString(StandardCharsets.UTF_8));
      }
      byte[] response = "{\"id\":\"pdf-as-web\"}".getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().add("Content-Type","application/json");
      exchange.sendResponseHeaders(201, response.length);
      exchange.getResponseBody().write(response);
      exchange.close();
      requestReceived.countDown();
    }
  }

  @Autowired
  JsonMapper om;

  @Test
  @SneakyThrows
  public void springBootAdminRegistrationTest() {
    Assertions.assertTrue(adminServer.requestReceived.await(5, TimeUnit.SECONDS), "A request to SpringBoot Admin was made");
    Assertions.assertEquals("POST", adminServer.method.get());
    Assertions.assertEquals("/instances", adminServer.path.get());

    val body = om.readValue(adminServer.body.get(), new TypeReference<Map<String, Object>>(){});
    Assertions.assertEquals("PDF-AS Web", body.get("name"));
    Assertions.assertEquals("https://example.org/pdf-as-web/", body.get("serviceUrl"));
  }
}
