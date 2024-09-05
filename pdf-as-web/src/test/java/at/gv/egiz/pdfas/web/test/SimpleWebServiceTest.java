package at.gv.egiz.pdfas.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import at.gv.egiz.pdfas.api.ws.PDFASSignParameters;
import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import at.gv.egiz.pdfas.api.ws.PDFASSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASSignResponse;
import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsValidationException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.PdfAsFactory;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.ws.PDFASSigningImpl;
import lombok.SneakyThrows;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleWebServiceTest {

  @BeforeClass
  public static void classInitializer() throws IOException {
    final String current = new java.io.File(".").getCanonicalPath();
    System.setProperty("pdf-as-web.conf", 
        current + "/src/test/resources/config/pdfas/pdf-as-web.properties");
    
    String webconfig = System.getProperty("pdf-as-web.conf");
    
    if(webconfig == null) {
      throw new RuntimeException("No web configuration provided!");
    }
    
    WebConfiguration.configure(webconfig);
    PdfAsHelper.init();
    
    try {
      PdfAsFactory.validateConfiguration((ISettings)PdfAsHelper.getPdfAsConfig());
      
    } catch (PdfAsSettingsValidationException e) {
      e.printStackTrace();
    } 
  }
  
  @Test
  @SneakyThrows
  public void sign() {     
    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/enc_own.pdf"));   
    PDFASSignResponse resp = executeTest(pdf);
    assertNotNull("signed doc", resp.getSignedPDF());
    assertEquals("sign check", 0, resp.getVerificationResponse().getValueCode());
    
  }
  
  @Test
  @SneakyThrows
  public void withSignatureFields() {     
    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/placeholder_sigfield_and_qr.pdf"));   
    PDFASSignResponse resp = executeTest(pdf);
    assertNotNull("signed doc", resp.getSignedPDF());
    assertEquals("sign check", 0, resp.getVerificationResponse().getValueCode());
    
  }
  
  @SneakyThrows
  private PDFASSignResponse executeTest(byte[] pdf) {
    PDFASSigningImpl service = new PDFASSigningImpl();
    
    PDFASSignRequest req = new PDFASSignRequest();
    req.setRequestID(UUID.randomUUID().toString());
    req.setInputData(pdf);
    PDFASSignParameters signParams = new PDFASSignParameters();
    signParams.setConnector(Connector.JKS);
    signParams.setTransactionId(UUID.randomUUID().toString());
    req.setParameters(signParams );
        
    PDFASSignResponse resp = service.signPDFDokument(req);    
    assertNotNull(resp);         
    return resp;
    
  }
}
