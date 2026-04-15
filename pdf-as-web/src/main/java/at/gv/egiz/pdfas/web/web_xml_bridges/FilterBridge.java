package at.gv.egiz.pdfas.web.web_xml_bridges;

import at.gv.egiz.pdfas.web.filter.ExceptionCatchFilter;
import at.gv.egiz.pdfas.web.filter.UserAgentFilter;
import com.thetransactioncompany.cors.CORSFilter;
import jakarta.servlet.Filter;
import lombok.val;
import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Takes the old web.xml filter mappings and exposes them to Spring Boot */
@Configuration
public class FilterBridge {
  @Bean
  public FilterRegistrationBean<Filter> setCharacterEncodingFilter() {
    val reg = new FilterRegistrationBean<Filter>(new SetCharacterEncodingFilter());
    reg.addUrlPatterns("/*");
    reg.addInitParameter("encoding", "UTF-8");
    reg.setOrder(1);
    return reg;
  }

  @Bean
  public FilterRegistrationBean<Filter> exceptionCatchFilter() {
    val reg = new FilterRegistrationBean<Filter>(new ExceptionCatchFilter());
    reg.addUrlPatterns("/*");
    reg.addInitParameter("statelessServlets", "/placeholder,/visblock");
    reg.setOrder(2);
    return reg;
  }

  @Bean
  public FilterRegistrationBean<Filter> userAgentFilter() {
    val reg = new FilterRegistrationBean<Filter>(new UserAgentFilter());
    reg.addUrlPatterns("/*");
    reg.setOrder(3);
    return reg;
  }

  @Bean
  public FilterRegistrationBean<Filter> cors() {
    val reg = new FilterRegistrationBean<Filter>(new CORSFilter());
    reg.addUrlPatterns("/*");
    reg.addInitParameter("cors.allowOrigin", "*");
    reg.setOrder(4);
    return reg;
  }
}
