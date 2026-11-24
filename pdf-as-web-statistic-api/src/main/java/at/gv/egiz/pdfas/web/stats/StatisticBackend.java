package at.gv.egiz.pdfas.web.stats;

import lombok.NonNull;

public interface StatisticBackend {
	public @NonNull String getName();
	public void storeEvent(StatisticEvent statisticEvent);
}
