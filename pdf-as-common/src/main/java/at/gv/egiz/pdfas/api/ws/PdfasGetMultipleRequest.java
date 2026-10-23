package at.gv.egiz.pdfas.api.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlType(name="getMultipleRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfasGetMultipleRequest {

  @XmlElement(required = true, nillable = false, name="token")
  String token;
  
}
