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
import iaik.x509.X509Certificate;

import java.security.cert.CertificateException;

import at.gv.egiz.pdfas.lib.api.StatusRequest;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class StatusRequestImpl implements StatusRequest {

  private final PdfAsImpl pdfAs;
  @Getter
  private final OperationStatus status;

  private StatusRequestImpl(PdfAsImpl pdfAs, OperationStatus status ) { this.pdfAs = pdfAs; this.status = status; }
  static StatusRequestImpl.Stage1 create(PdfAsImpl pdfAs, OperationStatus status) {
    return new StatusRequestImpl(pdfAs, status).new Stage1();
  }

  @Setter @Getter
  private byte[] signatureData;
  @Setter
  private int[] byteRange;

  @Override public int[] getSignatureDataByteRange() {
      return byteRange;
  }

  @Override public SignParameter getSignParameter() {
      return this.status.getSignParameter();
  }

  @Override public RequestedSignature getRequestedSignature() { return this.status.getRequestedSignature(); }

  class StageBase implements StatusRequest {
    public OperationStatus getStatus() { return status; }
    @Override public byte[] getSignatureData() { return signatureData; }
    @Override public int[] getSignatureDataByteRange() { return byteRange; }
    @Override public SignParameter getSignParameter() { return status.getSignParameter(); }
    @Override public RequestedSignature getRequestedSignature() { return status.getRequestedSignature(); }
  }

  class Stage1 extends StageBase implements StatusRequest.Stage1 {
    public StatusRequestImpl.Stage2 setCertificate(X509Certificate certificate) throws PDFASError {
      pdfAs.processCertificate(StatusRequestImpl.this, certificate);
      return new StatusRequestImpl.Stage2();
    }
    @Override
    public StatusRequestImpl.Stage2 setCertificate(byte[] encodedCertificate) throws CertificateException, PDFASError {
      return setCertificate(new X509Certificate(encodedCertificate));
    }
  }

  class Stage2 extends StageBase implements StatusRequest.Stage2 {
    @Override
    public StatusRequestImpl.Stage3 setSignature(byte[] signatureValue) throws PDFASError {
      pdfAs.processSignature(StatusRequestImpl.this, signatureValue);
      return new StatusRequestImpl.Stage3();
    }
  }

  class Stage3 extends StageBase implements StatusRequest.Stage3 {
    @Override
    public SignResult finishSign() throws PDFASError {
      return pdfAs.finishSign(StatusRequestImpl.this);
    }
  }
}
