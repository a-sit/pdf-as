package at.gv.egiz.pdfas.lib.impl.verify;

import java.util.List;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter;
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult;
import jakarta.activation.DataSource;
import lombok.NonNull;

public interface VerifyBackend {
	public @NonNull List<@NonNull VerifyResult> verify(@NonNull VerifyParameter parameter, @NonNull DataSource document) throws PDFASError;
}
