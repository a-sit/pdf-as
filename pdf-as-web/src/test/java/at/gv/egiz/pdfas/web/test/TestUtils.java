package at.gv.egiz.pdfas.web.test;

import at.gv.egiz.pdfas.web.stats.impl.StatisticMicrometerBackend;
import com.jayway.jsonpath.JsonPath;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

public class TestUtils {
  private static double getOperationCount(MockMvc mvc, String... tags) throws Exception {
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

  @SpringBootTest
  @AutoConfigureMockMvc
  public static abstract class CanWatchOperationCount {
    @Autowired MockMvc mvc;
    @Autowired private MeterRegistry meterRegistry;
    @Before
    public void rebindStatisticsBackend() {
      StatisticMicrometerBackend.SpringContextProxy.meterRegistry = meterRegistry;
    }
    protected AutoCloseable OperationCountWatcher(String... tags) throws Exception {
      val initialCount = TestUtils.getOperationCount(mvc, tags);
      return () -> Assert.assertEquals(initialCount+1.0, TestUtils.getOperationCount(mvc, tags), 0.0001);
    }
  }
}
