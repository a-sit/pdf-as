package at.gv.egiz.pdfas.lib.test.mains;

import at.gv.egiz.pdfas.common.utils.CheckSignatureBlockParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SignatureBlockParameterTest {


  @Test
  public void testKeyInvalid() {
    if(checkValid( "aaaaaaaaaaaaaaaaaaaaa" , "^([A-za-z]){1,20}$") == true){assert(false);}
    if(checkValid( "" , "^([A-za-z]){1,20}$") == true){assert(false);}
    if(checkValid( "a9" , "^([A-za-z]){1,20}$") == true){assert(false);}
  }
  @Test
  public void testKeyValid() {
    if(checkValid( "aaa" +"aa", "^([A-za-z]){1,20}$") == false){assert(false);}
    if(checkValid( "aaa" , "^([A-za-z]){1,20}$") == false){assert(false);}
    if(checkValid( "aaaaaaaaaaaaaaaaaaaa", "^([A-za-z]){1,20}$") == false){assert(false);}
    if(checkValid( "AA", "^([A-za-z]){1,20}$") == false){assert(false);}
  }
  @Test
  public void testValueValid() {
    if(checkValid( "aaa" +"aa", "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "aaa" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "a!\"$%&/()[]=?aa" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "a!\"$%&/()[]=?aa-_,;.:[]|{}" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
//    if(checkValid( "a!\"§$%&/()=?aa" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "aa!%&/()=?a" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "a{\"a!%&/()=?a" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "BB" , "^([\\p{Print}]){1,100}$") == false){assert(false);}
    if(checkValid( "BB " , "^([\\p{Print}]){1,100}$") == false){assert(false);}
  }
  @Test
  public void testValueInvalid() {
    if(checkValid((char) 13 +"aaa" +"aa", "^([\\p{Print}]){1,100}$") == true){assert(false);}
    if(checkValid((char) 13 +"", "^([\\p{Print}]){1,100}$") == true){assert(false);}
    if(checkValid( "aaa" +(char) 13, "^([\\p{Print}]){1,100}$") == true){assert(false);}
    if(checkValid("", "^([\\p{Print}]){1,100}$") == true){assert(false);}
    if(checkValid("a", "^([\\p{Print}]){2,100}$") == true){assert(false);}
    if(checkValid("aaa"+(char) 13 +"aa", "^([\\p{Print}]){1,100}$") == true){assert(false);}

  }

  public boolean checkValid(String s, String regex) {
    return CheckSignatureBlockParameters.isValid(s, regex);
  }
}
