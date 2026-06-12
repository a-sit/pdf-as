package at.gv.egiz.pdfas.web.store.db;

import java.util.Date;

import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import jakarta.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;

@Entity
@Table(name = "requests")
public class Request {

	private String uuid;	
	private Date created;
	private PdfasSignRequest signRequest;
	private StatisticEvent statisticEvent;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", unique = true)
	public String getId() {
		return this.uuid;
	}

	public void setId(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name = "created", nullable = false)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	@Column(name = "signRequest", nullable = false, length = 52428800)
	public PdfasSignRequest getSignRequest() {
		return this.signRequest;
	}

	public void setSignRequest(PdfasSignRequest signRequest) {
		this.signRequest = signRequest;
	}

	@Column(name = "statisticEvent", nullable = false, length = 52428800)
	@Embedded
	public StatisticEvent getStatisticEvent() {
		return this.statisticEvent;
	}

	public void setStatisticEvent(StatisticEvent statisticEvent) {
		this.statisticEvent = statisticEvent;
	}

}
