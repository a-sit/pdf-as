package at.gv.egiz.sl20.exceptions;

public class SLCommandoParserException extends SL20Exception {
	public SLCommandoParserException() {
		super("sl20.02");
		
	}
	
	public SLCommandoParserException(Throwable e) {
		super("sl20.02", e);
		
	}
}
