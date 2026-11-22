package at.gv.egiz.pdfas.api.ws;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name="VerifyResponse")
public class PDFASVerifyResponse {

	List<PDFASVerifyResult> verifyResults;

	@XmlElement(required = true, nillable = false, name="verifyResults")
	public List<PDFASVerifyResult> getVerifyResults() {
		return verifyResults;
	}

	public void setVerifyResults(List<PDFASVerifyResult> verifyResults) {
		this.verifyResults = verifyResults;
	}
}
