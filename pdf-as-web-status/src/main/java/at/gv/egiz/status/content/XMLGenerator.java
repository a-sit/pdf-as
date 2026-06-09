package at.gv.egiz.status.content;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;

import at.gv.egiz.status.TestResult;
import at.gv.egiz.status.TestStatus;
import at.gv.egiz.status.impl.TestStatusString;

public class XMLGenerator implements ContentGenerator {

	@Override
	public void generate(HttpServletRequest request,
			HttpServletResponse response, Map<String, TestResult> results,
			boolean details) throws IOException {
		boolean allOk = true;

        for (TestResult result : results.values()) {
          if (!result.getStatus().equals(TestStatus.OK)) {
            allOk = false;
            break;
          }
        }
		
		if(!allOk) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		response.setCharacterEncoding(StandardCharsets.UTF_8);
		response.setContentType("application/xml");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

		sb.append("<tests>");

        for (Entry<String, TestResult> entry : results.entrySet()) {
          TestResult result = entry.getValue();
          String testName = entry.getKey();

          sb.append("<test><name>");
          sb.append(StringEscapeUtils.escapeXml10(testName));
          sb.append("</name><status>");
          sb.append(StringEscapeUtils.escapeXml10(TestStatusString.getString(result.getStatus())));
          sb.append("</status>");

          if (details) {
            sb.append("<detail>");

            StringBuilder detail = new StringBuilder();

            for (String detailString : result.getDetails()) {
              detail.append(StringEscapeUtils.escapeXml10(detailString));
              detail.append(" ");
            }

            sb.append(detail.toString());
            sb.append("</detail>");
          }

          sb.append("</test>");
        }
		
		sb.append("</tests>");
		
		response.getOutputStream().write(sb.toString().getBytes(StandardCharsets.UTF_8));
		response.getOutputStream().close();
	}

}
