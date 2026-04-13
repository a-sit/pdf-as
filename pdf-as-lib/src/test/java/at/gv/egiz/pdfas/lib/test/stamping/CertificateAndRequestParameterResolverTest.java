package at.gv.egiz.pdfas.lib.test.stamping;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.gv.egiz.pdfas.common.exceptions.PDFASError;
import at.gv.egiz.pdfas.common.settings.IProfileConstants;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings;
import at.gv.egiz.pdfas.lib.api.sign.SignParameter;
import at.gv.egiz.pdfas.lib.impl.SignParameterImpl;
import at.gv.egiz.pdfas.lib.impl.stamping.ValueResolver;
import at.gv.egiz.pdfas.lib.impl.status.ICertificateProvider;
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus;
import iaik.x509.X509Certificate;
 
@RunWith(JUnit4.class)
public class CertificateAndRequestParameterResolverTest {

	private OperationStatus opStatus;
	private SignatureProfileSettings sigProfileSetting;
	
	@Before
	public void initialize() throws PDFASError {
		SignParameter signParams = new SignParameterImpl(null, null, null);
		opStatus = new OperationStatus(buildDummySettings(), signParams , null, null);
		
		sigProfileSetting = new SignatureProfileSettings("test", buildDummySettings());
		
	}
	
	@Test
	public void signerWithTitleOld() throws CertificateException, IOException {
		X509Certificate cert = new X509Certificate(
				CertificateAndRequestParameterResolverTest.class.getResourceAsStream(
						"/data/Hermann_Peyerl.20210930-20260930.SerNo651789F5.cer"));		
		ValueResolver resolver = new ValueResolver(buildCertProvider(cert), opStatus);
		
		String result = resolver.resolve("", 
				"${subject.T != null ? (subject.T + \" \") : \"\"}${subject.CN}", 
				sigProfileSetting);						
		assertEquals("wrong signer Name", "A.Univ.Prof.DDr. Hermann Peyerl", result);
		
	}
	
	@Test
	public void signerWithTitle() throws CertificateException, IOException {
		X509Certificate cert = new X509Certificate(
				CertificateAndRequestParameterResolverTest.class.getResourceAsStream(
						"/data/Hermann_Peyerl.20210930-20260930.SerNo651789F5.cer"));		
		ValueResolver resolver = new ValueResolver(buildCertProvider(cert), opStatus);
		
		String result = resolver.resolve("", 
				"${subject.title != null ? (subject.title + \" \") : \"\"}${subject.CN}", 
				sigProfileSetting);						
		assertEquals("wrong signer Name", "A.Univ.Prof.DDr. Hermann Peyerl", result);
		
	}
	
	private ICertificateProvider buildCertProvider(final X509Certificate cert) {
		return new ICertificateProvider() {
			
			@Override
			public X509Certificate getCertificate() {
				return cert;
			}
		};
	}

	private ISettings buildDummySettings() {
	  Map<String, String> configMap = new HashMap<>();
	  configMap.put(IProfileConstants.SIG_OBJ + "test", "test");
	  
	  return new ISettings() {
					  		  		  		  
			@Override
			public boolean hasValue(String key) {
				return configMap.containsKey(key);
			}
			
			@Override
			public boolean hasPrefix(String prefix) {
			  return configMap.keySet().stream()
			      .filter(el -> el.startsWith(prefix))
			      .findFirst()
			      .isPresent();

			}
			
			@Override
			public String getWorkingDirectory() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, String> getValuesPrefix(String prefix) {
			  return configMap.entrySet().stream()
			      .filter(el -> el.getKey().startsWith(prefix))
			      .collect(Collectors.toMap(key -> key.getKey(), value -> value.getValue()));

			}
			
			@Override
			public String getValue(String key) {
				return configMap.get(key);
				
			}
			
			@Override
			public Vector<String> getFirstLevelKeys(String prefix) {
				// TODO Auto-generated method stub
				return null;
			}

      @Override
      public boolean isValue(String key) {
        return isValue(key, false);
      }

      @Override
      public boolean isValue(String key, boolean defaultValue) {
        return hasValue(key) ? Boolean.valueOf(getValue(key)) : defaultValue;
      }
		};		
	}
}
