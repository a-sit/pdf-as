package at.gv.egiz.pdfas.api.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface PDFASVerification {
	@WebMethod(operationName = "verify")
	@WebResult(name="verifyResponse")
	public PDFASVerifyResponse verifyPDFDokument(@WebParam(name = "verifyRequest") PDFASVerifyRequest request);
	
}
