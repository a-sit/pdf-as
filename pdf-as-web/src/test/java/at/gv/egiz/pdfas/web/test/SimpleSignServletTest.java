package at.gv.egiz.pdfas.web.test;

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
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import at.gv.egiz.pdfas.web.servlets.ExternSignServlet;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Operation;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Source;

//@Ignore
@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleSignServletTest {

	
	@BeforeClass
	public static void classInitializer() throws IOException {
		final String current = new java.io.File(".").getCanonicalPath();
		System.setProperty("pdf-as-web.conf", 
				current + "/src/test/resources/config/pdfas/pdf-as-web.properties");
		
	}
	
	
	@Test
	public void sigBlockParameterTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, IOException, ServletException {	
		
		byte[] pdf = IOUtils.toByteArray(SimpleSignServletTest.class.getResourceAsStream("/data/enc_own.pdf"));		
		MockHttpServletRequest httpReq = new MockHttpServletRequest("POST", "https://localhost/pdfas");
		MockHttpServletResponse httpResp = new MockHttpServletResponse();		
		httpReq.setAttribute("REQ_DATA_URL", true);
		httpReq.setAttribute("connector", "jks");
		
		httpReq.setAttribute("sbp:schule", "Güssing");				
		
		
		
		// perform operation
		performTest(httpReq, httpResp, pdf);
		
		
		//validate state
		assertNotNull("httpResp", httpResp);
		assertNotNull("no dataUrl redirect", httpResp.getHeader("Location"));
		
	}


	private void performTest(HttpServletRequest httpReq, HttpServletResponse httpResp, byte[] pdf) throws NoSuchMethodException, 
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException {
		ExternSignServlet servlet = new ExternSignServlet();
		ServletConfig servletConfig = buildServletConfig();
		servlet.init(servletConfig);
		
		Method method = servlet.getClass().getDeclaredMethod("doSignature", 
				HttpServletRequest.class, HttpServletResponse.class, byte[].class, StatisticEvent.class);
		method.setAccessible(true);
		StatisticEvent statisticEvent = new StatisticEvent();
		statisticEvent.setStartNow();
		statisticEvent.setSource(Source.WEB);
		statisticEvent.setOperation(Operation.SIGN);
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
