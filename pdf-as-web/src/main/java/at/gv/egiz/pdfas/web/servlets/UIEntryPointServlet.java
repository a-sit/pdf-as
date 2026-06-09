/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
package at.gv.egiz.pdfas.web.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter.SignatureVerificationLevel;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.exception.PdfAsStoreException;
import at.gv.egiz.pdfas.web.exception.PdfAsWebException;
import at.gv.egiz.pdfas.web.helper.DigestHelper;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.store.RequestStore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UIEntryPointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String REQUEST_ID_PARAM = "reqId";

	public UIEntryPointServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doProcess(req, resp);
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
		  // invalidate existing http sessions at first
	    req.getSession().invalidate();
		  
			String storeId = req.getParameter(REQUEST_ID_PARAM);

			if (storeId == null) {
				throw new PdfAsStoreException("Wrong Parameters");
			}

			PdfasSignRequest pdfAsRequest = RequestStore.getInstance()
					.fetchStoreEntry(storeId);

			if (pdfAsRequest == null) {
				throw new PdfAsStoreException("Invalid " + REQUEST_ID_PARAM
						+ " value");
			}

			StatisticEvent statisticEvent = RequestStore.getInstance()
					.fetchStatisticEntry(storeId);

			PdfAsHelper.setStatisticEvent(req, resp, statisticEvent);
			
			Connector connector = pdfAsRequest.getCoreParams().getConnector();
			PdfAsHelper.checkConnectorSupported(connector);

			String invokeUrl = pdfAsRequest.getCoreParams().getInvokeUrl();
			PdfAsHelper.setInvokeURL(req, resp, invokeUrl);

			String invokeTarget = pdfAsRequest.getCoreParams().getInvokeTarget();
			PdfAsHelper.setInvokeTarget(req, resp, invokeTarget);

			String errorUrl = pdfAsRequest.getCoreParams().getInvokeErrorUrl();
			PdfAsHelper.setErrorURL(req, resp, errorUrl);

			SignatureVerificationLevel lvl = SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION;
			if (pdfAsRequest.getVerificationLevel() != null) {
				switch (pdfAsRequest.getVerificationLevel()) {
				case INTEGRITY_ONLY:
					lvl = SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION;
					break;
				default:
					lvl = SignatureVerificationLevel.FULL_VERIFICATION;
					break;
				}
			}
			PdfAsHelper.setVerificationLevel(req, lvl);

			if (pdfAsRequest.hasNext() && pdfAsRequest.getInput().get(0).getInputData() == null) {
				throw new PdfAsException("No Signature data available");
			}

			String pdfDataHash = DigestHelper.getHexEncodedHash(pdfAsRequest.getInput().get(0).getInputData());

			PdfAsHelper.setSignatureDataHash(req, pdfDataHash);
			log.debug("Storing signatures data hash: " + pdfDataHash);

			log.debug("Starting signature creation with: " + connector);

			// IPlainSigner signer;
			if (Connector.isAsynchronous(connector)) {
				// start asynchronous signature creation
				
				PdfAsHelper.startSignature(req, resp, getServletContext(), connector, pdfAsRequest);
				
			} else {
				throw new PdfAsWebException("Invalid connector ("
						+ Connector.BKU + " | " + Connector.ONLINEBKU + " | "
						+ Connector.MOBILEBKU + ")");
			}

		} catch (Throwable e) {
			log.warn("Failed to process Request: ", e);
			PdfAsHelper.setSessionException(req, resp, e.getMessage(), e);
			PdfAsHelper.gotoError(getServletContext(), req, resp);
		}
	}
}
