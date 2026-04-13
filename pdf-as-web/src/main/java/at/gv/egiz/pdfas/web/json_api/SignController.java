package at.gv.egiz.pdfas.web.json_api;

import at.gv.egiz.pdfas.api.ws.*;
import at.gv.egiz.pdfas.web.ws.PDFASSigningImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/sign")
@AllArgsConstructor
public class SignController {
  private final PDFASSigningImpl signingImpl;

  @PostMapping(value = "/single", consumes = "application/json", produces = "application/json")
  public PDFASSignResponse signSingle(@RequestBody PDFASSignRequest request) {
    return signingImpl.signPDFDokument(request);
  }

  @PostMapping(value = "/bulk", consumes = "application/json", produces = "application/json")
  public PDFASBulkSignResponse signBulk(@RequestBody PDFASBulkSignRequest request) {
    return signingImpl.signPDFDokument(request);
  }

  @PostMapping(value = "/multiple", consumes = "application/json", produces = "application/json")
  public PdfasSignMultipleResponse signMultiple(@RequestBody PdfasSignMultipleRequest request) {
    return signingImpl.signPDFDokument(request);
  }

  @PostMapping(value = "/multiple/get-result", consumes = "application/json", produces = "application/json")
  public PdfasSignMultipleResponse getMultiple(@RequestBody PdfasGetMultipleRequest request) {
    return signingImpl.getSignedDokument(request);
  }
}
