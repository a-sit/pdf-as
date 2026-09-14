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
package at.gv.egiz.pdfas.lib.impl;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.lib.api.sign.SignResult;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;

import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import at.gv.egiz.pdfas.lib.api.StatusRequest;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import lombok.*;

public class StatusRequestImpl {

  private final @NonNull PdfAsImpl pdfAs;
  private final @NonNull OperationStatus status;

  private StatusRequestImpl(@NonNull PdfAsImpl pdfAs, @NonNull OperationStatus status ) { this.pdfAs = pdfAs; this.status = status; }
  private byte[] signatureData;
  private int[] byteRange;

  static @NonNull StatusRequestImpl.Stage1 create(PdfAsImpl pdfAs, OperationStatus status) {
    return new StatusRequestImpl(pdfAs, status).new Stage1();
  }

  class StageBase implements StatusRequest {
    public OperationStatus getStatus() { return status; }
    @Override public @NonNull SignParameter getSignParameter() { return status.getSignParameter(); }
  }

  class StageBaseWithData extends StageBase implements StatusRequest.HasRequestedSignature {

    @Override public byte @NonNull[] getSignatureData() { return signatureData; }

    @Override public int @NonNull[] getSignatureDataByteRange() { return byteRange; }

    @Override public @NonNull RequestedSignature getRequestedSignature() { return status.getRequestedSignature(); }
  }

  public class Stage1 extends StageBase implements StatusRequest.Stage1 {
    public void setSignatureData(byte @NonNull[] data) { signatureData = data; }
    public void setByteRange(int @NonNull[] range) { byteRange = range; }
    public StatusRequestImpl.Stage2 setCertificate(iaik.x509.X509Certificate certificate, String pdfFilter, String pdfSubFilter) throws PDFASError {
      pdfAs.processCertificate(this, certificate, pdfFilter, pdfSubFilter);
      return new StatusRequestImpl.Stage2();
    }
    @Override
    public @NonNull StatusRequestImpl.Stage2 setCertificate(
        @NonNull X509Certificate certificate, @NonNull String pdfFilter, @NonNull String pdfSubFilter)
        throws CertificateException, PDFASError
    {
      @NonNull iaik.x509.X509Certificate c;
      if (certificate instanceof iaik.x509.X509Certificate xc) { c = xc; }
      else { c = new iaik.x509.X509Certificate(certificate.getEncoded()); }
      return setCertificate(c, pdfFilter, pdfSubFilter);
    }
    @Override
    public @NonNull StatusRequestImpl.Stage2 setCertificate(
        byte @NonNull[] encodedCertificate, @NonNull String pdfFilter, @NonNull String pdfSubFilter)
        throws CertificateException, PDFASError
    {
      return setCertificate(new iaik.x509.X509Certificate(encodedCertificate), pdfFilter, pdfSubFilter);
    }
  }

  public class Stage2 extends StageBaseWithData implements StatusRequest.Stage2 {
    @Override
    public @NonNull StatusRequestImpl.Stage3 setSignature(byte @NonNull[] signatureValue) throws PDFASError {
      pdfAs.processSignature(this, signatureValue);
      return new StatusRequestImpl.Stage3();
    }
  }

  public class Stage3 extends StageBaseWithData implements StatusRequest.Stage3 {
    @Override
    public @NonNull SignResult finishSign(@NonNull OutputStream output) throws PDFASError {
      return pdfAs.finishSign(this, output);
    }
  }
}
