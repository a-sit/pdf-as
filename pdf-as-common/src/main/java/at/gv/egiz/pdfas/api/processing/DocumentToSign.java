package at.gv.egiz.pdfas.api.processing;

import java.io.Serializable;

import lombok.Data;

@Data
public class DocumentToSign implements Serializable {

  private static final long serialVersionUID = 551977730667465367L;

  byte[] inputData;

  String fileName;
  
  String position;

  String qrCodeContent;

  String profile;

}
