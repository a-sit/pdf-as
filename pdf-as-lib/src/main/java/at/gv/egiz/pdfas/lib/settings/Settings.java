/*******************************************************************************
 * <copyright> Copyright 2014 by E-Government Innovation Center EGIZ, Graz, Austria </copyright>
 * PDF-AS has been contracted by the E-Government Innovation Center EGIZ, a
 * joint initiative of the Federal Chancellery Austria and Graz University of
 * Technology.
 * <p>
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * <p>
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 ******************************************************************************/
package at.gv.egiz.pdfas.lib.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsException;
import at.gv.egiz.pdfas.common.settings.IProfileConstants;
import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.common.settings.Profiles;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Settings implements ISettings, IProfileConstants { 
  protected Properties properties = new Properties();
  protected File workDirectory;

  public Settings(File workDirectory) {
    try {
      this.workDirectory = workDirectory;
      loadSettings(workDirectory);
    } catch (final PdfAsSettingsException e) {
      log.error(e.getMessage(), e);
    }
  }

  private void loadSettingsRecursive(File workDirectory, File file)
      throws RuntimeException {
    try {
      final String configDir = workDirectory.getAbsolutePath() + File.separator
          + CFG_DIR;
      final Properties tmpProps = new Properties();
      log.debug("Loading: " + file.getName());
      tmpProps.load(new FileInputStream(file));

      properties.putAll(tmpProps);

      this.getValuesPrefix(INCLUDE, tmpProps).values()
          .forEach(el -> loadAdditionConfigFile(el, configDir));

    } catch (final IOException e) {
      throw new RuntimeException(e);

    }
  }

  private void loadAdditionConfigFile(String includeFileName, String configDir) {
    File contextFolder = new File(configDir);

    final File includeInstruction = new File(contextFolder, includeFileName);
    contextFolder = includeInstruction.getParentFile();
    final String includeName = includeInstruction.getName();

    final WildcardFileFilter fileFilter = new WildcardFileFilter(
        includeName, IOCase.SENSITIVE);
    Collection<File> includeFiles = null;

    if (contextFolder != null && contextFolder.exists()
        && contextFolder.isDirectory()) {
      includeFiles = FileUtils.listFiles(contextFolder,
          fileFilter, null);
    }
    if (includeFiles != null && !includeFiles.isEmpty()) {
      log.debug("Including '" + includeFileName + "'.");
      for (final File includeFile : includeFiles) {
        loadSettingsRecursive(workDirectory, includeFile);
      }
    }

  }

  private void showAugments(Profiles profiles) {
    if (!profiles.getAugments().isEmpty()) {
      log.debug("\tAugments for {}", profiles.getName());
      for (final Profiles element : profiles.getAugments()) {
        log.debug("\t\t{}", element.getName());
      }
    }
  }

  private boolean isAugmentsReady(Profiles profiles) {
    boolean allInitialized = true;
    for (final Profiles element : profiles.getAugments()) {
      if (!element.isInitialized()) {
        allInitialized = false;
      }
    }
    return allInitialized;
  }

  private boolean isParentReady(Profiles profiles) {
    if (profiles.getParent() != null) {
      return profiles.getParent().isInitialized();
    } else {
      return false;
    }
  }

  private void performAugmentConfiguration(Profiles profiles) {
    final String childBase = "sig_obj."
        + profiles.getName();

    for (final Profiles augmentingProfile : profiles.getAugments()) {
      final String augmentingBase = "sig_obj." + augmentingProfile.getName();

      for (final String key : this.getKeys(
          augmentingBase + ".")) {
        final String keyToCopy = key.substring(augmentingBase
            .length());
        // log.debug("Profile: {} => {}",
        // key, childBase+keyToCopy);
        final String sourceKey = augmentingBase + keyToCopy;
        final String targetKey = childBase + keyToCopy;

        if (!this.hasValue(targetKey)) {
          properties.setProperty(targetKey,
              this.getValue(sourceKey));
          // log.debug("Replaced: {} with Value from {}",
          // childBase+keyToCopy, parentBase+keyToCopy);
        } else {
          // log.debug("NOT Replaced: {} with Value from {}",
          // childBase+keyToCopy, parentBase+keyToCopy);
        }
      }
    }
  }

  private void performParentConfiguration(Profiles profiles) {
    if (profiles.getParent() != null) {
      // If Parent is initialized Copy Properties from Parent
      // to this profile
      final String parentBase = "sig_obj." + profiles.getParent().getName();
      final String childBase = "sig_obj."
          + profiles.getName();

      for (final String key : this.getKeys(
          parentBase + ".")) {
        final String keyToCopy = key.substring(parentBase
            .length());
        // log.debug("Profile: {} => {}",
        // key, childBase+keyToCopy);
        final String sourceKey = parentBase + keyToCopy;
        final String targetKey = childBase + keyToCopy;

        if (!this.hasValue(targetKey)) {
          properties.setProperty(targetKey,
              this.getValue(sourceKey));
          // log.debug("Replaced: {} with Value from {}",
          // childBase+keyToCopy, parentBase+keyToCopy);
        } else {
          // log.debug("NOT Replaced: {} with Value from {}",
          // childBase+keyToCopy, parentBase+keyToCopy);
        }
      }
    }
  }

  private void buildProfiles() {
    final Map<String, Profiles> profiles = new HashMap<>();

    for (final String key : this.getFirstLevelKeys("sig_obj.types.")) {
      final String profile = key.substring("sig_obj.types.".length());
      // System.out.println("[" + profile + "]: " + this.getValue(key));
      if (this.getValue(key).equals("on")) {
        final Profiles prof = new Profiles(profile);
        profiles.put(profile, prof);
      }
    }

    for (final Entry<String, Profiles> entry : profiles.entrySet()) {
      entry.getValue().findParent(properties, profiles);
    }

    for (final Entry<String, Profiles> entry : profiles.entrySet()) {
      if (entry.getValue().getParent() == null) {
        log.debug("Got Profile: [{}] : {}", entry.getKey(), entry.getValue().getName());
        showAugments(entry.getValue());
      } else {
        log.debug("Got Profile: [{}] : {} (Parent {})", entry.getKey(),
            entry.getValue().getName(), entry.getValue().getParent().getName());
        showAugments(entry.getValue());
      }
    }

    log.debug("Configured Settings: {}",
        properties.size());

    // Resolve Parent Structures ...
    while (!profiles.isEmpty()) {
      final List<String> removes = new ArrayList<>();
      for (final Entry<String, Profiles> entry : profiles.entrySet()) {
        // Remove all base Profiles ...
        if (entry.getValue().getParent() == null && entry.getValue().getAugments().isEmpty()) {
          // Has neither parent or augmenting profiles

          entry.getValue().setInitialized(true);
          removes.add(entry.getKey());
        } else if (entry.getValue().getParent() == null) {
          // Has augmenting profiles but no parent

          // check if all augmenting profiles are initialized if so
          // add them

          final Profiles profile = entry.getValue();
          if (this.isAugmentsReady(profile)) {
            this.performAugmentConfiguration(profile);
            // Copy done
            entry.getValue().setInitialized(true);
            removes.add(entry.getKey());
          } else {
            log.debug("Not all augmenting profiles are ready yet for {}", entry.getValue().getName());
          }
        } else if (entry.getValue().getAugments().isEmpty()) {

          // Has parent but no augmenting profiles
          final Profiles profile = entry.getValue();

          if (this.isParentReady(profile)) {
            this.performParentConfiguration(profile);
            // Copy done
            entry.getValue().setInitialized(true);
            removes.add(entry.getKey());
          }
        } else {

          // Has parent and augmenting profiles

          final Profiles profile = entry.getValue();
          if (this.isAugmentsReady(profile) && this.isParentReady(profile)) {
            // order is curcial, augments preceed over parent configuration
            this.performAugmentConfiguration(profile);
            this.performParentConfiguration(profile);

            // Copy done
            entry.getValue().setInitialized(true);
            removes.add(entry.getKey());
          }
        }
      }

      // Remove all Profiles from Remove List

      if (removes.isEmpty() && !profiles.isEmpty()) {
        log.error("Failed to build inheritant Profiles, running in infinite loop! (aborting ...)");
        log.error("Profiles that cannot be resolved completly:");
        for (final Entry<String, Profiles> entry : profiles.entrySet()) {
          log.error("Problem Profile: [{}] : {}", entry.getKey(), entry.getValue().getName());
        }
        return;
      }

      for (final String element : removes) {
        profiles.remove(element);
      }
    }

    log.debug("Derived Settings: {}",
        properties.size());

  }

  public void debugDumpProfileSettings(String profileName) {
    log.debug("Configuration for {}", profileName);
    for (final String key : this.getKeys("sig_obj." + profileName + ".")) {
      log.debug("  {}: {}", key, this.getValue(key));
    }
  }

  public void loadSettings(File workDirectory) throws PdfAsSettingsException {
    try {
      final String configDir = workDirectory.getAbsolutePath() + File.separator
          + CFG_DIR;
      final String configFile = configDir + File.separator + CFG_FILE;
      loadSettingsRecursive(workDirectory, new File(configFile));
      buildProfiles();

    } catch (final RuntimeException e) {
      throw new PdfAsSettingsException("Failed to read settings!", e);

    }
  }

  @Override
  public String getValue(String key) {
    return properties.getProperty(key);
  }

  @Override
  public boolean hasValue(String key) {
    return properties.containsKey(key);
  }

  private Map<String, String> getValuesPrefix(String prefix, Properties props) {
    return props.entrySet().stream()
        .filter(el -> el.getKey().toString().startsWith(prefix))
        .collect(Collectors.toMap(key -> key.getKey().toString(), value -> value.getValue().toString()));

  }

  @Override
  public Map<String, String> getValuesPrefix(String prefix) {
    return getValuesPrefix(prefix, properties);
  }

  public Vector<String> getKeys(String prefix) {
    final Iterator<Object> keyIterator = properties.keySet().iterator();
    final Vector<String> valueMap = new Vector<>();
    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(prefix)) {
        valueMap.add(key);
      }
    }
    return valueMap;
  }

  @Override
  public Vector<String> getFirstLevelKeys(String prefix) {
    final String mPrefix = prefix.endsWith(".") ? prefix : prefix + ".";
    final Iterator<Object> keyIterator = properties.keySet().iterator();
    final Vector<String> valueMap = new Vector<>();
    while (keyIterator.hasNext()) {
      final String key = keyIterator.next().toString();

      if (key.startsWith(prefix)) {
        final int keyIdx = key.indexOf('.', mPrefix.length()) > 0 ? key
            .indexOf('.', mPrefix.length()) : key.length();
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
    final Iterator<Object> keyIterator = properties.keySet().iterator();
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
    return this.workDirectory.getAbsolutePath();
  }

}
