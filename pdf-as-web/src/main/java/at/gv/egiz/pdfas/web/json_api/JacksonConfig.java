package at.gv.egiz.pdfas.web.json_api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** To match how SOAP serializes enum values */
@Configuration
public class JacksonConfig {
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer enumsShouldUseToStringToMatchXML() {
    return b -> b.featuresToEnable(
        SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
        DeserializationFeature.READ_ENUMS_USING_TO_STRING
    );
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer useJaxbJsonNames() {
    return b -> b.modulesToInstall(
        new JakartaXmlBindAnnotationModule()
    );
  }
}
