package at.gv.egiz.pdfas.api.ws;

import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "VerificationLevel")
public enum VerificationLevel {

	@XmlEnumValue("intOnly") INTEGRITY_ONLY("intOnly"), 
	@XmlEnumValue("full") FULL_CERT_PATH("full");

	private final String name;

	private VerificationLevel(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : name.equals(otherName);
	}

	public String toString() {
		return name;
	}
}
