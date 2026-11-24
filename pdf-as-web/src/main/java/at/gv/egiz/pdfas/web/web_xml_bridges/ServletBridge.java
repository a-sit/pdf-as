package at.gv.egiz.pdfas.web.web_xml_bridges;

import lombok.NonNull;
import at.gv.egiz.pdfas.web.servlets.*;
import jakarta.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Takes the old web.xml servlet mappings and exposes them to Spring Boot */
@Configuration
public class ServletBridge {
  @Bean
  public @NonNull ServletRegistrationBean<Servlet> cxfServlet() {
    return new ServletRegistrationBean<>(
        /** from <servlet> */         new SoapServiceServlet(),
        /** from <servlet-mapping> */ "/services/*"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> visBlockServlet() {
    return new ServletRegistrationBean<>(
        new VisBlockServlet(),
        "/visblock"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> providePDF() {
    return new ServletRegistrationBean<>(
        new ProvidePDFServlet(),
        "/ProvidePDF"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> dataURLServlet() {
    return new ServletRegistrationBean<>(
        new DataURLServlet(),
        "/DataURL"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> slDataURLServlet() {
    return new ServletRegistrationBean<>(
        new SLDataURLServlet(),
        "/DataURLSL20"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> pdfData() {
    return new ServletRegistrationBean<>(
        new PDFData(),
        "/PDFData"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> errorPage() {
    return new ServletRegistrationBean<>(
        new ErrorPage(),
        "/ErrorPage"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> pdfVerifyData() {
    return new ServletRegistrationBean<>(
        new PDFSignatureData(),
        "/signData"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> pdfVerifyCert() {
    return new ServletRegistrationBean<>(
        new PDFSignatureCertificateData(),
        "/signCert"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> uiEntryPointServlet() {
    return new ServletRegistrationBean<>(
        new UIEntryPointServlet(),
        "/userentry"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> qrPlaceholderGenerator() {
    return new ServletRegistrationBean<>(
        new PlaceholderGeneratorServlet(),
        "/placeholder"
    );
  }

  @Bean
  public @NonNull ServletRegistrationBean<Servlet> jsonAPIServlet() {
    return new ServletRegistrationBean<>(
        new JSONAPIServlet(),
        "/api/v1/sign"
    );
  }
}
