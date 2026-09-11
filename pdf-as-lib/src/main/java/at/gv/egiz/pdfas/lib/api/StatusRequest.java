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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.api.sign.SignResult;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;
import lombok.NonNull;

/**
 * Status of a signature process
 */
public interface StatusRequest {
    public @NonNull SignParameter getSignParameter();

    interface HasRequestedSignature extends StatusRequest {
      /** Gets the data to be signed */
      public byte @NonNull[] getSignatureData();

      /** Gets the byte range of the data to be signed */
      public int @NonNull[] getSignatureDataByteRange();

      /** Gets the requested signature metadata */
      public @NonNull RequestedSignature getRequestedSignature();
    }

    /** A {@link StatusRequest} that has just been initialized, and is waiting for certificate data.
     * @see StatusRequest#getSignParameter() getSignParameter()  */
    public interface Stage1 extends StatusRequest {
        /** Set the signing certificate that will be used, as well as the pdfFilter/pdfSubFilter combination that is requested.
         * @see StatusRequest.Stage1 */
        public @NonNull Stage2 setCertificate(@NonNull X509Certificate certificate, @NonNull String pdfFilter, @NonNull String pdfSubFilter) throws CertificateException, PDFASError;

        /** <b>See:</b> {@link StatusRequest.Stage1} */
        public @NonNull Stage2 setCertificate(byte @NonNull[] encodedCertificate, @NonNull String pdfFilter, @NonNull String pdfSubFilter) throws CertificateException, PDFASError;
    }

    /** A {@link StatusRequest} that is ready to be signed, and is waiting for {@link setSignature(byte[]) the generated signature bytes}.
     *  @see StatusRequest.HasRequestedSignature#getSignatureData() getSignatureData()
     *  @see StatusRequest.HasRequestedSignature#getSignatureDataByteRange() getSignatureDataByteRange()
     *  @see StatusRequest#getSignParameter() getSignParameter()
     *  @see StatusRequest.HasRequestedSignature#getRequestedSignature() getRequestedSignature() */
    public interface Stage2 extends StatusRequest.HasRequestedSignature {
        /** Set the `/Contents` for the signed PDF. In PAdES, this is the DER-encoded CMS `SignedData`.
         * <b>See:</b> {@link StatusRequest.Stage2} */
        public @NonNull Stage3 setSignature(byte @NonNull[] signatureValue) throws PDFASError;
    }

    /** A {@link StatusRequest} that has all the necessary information to {@link finishSign(OutputStream)} finalize the signature. */
    public interface Stage3 extends StatusRequest.HasRequestedSignature {
      /** Finalize the signature operation, obtaining the result.
       * @param output The signed document is written to this stream. */
      public @NonNull SignResult finishSign(@NonNull OutputStream output) throws PDFASError;
    }
	
}
