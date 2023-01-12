package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlType(name="signedMultipleFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfasSignedDocument implements Serializable {

	private static final long serialVersionUID = -2621030544869357960L;

	@XmlElement(required = true, nillable = false, name="outputData")
	byte[] outputData;
	
	@XmlElement(required = false, nillable = true, name="fileName")
	String fileName;
		
	@XmlElement(required = false, nillable = false, name="verificationResponse")
	PDFASVerificationResponse verificationResponse;
	
}
