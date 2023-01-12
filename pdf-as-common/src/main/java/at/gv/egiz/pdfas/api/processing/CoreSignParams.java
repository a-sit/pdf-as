package at.gv.egiz.pdfas.api.processing;

import java.io.Serializable;
import java.util.Map;

import at.gv.egiz.pdfas.api.ws.PDFASSignParameters.Connector;
import lombok.Data;

@Data
public class CoreSignParams implements Serializable {
  
  private static final long serialVersionUID = 947480605651880556L;
  
  String transactionId;
  
  Connector connector;
  
  String invokeUrl;
  
  String invokeTarget;
  
  String invokeErrorUrl;

  String keyIdentifier;
  
  Map<String,String> preprocessor;
  
  Map<String,String> overrides;
  
  Map<String,String> signatureBlockParameters;
 
}
