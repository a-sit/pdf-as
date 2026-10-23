package at.gv.egiz.pdfas.lib.impl.verify;

import java.util.Date;
import java.util.List;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter.SignatureVerificationLevel;
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult;

public interface IVerifier {
	public List<VerifyResult> verify(byte[] signature,
			SignatureInputData signedData, Date verificationTime) throws PdfAsException;
	
	public void setConfiguration(ISettings config);
	
	public SignatureVerificationLevel getLevel();
}
