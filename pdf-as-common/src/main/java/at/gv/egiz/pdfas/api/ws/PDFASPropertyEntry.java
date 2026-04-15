package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name="PropertyEntry") 
public class PDFASPropertyEntry implements Serializable {
	private static final long serialVersionUID = -312145729002273058L;
	
  String key;
	String value;
	
	@XmlElement(required = true, nillable = false, name="key")
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	@XmlElement(required = true, nillable = false, name="value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
