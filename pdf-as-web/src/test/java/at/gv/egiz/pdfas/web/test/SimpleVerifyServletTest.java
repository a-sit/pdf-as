package at.gv.egiz.pdfas.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsValidationException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.PdfAsFactory;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.servlets.VerifyServlet;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Operation;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Source;
import lombok.SneakyThrows;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleVerifyServletTest {

	
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
	public void unsignedPdf() {			
		byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/enc_own.pdf"));		
		executeTest(pdf);
		
	}
	
	 @Test
	  @SneakyThrows
	  public void unsignedWithSigField() {     
	    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/placeholder_sigfield_and_qr.pdf"));   
	    executeTest(pdf);
	    
	  }
	
	@Test
  @SneakyThrows
  public void signedPdf() {     
    byte[] pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream("/data/dummy-pdf-signed.pdf"));   
    executeTest(pdf);
    
  }
	
	
	

	@SneakyThrows
	private MockHttpServletResponse executeTest(byte[] pdf) {
	   MockHttpServletRequest httpReq = new MockHttpServletRequest("POST", "https://localhost/pdfas");
	    MockHttpServletResponse httpResp = new MockHttpServletResponse();   
	      
	    // perform operation
	    performTest(httpReq, httpResp, pdf);
	      
	    //validate state
	    assertNotNull("httpResp", httpResp);
	    assertEquals("httpStatus", 200, httpResp.getStatus());
	    String body = httpResp.getContentAsString();
	    assertFalse("Empty body", StringUtils.isEmpty(body));
	    
	    return httpResp;
	  
	}
	

	private void performTest(HttpServletRequest httpReq, HttpServletResponse httpResp, byte[] pdf) throws NoSuchMethodException, 
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException {
	  VerifyServlet  servlet = new VerifyServlet ();
		ServletConfig servletConfig = buildServletConfig();
		servlet.init(servletConfig);
		
		Method method = servlet.getClass().getDeclaredMethod("doVerify", 
				HttpServletRequest.class, HttpServletResponse.class, byte[].class, StatisticEvent.class);
		method.setAccessible(true);
		StatisticEvent statisticEvent = new StatisticEvent();
		statisticEvent.setStartNow();
		statisticEvent.setSource(Source.WEB);
		statisticEvent.setOperation(Operation.VERIFY);
		statisticEvent.setUserAgent(UserAgentFilter.getUserAgent());
		
		method.invoke(servlet, httpReq, httpResp, pdf, statisticEvent);
		
	}


	private ServletConfig buildServletConfig() {
		
		return new ServletConfig() {
			
			private ServletContext servletContext = null; 
			
			@Override
			public String getServletName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ServletContext getServletContext() {
				return servletContext;
			}
			
			@Override
			public Enumeration<String> getInitParameterNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getInitParameter(String name) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
