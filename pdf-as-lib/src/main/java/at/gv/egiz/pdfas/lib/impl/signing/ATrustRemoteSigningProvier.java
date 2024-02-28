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

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import at.gv.egiz.pdfas.common.exceptions.PdfAsException;
import at.gv.egiz.pdfas.lib.impl.signing.AtrustRemoteSigner.InternalPrivateKeyImpl;
import iaik.asn1.DerCoder;
import iaik.asn1.INTEGER;
import iaik.asn1.SEQUENCE;
import iaik.asn1.structures.AlgorithmID;
import iaik.cms.IaikProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ATrustRemoteSigningProvier extends IaikProvider {

  private final static String ID_ECSIGTYPE = "1.2.840.10045.4"; 
  private final static String ATRUST_REQ_SIG_ALG = "SHA256WithRSA";
 
  
  @Getter
  private final String baseUrl;
  private final PrivateKey apiKey;
  private final X509Certificate apiCert;
    
  /**
   * CMS signing provider that uses A-Trust remote-signing API to calculate signatures.
   * 
   * @param atrustBaseUrl Base URL of A-Trust signing service
   * @param remoteApiKeyStore Keystore to authenticate on API
   * @param keyAlias Alias of the API key, or <code>null</code> if should be auto-selected
   * @param keyPassword Password of the API key
   * @throws Exception In case of an initialization error
   */
  public ATrustRemoteSigningProvier(String atrustBaseUrl, KeyStore remoteApiKeyStore, String keyAlias, char[] keyPassword) throws Exception {
    this.baseUrl = atrustBaseUrl;
  
    String selectedkeyAlias = StringUtils.isNotEmpty(keyAlias) ? keyAlias : selectFirstKeyFromStore(remoteApiKeyStore); 

    log.info("Initialize A-Trust Remote-Signer with baseURL:{} and keyAlias:{}", baseUrl, selectedkeyAlias);
    apiKey = (PrivateKey) remoteApiKeyStore.getKey(selectedkeyAlias, keyPassword);
    apiCert = (X509Certificate) remoteApiKeyStore.getCertificate(selectedkeyAlias);
    
  }
 


  /**
   * Get signer certificate. 
   * 
   * @return {@link X509Certificate} of the signer
   * @throws PdfAsException In case of a communication error
   */
  public X509Certificate getCertificate() throws PdfAsException {
    try {
      String getUrl = baseUrl + "/Certificate/" + apiCert.getSerialNumber().toString() + "/nosessionid";
      byte[] seal_certificate_raw = Get(getUrl);
      return new iaik.x509.X509Certificate(seal_certificate_raw);
      
    } catch (Exception e) {
      throw new PdfAsException("error.pdf.sig.17", e);
      
    }
    
  }
  
  @Override
  public byte[] calculateSignatureFromSignedAttributes(AlgorithmID signatureAlgorithm, AlgorithmID digestAlgorithm,
      PrivateKey privateKey, byte[] signedAttributes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    try {
            
    MessageDigest hashAlgo = MessageDigest.getInstance(((InternalPrivateKeyImpl)privateKey).getDigestAlgorithm());
    byte[] hashToSign = hashAlgo.digest(signedAttributes);
   
    // prepare Request
    Signature sig = Signature.getInstance(ATRUST_REQ_SIG_ALG);
    sig.initSign(apiKey);
    sig.update(hashToSign);
    byte[] HashSignature = sig.sign();

    Base64.getEncoder().encodeToString(hashToSign);

    String request = "{\"AuthSerial\": \"" + apiCert.getSerialNumber() + "\", \"Hash\": \"";
    request += new String(Base64.getEncoder().encode(hashToSign));
    request +="\", \"HashSignature\": \"";
    request += new String(Base64.getEncoder().encode(HashSignature));
    request +="\", \"HashSignatureMechanism\": \"SHA256WithRSA\" }";

    String postUrl = baseUrl + "Sign/nosessionid";
    String result = Post(postUrl,request);
        
    JsonElement json = JsonParser.parseString(result);
    byte[] jwsSignature = Base64.getDecoder().decode(json.getAsJsonObject().get("Signature").getAsString());    
    return wrapSignatureValue(jwsSignature, signatureAlgorithm);
        
    } catch (Exception e) {
      throw new SignatureException(e);
      
    }
        
  }
  
  private String selectFirstKeyFromStore(KeyStore keystore) throws KeyStoreException {
    for (Enumeration<String> en = keystore.aliases(); en.hasMoreElements();) {
      String alias = (String)en.nextElement();
      if(keystore.isKeyEntry(alias)) {
          return alias;
          
      }    
    }    
    throw new KeyStoreException("No A-Trust API Key in KeyStore");
    
  }
  
  private static byte[] wrapSignatureValue(byte[] sig, AlgorithmID sigAlgorithmID) {
    String id = sigAlgorithmID.getAlgorithm().getID();
    if (id.startsWith(ID_ECSIGTYPE)) //X9.62 Format ECDSA signatures
    {
      //Wrap r and s in ASN.1 SEQUENCE
      byte[] r = Arrays.copyOfRange(sig, 0, sig.length/2);
      byte[] s = Arrays.copyOfRange(sig, sig.length/2, sig.length);
      SEQUENCE sigS = new SEQUENCE();
      sigS.addComponent(new INTEGER(new BigInteger(1, r)));
      sigS.addComponent(new INTEGER(new BigInteger(1, s)));
      return DerCoder.encode(sigS);
      
    } else
      return sig;
    
  }
  
  private static String Post(String postUrl, String requestJson) throws Exception
  {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(postUrl);
      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Content-type", "application/json");
      httpPost.setEntity(new ByteArrayEntity(requestJson.getBytes("UTF8")));

      CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

      HttpEntity resEntity = httpResponse.getEntity();
      String content = EntityUtils.toString(resEntity);
      httpClient.close();
      return content;
      
  }

  private static byte[] Get(String getUrl) throws Exception
  {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(getUrl);
      CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

      byte[] content = EntityUtils.toByteArray(httpResponse.getEntity());
      httpClient.close();
      return content;
      
  }
  
}
