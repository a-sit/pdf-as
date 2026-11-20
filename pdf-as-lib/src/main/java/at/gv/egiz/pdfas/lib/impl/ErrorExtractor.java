package at.gv.egiz.pdfas.lib.impl;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsMOAException;
import at.gv.egiz.pdfas.common.exceptions.SLPdfAsException;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import lombok.extern.slf4j.Slf4j;

import java.security.cert.CertificateException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ErrorExtractor implements ErrorConstants {

  private static final int MAX_CAUSE_DEPTH = 30;

	private static PDFASError convertPdfAsError(Throwable e) {
		if (e instanceof SLPdfAsException ex) {
            if (ex.getInfo() != null) {
				return new PDFASError(ex.getCode(), ex.getInfo(), e);
			} else {
				return new PDFASError(ex.getCode(), e);
			}
		} else if (e instanceof PdfAsMOAException ex) {
            int code = -1;
			String errorInfo = ex.getErrorResponse();
			
			if (ex.getErrorCode() != null && !ex.getErrorCode().isEmpty()) {
				try {
					code = Integer.parseInt(ex.getErrorCode());
				} catch(NumberFormatException numfo) {
					log.warn("Failed to convert MOA ErrorCode: " + ex.getErrorCode(), e);
				}
			}
			
			if (errorInfo != null) {
				return new PDFASError(code, errorInfo, e);
			} else {
				return new PDFASError(code, e);
			}
		} else if (e instanceof PdfAsException) {
		  return new PDFASError(ErrorConstants.ERROR_PDF_PROCESSING_FAILED, e.getMessage(), e);
		} else if (e instanceof CertificateException) {
          return new PDFASError(ErrorConstants.ERROR_INVALID_CERTIFICATE, e);
		}
		
		// TODO: Handle more exceptions

		return null;
	}

	/** Resolves legacy [PdfAsException] -> error-coded [PDFASError] */
	public static PDFASError searchPdfAsError(Throwable e, OperationStatus status) {
		while ((e instanceof CompletionException || e instanceof ExecutionException) && e.getCause() != null) {
			e = e.getCause();
		}
		if (e instanceof CancellationException cancellation) {
			throw cancellation;
		}
		if (e instanceof Error error) {
			throw error;
		}

		Throwable cur = e;
		PDFASError err = null;

		// Search PDFASError
		for (int i = 0; i < MAX_CAUSE_DEPTH; i++) {
			if (cur instanceof PDFASError) {
				err = (PDFASError) cur;
			}
			if (err != null) {
				break;
			}
			cur = cur.getCause();
			if (cur == null) {
				break;
			}
		}

		if (err == null) {
			cur = e;
			// Search other reasons
			for (int i = 0; i < MAX_CAUSE_DEPTH; i++) {
				if (cur == null) {
					break;
				}
				err = convertPdfAsError(cur);
				if (err != null) {
					break;
				}
				cur = cur.getCause();
			}
		}

		if (err == null) {
			log.info("Cannot extract correct failure code from: ", e);
			err =  new PDFASError(ERROR_GENERIC, e);
		}

		if(status != null) {
			err.getProcessInformations().putAll(status.getMetaInformations());
		}
		
		return err;
	}
}
