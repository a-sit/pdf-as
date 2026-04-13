package at.gv.egiz.pdfas.web.json_api;

import at.gv.egiz.pdfas.api.ws.PDFASVerifyRequest;
import at.gv.egiz.pdfas.api.ws.PDFASVerifyResponse;
import at.gv.egiz.pdfas.web.ws.PDFASVerificationImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/verify")
@AllArgsConstructor
public class VerifyController {
  private final PDFASVerificationImpl verifyImpl;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public PDFASVerifyResponse verify(@RequestBody PDFASVerifyRequest request) {
    return verifyImpl.verifyPDFDokument(request);
  }
}
