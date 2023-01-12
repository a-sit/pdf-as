package at.gv.egiz.pdfas.api.processing;

import java.io.Serializable;

import at.gv.egiz.pdfas.api.ws.PDFASVerificationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignedDocument implements Serializable {

	private static final long serialVersionUID = 5409915106152807937L;

	  Long signingTimestamp;
	
	  byte[] outputData;

	  String fileName;
	  
	  String signerCertificate;
	 
	  PDFASVerificationResponse verificationResponse;
	
}
