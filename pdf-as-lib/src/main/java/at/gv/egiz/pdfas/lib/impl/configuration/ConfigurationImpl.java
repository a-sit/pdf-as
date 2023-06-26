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
package at.gv.egiz.pdfas.lib.impl.configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.Configuration;
import at.gv.egiz.pdfas.lib.settings.Settings;

public class ConfigurationImpl implements ISettings, Configuration {

  protected Properties overwrittenProperties = new Properties();

  protected ISettings settings;

  public ConfigurationImpl(ISettings settings) {
    this.settings = settings;
  }

  @Override
  public void setValue(String key, String value) {
    overwrittenProperties.setProperty(key, value);
  }

  @Override
  public String getValue(String key) {
    if (overwrittenProperties.containsKey(key)) {
      return overwrittenProperties.getProperty(key);
    } else {
      return this.settings.getValue(key);
    }
  }

  @Override
  public boolean hasValue(String key) {
    if (overwrittenProperties.containsKey(key)) {
      return true;
    } else {
      return this.settings.hasValue(key);
    }
  }

  @Override
  public Map<String, String> getValuesPrefix(String prefix) {
    final Map<String, String> valueMap = this.settings.getValuesPrefix(prefix);
    final Iterator<Object> keyIterator = overwrittenProperties.keySet().iterator();

    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(prefix)) {
        valueMap.put(key, overwrittenProperties.getProperty(key));
      }
    }

    if (valueMap.isEmpty()) {
      return null;
    }

    return valueMap;
  }

  @Override
  public Vector<String> getFirstLevelKeys(String prefix) {

    Vector<String> valueMap = this.settings.getFirstLevelKeys(prefix);
    if (valueMap == null) {
      valueMap = new Vector<>();
    }

    final String mPrefix = prefix.endsWith(".") ? prefix : prefix + ".";
    final Iterator<Object> keyIterator = overwrittenProperties.keySet().iterator();

    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(prefix)) {
        final int keyIdx = key.indexOf('.', mPrefix.length()) > 0 ? key.indexOf('.', mPrefix.length())
            : key.length();
        final String firstLevels = key.substring(0, keyIdx);
        if (!valueMap.contains(firstLevels)) {
          valueMap.add(firstLevels);
        }
      }
    }

    if (valueMap.isEmpty()) {
      return null;
    }

    return valueMap;
  }

  @Override
  public boolean hasPrefix(String prefix) {

    if (this.settings.hasPrefix(prefix)) {
      return true;
    }

    final Iterator<Object> keyIterator = overwrittenProperties.keySet().iterator();
    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getWorkingDirectory() {
    return this.settings.getWorkingDirectory();
  }

  @Override
  public void cloneProfile(String originalPrefix, String clonedPrefix) {
    final Map<String, String> source = getValuesPrefix(originalPrefix);

    for (final String origKey : source.keySet()) {
      final String cloneKey = origKey.replace(originalPrefix, clonedPrefix);
      this.overwrittenProperties.setProperty(cloneKey, source.get(origKey));
    }
  }

  @Override
  public void removeProfile(String configurationPrefix) {
    final Iterator<Object> keyIterator = overwrittenProperties.keySet().iterator();
    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(configurationPrefix)) {
        overwrittenProperties.remove(key);
      }
    }
  }

  public void debugDumpProfileSettings(String profileName) {
    ((Settings) settings).debugDumpProfileSettings(profileName);
  }

}
