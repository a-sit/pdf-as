package at.gv.egiz.pdfas.lib.impl.signing;

import lombok.NonNull;

public interface PDFASSignatureExtractor extends PDFASSignatureInterface {
	public byte @NonNull [] getSignatureData();
}
