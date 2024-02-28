/*
 * Copyright 2024 by A-SIT, Secure Information Technology Center Austria
 *
 * Licensed under the EUPL, Version 1.2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.gv.egiz.pdfas.lib.impl.signing;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.dsig.SignatureMethod;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.common.exceptions.PdfAsSignatureException;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import at.gv.egiz.pdfas.lib.api.PdfAsFactory;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature;
import at.gv.egiz.pdfas.lib.util.CertificateUtils;
import at.gv.egiz.pdfas.lib.util.SignatureUtils;
import at.gv.egiz.sl.util.ISignatureConnector;
import iaik.asn1.ASN1Object;
import iaik.asn1.CodingException;
import iaik.asn1.ObjectID;
import iaik.asn1.SEQUENCE;
import iaik.asn1.UTF8String;
import iaik.asn1.structures.AlgorithmID;
import iaik.asn1.structures.Attribute;
import iaik.asn1.structures.ChoiceOfTime;
import iaik.cms.CMSException;
import iaik.cms.ContentInfo;
import iaik.cms.IssuerAndSerialNumber;
import iaik.cms.SignedData;
import iaik.cms.SignerInfo;
import iaik.smime.ess.ESSCertID;
import iaik.smime.ess.ESSCertIDv2;
import iaik.x509.X509Certificate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AtrustRemoteSigner implements ISignatureConnector {
    
  private ATrustRemoteSigningProvier atrustSecProvider;
  private static final String SIGNATURE_DEVICE = "A-Trust Remote-Signing API";

  private String baseurl = "http://hs-abnahme.a-trust.at/SealQualified/v1/";
  
  
  public AtrustRemoteSigner() throws Exception {
    
    // Load KeyStore for API autentication
    char[] pfxPassword = "testpwd".toCharArray();
    String pfxFile = "/home/tlenz/Projekte/pdfas4/source/pdf-as-lib/src/main/resources/authentication_certificate.p12";        
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(new FileInputStream(pfxFile), pfxPassword);
    
    // Initialize security provider for A-Trust remote-signing
    atrustSecProvider = new ATrustRemoteSigningProvier(baseurl, keystore, null, pfxPassword);
    
  }
  
  @Override
  public X509Certificate getCertificate(SignParameter parameter) throws PdfAsException {
    try {
      return new X509Certificate(atrustSecProvider.getCertificate().getEncoded());
      
    } catch (CertificateException e) {
      throw new PdfAsException("error.pdf.sig.17", e);
      
    }     
  }

  @Override
  public byte[] sign(byte[] input, int[] byteRange, SignParameter parameter,
      RequestedSignature requestedSignature) throws PdfAsException {
    try {
      log.debug("Creating PDF signature by using A-Trust remote-signing API ... ");

      requestedSignature.getStatus().getMetaInformations().put(ErrorConstants.STATUS_INFO_SIGDEVICE, SIGNATURE_DEVICE);
      requestedSignature.getStatus().getMetaInformations().put(ErrorConstants.STATUS_INFO_SIGDEVICEVERSION, PdfAsFactory.getVersion());
      
            // sign content
      byte[] signature = generateSignerInformation(parameter, input);           
            
      // verify signature value
      SignatureUtils.verifySignature(signature, input);
      
      log.info("Return signature by using A-Trust remote-signing API");          
      return signature;
      
    } catch (PDFASError e) {
      throw new PdfAsSignatureException("error.pdf.sig.01", e);
      
    }
  }
  
  private byte[] generateSignerInformation(SignParameter parameter, byte[] input) throws PdfAsSignatureException {    
    try {
      X509Certificate signerCert = getCertificate(parameter);
      
      SignedData signedData = new SignedData(input, SignedData.EXPLICIT);                 
      signedData.addCertificates(new Certificate[] { signerCert });   
      signedData.setSecurityProvider(atrustSecProvider);
      signedData.addSignerInfo(buildSignerInfo(signerCert, parameter));
            
      ContentInfo contentInfo = new ContentInfo(signedData);
      return contentInfo.getEncoded();
            
    } catch (PdfAsException | NoSuchAlgorithmException | CMSException e) {
      throw new PdfAsSignatureException("error.pdf.sig.01", e);
      
    }    
  }

  private SignerInfo buildSignerInfo(X509Certificate signerCert, SignParameter parameter) throws PdfAsSignatureException {    
    try {
      IssuerAndSerialNumber issuer = new IssuerAndSerialNumber(signerCert);
      AlgorithmID[] algorithms = CertificateUtils.getAlgorithmIDs(signerCert);           
      PrivateKey privKey = new InternalPrivateKeyImpl(algorithms);
      SignerInfo signer = new SignerInfo(issuer, algorithms[1], algorithms[0], privKey);      

      //Check PAdES Flag
      if (parameter.getConfiguration().hasValue(IConfigurationConstants.SIG_PADES_FORCE_FLAG))
      {
        if (IConfigurationConstants.TRUE.equalsIgnoreCase(parameter.getConfiguration().getValue(IConfigurationConstants.SIG_PADES_FORCE_FLAG)))
        {
          setAttributes(signerCert, signer);
          
        } else {
          setAttributes("application/pdf", signerCert, new Date(), signer);
          
        }
        
      } else {
        setAttributes("application/pdf", signerCert, new Date(), signer);
        
      }
      
      return signer;
      
    } catch (NoSuchAlgorithmException | CertificateException | CodingException e) {
      throw new PdfAsSignatureException("error.pdf.sig.01", e);
      
    }      
  }
  
  private void setMimeTypeAttrib(List<Attribute> attributes, String mimeType) {
    String oidStr = "0.4.0.1733.2.1";
    String name = "mime-type";
    ObjectID mimeTypeOID = new ObjectID(oidStr, name);

    Attribute mimeTypeAtt = new Attribute(mimeTypeOID, new ASN1Object[] { new UTF8String(mimeType) });
    attributes.add(mimeTypeAtt);
  }

  private void setContentTypeAttrib(List<Attribute> attributes) {
    Attribute contentType = new Attribute(ObjectID.contentType, new ASN1Object[] { ObjectID.cms_data });
    attributes.add(contentType);
  }

  private void setSigningCertificateAttrib(List<Attribute> attributes,
      X509Certificate signingCertificate) throws CertificateException,
      NoSuchAlgorithmException, CodingException {
    ObjectID id;
    ASN1Object value = new SEQUENCE();
    AlgorithmID[] algorithms = CertificateUtils.getAlgorithmIDs(signingCertificate);
    if (algorithms[1].equals(AlgorithmID.sha1)) {
      id = ObjectID.signingCertificate;
      value.addComponent(new ESSCertID(signingCertificate, true).toASN1Object());
    
    } else {
      id = ObjectID.signingCertificateV2;
      value.addComponent(new ESSCertIDv2(algorithms[1], signingCertificate, true).toASN1Object());
    
    }
    
    ASN1Object signingCert = new SEQUENCE();
    signingCert.addComponent(value);
    Attribute signingCertificateAttrib = new Attribute(id, new ASN1Object[] { signingCert });
    attributes.add(signingCertificateAttrib);
    
  }

  private void setSigningTimeAttrib(List<Attribute> attributes, Date date) {
    Attribute signingTime = new Attribute(ObjectID.signingTime, new ASN1Object[] { new ChoiceOfTime(date).toASN1Object() });
    attributes.add(signingTime);
    
  }

  private void setAttributes(String mimeType,
      X509Certificate signingCertificate, Date signingTime,
      SignerInfo signerInfo) throws CertificateException,
      NoSuchAlgorithmException, CodingException {
    List<Attribute> attributes = new ArrayList<Attribute>();

    setMimeTypeAttrib(attributes, mimeType);
    setContentTypeAttrib(attributes);
    setSigningCertificateAttrib(attributes, signingCertificate);
    setSigningTimeAttrib(attributes, signingTime);
    Attribute[] attributeArray = attributes.toArray(new Attribute[attributes.size()]);
    signerInfo.setSignedAttributes(attributeArray);
    
  }

  private void setAttributes(X509Certificate signingCertificate, SignerInfo signerInfo) throws CertificateException,
      NoSuchAlgorithmException, CodingException {
    List<Attribute> attributes = new ArrayList<Attribute>();

    setContentTypeAttrib(attributes);
    setSigningCertificateAttrib(attributes, signingCertificate);
    Attribute[] attributeArray = attributes.toArray(new Attribute[attributes.size()]);
    signerInfo.setSignedAttributes(attributeArray);
    
  }
  
  public class InternalPrivateKeyImpl implements PrivateKey {

    private static final long serialVersionUID = 1L;

    private String algorithm;
        
    @Getter
    private String digestAlgorithm;
    
    public InternalPrivateKeyImpl(AlgorithmID[] algorithms) throws NoSuchAlgorithmException {
      if (AlgorithmID.dsaWithSHA1.equals(algorithms[0]) ) {
        algorithm = SignatureMethod.DSA_SHA1;
        digestAlgorithm = "SHA-1";
        
      } else if (AlgorithmID.sha256WithRSAEncryption.equals(algorithms[0]) ) {
        algorithm = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
        digestAlgorithm = "SHA-256";
        
      } else if (AlgorithmID.ecdsa_With_SHA512.equals(algorithms[0]) ) {
        algorithm = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512";
        digestAlgorithm = "SHA-512";
        
      } else if (AlgorithmID.ecdsa_With_SHA256.equals(algorithms[0]) ) {
        algorithm = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";
        digestAlgorithm = "SHA-256";
        
      } else if (AlgorithmID.ecdsa_With_SHA1.equals(algorithms[0]) ) {
        algorithm = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1";
        digestAlgorithm = "SHA-1";
        
      } else {
        throw new NoSuchAlgorithmException("Public key algorithm '" + algorithm + "' not supported.");
        
      }
      
    }
    
    @Override
    public String getAlgorithm() {
      return algorithm;
    }

    @Override
    public String getFormat() {
      return null;
    }

    @Override
    public byte[] getEncoded() {
      throw new UnsupportedOperationException("InternalPrivateKey does not support the getEncoded() method.");
    }
    
    
  }
  
}
