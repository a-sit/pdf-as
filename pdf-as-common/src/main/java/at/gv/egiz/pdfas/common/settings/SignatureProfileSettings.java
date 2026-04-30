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
package at.gv.egiz.pdfas.common.settings;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.common.exceptions.ErrorConstants;
import at.gv.egiz.pdfas.common.exceptions.PDFASError;

public class SignatureProfileSettings implements IProfileConstants {

  private static final Logger logger = LoggerFactory
      .getLogger(SignatureProfileSettings.class);

  private final Map<String, SignatureProfileEntry> profileInformations =
      new HashMap<>();

  private final Map<String, String> profileSettings = new HashMap<>();

  private final String profileID;

  private String pdfAVersion = null;

  private final ISettings configuration;

  public SignatureProfileSettings(String profileID, ISettings configuration) throws PDFASError {

    if (!configuration.hasPrefix(SIG_OBJ + profileID)) {
      throw new PDFASError(ErrorConstants.ERROR_SIG_INVALID_PROFILE,
          PDFASError.buildInfoString(ErrorConstants.ERROR_SIG_INVALID_PROFILE,
              profileID));
    }

    this.profileID = profileID;
    final String profilePrefix = SIG_OBJ + profileID + KEY_SEPARATOR;
    final String keysPrefix = profilePrefix + PROFILE_KEY;
    final String valuesPrefix = profilePrefix + PROFILE_VALUE;
    final String tablePrefix = profilePrefix + TABLE;
    this.configuration = configuration;

    logger.debug("Reading Profile: " + profileID);
    logger.debug("Keys Prefix: " + keysPrefix);
    logger.debug("Values Prefix: " + valuesPrefix);
    logger.debug("Table Prefix: " + tablePrefix);

    final Map<String, String> keys = configuration.getValuesPrefix(keysPrefix);
    final Map<String, String> values = configuration.getValuesPrefix(valuesPrefix);

    for (String key : keys.keySet()) {
      key = key.substring(key.lastIndexOf('.') + 1);
      String valueKey = keys.get(keysPrefix + KEY_SEPARATOR + key);

      String valueValue = values.get(valuesPrefix + KEY_SEPARATOR
          + key);

      // Lookup default values
      if (valueKey == null) {
        valueKey = DefaultSignatureProfileSettings.getDefaultKeyCaption(key);
      }

      if (valueValue == null) {
        valueValue = DefaultSignatureProfileSettings.getDefaultKeyValue(key);
      }

      final SignatureProfileEntry entry = new SignatureProfileEntry();
      entry.setKey(key);
      entry.setCaption(valueKey);
      entry.setValue(valueValue);
      profileInformations.put(key, entry);
      logger.debug("   " + entry.toString());
    }

    for (String key : values.keySet()) {
      key = key.substring(key.lastIndexOf('.') + 1);

      String valueValue = values.get(valuesPrefix + KEY_SEPARATOR
          + key);

      // Lookup default values
      if (valueValue == null) {
        valueValue = DefaultSignatureProfileSettings.getDefaultKeyValue(key);
      }

      SignatureProfileEntry entry = profileInformations.get(key);
      if (entry == null) {
        entry = new SignatureProfileEntry();
        entry.setKey(key);
        entry.setCaption(null);
        entry.setValue(valueValue);
        profileInformations.put(key, entry);
      }

      logger.debug("   " + entry.toString());
    }

    final Map<String, String> others = configuration
        .getValuesPrefix(profilePrefix);

    if (others != null) {
      for (String key : others.keySet()) {
        logger.trace("Checking key " + key);
        if (key.startsWith(keysPrefix) || key.startsWith(valuesPrefix)
            || key.startsWith(tablePrefix)) {
          continue;
        }

        final String value = others.get(key);
        key = key.substring(key.lastIndexOf('.') + 1);

        profileSettings.put(key, value);

        logger.debug("   Settings: " + key + " : " + value);
      }
    }

    logger.debug("Settings for profile {}", profileID);
    for (final SignatureProfileEntry entry : profileInformations.values()) {
      logger.debug("  " + entry.toString());
    }
  }

  public String getCaption(String key) {
    final SignatureProfileEntry entry = profileInformations.get(key);
    if (entry != null) {
      return entry.getCaption();
    }
    return null;
  }

  protected String getDefaultValue(String key) {
    final String profilePrefix = SIG_OBJ + profileID + KEY_SEPARATOR;
    logger.debug("Searching default value for: " + key);
    if (key.startsWith(profilePrefix)) {
      key = key.substring(profilePrefix.length());
    }
    key = "default." + key;
    logger.debug("Searching default value for: " + key);
    return this.configuration.getValue(key);
  }

  public String getValue(String key) {
    logger.debug("Searching: " + key);
    final SignatureProfileEntry entry = profileInformations.get(key);
    if (entry != null) {
      final String value = entry.getValue();

      if (value == null) {
        return getDefaultValue(key);
      }

      return value;
    }
    final String v = profileSettings.get(key);
    if (v != null) {
      return v;
    }
    return getDefaultValue(key);
  }

  public String getProfileID() {
    return profileID;
  }

  public String getSigningReason() {
    return this.getValue(SIGNING_REASON);
  }

  public String getSignFieldValue() {
    return this.getValue(SIGNFIELD_VALUE);
  }

  public String getProfileTimeZone() {
    return this.getValue(TIMEZONE_BASE);
  }

  public void setPDFAVersion(String version) {
    this.pdfAVersion = version;
  }

  public boolean isPDFA() {

    if (this.pdfAVersion != null) {
      return "1".equals(this.pdfAVersion);
    }

    SignatureProfileEntry entry = profileInformations.get(SIG_PDFA_VALID);
    if (entry != null) {
      final String value = entry.getCaption();
      return Boolean.parseBoolean(value);
    }

    entry = profileInformations.get(SIG_PDFA1B_VALID);
    if (entry != null) {
      final String value = entry.getCaption();
      return Boolean.parseBoolean(value);
    }
    return false;
  }

  public boolean isPDFUA() {
    final SignatureProfileEntry entry = profileInformations.get(SIG_PDFUA_FORCE);
    if (entry != null) {
      final String value = entry.getCaption();
      return Boolean.parseBoolean(value);
    }
    return false;
  }

  public boolean isLatin1Encoding() {
    final SignatureProfileEntry entry = profileInformations.get(LATIN1_ENCODING);
    if (entry != null) {
      final String value = entry.getCaption();
      return Boolean.parseBoolean(value);
    }
    return false;
  }

  public boolean isPDFA3() {
    if (this.pdfAVersion != null) {
      return "3".equals(this.pdfAVersion);
    }

    final SignatureProfileEntry entry = profileInformations.get(SIG_PDFA_VALID);
    if (entry != null) {
      final String value = entry.getCaption();
      return Boolean.parseBoolean(value);
    }
    return false;
  }
}
