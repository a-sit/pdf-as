package at.gv.egiz.pdfas.api.processing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.gv.egiz.pdfas.api.ws.VerificationLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PdfasSignRequest implements Serializable, Iterator<DocumentToSign> {

	private static final long serialVersionUID = -7245405996920651806L;
	
	@Setter
  String requestID;
  
	@Setter
	CoreSignParams coreParams;
  
	@Setter
  VerificationLevel verificationLevel;
  
  List<DocumentToSign> input;

  /**
   * Add single PDF for signing.
   * 
   * @param pdf PDF to sign
   */
  public void addDocumentToSign(DocumentToSign pdf) {
    if (input == null) {
      input = new ArrayList<>();
      
    }    
    input.add(pdf);
    
  }
  
  
  /**
   * <code>true</code> if there is one or more {@link DocumentToSign}, otherwise <code>false</code>.
   */
  @Override
  public boolean hasNext() {
    return input != null && !input.isEmpty();
    
  }

  /**
   * Get next {@link DocumentToSign}.
   */
  @Override
  public synchronized DocumentToSign next() {
    return input.remove(0);
    
  }
  
  
}
