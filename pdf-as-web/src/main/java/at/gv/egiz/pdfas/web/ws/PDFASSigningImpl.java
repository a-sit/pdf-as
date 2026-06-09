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
package at.gv.egiz.pdfas.web.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.impl.ErrorExtractor;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.soap.MTOM;

import lombok.val;
import org.apache.commons.lang3.StringUtils;

import at.gv.egiz.pdfas.api.processing.CoreSignParams;
import at.gv.egiz.pdfas.api.processing.DocumentToSign;
import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;
import at.gv.egiz.pdfas.api.processing.PdfasSignResponse;
import at.gv.egiz.pdfas.api.processing.SignedDocument;
import at.gv.egiz.pdfas.api.ws.PDFASBulkSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASBulkSignResponse;
import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import at.gv.egiz.pdfas.api.ws.PDFASSignRequest;
import at.gv.egiz.pdfas.api.ws.PDFASSignResponse;
import at.gv.egiz.pdfas.api.ws.PDFASSigning;
import at.gv.egiz.pdfas.api.ws.PdfasGetMultipleRequest;
import at.gv.egiz.pdfas.api.ws.PdfasSignMultipleRequest;
import at.gv.egiz.pdfas.api.ws.PdfasSignMultipleResponse;
import at.gv.egiz.pdfas.api.ws.PdfasSignedDocument;
import at.gv.egiz.pdfas.api.ws.VerificationLevel;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter.SignatureVerificationLevel;
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import at.gv.egiz.pdfas.web.helper.PdfAsHelper;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Operation;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Source;
import at.gv.egiz.pdfas.web.stats.StatisticEvent.Status;
import at.gv.egiz.pdfas.web.stats.StatisticFrontend;
import at.gv.egiz.pdfas.web.store.RequestStore;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MTOM
@WebService(endpointInterface = "at.gv.egiz.pdfas.api.ws.PDFASSigning")
public class PDFASSigningImpl implements PDFASSigning {

  /*
   * public byte[] signPDFDokument(byte[] inputDocument, PDFASSignParameters
   * parameters) { checkSoapSignEnabled(); try { return
   * PdfAsHelper.synchornousServerSignature(inputDocument, parameters); } catch
   * (Throwable e) { logger.error("Server Signature failed.", e); if
   * (WebConfiguration.isShowErrorDetails()) { throw new
   * WebServiceException("Server Signature failed.", e); } else { throw new
   * WebServiceException("Server Signature failed."); } } }
   */

  @Override
  public PDFASSignResponse signPDFDokument(PDFASSignRequest request) {
    log.debug("Starting SOAP Sign Request");
    checkSoapSignEnabled();
    if (request == null) {
      log.warn("SOAP Sign Request is null!");
      return null;
    }

    // map request into internal data-structure
    final PdfasSignRequest internalReq = buildOperationRequest(request);

    val connector = request.getParameters().getConnector();

    final StatisticEvent statisticEvent = new StatisticEvent();
    statisticEvent.setSource(Source.SOAP);
    statisticEvent.setOperation(Operation.SIGN);
    statisticEvent.setUserAgent(UserAgentFilter.getUserAgent());
    statisticEvent.setProfileId(request.getParameters().getProfile());
    if (connector != null) {
      statisticEvent.setDevice(connector.toString());
    }
    statisticEvent.setStartNow();

    PDFASSignResponse response = new PDFASSignResponse();
    try {
      PdfAsHelper.checkConnectorSupported(connector);

      if (connector.equals(Connector.MOA) || connector.equals(Connector.JKS)) {

        // perform technical signing process
        final PdfasSignResponse internalResp = PdfAsHelper.synchronousServerSignature(internalReq);

        // validate signatures
        internalResp.getSignedPdfs().forEach(el -> validatePdfSignature(el, internalReq, statisticEvent));

        // must be done later, because we should verify signed documents before
        response = buildResponseFromInternalResult(internalResp, internalReq.getRequestID());

      } else {
        // Signatures with user interaction!!
        final String id = RequestStore.getInstance().createNewStoreEntry(internalReq, statisticEvent);

        if (id == null) {
          throw new WebServiceException("Failed to store request");
        }

        final String userEntryURL = PdfAsHelper.generateUserEntryURL(id);

        log.debug("Generated request store: " + id);
        log.debug("Generated UI URL: " + userEntryURL);

        if (userEntryURL == null) {
          throw new WebServiceException(
              "Failed to generate User Entry URL");
        }

        response.setRedirectUrl(userEntryURL);
      }
    } catch (final Throwable e) {

      val pdfAsError = ErrorExtractor.searchPdfAsError(e, null);

      statisticEvent.setStatus(Status.ERROR);
      statisticEvent.setException(e);
      statisticEvent.setErrorCode(pdfAsError.getCode());
      statisticEvent.setEndNow();
      statisticEvent.setTimestampNow();
      StatisticFrontend.getInstance().storeEvent(statisticEvent);
      statisticEvent.setLogged(true);

      log.warn("Error in Soap Service", e);
      response.setErrorCode(pdfAsError.getCode());
      if (e.getCause() != null) {
        response.setError(e.getCause().getMessage());
      } else {
        response.setError(e.getMessage());

      }

    } finally {
      log.debug("Done SOAP Sign Request");

    }

    response.setRequestID(request.getRequestID());
    return response;

  }

