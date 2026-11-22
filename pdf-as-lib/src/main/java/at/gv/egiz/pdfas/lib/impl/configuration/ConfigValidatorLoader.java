package at.gv.egiz.pdfas.lib.impl.configuration;

import java.util.ServiceLoader;
import lombok.NonNull;
import at.gv.egiz.pdfas.lib.configuration.ConfigurationValidator;
import org.apache.commons.collections4.IterableUtils;

public class ConfigValidatorLoader {
	private static final Iterable<@NonNull ConfigurationValidator> configurationValidator =
			IterableUtils.toList(ServiceLoader.load(ConfigurationValidator.class));

	public static Iterable<@NonNull ConfigurationValidator> getAvailableValidators() { return configurationValidator; }
}
