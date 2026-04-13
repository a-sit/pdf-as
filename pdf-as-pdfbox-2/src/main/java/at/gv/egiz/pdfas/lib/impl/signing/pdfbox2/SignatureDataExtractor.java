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
package at.gv.egiz.pdfas.lib.impl.signing.pdfbox2;

import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureExtractor;
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureInterface;
import iaik.x509.X509Certificate;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.Calendar;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import at.gv.egiz.pdfas.common.utils.StreamUtils;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;

public class SignatureDataExtractor implements PDFASSignatureExtractor, PDFASSignatureInterface, SignatureInterface {

	@Getter
    protected X509Certificate certificate;
	@Getter
    protected byte[] signatureData;
	
	protected String pdfSubFilter;
	protected String pdfFilter;
	@Getter
    protected int[] byteRange;
	protected Calendar date;
	
	public SignatureDataExtractor(X509Certificate certificate, 
			String filter, String subfilter, Calendar date) {
		this.certificate = certificate;
		this.pdfFilter = filter;
		this.pdfSubFilter = subfilter;
		this.date = date;
	}

  public String getPDFSubFilter() {
		return this.pdfSubFilter;
	}

  public String getPDFFilter() {
		return this.pdfFilter;
	}

  /** Called by PDFBox.
   * We save the data to be signed and return an all-zeros signature (padded by pdfbox).
   * We splice the actual signature in at a later point.
   */
  public byte[] sign(InputStream content) throws IOException {
    this.signatureData = StreamUtils.inputStreamToByteArray(content);
    return new byte[] { 0 };
  }

  public void setPDSignature(PDSignature signature) {
      this.byteRange = signature.getByteRange();
  }

  public Calendar getSigningDate() {
      return this.date;
  }
}
