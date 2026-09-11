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

import at.gv.egiz.pdfas.lib.api.PdfAs;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;

import java.io.OutputStream;
import java.security.cert.X509Certificate;
import jakarta.activation.DataSource;

/**
 * Synchronous signer interface.
 * <p>
 * PDF-AS can use three ways to sign a document.
 * <li> Use the multi-stage API yourself. See {@link PdfAs#startSign(SignParameter, DataSource)}.
 * <li> Use a synchronous IPlainSigner. See {@link PdfAs#sign(SignParameter, DataSource, IPlainSigner, OutputStream)}. <b>You are here.</b>
 * <li> Use an asynchronous IAsyncSigner. See {@link PdfAs#signAsync(SignParameter, DataSource, IAsyncSigner, OutputStream)}.
 */
public interface IPlainSigner {
	
	/** Should return the signing certificate to use with this operation. */
	X509Certificate getCertificate(SignParameter parameter) throws PdfAsException;
	
	/** Should return the PDF signature `/Contents` covering the specified bytes. For PAdES, this is the encoded bytes of the CMS `SignedData`. */
    byte[] sign(byte[] input, int[] byteRange, SignParameter parameter, RequestedSignature requestedSignature) throws PdfAsException;
    
    /** Gets the PDF Filter for this signer */
    String getPDFFilter();

	/** Gets the PDF Subfilter for this signer */
	String getPDFSubFilter();
}
