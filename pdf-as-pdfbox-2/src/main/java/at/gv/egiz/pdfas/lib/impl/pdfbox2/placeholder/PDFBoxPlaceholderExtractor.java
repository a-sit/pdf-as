package at.gv.egiz.pdfas.lib.impl.pdfbox2.placeholder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import at.gv.egiz.pdfas.common.exceptions.PDFIOException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.PDFBOXObject;
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractor;
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData;
import at.gv.egiz.pdfas.lib.impl.status.PDFObject;

public class PDFBoxPlaceholderExtractor implements PlaceholderExtractor {


	@Override
	public SignaturePlaceholderData extract(PDFObject doc, String placeholderId, int matchMode) throws PdfAsException {
		if (doc instanceof PDFBOXObject object) {
            try {
				SignaturePlaceholderExtractor extractor = new SignaturePlaceholderExtractor();
				return extractor.extract(object.getDocument(), placeholderId, matchMode);
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e2) {
				throw new PDFIOException("error.pdf.io.04", e2);
			}
		}
		throw new PdfAsException("INVALID STATE");
	}
}
