package at.gv.egiz.sl20.exceptions;

public class SLCommandoBuildException extends SL20Exception {
	
	public SLCommandoBuildException() {
		super("sl20.01");
		
	}
	
	public SLCommandoBuildException(Throwable e) {
		super("sl20.01", e);
		
	}
}
