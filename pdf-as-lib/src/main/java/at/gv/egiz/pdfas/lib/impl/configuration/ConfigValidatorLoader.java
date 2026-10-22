package at.gv.egiz.pdfas.lib.impl.configuration;

import java.util.ServiceLoader;
import lombok.NonNull;
import at.gv.egiz.pdfas.lib.configuration.ConfigurationValidator;

public class ConfigValidatorLoader {
	private static final ServiceLoader<@NonNull ConfigurationValidator> configurationValidator = ServiceLoader.load(ConfigurationValidator.class);

	public static Iterable<@NonNull ConfigurationValidator> getAvailableValidators() { return configurationValidator; }
}
