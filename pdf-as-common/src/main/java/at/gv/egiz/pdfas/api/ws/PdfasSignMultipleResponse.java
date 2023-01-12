package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlType(name="signMultipleResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfasSignMultipleResponse implements Serializable {
	private static final long serialVersionUID = 2544165926674778203L;

	@XmlElement(required = true, nillable = false, name="requestID")
	String requestID;
	
  @XmlElement(required = false, nillable = true, name="transactionId")
	String transactionId;
  
	@XmlElement(required = false, name="error")
	String error;
	
	@XmlElement(required = false, name="redirectUrl")
	String redirectUrl;
	
	@XmlElement(required = true, nillable = false, name="documents")
	List<PdfasSignedDocument> output;
}
