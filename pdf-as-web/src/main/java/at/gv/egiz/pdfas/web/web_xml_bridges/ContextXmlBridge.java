package at.gv.egiz.pdfas.web.web_xml_bridges;

import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** translates the tomcat context.xml file for spring embedded tomcat */
@Configuration
@ConditionalOnClass(name = "org.springframework.boot.tomcat.TomcatContextCustomizer")
public class ContextXmlBridge {
  @Bean
  public org.springframework.boot.tomcat.TomcatContextCustomizer sameSiteNone() {
    return ctx -> {
      val processor = new org.apache.tomcat.util.http.Rfc6265CookieProcessor();
      processor.setSameSiteCookies("none");
      ctx.setCookieProcessor(processor);
    };
  }
}
