package at.gv.egiz.pdfas.web;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;

@SpringBootApplication
public class PdfAsWeb extends SpringBootServletInitializer {

  // registered in resources/META-INF/spring.factories
  public static class DefaultProperties implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(
        @NotNull ConfigurableEnvironment environment, @NotNull SpringApplication application)
    {
      /*
        == DEFAULT PROPERTIES ==
          these are properties with the lowest precedence
          so anything else (pdf-as-web.properties, system params, etc) can override them
      */
      val defaultProperties = new HashMap<String, Object>();
      // set the default application name
      defaultProperties.put("spring.application.name", "PDF-AS Web");
      // disable spring boot admin client by default
      defaultProperties.put("spring.boot.admin.client.enabled", "false");
      // compatibility layer: map the old logback.configurationFile to the new logging.config
      val legacyLogProperty = System.getProperty("logback.configurationFile");
      if (legacyLogProperty != null) {
        defaultProperties.put("logging.config", legacyLogProperty);
      }

      /*
        == END DEFAULT PROPERTIES ==
      */


      environment.getPropertySources().addLast(new MapPropertySource("pdf-as-web-defaults", defaultProperties));
    }
  }

  @Override
  protected @NotNull SpringApplicationBuilder configure(@NotNull SpringApplicationBuilder application) {
    return application.sources(PdfAsWeb.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(PdfAsWeb.class, args);
  }
}
