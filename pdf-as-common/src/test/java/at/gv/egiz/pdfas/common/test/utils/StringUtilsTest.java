package at.gv.egiz.pdfas.common.test.utils;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import at.gv.egiz.pdfas.common.utils.StringUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class StringUtilsTest {

  @Test    
  public void checkTabulatorReplacing() throws UnsupportedEncodingException {    
    assertEquals("asdfsaf asdfsafsafwer", StringUtils.convertStringToPDFFormat("asdfsaf\u0009asdfsafsafwer"));
    
  }
  
}
