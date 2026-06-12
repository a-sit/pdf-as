package at.gv.egiz.pdfas.web.stats;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import jakarta.persistence.Transient;


/**
 * Timestamp; [Der Zeitpunkt des Signaturvorgangs]
Operation; [Die Operation des Signaturvorgangs (SIGN | VERIFY) ]
Signaturemode; [Der Siganturemode (BINARY | TEXTUAL) default BINARY]
Device; [Das Signaturgeraet (bku (lokale BKU) | moa (configured moa instance) | moc (online bku MOCCA) | mobile (Handy Signatur))]
ProfileId; [Das verwendete Signaturprofil ein Beispiel waere: SIGNATURBLOCK_DE]
Filesize; [Die Dateigroesse des PDF Dokuments]
User Agent; [Der User-Agent (wenn verfuegbar)]
Status; [Der Status der Operation: (OK | ERROR)]
Exception Class; [Exception Klasse falls ein Fehler vorliegt]
ErrorCode; [Exception Code falls ein Fehler vorliegt]
External Errorcode; [Exception Code von externer Componente falls vorhanden]
Duration [Verbrauchte Zeit fuer diese Operation in Millisekunden, wenn feststellbar] 
 * @author Andreas Fitzek
 *
 */
public class StatisticEvent {

	public enum Operation {
		SIGN("sign"),
		SIGNBULK("signBulk"),
		VERIFY("verify");

		@Getter
		private final String name;
		
		Operation(String name) {
			this.name = name;
		}
	}
	
	public enum Source {
		WEB("web"),
		SOAP("soap"),
		JSON("json");

		@Getter
		private final String name;
		
		Source(String name) {
			this.name = name;
		}
	}
	
	public enum Status {
		OK("ok"), 
		ERROR("error");

		@Getter
		private final String name;
		
		Status(String name) {
			this.name = name;
		}
	}

	@Getter @Setter
	private long timestamp;
	@Getter @Setter
	private Operation operation;
	@Getter @Setter
	private String device;
	@Getter @Setter
	private String profileId;
	@Getter @Setter
	private long filesize;
	@Getter @Setter
	private String userAgent;
	@Getter @Setter
	private Status status;
	@Getter @Setter
	private Throwable exception;
	@Getter @Setter
	private long errorCode;
	@Getter(onMethod_ = @Column(name = "startTime")) @Setter
	private long start;
	@Getter(onMethod_ = @Column(name = "endTime")) @Setter
	private long end;
	@Getter @Setter
	private Source source;
	private boolean logged = false;
	
	public StatisticEvent() {}
	
	public void setStartNow() {
		this.start = (new Date()).getTime();
	}
	public void setEndNow() {
		this.end = (new Date()).getTime();
	}
	public void setTimestampNow() {
		this.timestamp = (new Date()).getTime();
	}
	@Transient
	public long getDuration() {
		return this.end - this.start;
	}
	@Transient
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
}
