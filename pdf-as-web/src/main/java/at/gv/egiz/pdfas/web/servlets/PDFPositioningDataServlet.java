package at.gv.egiz.pdfas.web.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;

public class PDFPositioningDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8729046942909293640L;

	public static final String PARAM_TOKEN = "token";

	private static final Logger logger = LoggerFactory
			.getLogger(PDFPositioningDataServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.process(request, response);
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			logger.debug("Access to pdf data");
			String token = request.getParameter(PARAM_TOKEN);

			if (token == null) {
				logger.warn("No access token provided");
				throw new PdfAsException("No access token provided");
			}

			logger.debug("Access to pdf data with token: {}", token);
			
			byte[] pdfData = PdfAsHelper.getPdfData(request);
			
			if(pdfData == null) {
				logger.warn("No PDF Data available or token does not match");
				throw new PdfAsException("No PDF Data available");
			}
			
			response.setContentType("application/pdf");
			OutputStream os = response.getOutputStream();
			os.write(pdfData);
			os.close();
		} catch (Throwable e) {
			PdfAsHelper.clearPdfData(request);
			logger.error("Failed to access pdf data for positioning!");
			PdfAsHelper.setSessionException(request, response, e.getMessage(),
					e);
			PdfAsHelper.gotoError(getServletContext(), request, response);
		}
	}
}
