package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import java.io.IOException;

import at.gv.egiz.pdfas.common.exceptions.PDFIOException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.PDFBOXObject;
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractor;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import at.gv.egiz.pdfas.lib.impl.status.PDFObject;

public class PDFBoxPlaceholderExtractor implements PlaceholderExtractor {


	@Override
	public SignaturePlaceholderData extract(PDFObject doc, String placeholderId, int matchMode) throws PdfAsException {
		if (doc instanceof PDFBOXObject) {
			PDFBOXObject object = (PDFBOXObject) doc;
			try {
				SignaturePlaceholderExtractor extractor = new SignaturePlaceholderExtractor();
				return extractor.extract(object.getDocument(), placeholderId, matchMode);
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e2) {
				throw new PDFIOException("error.pdf.io.04", e2);
			}
		}
		throw new PdfAsException("INVALID STATE");
	}
}
