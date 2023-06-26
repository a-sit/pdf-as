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

import at.gv.egiz.pdfas.common.settings.ISettings;
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaceholderConfiguration extends SpecificBaseConfiguration
    implements IConfigurationConstants {

  public PlaceholderConfiguration(ISettings configuration) {
    super(configuration);
  }

  public boolean isGlobalPlaceholderEnabled() {
    if (configuration.hasValue(PLACEHOLDER_SEARCH_ENABLED)) {
      final String value = configuration.getValue(PLACEHOLDER_SEARCH_ENABLED);
      if (value.equalsIgnoreCase(TRUE)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Match selected Profile for Placeholder Enables to activate placeholder
   * search/match for different profiles
   *
   * @return
   */
  public boolean isProfileConfigurationEnabled(String profileID) {
    log.trace("Check if placeHolders are enabled for profile: {}", profileID);
    
    final String profileMatch = SIG_OBJECT + SEPERATOR + profileID + SEPERATOR
        + PLACEHOLDER_SEARCH_ENABLED;
    final String value = configuration.getValue(profileMatch);
    if (TRUE.equalsIgnoreCase(value)) {
      log.debug("Placeholders enabled for profile: {} ", profileID);
      return true;
      
    }    
    return false;
    
  }

  /**
   * Get placeholderId for a specific profile.
   * 
   * @param selectedProfileID ProfileName
   * @return Placeholder Id
   */
  public String getProfilePlaceholderID(String selectedProfileID) {
    log.info("SelectedProfileID in ProfileConfEnabled: " + selectedProfileID);
    final String profileMatch = SIG_OBJECT + SEPERATOR + selectedProfileID + SEPERATOR + PLACEHOLDER_ID;
    return configuration.getValue(profileMatch);

  }
}
