package at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.impl.pdfbox2.PDFBOXObject;
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFStamper;

public class StamperFactory {

	//public static final String DEFAULT_STAMPER_CLASS = "at.gv.egiz.pdfas.stmp.itext.ITextStamper";
	public static final String DEFAULT_STAMPER_CLASS = "at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2.PdfBoxStamper";

	public static IPDFStamper<PDFBOXObject> createDefaultStamper(ISettings settings) throws PdfAsException {
		try {
			Class<?> cls = Class.forName(DEFAULT_STAMPER_CLASS);
			Object st = cls.getDeclaredConstructor().newInstance();
			if (!(st instanceof IPDFStamper))
				throw new ClassCastException();
            @SuppressWarnings("unchecked")
			IPDFStamper<PDFBOXObject> stamper = (IPDFStamper<PDFBOXObject>) st;
			return stamper;
		} catch (Throwable e) {
			throw new PdfAsException("error.pdf.stamp.10", e);
		}
	}
}