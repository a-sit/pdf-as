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
package at.gv.egiz.pdfas.common.utils;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class DNUtils {
  private static final Map<String, String> RFC2255_NAME_MAPPER = Map.of("title", "T");

  public static Map<String, String> dnToMap(String dn) throws InvalidNameException {
    final Map<String, String> map = new HashMap<>();

    final LdapName ldapName = new LdapName(dn);

    for (Rdn rdn : ldapName.getRdns()) {
      map.put(rdn.getType(), rdn.getValue().toString());

      // map specific RFC2255 names to support old PDF-AS signature-profile definitions
      if (RFC2255_NAME_MAPPER.containsKey(rdn.getType())) {
        map.put(RFC2255_NAME_MAPPER.get(rdn.getType()), rdn.getValue().toString());

      }

    }

    map.put("DN", dn);

    return map;
  }
}
