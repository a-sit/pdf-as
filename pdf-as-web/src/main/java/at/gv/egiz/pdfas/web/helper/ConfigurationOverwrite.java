package at.gv.egiz.pdfas.web.helper;

import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.web.config.WebConfiguration;

@Slf4j
public class ConfigurationOverwrite {

    public static void overwriteConfiguration(Map<String, String> overwrite, Configuration config) {
        if (WebConfiguration.isAllowExtOverwrite() && overwrite != null && config != null) {
          for (Entry<String, String> entry : overwrite.entrySet()) {
            if (WebConfiguration.isOverwriteAllowed(entry.getKey())) {
              config.setValue(entry.getKey(), entry.getValue());
            } else {
              log.warn(
                  "External component tried to overwrite cfg {}. This is not in the whitelist!",
                  entry.getKey());
            }
          }
        }
    }
}
