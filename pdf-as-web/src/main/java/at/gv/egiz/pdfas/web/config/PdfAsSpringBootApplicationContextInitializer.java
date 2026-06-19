package at.gv.egiz.pdfas.web.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfAsSpringBootApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final String SYSTEMD_PROP_NAME = "pdf-as-web.conf";
  private static final String FILE_PREFIX = "file:";

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    final String rawConfigPath = System.getProperty(SYSTEMD_PROP_NAME);

    if (StringUtils.isBlank(rawConfigPath)) {
      log.info("No SystemD property '{}' found. No external configuration loaded.", SYSTEMD_PROP_NAME);
      return;
    }

    log.debug("Found configuration source from SystemD property '{}'.", SYSTEMD_PROP_NAME);

    final String configPath = stripFilePrefix(rawConfigPath);
    injectConfiguration(Path.of(configPath), applicationContext);
  }

  private static String stripFilePrefix(String configPath) {
    return configPath.startsWith(FILE_PREFIX)
        ? configPath.substring(FILE_PREFIX.length())
        : configPath;
  }

  private void injectConfiguration(
      Path configPath,
      ConfigurableApplicationContext applicationContext) {

    if (!Files.isRegularFile(configPath)) {
      log.error("Configuration from SystemD property '{}' does not exist or is not a file: {}",
          SYSTEMD_PROP_NAME, configPath);
      return;
    }

    try (InputStream inputStream = Files.newInputStream(configPath)) {
      final Properties properties = new Properties();
      properties.load(inputStream);

      applicationContext
          .getEnvironment()
          .getPropertySources()
          .addFirst(new PropertiesPropertySource(SYSTEMD_PROP_NAME, properties));

      log.info("Loaded configuration source from SystemD property '{}': {}",
          SYSTEMD_PROP_NAME, configPath);

    } catch (IOException e) {
      log.error("Configuration from SystemD property '{}' at location '{}' cannot be loaded.",
          SYSTEMD_PROP_NAME, configPath, e);
    }
  }
}