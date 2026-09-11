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
package at.gv.egiz.pdfas.lib.impl;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import at.gv.egiz.pdfas.lib.api.sign.IAsyncSigner;
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner;
import at.gv.egiz.pdfas.lib.util.TimedFunction;
import jakarta.activation.DataSource;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.common.utils.PDFUtils;
import at.gv.egiz.pdfas.common.utils.StreamUtils;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.api.preprocessor.PreProcessor;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.api.sign.SignResult;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter;
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult;
import at.gv.egiz.pdfas.lib.backend.PDFASBackend;
import at.gv.egiz.pdfas.lib.impl.backend.BackendLoader;
import at.gv.egiz.pdfas.lib.impl.configuration.ConfigurationImpl;
import at.gv.egiz.pdfas.lib.impl.preprocessor.PreProcessorLoader;
import at.gv.egiz.pdfas.lib.impl.signing.IPdfSigner;
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureExtractor;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;
import at.gv.egiz.pdfas.lib.settings.Settings;
import at.gv.egiz.pdfas.lib.util.SignatureUtils;
import at.gv.egiz.sl.util.BKUHeader;
import iaik.x509.X509Certificate;

public class PdfAsImpl implements PdfAs, IConfigurationConstants,
    ErrorConstants {

  private static final @NonNull Logger logger = LoggerFactory
      .getLogger(PdfAsImpl.class);

  private final @NonNull ISettings settings;

  public PdfAsImpl(@NonNull File cfgFile) {
    logger.debug("Initializing PDF-AS with config: " + cfgFile.getPath());
    this.settings = new Settings(cfgFile);
  }

  public PdfAsImpl(@NonNull ISettings cfgObject) {
    logger.info("Initializing PDF-AS with config: "
        + cfgObject.getClass().getName());
    this.settings = cfgObject;
  }

  private static void verifySignParameter(@NonNull SignParameter parameter) throws PDFASError {
    // Status initialization
    if (!(parameter.getConfiguration() instanceof ISettings settings)) {
      throw new PDFASError(ERROR_SET_INVALID_SETTINGS_OBJ);
    }

    final String signatureProfile = parameter.getSignatureProfileId();
    if (signatureProfile != null) {
      if (!settings.hasPrefix("sig_obj." + signatureProfile)) {
        throw new PDFASError(ERROR_SIG_INVALID_PROFILE,
            PDFASError.buildInfoString(ERROR_SIG_INVALID_PROFILE,
                signatureProfile));
      }
    }

  }

  private static void verifyVerifyParameter(@NonNull VerifyParameter parameter)
      throws PDFASError {
    // Status initialization
    if (!(parameter.getConfiguration() instanceof ISettings)) {
      throw new PDFASError(ERROR_SET_INVALID_SETTINGS_OBJ);
    }
  }

  @Override
  public @NonNull SignResult sign(@NonNull SignParameter parameter, @NonNull DataSource document, @NonNull IPlainSigner signer, @NonNull OutputStream output) throws PDFASError {
    val state1 = startSign(parameter, document);
    try {
      val state2 = state1.setCertificate(
          signer.getCertificate(state1.getSignParameter()),
          signer.getPDFFilter(), signer.getPDFSubFilter());
      val state3 = state2.setSignature(
          signer.sign(
              state2.getSignatureData(), state2.getSignatureDataByteRange(),
              state2.getSignParameter(), state2.getRequestedSignature()));
      return state3.finishSign(output);
    } catch (final CertificateException e) {
      throw new PDFASError(ErrorConstants.ERROR_INVALID_CERTIFICATE, e);
    } catch (final PdfAsException e) {
      throw ErrorExtractor.searchPdfAsError(e, state1.getStatus());
    }
  }

  @Override
  public @NonNull CompletionStage<@NonNull SignResult> signAsync(@NonNull SignParameter parameter, @NonNull DataSource document, @NonNull IAsyncSigner signer, @NonNull OutputStream output) {
    try {
      val state1 = startSign(parameter, document);
      return CompletableFuture.completedStage(state1)
          .thenCompose(state -> {
            try {
              return signer.getCertificateData(state.getSignParameter()).thenApply(cert -> {
                try {
                  return state.setCertificate(cert.cert(), cert.pdfFilter(), cert.pdfSubFilter());
                } catch (final CertificateException e) {
                  throw new CompletionException(new PDFASError(ErrorConstants.ERROR_INVALID_CERTIFICATE, e));
                } catch (final PDFASError e) {
                  throw new CompletionException(e);
                }
              });
            } catch (final PdfAsException e) {
              throw new CompletionException(e);
            }
          })
          .thenCompose(state -> {
            try {
              return signer.sign(state.getSignatureData(), state.getSignatureDataByteRange(), state.getSignParameter(), state.getRequestedSignature())
                  .thenApply(signature -> {
                    try {
                      return state.setSignature(signature).finishSign(output);
                    } catch (final PDFASError e) {
                      throw new CompletionException(e);
                    }
                  });
            } catch (final PdfAsException e) {
              throw new CompletionException(e);
            }
          })
          .exceptionallyCompose(e -> {
            @NonNull val cause = Objects.requireNonNull(e instanceof CompletionException ? e.getCause() : e);
            if (cause instanceof PdfAsException pe) {
              return CompletableFuture.failedStage(pe);
            }
            return CompletableFuture.failedStage(cause);
          });
    } catch (final PDFASError e) {
      return CompletableFuture.failedStage(e);
    }
  }

  private X509Certificate getValidCertificate(X509Certificate certificate) throws PDFASError {
    Date notAfter = certificate.getNotAfter();
    Date notBefore = certificate.getNotBefore();
    Date now = new Date();
    
    if (now.after(notAfter) || now.before(notBefore)) {
      logger.warn("Signer certificate is not valid. notBefore: {} | notAfter: {} | now: {}",
          notBefore, notAfter, now);
      throw new PDFASError(ErrorConstants.ERROR_SIGNER_CERT_TIMEFRAME_INVALID);
      
    } else {
      return certificate;
      
    }
  }

  private final TimedFunction verifyTimer = new TimedFunction("pdfas.verify");
  @Override
  public @NonNull List<@NonNull VerifyResult> verify(@NonNull VerifyParameter parameter, @NonNull DataSource document)
      throws PDFASError {

    return verifyTimer.timed(() -> {
        verifyVerifyParameter(parameter);

        // execute pre Processors
        verifyPreProcessing(parameter);

        // allocated Backend
        final PDFASBackend backend = BackendLoader.getPDFASBackend(parameter.getConfiguration());

        if (backend == null) {
            throw new PDFASError(ERROR_NO_BACKEND);
        }

        try {
            return backend.getVerifier().verify(parameter, document);
        } catch (final Throwable e) {
            throw ErrorExtractor.searchPdfAsError(e, null);
        }
    });
  }

  @Override
  public @NonNull Configuration getConfiguration() {
    return new ConfigurationImpl(this.settings);
  }

  private final TimedFunction signTimer = new TimedFunction("pdfas.sign");
  @Override
  public @NonNull StatusRequestImpl.Stage1 startSign(
      @NonNull SignParameter parameter, @NonNull DataSource document) throws PDFASError
  {

    verifySignParameter(parameter);
    OperationStatus status = null;
    try {
      // Status initialization
      if (!(parameter.getConfiguration() instanceof ISettings config)) {
        throw new PdfAsSettingsException("Invalid settings object!");
      }

      // execute pre Processors
      signPreProcessing(parameter);

      // allocated Backend
      final PDFASBackend backend = BackendLoader.getPDFASBackend(parameter.getConfiguration());

      if (backend == null) {
        throw new PDFASError(ERROR_NO_BACKEND);
      }

      status = new OperationStatus(config, parameter,
          backend, signTimer.start());

      final IPdfSigner signer = backend.getPdfSigner();

      val pdfObject = signer.buildPDFObject(status);
      pdfObject.setOriginalDocument(document);
      status.setPdfObject(pdfObject);
      signer.checkPDFPermissions(pdfObject);

      return StatusRequestImpl.create(this, status);
    } catch (final Throwable e) {
      if (status != null) status.getSignTimer().finishFailure(e);
      logger.warn("startSign", e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    }
  }

  public void processCertificate(
      @NonNull StatusRequestImpl.Stage1 request, @NonNull X509Certificate certificate,
      @NonNull String pdfFilter, @NonNull String pdfSubFilter) throws PDFASError
  {
    final OperationStatus status = request.getStatus();
    try {
      status.setRequestedSignature(new RequestedSignature(status, certificate, pdfFilter, pdfSubFilter));
      if (request.getSignParameter() instanceof BKUHeaderHolder holder) {

          for (BKUHeader header : holder.getProcessInfo()) {
              if ("Server".equalsIgnoreCase(header.getName())) {
                  status.getRequestedSignature()
                          .getStatus()
                          .getMetaInformations()
                          .put(ErrorConstants.STATUS_INFO_SIGDEVICEVERSION,
                                  header.getValue());
              } else if (ErrorConstants.STATUS_INFO_SIGDEVICE.equalsIgnoreCase(header.getName())) {
                  status.getRequestedSignature()
                          .getStatus()
                          .getMetaInformations()
                          .put(ErrorConstants.STATUS_INFO_SIGDEVICE,
                                  header.getValue());
              }
          }
      }

      status.setSigningDate(Calendar.getInstance());

      final IPdfSigner signer = status.getBackend().getPdfSigner();

      final PDFASSignatureExtractor signatureDataExtractor = signer
              .buildBlindSignaturInterface(certificate,
                      pdfFilter, pdfSubFilter,
                      status.getSigningDate());

      signer.signPDF(status.getPdfObject(),
              status.getRequestedSignature(), signatureDataExtractor);

      final StringBuilder sb = new StringBuilder();

      final int[] byteRange = PDFUtils
              .extractSignatureByteRange(signatureDataExtractor
                      .getSignatureData());
      if ((byteRange == null) || (byteRange.length != 4)) {
        throw new PDFASError(ERROR_PDF_PROCESSING_FAILED, "Invalid or no byte range in PDF (expected exactly 4 elements)");
      }

      if (logger.isDebugEnabled()) {
        for (final int element : byteRange) {
          sb.append(" ").append(element);
        }
        logger.debug("ByteRange: {}", sb);
      }

      request.setSignatureData(signatureDataExtractor.getSignatureData());
      request.setByteRange(byteRange);

    } catch (final Throwable e) {
        status.getSignTimer().finishFailure(e);
        logger.warn("process", e);
        throw ErrorExtractor.searchPdfAsError(e, status);

    }
  }

  public void processSignature(
      @NonNull StatusRequestImpl.Stage2 request, byte @NonNull[] signatureValue) throws PDFASError
  {
    final OperationStatus status = request.getStatus();
    try {
      // Inject signature byte[] into signedDocument
      final int offset = request.getSignatureDataByteRange()[1] + 1;

      final byte[] pdfSignature = status.getBackend().getPdfSigner()
          .rewritePlainSignature(signatureValue);
      // byte[] input =
      // PDFUtils.blackOutSignature(status.getPdfObject().getSignedDocument(),
      // request.getSignatureDataByteRange());
      final VerifyResult verifyResult = SignatureUtils.verifySignature(
          signatureValue, request.getSignatureData());
      final RequestedSignature requestedSignature = request.getStatus()
          .getRequestedSignature();

      if (!StreamUtils.dataCompare(
          requestedSignature.getCertificate().getFingerprintSHA(),
          ((X509Certificate) verifyResult.getSignerCertificate()).getFingerprintSHA()
      )) {
        throw new PDFASError(ERROR_SIG_CERTIFICATE_MISSMATCH);
      }

      final int signatureEnd = request.getSignatureDataByteRange()[2] - 1;
      if (signatureEnd < (offset + pdfSignature.length)) {
        logger.error("Signature returned from signer encodes to {} bytes (hex string), but we only allocated {} bytes.",
            pdfSignature.length, signatureEnd - offset - 1);
        throw new PDFASError(ERROR_PDF_PROCESSING_FAILED, "Signer returned a signature that is too large");
      }

      for (int i = 0; i < pdfSignature.length; i++) {
        status.getPdfObject().getSignedDocument()[offset + i] = pdfSignature[i];
      }
    } catch (final Throwable e) {
      status.getSignTimer().finishFailure(e);
      throw e;
    }
  }

  public @NonNull SignResult finishSign(@NonNull StatusRequestImpl.Stage3 request, @NonNull OutputStream output) throws PDFASError {
    final OperationStatus status = request.getStatus();

    try {

      val signedDocument = status.getPdfObject().getSignedDocument();
      if (signedDocument == null || signedDocument.length == 0) {
        logger.warn("No signed document in session. Maybe signing-service communication stopped by an error");
        throw new PDFASError(ERROR_SIG_INVALID_STATUS,
            "No signed document in session. Maybe signing-service communication stopped by an error");
      }

      output.write(status.getPdfObject().getSignedDocument());
      output.flush();

      // ================================================================
      // Create SignResult
      val signResult = (SignResult) new SignResultImpl(
          status.getRequestedSignature().getCertificate(),
          status.getRequestedSignature().getSignaturePosition(),
          status.getMetaInformations()
      );
      status.getSignTimer().finishSuccess();
      return signResult;
    } catch (final IOException e) {
      // new PdfAsException("error.pdf.sig.06", e);
      status.getSignTimer().finishFailure(e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    } finally {
      if (status != null) {
        status.clear();
      }
    }
  }

  private void listPreProcessors(List<PreProcessor> preProcessors) {
    logger.debug("--------------");
    logger.debug("Listing PreProcessors:");

    final Iterator<PreProcessor> preProcessorsIterator = preProcessors.iterator();
    int idx = 0;
    while (preProcessorsIterator.hasNext()) {
      final PreProcessor preProcessor = preProcessorsIterator.next();
      logger.debug("{}: {} [{}]", idx, preProcessor.getName(),
          preProcessor.getClass().getName());
      idx++;
    }
    logger.debug("--------------");
  }

  private void verifyPreProcessing(VerifyParameter parameter)
      throws PDFASError {
    final List<PreProcessor> preProcessors = PreProcessorLoader
        .getPreProcessors(parameter.getConfiguration());

    listPreProcessors(preProcessors);

    logger.debug("executing PreProcessors for verifing:");

    for (PreProcessor preProcessor : preProcessors) {
      logger.debug("executing: {} [{}]", preProcessor.getName(),
          preProcessor.getClass().getName());
      preProcessor.verify(parameter);
      logger.debug("done executing: {} [{}]", preProcessor.getName(),
          preProcessor.getClass().getName());
    }

    logger.debug("executing PreProcessors for verifing done");
  }

  private void signPreProcessing(SignParameter parameter) throws PDFASError {
    final List<PreProcessor> preProcessors = PreProcessorLoader
        .getPreProcessors(parameter.getConfiguration());

    listPreProcessors(preProcessors);

    logger.debug("executing PreProcessors for signing:");

    for (PreProcessor preProcessor : preProcessors) {
      logger.debug("executing: {} [{}]", preProcessor.getName(),
          preProcessor.getClass().getName());
      preProcessor.sign(parameter);
      logger.debug("done executing: {} [{}]", preProcessor.getName(),
          preProcessor.getClass().getName());
    }

    logger.debug("executing PreProcessors for signing done");
  }

  @Override
  public Image generateVisibleSignaturePreview(
      @NonNull SignParameter parameter,
      @NonNull java.security.cert.X509Certificate cert, int resolution)
      throws PDFASError {

    OperationStatus status = null;
    try {
      // Status initialization
      if (!(parameter.getConfiguration() instanceof ISettings config)) {
        throw new PDFASError(ERROR_SET_INVALID_SETTINGS_OBJ);
      }
      X509Certificate iaikCert;
      if (!(cert instanceof X509Certificate)) {
        iaikCert = new X509Certificate(cert.getEncoded());
      } else {
        iaikCert = (X509Certificate) cert;
      }
      // allocated Backend
      final PDFASBackend backend = BackendLoader.getPDFASBackend(parameter.getConfiguration());

      status = new OperationStatus(config, parameter, backend, null);

      final IPdfSigner<?, ?> signer = backend.getPdfSigner();

      status.setPdfObject(signer.buildPDFObject(status));

      final RequestedSignature requestedSignature = new RequestedSignature(
          status, iaikCert, "", "");

      if (!requestedSignature.isVisual()) {
        logger.warn("Profile is invisible so not block image is generated");
        return null;
      }

      return signer.generateVisibleSignaturePreview(parameter, iaikCert,
          resolution, status, requestedSignature);
    } catch (final PdfAsException e) {
      logger.warn("PDF-AS  Exception", e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    } catch (final Throwable e) {
      logger.warn("Throwable  Exception", e);
      throw ErrorExtractor.searchPdfAsError(e, status);
    }

  }
}
