package at.gv.egiz.pdfas.api.ws;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlType(name="signMultipleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfasSignMultipleRequest implements Serializable {
	
	private static final long serialVersionUID = 4338536417988335976L;

	@XmlElement(required = true, nillable = false, name="requestID")
	String requestID;
	
	@XmlElement(required = false, nillable = true, name="transactionId")
	String transactionId;
	
	@XmlElement(required = true, nillable = false, name="connector")
	Connector connector;
	
	@XmlElement(required = false, nillable = true, name="invoke-url")
	String invokeUrl;
	
	@XmlElement(required = false, nillable = true, name="invoke-target")
	String invokeTarget;
	
	@XmlElement(required = false, nillable = true, name="invoke-error-url")
	String invokeErrorUrl;

	@XmlElement(required = false, nillable = true, name="keyIdentifier")
	String keyIdentifier;
	
	@XmlElement(required = false, nillable = true, name="preprocessorArguments")
	PDFASPropertyMap preprocessor;
	
	@XmlElement(required = false, nillable = true, name="configurationOverrides")
	PDFASPropertyMap overrides;
	
	@XmlElement(required = false, nillable = true, name="signatureBlockParameter")
	Map<String,String> signatureBlockParameters;
	
	@XmlElement(required = false, nillable = true, name="verificationLevel")
	VerificationLevel verificationLevel;
	
	@XmlElement(required = true, nillable = false, name="documents")
	List<PdfasSignDocument> input;
	
}