  @Override
  public PDFASBulkSignResponse signPDFDokument(PDFASBulkSignRequest request) {
    log.debug("Starting SOAP BulkSign Request");
    checkSoapSignEnabled();
    final List<PDFASSignResponse> responses = new ArrayList<>();
    if (request.getSignRequests() != null) {
      for (int i = 0; i < request.getSignRequests().size(); i++) {
        final PDFASSignResponse response = signPDFDokument(request
            .getSignRequests().get(i));
        if (response != null) {
          responses.add(response);
        }
      }
      final PDFASBulkSignResponse response = new PDFASBulkSignResponse();
      response.setSignResponses(responses);
      log.debug("Done SOAP Sign Request");
      return response;
    }
    log.warn("Server Signature failed. [PDFASBulkSignRequest is NULL]");

    if (WebConfiguration.isShowErrorDetails()) {
      throw new WebServiceException("PDFASBulkSignRequest is NULL");
    } else {
      throw new WebServiceException("Server Signature failed.");
    }
  }

  @Override
  public PdfasSignMultipleResponse signPDFDokument(PdfasSignMultipleRequest request) {

    log.debug("Starting SOAP Bulk-Sign Request");
    checkSoapSignEnabled();
    if (request == null) {
      log.warn("SOAP Sign Request is null!");
      return null;
    }

    // map request into internal data-structure
    final PdfasSignRequest internalReq = buildOperationRequest(request);
    val connector = internalReq.getCoreParams().getConnector();

    final StatisticEvent statisticEvent = new StatisticEvent();
    statisticEvent.setSource(Source.SOAP);
    statisticEvent.setOperation(Operation.SIGNBULK);
    statisticEvent.setUserAgent(UserAgentFilter.getUserAgent());
    if (connector != null) {
      statisticEvent.setDevice(connector.toString());
    }
    statisticEvent.setStartNow();

    PdfasSignMultipleResponse response = new PdfasSignMultipleResponse();
    try {
      PdfAsHelper.checkConnectorSupported(connector);

      if (connector.equals(Connector.MOA) || connector.equals(Connector.JKS)) {

        // perform technical signing process
        final PdfasSignResponse internalResp = PdfAsHelper.synchronousServerSignature(internalReq);

        // validate signatures
        internalResp.getSignedPdfs().forEach(el -> validatePdfSignature(el, internalReq, statisticEvent));

        // must be done later, because we should verify signed documents before
        response = buildMultiResponseFromInternalResult(internalResp, internalReq.getRequestID(), internalReq
            .getCoreParams().getTransactionId());

      } else {
        // Signatures with user interaction!!
        final String id = RequestStore.getInstance().createNewStoreEntry(internalReq, statisticEvent);

        if (id == null) {
          throw new WebServiceException("Failed to store request");
        }

        final String userEntryURL = PdfAsHelper.generateUserEntryURL(id);

        log.debug("Generated request store: " + id);
        log.debug("Generated UI URL: " + userEntryURL);

        if (userEntryURL == null) {
          throw new WebServiceException(
              "Failed to generate User Entry URL");
        }

        response.setRedirectUrl(userEntryURL);
        response.setRequestID(request.getRequestID());

      }
    } catch (final Throwable e) {

      statisticEvent.setStatus(Status.ERROR);
      statisticEvent.setException(e);
      if (e instanceof PDFASError) {
        statisticEvent.setErrorCode(((PDFASError) e).getCode());
      }
      statisticEvent.setEndNow();
      statisticEvent.setTimestampNow();
      StatisticFrontend.getInstance().storeEvent(statisticEvent);
      statisticEvent.setLogged(true);

      log.warn("Error in Soap Service", e);
      if (e.getCause() != null) {
        response.setError(e.getCause().getMessage());

      } else {
        response.setError(e.getMessage());

      }

    } finally {
      log.debug("Done SOAP Sign Request");

    }

    return response;

  }

