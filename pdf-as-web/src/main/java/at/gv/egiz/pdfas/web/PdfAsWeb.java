package at.gv.egiz.pdfas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PdfAsWeb extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder createSpringApplicationBuilder() {
    SpringApplicationBuilder builder = new SpringApplicationBuilder();
    builder.sources(PdfAsWeb.class);
    return builder;
    
  }
  
  public static void main(String[] args) {
    log.info("=============== Initializing Spring-Boot context! ===============");
    final SpringApplication springApp = new SpringApplication(PdfAsWeb.class);
    
    log.debug("Run SpringBoot initialization process ... ");
    springApp.run(args);
    
    log.info("Initialization of PDF-AS-Web finished.");

  }
}
