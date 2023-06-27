package at.gv.egiz.pdfas.common.test.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import at.gv.egiz.pdfas.common.settings.DefaultSignatureProfileSettings;
import at.gv.egiz.pdfas.common.utils.CheckSignatureBlockParameters;

@RunWith(BlockJUnit4ClassRunner.class)
public class CheckSignatureParametersTest {

	@Test
	public void singleTest() {
		assertTrue("valid characters are not possilbe", 
				CheckSignatureBlockParameters.isValid("Güssing", 
						DefaultSignatureProfileSettings.SIG_BLOCK_PARAMETER_DEFAULT_VALUE_REGEX));
		
	}
	
	@Test
	public void specialCharactersCompiletimeConfig() {		
		Map<String, String> toTest = new HashMap<>();
		toTest.put("test", "Güssing");
		
		assertTrue("valid characters are not possilbe", 
				CheckSignatureBlockParameters.checkSignatureBlockParameterMapIsValid(toTest , null, null));
		
	}

	@Test
	public void specialCharactersExampleConfig() {		
		Map<String, String> toTest = new HashMap<>();
		toTest.put("test", "Güssing");
		
		assertFalse("valid characters are not possilbe", 
				CheckSignatureBlockParameters.checkSignatureBlockParameterMapIsValid(toTest , 
						"^([A-za-z]){1,20}$", "^([\\p{Print}]){1,100}$"));
		
	}
	
}
