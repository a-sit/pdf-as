package at.gv.egiz.pdfas.web.json_api;

import at.gv.egiz.pdfas.web.ws.PDFASSigningImpl;
import at.gv.egiz.pdfas.web.ws.PDFASVerificationImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Exposes the SOAP service implementations as Spring beans to new code */
@Configuration
public class SoapLogicBridgeBean {
  @Bean
  public PDFASSigningImpl signingImplBridge() { return new PDFASSigningImpl(); }
  @Bean
  public PDFASVerificationImpl verificationImplBridge() { return new PDFASVerificationImpl(); }
}
