package at.gv.egiz.pdfas.lib.impl.pdfbox2;

import java.io.IOException;

import javax.activation.DataSource;

import org.apache.pdfbox.pdmodel.PDDocument;

import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2.PDFAsFontCache;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import at.gv.egiz.pdfas.lib.impl.status.PDFObject;

public class PDFBOXObject extends PDFObject {

	private PDDocument doc;
		
	private PDFAsFontCache sigBlockFontCache = new PDFAsFontCache();
	
	public PDFAsFontCache getSigBlockFontCache() {
		return sigBlockFontCache;
	}

	public void setSigBlockFontCache(PDFAsFontCache sigBlockFontCache) {
		this.sigBlockFontCache = sigBlockFontCache;
	}

	public PDFBOXObject(OperationStatus operationStatus) {
		super(operationStatus);
	}

	// Note: finalize() method removed as it's deprecated in Java 9+
	// Resource cleanup should be handled explicitly via close() method
	
	public void close() {
		if(doc != null) {
			try {
				doc.close();
			} catch(Throwable e) {
				// Ignore Throwables during close
			}
			doc = null;
		}
	}

	public void setOriginalDocument(DataSource originalDocument) throws IOException {
		this.originalDocument = originalDocument;
		if(doc != null) {
			doc.close();
		}
		synchronized(PDDocument.class) {
			this.doc = PDDocument.load(this.originalDocument.getInputStream());
		}
		if(this.doc != null) {
			this.doc.getDocument().setWarnMissingClose(false);
		}
	}
	
	public PDDocument getDocument() {
		return this.doc;
	}

	@Override
	public String getPDFVersion() {
		return String.valueOf(getDocument().getDocument().getVersion());
	}

}