  @Override
  public PdfasSignMultipleResponse getSignedDokument(PdfasGetMultipleRequest request) {
    log.debug("Starting SOAP Get-Signed Request");
    checkSoapSignEnabled();
    if (request == null) {
      log.warn("SOAP Get-Signed Request is null!");
      return null;
      
    }

    final PdfasSignMultipleResponse response = new PdfasSignMultipleResponse();
    
    try {
      if (StringUtils.isEmpty(request.getToken())) {
        log.warn("SOAP Get-Signed Request contains NO token");
        throw new WebServiceException("SOAP Get-Signed Request contains NO token");
        
      }
      
      final PdfasSignResponse result = RequestStore.getInstance().fetchStoreResponse(request.getToken());
      if (result != null) {  
        response.setRequestID(result.getRequestId());
        response.setTransactionId(result.getTransactionId());
        response.setOutput(result.getSignedPdfs().stream()
            .map(el -> {
              PdfasSignedDocument out = new PdfasSignedDocument();
              out.setFileName(el.getFileName());
              out.setOutputData(el.getOutputData());
              out.setVerificationResponse(el.getVerificationResponse());
              return out;
              
            })
            .collect(Collectors.toList()));
                            
      } else {
        log.warn("SOAP Get-Signed Request token is unknown or expired");
        throw new WebServiceException("SOAP Get-Signed Request token is unknown or expired");
        
      }
            
    } catch (final Throwable e) {

      log.warn("Error in Soap Service", e);
      if (e.getCause() != null) {
        response.setError(e.getCause().getMessage());

      } else {
        response.setError(e.getMessage());

      }

    } finally {
      log.debug("Done SOAP Sign Request");

    }
    
    return response;

  }

  private PdfasSignRequest buildOperationRequest(PdfasSignMultipleRequest request) {
    final PdfasSignRequest data = new PdfasSignRequest();
    data.setRequestID(request.getRequestID());
    data.setVerificationLevel(request.getVerificationLevel());

    final CoreSignParams coreParams = new CoreSignParams();
    coreParams.setSignatureBlockParameters(request.getSignatureBlockParameters());
    coreParams.setConnector(request.getConnector());
    coreParams.setKeyIdentifier(request.getKeyIdentifier());
    coreParams.setOverrides(
        request.getOverrides() != null ? request.getOverrides().getMap() : null);
    coreParams.setPreprocessor(
        request.getPreprocessor() != null ? request.getPreprocessor().getMap() : null);
    coreParams.setInvokeErrorUrl(request.getInvokeErrorUrl());
    coreParams.setInvokeTarget(request.getInvokeTarget());
    coreParams.setInvokeUrl(request.getInvokeUrl());
    coreParams.setTransactionId(request.getTransactionId());
    data.setCoreParams(coreParams);

    request.getInput().forEach(el -> {
      final DocumentToSign document = new DocumentToSign();
      document.setInputData(el.getInputData());
      document.setPosition(el.getPosition());
      document.setProfile(el.getProfile());
      document.setQrCodeContent(el.getQrCodeContent());
      document.setFileName(el.getFileName());
      data.addDocumentToSign(document);

    });

    return data;

  }

  private PdfasSignMultipleResponse buildMultiResponseFromInternalResult(PdfasSignResponse internalResp,
      String reqId, String transactionId) {
    final PdfasSignMultipleResponse resp = new PdfasSignMultipleResponse();
    resp.setRequestID(reqId);
    resp.setTransactionId(transactionId);
    resp.setOutput(
        internalResp.getSignedPdfs().stream()
            .map(el -> {
              final PdfasSignedDocument out = new PdfasSignedDocument();
              out.setFileName(el.getFileName());
              out.setOutputData(el.getOutputData());
              out.setVerificationResponse(el.getVerificationResponse());
              return out;
            })
            .collect(Collectors.toList()));

    return resp;

  }

  private void checkSoapSignEnabled() {
    if (!WebConfiguration.getSoapSignEnabled()) {
      throw new WebServiceException("Service disabled!");
    }
  }

