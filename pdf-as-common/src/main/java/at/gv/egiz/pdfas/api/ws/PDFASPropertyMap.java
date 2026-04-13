package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name="PropertyMap")
public class PDFASPropertyMap implements Serializable {	
  private static final long serialVersionUID = -8099703140108251423L;
  
  List<PDFASPropertyEntry> propertyEntries;
	
	@XmlElement(required = true, nillable = false, name="propertyEntries")
	public List<PDFASPropertyEntry> getPropertyEntries() {
		return propertyEntries;
	}

	public void setPropertyEntries(List<PDFASPropertyEntry> propertyEntries) {
		this.propertyEntries = propertyEntries;
	}
	
	@XmlTransient
	public Map<String, String> getMap() {
		if(propertyEntries != null) {
			Map<String, String> map = new HashMap<String, String>();
			Iterator<PDFASPropertyEntry> propsIt = propertyEntries.iterator();
			while(propsIt.hasNext()) {
				PDFASPropertyEntry entry = propsIt.next();
				map.put(entry.getKey(), entry.value);
			}
			return map;
		}
		return null;
	}
	
	public void setMap(Map<String, String> map) {
		
		if(map != null) {
			propertyEntries = new ArrayList<PDFASPropertyEntry>();
			Iterator<String> keyIt = map.keySet().iterator();
			while(keyIt.hasNext()) {
				String key = keyIt.next();
				PDFASPropertyEntry entry = new PDFASPropertyEntry();
				entry.setKey(key);
				entry.setValue(map.get(key));
				propertyEntries.add(entry);
			}
		}
	}
}
