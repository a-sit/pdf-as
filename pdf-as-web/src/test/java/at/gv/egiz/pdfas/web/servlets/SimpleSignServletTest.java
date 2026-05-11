package at.gv.egiz.pdfas.web.servlets;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import at.gv.egiz.pdfas.web.config.PdfAsWebSpringConfiguration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Operation;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Source;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

//@Ignore
@RunWith(SpringRunner.class)
@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class SimpleSignServletTest {

	
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
					"ExternSignServlet")
		);
	}

	@Autowired private ExternSignServlet servlet;

	@Test
	@SneakyThrows
	public void sigBlockParameterTest()  {
		
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

	@SneakyThrows
	private void performTest(HttpServletRequest httpReq, HttpServletResponse httpResp, byte[] pdf)  {
		StatisticEvent statisticEvent = new StatisticEvent();
		statisticEvent.setStartNow();
		statisticEvent.setSource(Source.WEB);
		statisticEvent.setOperation(Operation.SIGN);
		statisticEvent.setUserAgent(UserAgentFilter.getUserAgent());
		servlet.doSignature(httpReq, httpResp, pdf, statisticEvent);
		
	}
}
