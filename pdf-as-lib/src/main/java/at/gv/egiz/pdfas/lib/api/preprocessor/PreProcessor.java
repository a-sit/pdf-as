package at.gv.egiz.pdfas.lib.api.preprocessor;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter;

/** A pre-processor that can process sign/verify requests before they are performed. Loaded via {@link java.util.ServiceLoader}. */
public interface PreProcessor {
	
	/**
	 * This Method is called once for each sign call.
	 * You can modify `parameter` in place, or throw `PDFASError` to bail out.
	 */
	public void sign(SignParameter parameter) throws PDFASError;
	
	/**
	 * This Method is called once for each verify call.
	 * You can modify `parameter` in place, or throw `PDFASError` to bail out.
	 */
	public void verify(VerifyParameter parameter) throws PDFASError;
	
	
	/**
	 * Pre-processors are executed in ascending order of registration position.
	 * This can be used for interdependencies.
	 */
	public int registrationPosition();
	
	/**
	 * The preprocessor's name. Only used for debugging.
	 */
	public String getName();
}
