package at.gv.egiz.status.content;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import at.gv.egiz.status.TestResult;
import at.gv.egiz.status.TestStatus;
import at.gv.egiz.status.impl.TestStatusString;
import org.apache.commons.text.StringEscapeUtils;

public class HtmlGenerator implements ContentGenerator {

	@Override
	public void generate(HttpServletRequest request,
			HttpServletResponse response, Map<String, TestResult> results, boolean details) throws IOException {
		
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
		
		response.setContentType("text/html");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><head></head><body>");
		sb.append("<table border='1'><thead><tr><th>Name</th><th>Status</th>");
		
		if(details) {
			sb.append("<th>Details</th>");
		} 

		sb.append("</tr></thead><tbody>");

        for (Entry<String, TestResult> entry : results.entrySet()) {
          TestResult result = entry.getValue();
          String testName = entry.getKey();

          sb.append("<tr><td>");
          sb.append(StringEscapeUtils.escapeHtml4(testName));
          sb.append("</td><td>");
          sb.append(StringEscapeUtils.escapeHtml4(TestStatusString.getString(result.getStatus())));


          if (details) {
            sb.append("</td><td>");

            StringBuilder detail = new StringBuilder();

            for (String detailString : result.getDetails()) {
              detail.append(StringEscapeUtils.escapeHtml4(detailString));
              detail.append("</br>");
            }

            sb.append(detail);
          }

          sb.append("</td></tr>");
        }
		
		sb.append("</tbody></table>");
		
		sb.append("</body></html>");
		
		response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
		response.getOutputStream().close();
	}

}
