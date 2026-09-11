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
package at.gv.egiz.pdfas.lib.api;

import java.io.OutputStream;
import java.security.cert.X509Certificate;

import java.awt.Image;
import java.util.List;
import java.util.concurrent.CompletionStage;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.api.sign.IAsyncSigner;
import at.gv.egiz.pdfas.lib.api.sign.IPlainSigner;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.api.sign.SignResult;
import at.gv.egiz.pdfas.lib.api.verify.VerifyParameter;
import at.gv.egiz.pdfas.lib.api.verify.VerifyResult;
import jakarta.activation.DataSource;
import lombok.NonNull;
import lombok.val;

public interface PdfAs {	
	/** Signs a PDF document synchronously, using a {@link IPlainSigner}. */
	public @NonNull SignResult sign(@NonNull SignParameter parameter, @NonNull DataSource document, @NonNull IPlainSigner plainSigner, @NonNull OutputStream output) throws PDFASError;

	/** Signs a PDF document asynchronously, using a {@link IAsyncSigner}. */
	public @NonNull CompletionStage<@NonNull SignResult> signAsync(@NonNull SignParameter parameter, @NonNull DataSource document, @NonNull IAsyncSigner asyncSigner, @NonNull OutputStream output);

	/** Legacy interface that expects the {@link DataSource}, {@link IPlainSigner}, and {@link OutputStream} to be set on the {@link SignParameter}.
	 * Deprecated in favor of {@link PdfAs#sign(SignParameter, DataSource, IPlainSigner, OutputStream)}. */
	@Deprecated
	public default @NonNull SignResult sign(@NonNull SignParameter parameter) throws PDFASError {
		val input = parameter.getDataSource();
		if (input == null) {
			throw new IllegalArgumentException("SignParameter is missing dataSource for use of legacy sign().");
		}
		val signer = parameter.getPlainSigner();
		if (signer == null) {
			throw new IllegalArgumentException("SignParameter is missing plainSigner for use of legacy sign().");
		}
		val outputStream = parameter.getOutputStream();
		if (outputStream == null) {
			throw new IllegalArgumentException("SignParameter is missing outputStream for use of legacy sign().");
		}
		return sign(parameter, input, signer, outputStream);
	}

	
	/**
	 * Verifies a document with (potentially multiple) PDF signatures.
	 *  
	 * @param parameter The verification parameter
	 * @return A list of verification Results
	 */
	public @NonNull List<@NonNull VerifyResult> verify(@NonNull VerifyParameter parameter, @NonNull DataSource document) throws PDFASError;
	/** Legacy interface that expects the {@link DataSource} to be set on the {@link VerifyParameter}.
	 * Deprecated in favor of {@link PdfAs#verify(VerifyParameter, DataSource). */
	@Deprecated
	public default @NonNull List<@NonNull VerifyResult> verify(@NonNull VerifyParameter parameter) throws PDFASError {
		val input = parameter.getDataSource();
		if (input == null) {
			throw new IllegalArgumentException("VerifyParameter is missing dataSource for use of legacy verify().");
		}
		return verify(parameter, input);
	}
	
	/**
	 * Gets a copy of the PDF-AS configuration, to allow the application to 
	 * override configuration parameters at runtime.
	 * 
	 * @return A private copy of the PDF-AS configuration
	 */
	public @NonNull Configuration getConfiguration();

	/**
	 * Multi-stage signing interface.
	 * <p>
	 * PDF-AS can use three ways to sign a document.
	 * <li> Use the multi-stage API yourself. <b>You are here.</b>
	 * <li> Use a synchronous IPlainSigner. See {@link PdfAs#sign(SignParameter, DataSource, IPlainSigner, OutputStream)}.
	 * <li> Use an asynchronous IAsyncSigner. See {@link PdfAs#signAsync(SignParameter, DataSource, IAsyncSigner, OutputStream)}.
	 */
	public @NonNull StatusRequest.Stage1 startSign(@NonNull SignParameter parameter, @NonNull DataSource document) throws PDFASError;
	
	/**
	 * Generates a Image of the visual signatur block as Preview
	 * 
	 * @param parameter The signing Parameter
	 * @param cert The certificate to use to build the signature block
	 * @param resolution the resolution in dpi (dots per inch) (default is 72)
	 * @return The {@link Image} preview of the parameter specifies a visible signature, `null` otherwise.
	 */
	public Image generateVisibleSignaturePreview(@NonNull SignParameter parameter, @NonNull X509Certificate cert, int resolution) throws PDFASError;
}
