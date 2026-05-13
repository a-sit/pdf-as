package at.gv.egiz.pdfas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PdfAsWeb extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(PdfAsWeb.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PdfAsWeb.class, args);
  }
}
