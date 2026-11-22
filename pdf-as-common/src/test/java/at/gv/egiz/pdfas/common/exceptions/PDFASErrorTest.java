package at.gv.egiz.pdfas.common.exceptions;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PDFASErrorTest {
    @Test
    public void codedErrorsHaveMessages() {
        PDFASError error = new PDFASError(ErrorConstants.ERROR_GENERIC);
        assertEquals(ErrorConstants.ERROR_GENERIC, error.getCode());
        assertFalse(error.getMessage().isEmpty());
        assertEquals(error.getMessage(), error.getLocalizedMessage());
    }

    @Test
    public void explicitDetailsAreUnparsed() {
        // the info string (literal) contains a valid message key, but should still not be parsed
        PDFASError error = new PDFASError(ErrorConstants.ERROR_GENERIC, "error.pdf.sig.02");
        assertEquals("error.pdf.sig.02", error.getMessage());
        assertEquals("error.pdf.sig.02", error.getLocalizedMessage());
    }

    @Test
    public void codedFailuresPreserveTheirCause() {
        Throwable cause = new IOException("write failed");
        PDFASError error = new PDFASError(ErrorConstants.ERROR_SIG_FINISH_FAILED, cause);
        assertEquals(11006, error.getCode());
        assertSame(cause, error.getCause());
    }
}
