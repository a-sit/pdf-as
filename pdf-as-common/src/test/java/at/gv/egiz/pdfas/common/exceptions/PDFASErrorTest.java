package at.gv.egiz.pdfas.common.exceptions;

import org.junit.Test;

import static org.junit.Assert.*;

public class PDFASErrorTest {
    @Test
    public void resolvedMessagesArePreserved() {
        PDFASError error = new PDFASError(ErrorConstants.ERROR_GENERIC);
        assertEquals("Generic Error", error.getMessage());
        assertEquals("Generic Error", error.getLocalizedMessage());
    }

    @Test
    public void explicitDetailsAreUnparsed() {
        // the info string (literal) contains a valid message key, but should still not be parsed
        PDFASError error = new PDFASError(ErrorConstants.ERROR_GENERIC, "error.pdf.sig.01");
        assertEquals("error.pdf.sig.01", error.getMessage());
        assertEquals("error.pdf.sig.01", error.getLocalizedMessage());
    }
}
