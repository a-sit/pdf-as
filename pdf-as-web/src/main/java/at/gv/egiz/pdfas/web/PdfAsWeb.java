package at.gv.egiz.pdfas.web;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PdfAsWeb extends SpringBootServletInitializer {

  static {
    // compatibility layer: map the old logback.configurationFile to the new logging.config
    final String SPRING_LOG_CFG = "logging.config";
    final String LEGACY_LOGBACK_LOG_CFG = "logback.configurationFile";
    if (System.getProperty(SPRING_LOG_CFG) == null) {
      val legacyValue = System.getProperty(LEGACY_LOGBACK_LOG_CFG);
      if (legacyValue != null) {
        System.setProperty(SPRING_LOG_CFG, legacyValue);
      }
    }
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PdfAsWeb.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PdfAsWeb.class, args);
  }
}
