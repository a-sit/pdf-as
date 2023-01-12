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
package at.gv.egiz.pdfas.web.store;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;
import at.gv.egiz.pdfas.api.processing.PdfasSignResponse;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;

public class InMemoryRequestStore implements IRequestStore {

  // expires after 10 minutes
  private static final long DEFAULT_EXPIRATION = 10 * 60 * 1000;
  
  private Map<String, PdfasSignRequest> reqStore = new PassiveExpiringMap<>(DEFAULT_EXPIRATION);
  private Map<String, PdfasSignResponse> respStore = new PassiveExpiringMap<>(DEFAULT_EXPIRATION);
  private Map<String, StatisticEvent> statEvents = new PassiveExpiringMap<>(DEFAULT_EXPIRATION);
  
	public InMemoryRequestStore() {
		  
	}
	
	public String createNewStoreEntry(PdfasSignRequest request, StatisticEvent event) {
		UUID id = UUID.randomUUID();
		String sid = id.toString();
		this.reqStore.put(sid, request);
		this.statEvents.put(sid, event);
		return sid;
		 	
	}

	public StatisticEvent fetchStatisticEntry(String id) {
		if(statEvents.containsKey(id)) {
			StatisticEvent event = statEvents.get(id);
			statEvents.remove(id);
			return event;
			
		}
		
		return null;
	}
	
	public PdfasSignRequest fetchStoreEntry(String id) {
		if(reqStore.containsKey(id)) {
			PdfasSignRequest request = reqStore.get(id);
			reqStore.remove(id);
			return request;
			
		}
		
		return null;
	}

  @Override
  public String createNewResponseEntry(PdfasSignResponse response) {
    String sid = UUID.randomUUID().toString();
    this.respStore.put(sid, response);
    return sid;
    
  }

  @Override
  public PdfasSignResponse fetchStoreResponse(String id) {
    if (respStore.containsKey(id)) {
      PdfasSignResponse response = respStore.get(id);
      respStore.remove(id);
      return response;
      
    }
    
    return null;
  }

}
