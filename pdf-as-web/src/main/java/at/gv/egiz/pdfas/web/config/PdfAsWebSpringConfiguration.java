package at.gv.egiz.pdfas.web.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PdfAsWebSpringConfiguration {
  /** path to pdf-as-web.properties as loaded from spring sources */
  @Value("${pdf-as-web.conf}")
  @Getter
  @NonNull String pdfAsWebConfPath;
}
