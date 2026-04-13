package at.gv.egiz.pdfas.web.test;

import com.jayway.jsonpath.JsonPath;
import lombok.val;
import org.junit.Assert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

public class TestUtils {
  public static double getOperationCount(MockMvc mvc, String... tags) throws Exception {
    val builder = MockMvcRequestBuilders.get("/actuator/metrics/pdfas_requests");
    Arrays.stream(tags).forEach(tag -> builder.param("tag", tag));
    val result =
        mvc.perform(builder).andReturn().getResponse();
    if (result.getStatus() == 404) return 0.0;
    Assert.assertEquals(200, result.getStatus());
    return JsonPath.<List<Double>>read(
        result.getContentAsString(),
        "$.measurements[?(@.statistic == 'COUNT')].value")
        .get(0);
  }

  public static AutoCloseable OperationCountWatcher(MockMvc mvc, String... tags) throws Exception {
    val initialCount = TestUtils.getOperationCount(mvc, tags);
    return () -> Assert.assertEquals(initialCount+1.0, TestUtils.getOperationCount(mvc, tags), 0.0001);
  }
}
