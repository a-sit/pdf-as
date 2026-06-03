package at.gv.egiz.pdfas.web.json_api;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** To match how SOAP serializes enum values */
@Configuration
public class JacksonConfig {
  @Bean
  public JsonMapperBuilderCustomizer enumsShouldUseToStringToMatchXML() {
    return b -> b.enable(
        EnumFeature.WRITE_ENUMS_USING_TO_STRING,
        EnumFeature.READ_ENUMS_USING_TO_STRING
    );
  }

  @Bean
  public JsonMapperBuilderCustomizer useJaxbJsonNames() {
    return b -> b.addModule(
        new JakartaXmlBindAnnotationModule()
    );
  }
}
