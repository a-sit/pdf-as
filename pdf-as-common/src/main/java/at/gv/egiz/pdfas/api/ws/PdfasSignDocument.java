package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlType(name="signMultipleFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfasSignDocument implements Serializable {

	private static final long serialVersionUID = -2422995343468207094L;
	
	@XmlElement(required = true, nillable = false, name="inputData")
	byte[] inputData;
	
	@XmlElement(required = true, nillable = false, name="fileName")
	String fileName;
	
	@XmlElement(required = false, nillable = true, name="position")
	String position;	
		
	@XmlElement(required = false, nillable = true, name="qrCodeContent")
	String qrCodeContent;
	
	@XmlElement(required = false, nillable = true, name="profile")
	String profile;
	
}

