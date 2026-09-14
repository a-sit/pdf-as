/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
package at.gv.egiz.pdfas.lib.api.sign;

import java.io.OutputStream;
import jakarta.activation.DataSource;

import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.lib.api.PdfAsParameter;

public interface SignParameter extends PdfAsParameter {
	
	/** Gets the signature profile to use */
	public String getSignatureProfileId();

	/** Sets the signature profile to use */
	public void setSignatureProfileId(String signatureProfileId);

	/** Gets the signature position string */
	public String getSignaturePosition();

	/** Sets the signature position string */
	public void setSignaturePosition(String signaturePosition);
	
	
	/** Get Id of a placeholder that should be used for positioning. */
	String getPlaceHolderId();
	
	
	/** Set Id of a placeholder that should be used for positioning. */
	void setPlaceHolderId(String id);
	
	/** Is QR-Code placeholder search enabled for this request? */
	boolean isPlaceHolderSearchEnabled();
	
	
	/** Enable / disable QR-Code placeholder search on request level.
	 * Default value is <code>true</code>. */
	void setPlaceHolderSearchEnabled(boolean flag);

	/** Deprecated compatibility setter.
	 * Instead, use {@link PdfAs#sign(SignParameter, DataSource, IPlainSigner, OutputStream)}
	 * and pass the {@link IPlainSigner} this way. */
	@Deprecated
	public void setPlainSigner(IPlainSigner signer);
	@Deprecated
	public IPlainSigner getPlainSigner();

	/** Deprecated compatibility setter.
	 * Instead, pass the output stream directly to {@link PdfAs#sign} etc. */
	@Deprecated
    public void setOutputStream(OutputStream stream);

	/** Deprecated compatibility getter.
	 * Instead, pass the output stream directly to {@link PdfAs#sign} etc. */
	@Deprecated
    public default OutputStream getOutputStream() { return getSignatureResult(); }
	/** Deprecated compatibility getter.
	 * Instead, pass the output stream directly to {@link PdfAs#sign} etc. */
	@Deprecated
	public OutputStream getSignatureResult();
}
