package at.gv.egiz.pdfas.web.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Operation;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Source;
import lombok.SneakyThrows;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleVerifyServletTest {

	
	@BeforeClass
	public static void classInitializer() throws IOException {
		final String current = new java.io.File(".").getCanonicalPath();
		System.setProperty("pdf-as-web.conf", 
				current + "/src/test/resources/config/pdfas/pdf-as-web.properties");
	}

	@Before
	@SneakyThrows
	public void setup() {
		servlet.init(
			new MockServletConfig(
				new MockServletContext("src/main/webapp"),
				"VerifyServlet")
		);
	}


	@SuppressWarnings("unused") @Autowired private ExternSignServlet dummy; /* ExternSignServlet ctor does web config init */
	@Autowired private VerifyServlet servlet;
	
	
	@Test
	@SneakyThrows
	public void unsignedPdf() {
	  executeTest("/data/enc_own.pdf");
	}
	
	@Test
	@SneakyThrows
	public void unsignedWithSigField() {
	  executeTest("/data/placeholder_sigfield_and_qr.pdf");
	}
	
	@Test
    @SneakyThrows
    public void signedPdf() {
  	  executeTest("/data/dummy-pdf-signed.pdf");

    }
	
	
	

	@SneakyThrows
	private MockHttpServletResponse executeTest(String pdfResource) {
		val pdf = IOUtils.toByteArray(SimpleVerifyServletTest.class.getResourceAsStream(pdfResource));
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
	

	@SneakyThrows
	private void performTest(HttpServletRequest httpReq, HttpServletResponse httpResp, byte[] pdf) {
		
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
}
