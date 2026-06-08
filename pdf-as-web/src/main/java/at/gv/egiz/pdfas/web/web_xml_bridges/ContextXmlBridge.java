package at.gv.egiz.pdfas.web.web_xml_bridges;

import lombok.val;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.boot.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** translates the tomcat context.xml file */
@Configuration
public class ContextXmlBridge {
  @Bean
  public TomcatContextCustomizer sameSiteNone() {
    return ctx -> {
      val processor = new Rfc6265CookieProcessor();
      processor.setSameSiteCookies("none");
      ctx.setCookieProcessor(processor);
    };
  }
}
