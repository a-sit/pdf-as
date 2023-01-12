package at.gv.egiz.pdfas.api.processing;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
public class PdfasSignResponse implements Serializable {

	private static final long serialVersionUID = -3235466827933651452L;

	String requestId;
	
	String transactionId;
	
	@Singular
	List<SignedDocument> signedPdfs;
	
}
