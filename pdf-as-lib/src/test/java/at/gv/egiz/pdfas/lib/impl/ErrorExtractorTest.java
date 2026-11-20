package at.gv.egiz.pdfas.lib.impl;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class ErrorExtractorTest {
    @Test
    public void cancellationIsRethrownThroughTransportWrappers() {
        CancellationException failure = new CancellationException("cancelled");
        failure.initCause(new PDFASError(ErrorConstants.ERROR_GENERIC));
        for (Throwable input : new Throwable[] {
                failure, new CompletionException(failure), new ExecutionException(failure),
                new CompletionException(new ExecutionException(new CompletionException(failure))) })
        {
            val x = assertThrows(CancellationException.class, () -> {
                throw ErrorExtractor.searchPdfAsError(input, null);
            });
            assertSame(failure, x);
        }
    }

    @Test
    public void errorsAreRethrownThroughTransportWrappers() {
        Error failure = new AssertionError("fatal failure", new PDFASError(ErrorConstants.ERROR_GENERIC));
        for (Throwable input : new Throwable[] {
                failure, new CompletionException(failure), new ExecutionException(failure),
                new ExecutionException(new CompletionException(new ExecutionException(failure))) })
        {
            val x = assertThrows(Error.class, () -> {
                throw ErrorExtractor.searchPdfAsError(input, null);
            });
            assertSame(failure, x);
        }
    }

    @Test
    public void codedErrorsKeepTheirIdentityMessageCauseAndMetadata() {
        Throwable cause = new IllegalStateException("original cause");
        PDFASError failure = new PDFASError(ErrorConstants.ERROR_SIG_INVALID_BKU_SIG, "specific failure", cause);
        failure.getProcessInformations().put("device", "original-device");
        for (Throwable input : new Throwable[] {
                failure, new CompletionException(new ExecutionException(failure)) }) {
            PDFASError result = ErrorExtractor.searchPdfAsError(input, null);
            assertSame(failure, result);
            assertEquals(ErrorConstants.ERROR_SIG_INVALID_BKU_SIG, result.getCode());
            assertEquals("specific failure", result.getMessage());
            assertSame(cause, result.getCause());
            assertEquals("original-device", result.getProcessInformations().get("device"));
        }
    }

    @Test
    public void codedErrorsAreNotReplacedByTheirCauses() {
        for (Throwable cause : new Throwable[] {
                new CancellationException("cancelled"), new AssertionError("fatal failure"),
                new CompletionException(new CancellationException("cancelled")),
                new ExecutionException(new AssertionError("fatal failure")),
                new PDFASError(ErrorConstants.ERROR_GENERIC) }) {
            PDFASError failure = new PDFASError(ErrorConstants.ERROR_SIG_INVALID_BKU_SIG, "specific failure", cause);
            assertSame(failure, ErrorExtractor.searchPdfAsError(failure, null));
            assertSame(failure, ErrorExtractor.searchPdfAsError(new CompletionException(failure), null));
        }
    }

    @Test
    public void existingCauseChainSearchStillPreservesCodedErrors() {
        PDFASError failure = new PDFASError(ErrorConstants.ERROR_SIG_INVALID_BKU_SIG);
        PdfAsException wrapper = new PdfAsException("error.pdf.sig.01", failure);
        assertSame(failure, ErrorExtractor.searchPdfAsError(wrapper, null));
    }

    @Test
    public void transportWrappersAreRemovedFromGenericFailures() {
        IllegalStateException unexpectedFailure = new IllegalStateException("unexpected failure");
        PDFASError genericError = ErrorExtractor.searchPdfAsError(
                new CompletionException(new ExecutionException(unexpectedFailure)), null);
        assertEquals(ErrorConstants.ERROR_GENERIC, genericError.getCode());
        assertSame(unexpectedFailure, genericError.getCause());
    }

}
