package at.gv.egiz.pdfas.web.test;

import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringIntegrationTest {
  @BeforeAll
  @SneakyThrows
  public static void classInitializer() {
    final String current = new java.io.File(".").getCanonicalPath();
    System.setProperty("pdf-as-web.conf",
        current + "/src/test/resources/config/pdfas/pdf-as-web.properties");
  }

  @Value("${dummy.springtest.parameter:#{null}}")
  @Getter
  String dummy;

  @Test
  @SneakyThrows
  public void springPropertySourceTest() {
    // this is set in pdf-as-web.properties in the test resources, as loaded by the class initializer
    Assertions.assertEquals("42", dummy);
  }

}
