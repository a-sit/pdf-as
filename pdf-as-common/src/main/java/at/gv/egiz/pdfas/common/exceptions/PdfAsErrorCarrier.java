package at.gv.egiz.pdfas.common.exceptions;

public class PdfAsErrorCarrier extends PdfAsException {

	public PdfAsErrorCarrier(PDFASError error) {
		super("Carrier", error);
	}
	
}
