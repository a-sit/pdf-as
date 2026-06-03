package at.gv.egiz.pdfas.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import at.gv.egiz.pdfas.api.ws.PDFASSigning;
import at.gv.egiz.pdfas.web.servlets.SimpleVerifyServletTest;
import jakarta.xml.ws.Service;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import at.gv.egiz.pdfas.api.ws.PDFASSignParameters;
import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import at.gv.egiz.pdfas.api.ws.PDFASSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASSignResponse;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.xml.namespace.QName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleWebServiceWithoutVerificationTest {

  @BeforeAll
  public static void classInitializer() throws IOException {
    final String current = new java.io.File(".").getCanonicalPath();
    System.setProperty("pdf-as-web.conf", 
        current + "/src/test/resources/config/pdfas/pdf-as-web-verify-disabled.properties");
  }

  @BeforeAll
  public static void jceWorkaround() {
    System.setProperty("javax.net.ssl.trustStoreType", "JKS");
  }

  @LocalServerPort
  int port;
  
  @Test
  @SneakyThrows
  public void sign() {     
    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/enc_own.pdf"));
    PDFASSignResponse resp = executeTest(pdf);
    assertNotNull("signed doc", resp.getSignedPDF());
    assertEquals("sign check", 0, resp.getVerificationResponse().getValueCode());
    assertNotNull("sigern Cert", resp.getVerificationResponse().getSignerCertificate());
    
  }
  
  @Test
  @SneakyThrows
  public void withSignatureFields() {     
    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/placeholder_sigfield_and_qr.pdf"));
    PDFASSignResponse resp = executeTest(pdf);
    assertNotNull("signed doc", resp.getSignedPDF());
    assertEquals("sign check", 0, resp.getVerificationResponse().getValueCode());
    assertNotNull("sigern Cert", resp.getVerificationResponse().getSignerCertificate());
    
  }
  
  @SneakyThrows
  private PDFASSignResponse executeTest(byte[] pdf) {
    val wsdl = new URL("http://localhost:"+port+"/services/wssign?wsdl");
    val serviceName = new QName(
        "http://ws.web.pdfas.egiz.gv.at/",
        "PDFASSigningImplService");
    val proxy = Service.create(wsdl, serviceName).getPort(PDFASSigning.class);
    
    PDFASSignRequest req = new PDFASSignRequest();
    req.setRequestID(UUID.randomUUID().toString());
    req.setInputData(pdf);
    PDFASSignParameters signParams = new PDFASSignParameters();
    signParams.setConnector(Connector.JKS);
    signParams.setTransactionId(UUID.randomUUID().toString());
    req.setParameters(signParams );
        
    PDFASSignResponse resp = proxy.signPDFDokument(req);
    assertNotNull(resp);         
    return resp;
    
  }
}
