package at.gv.egiz.pdfas.web.json_api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = "at.gv.egiz.pdfas.web.json_api")
public class ExceptionFormatter {
  @ExceptionHandler(jakarta.xml.ws.WebServiceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> mapError(jakarta.xml.ws.WebServiceException e) {
    return Map.of("error", e.getMessage());
  }
}