  private PdfasSignRequest buildOperationRequest(PDFASSignRequest request) {
    final PdfasSignRequest data = new PdfasSignRequest();
    data.setRequestID(request.getRequestID());
    data.setVerificationLevel(request.getVerificationLevel());

    final CoreSignParams coreParams = new CoreSignParams();
    coreParams.setSignatureBlockParameters(request.getSignatureBlockParameters());
    coreParams.setConnector(request.getParameters().getConnector());
    coreParams.setKeyIdentifier(request.getParameters().getKeyIdentifier());
    coreParams.setOverrides(
        request.getParameters().getOverrides() != null ? request.getParameters().getOverrides().getMap()
            : null);
    coreParams.setPreprocessor(
        request.getParameters().getPreprocessor() != null ? request.getParameters().getPreprocessor().getMap()
            : null);
    coreParams.setInvokeErrorUrl(request.getParameters().getInvokeErrorURL());
    coreParams.setInvokeTarget(request.getParameters().getInvokeTarget());
    coreParams.setInvokeUrl(request.getParameters().getInvokeURL());
    coreParams.setTransactionId(request.getParameters().getTransactionId());
    data.setCoreParams(coreParams);

    final DocumentToSign document = new DocumentToSign();
    document.setInputData(request.getInputData());
    document.setPosition(request.getParameters().getPosition());
    document.setProfile(request.getParameters().getProfile());
    document.setQrCodeContent(request.getParameters().getQRCodeContent());
    data.addDocumentToSign(document);

    return data;

  }

  private PDFASSignResponse buildResponseFromInternalResult(PdfasSignResponse internalResp, String reqId) {
    final PDFASSignResponse resp = new PDFASSignResponse();
    resp.setRequestID(reqId);
    resp.setSignedPDF(internalResp.getSignedPdfs().get(0).getOutputData());
    resp.setVerificationResponse(internalResp.getSignedPdfs().get(0).getVerificationResponse());
    return resp;

  }

  @SneakyThrows
  private void validatePdfSignature(SignedDocument el, PdfasSignRequest request,
      StatisticEvent statisticEvent) {
    
    if (WebConfiguration.isSoapSignWithVerifyEnabled()) {                 
      Map<String, String> preProcessor = null;
      if (request.getCoreParams().getPreprocessor() != null) {
        preProcessor = request.getCoreParams().getPreprocessor();
  
      }
  
      VerifyResult verifyResult = null;
      if (request.getVerificationLevel() != null &&
          request.getVerificationLevel().equals(
              VerificationLevel.FULL_CERT_PATH)) {
        final List<VerifyResult> verResults = PdfAsHelper
            .synchronousVerify(
                el.getOutputData(),
                -1,
                SignatureVerificationLevel.FULL_VERIFICATION,
                preProcessor);
  
        if (verResults.size() < 1) {
          throw new WebServiceException(
              "Document verification failed! " + verResults.size());
        }
        verifyResult = verResults.get(verResults.size() - 1);
      } else {
        final List<VerifyResult> verResults = PdfAsHelper
            .synchronousVerify(
                el.getOutputData(),
                -1,
                SignatureVerificationLevel.INTEGRITY_ONLY_VERIFICATION,
                preProcessor);
  
        if (verResults.size() < 1) {
          throw new WebServiceException(
              "Document verification failed! " + verResults.size());
        }
  
        verifyResult = verResults.get(verResults.size() - 1);
  
      }
  
      if (verifyResult.getValueCheckCode().getCode() == 0) {
        statisticEvent.setStatus(Status.OK);
        statisticEvent.setEndNow();
        statisticEvent.setTimestampNow();
        statisticEvent.setFilesize(el.getOutputData().length);
        StatisticFrontend.getInstance().storeEvent(statisticEvent);
        statisticEvent.setLogged(true);
        
      } else {
        statisticEvent.setStatus(Status.ERROR);
        statisticEvent.setErrorCode(verifyResult.getValueCheckCode().getCode());
        statisticEvent.setEndNow();
        statisticEvent.setTimestampNow();
        statisticEvent.setFilesize(el.getOutputData().length);
        StatisticFrontend.getInstance().storeEvent(statisticEvent);
        statisticEvent.setLogged(true);
      }
  
      el.getVerificationResponse().setCertificateCode(
          verifyResult.getCertificateCheck().getCode());
      el.getVerificationResponse().setValueCode(
          verifyResult.getValueCheckCode().getCode());

    } else {
      log.debug("Implicite signature-verification skipped by configuration");
      statisticEvent.setStatus(Status.OK);
      statisticEvent.setEndNow();
      statisticEvent.setTimestampNow();
      statisticEvent.setFilesize(el.getOutputData().length);
      StatisticFrontend.getInstance().storeEvent(statisticEvent);
      statisticEvent.setLogged(true);
      
    }
    
  }
}
