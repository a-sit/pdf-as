package at.gv.egiz.pdfas.common.utils;

import at.gv.egiz.pdfas.common.settings.DefaultSignatureProfileSettings;

import java.util.Map;

public class CheckSignatureBlockParameters {

  public static boolean checkSignatureBlockParameterMapIsValid(Map<String, String> map, String keyRegex,
                                                               String valueRegex) {
    if(keyRegex == null || keyRegex.length() == 0) {
      keyRegex = DefaultSignatureProfileSettings.SIG_BLOCK_PARAMETER_DEFAULT_KEY_REGEX;
    }
    if(valueRegex == null || valueRegex.length() == 0) {
      valueRegex = DefaultSignatureProfileSettings.SIG_BLOCK_PARAMETER_DEFAULT_VALUE_REGEX;
    }
  for(String key : map.keySet()){
    if(isValid(key, keyRegex) == false)
      return false;
    if(isValid(map.get(key), valueRegex) == false)
      return false;
  }

    return true;
  }

  public static boolean isValid(String s, String regex) {
    return s.matches(regex);
  }

}
