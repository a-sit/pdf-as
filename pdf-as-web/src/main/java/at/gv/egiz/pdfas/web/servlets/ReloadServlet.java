package at.gv.egiz.pdfas.web.servlets;

import java.io.IOException;
import java.io.OutputStream;

import at.gv.egiz.pdfas.web.config.PdfAsWebSpringConfiguration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServletRegistration(urlMappings = "/Reload")
public class ReloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6108555300743896727L;

	public static final String PARAM_PASSWD = "PASSWD";
	
	private final PdfAsWebSpringConfiguration config;
	public ReloadServlet(final PdfAsWebSpringConfiguration config) {
		super();
		this.config = config;
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		if(!WebConfiguration.getReloadEnabled()) {
			log.info("Reload Servlet disabled. " + request.getRemoteAddr() + " tried to call it");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentLength(0);
			return;
		}
		
		log.info("Called Reload Servlet from: " + request.getRemoteAddr());
		
		log.info("Checking Password!");
		
		String pwd = request.getParameter(PARAM_PASSWD);
		
		if(pwd == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentLength(0);
			return;
		}
		
		if(!pwd.equals(WebConfiguration.getReloadPassword())) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentLength(0);
			return;
		}
		
		String webconfig = config.getPdfAsWebConfPath();
		WebConfiguration.configure(webconfig);
		PdfAsHelper.reloadConfig();
		
		log.info("Reloaded!");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head></head><body>OK</body></html>");
		
		response.setContentType("text/html");
		OutputStream os = response.getOutputStream();
		os.write(sb.toString().getBytes());
		os.close();
	}
}
