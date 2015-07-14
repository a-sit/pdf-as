package at.gv.egiz.pdfas.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.exception.PdfAsWebException;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.helper.PdfAsParameterExtractor;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticFrontend;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Status;

public class SignNowServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8729046942909293640L;

	private static final Logger logger = LoggerFactory
			.getLogger(SignNowServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug("Get signing request");
		
		StatisticEvent statisticEvent = PdfAsHelper.getStatisticEvent(request,
				response);
		
		try {
			this.process(request, response, statisticEvent);
		} catch (Exception e) {

			statisticEvent.setStatus(Status.ERROR);
			statisticEvent.setException(e);
			if (e instanceof PDFASError) {
				statisticEvent.setErrorCode(((PDFASError) e).getCode());
			}
			statisticEvent.setEndNow();
			statisticEvent.setTimestampNow();
			StatisticFrontend.getInstance().storeEvent(statisticEvent);
			statisticEvent.setLogged(true);

			PdfAsHelper.setSessionException(request, response, e.getMessage(),
					e);
			PdfAsHelper.gotoError(getServletContext(), request, response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug("Get signing request");
		
		StatisticEvent statisticEvent = PdfAsHelper.getStatisticEvent(request,
				response);
		
		try {
			this.process(request, response, statisticEvent);
		} catch (Exception e) {

			statisticEvent.setStatus(Status.ERROR);
			statisticEvent.setException(e);
			if (e instanceof PDFASError) {
				statisticEvent.setErrorCode(((PDFASError) e).getCode());
			}
			statisticEvent.setEndNow();
			statisticEvent.setTimestampNow();
			StatisticFrontend.getInstance().storeEvent(statisticEvent);
			statisticEvent.setLogged(true);

			PdfAsHelper.setSessionException(request, response, e.getMessage(),
					e);
			PdfAsHelper.gotoError(getServletContext(), request, response);
		}
	}
	
	protected void process(HttpServletRequest request,
			HttpServletResponse response, StatisticEvent statisticEvent) throws Exception {

		String connector = PdfAsHelper.getConnector(request);

		if (connector == null) {
			connector = PdfAsParameterExtractor.getConnector(request);
		}

		if (connector == null) {
			throw new PdfAsWebException("No connector defined signature");
		}

		String transactionId = PdfAsHelper.getTransactionid(request);

		byte[] pdfData = PdfAsHelper.getPdfData(request);

		if (pdfData == null) {
			throw new PdfAsWebException("No pdf data available");
		}

		PdfAsHelper.clearPdfData(request);

		String positioningString = PdfAsHelper.buildPosString(request, response);
		logger.debug("Build position string: {}", positioningString);
		
		// IPlainSigner signer;
		if (connector.equals("bku") || connector.equals("onlinebku")
				|| connector.equals("mobilebku")) {
			// start asynchronous signature creation

			if (connector.equals("bku")) {
				if (WebConfiguration.getLocalBKUURL() == null) {
					throw new PdfAsWebException(
							"Invalid connector bku is not supported");
				}
			}

			if (connector.equals("onlinebku")) {
				if (WebConfiguration.getLocalBKUURL() == null) {
					throw new PdfAsWebException(
							"Invalid connector onlinebku is not supported");
				}
			}

			if (connector.equals("mobilebku")) {
				if (WebConfiguration.getLocalBKUURL() == null) {
					throw new PdfAsWebException(
							"Invalid connector mobilebku is not supported");
				}
			}

			PdfAsHelper.setStatisticEvent(request, response, statisticEvent);

			PdfAsHelper.startSignature(request, response, getServletContext(),
					pdfData, connector,
					positioningString,
					transactionId, PdfAsHelper.getSignatureType(request),
					PdfAsHelper.getPreProcessorMap(request),
					PdfAsHelper.getOverwriteMap(request));
			return;
		} else if (connector.equals("jks") || connector.equals("moa")) {
			// start synchronous siganture creation

			if (connector.equals("jks")) {

				String keyIdentifier = PdfAsHelper.getKeyIdentifier(request);

				boolean ksEnabled = false;

				if (keyIdentifier != null) {
					ksEnabled = WebConfiguration
							.getKeystoreEnabled(keyIdentifier);
				} else {
					ksEnabled = WebConfiguration.getKeystoreDefaultEnabled();
				}

				if (!ksEnabled) {
					if (keyIdentifier != null) {
						throw new PdfAsWebException("JKS connector ["
								+ keyIdentifier + "] disabled or not existing.");
					} else {
						throw new PdfAsWebException(
								"DEFAULT JKS connector disabled.");
					}
				}
			}

			if (connector.equals("moa")) {
				if (!WebConfiguration.getMOASSEnabled()) {
					throw new PdfAsWebException(
							"Invalid connector moa is not supported");
				}
			}

			byte[] pdfSignedData = PdfAsHelper.synchornousSignature(request,
					response, pdfData);
			PdfAsHelper.setSignedPdf(request, response, pdfSignedData);

			statisticEvent.setStatus(Status.OK);
			statisticEvent.setEndNow();
			statisticEvent.setTimestampNow();
			StatisticFrontend.getInstance().storeEvent(statisticEvent);
			statisticEvent.setLogged(true);

			PdfAsHelper.gotoProvidePdf(getServletContext(), request, response);
			return;
		} else {
			throw new PdfAsWebException("Invalid connector (bku | moa | jks)");
		}

	}
}
