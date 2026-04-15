package at.gv.egiz.pdfas.web.web_xml_bridges;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeFileBridge {
  @GetMapping("/")
  public String welcomeFile() {
    return "forward:/index.jsp";
  }
}
