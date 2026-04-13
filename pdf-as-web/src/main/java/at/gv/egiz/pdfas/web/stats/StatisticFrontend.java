package at.gv.egiz.pdfas.web.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.web.config.WebConfiguration;

public class StatisticFrontend implements StatisticBackend {

	private static StatisticFrontend _instance;

	private static ServiceLoader<StatisticBackend> backendLoader = ServiceLoader
			.load(StatisticBackend.class);

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticFrontend.class);

	private List<StatisticBackend> statisticBackends = new ArrayList<StatisticBackend>();

	private StatisticFrontend() {
		List<String> enabledBackends = WebConfiguration.getStatisticBackends();

		if (enabledBackends == null) {
			logger.info("No statistic backends configured, using all available.");
		} else {
			logger.info("Allowing the following statistic backends:");
			enabledBackends.forEach(it -> logger.info(" - {}", it));
		}

		for (StatisticBackend statisticBackend : backendLoader) {
			if (enabledBackends == null
					|| enabledBackends.contains(statisticBackend.getName())) {
				logger.info("adding Statistic Logger {} [{}]", statisticBackend
						.getName(), statisticBackend.getClass().getName());

				statisticBackends.add(statisticBackend);
			} else {
				logger.info("skipping Statistic Logger {} [{}]",
						statisticBackend.getName(), statisticBackend.getClass()
								.getName());
			}
		}

		if (enabledBackends != null) {
			for (String enabledBackend : enabledBackends) {
				if (statisticBackends.stream().noneMatch(it -> it.getName().equals(enabledBackend))) {
					logger.warn(
							"Failed to load statistic backend {}. Not in classpath?",
							enabledBackend);
				}
			}
		}
	}

	public static StatisticFrontend getInstance() {
		if (_instance == null) {
			_instance = new StatisticFrontend();
		}
		return _instance;
	}

	@Override
	public String getName() {
		return StatisticFrontend.class.getSimpleName();
	}

	@Override
	public void storeEvent(StatisticEvent statisticEvent) {

		if (statisticEvent == null) {
			logger.warn("Tried to log null as statisticEvent!");
			return;
		}

		if (statisticEvent.isLogged()) {
			logger.warn("Tried to relog statisticEvent!");
			return;
		}

		statisticBackends.forEach(statisticBackend -> statisticBackend.storeEvent(statisticEvent));
	}

}
