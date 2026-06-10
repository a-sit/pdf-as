package at.gv.egiz.pdfas.web.test;

import at.gv.egiz.pdfas.web.stats.impl.StatisticMicrometerBackend;
import com.jayway.jsonpath.JsonPath;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestUtils {
  private static double getOperationCount(MockMvc mvc, String... tags) throws Exception {
    val builder = MockMvcRequestBuilders.get("/actuator/metrics/pdfas_requests");
    Arrays.stream(tags).forEach(tag -> builder.param("tag", tag));
    val result =
        mvc.perform(builder).andReturn().getResponse();
    if (result.getStatus() == 404) return 0.0;
    Assertions.assertEquals(200, result.getStatus());
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
    @BeforeEach
    public void rebindStatisticsBackend() {
      StatisticMicrometerBackend.SpringContextProxy.meterRegistry = meterRegistry;
    }
    protected AutoCloseable OperationCountWatcher(String... tags) throws Exception {
      val initialCount = TestUtils.getOperationCount(mvc, tags);
      return () -> Assertions.assertEquals(initialCount+1.0, TestUtils.getOperationCount(mvc, tags), 0.0001);
    }
  }

  public static String azstring(int length) {
    return
        new Random().ints(97,123).limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
  }

  // how is this not in lang.commons? anyway it's boyer-moore
  private static boolean arrayContainsArray(byte[] haystack, byte[] needle) {
    val n = haystack.length;
    val m = needle.length;
    val badCharTable = new int[256];
    Arrays.fill(badCharTable, -1);
    for (int i=0; i<needle.length; ++i) { badCharTable[needle[i] & 0xff] = i; }

    int skip;
    for (int i = 0; i <= n-m; i += skip) {
      skip = 0;
      for (int j = m-1; j >= 0; j--) {
        if (needle[j] != haystack[i+j]) {
          int badCharShift = j-badCharTable[haystack[i+j] & 0xff];
          skip = Math.max(1, badCharShift);
          break;
        }
      }
      if (skip == 0) {
        return true;
      }
    }
    return false;
  }

  @Test
  @SneakyThrows
  public void arrayContainsArrayTest() {
    Assertions.assertTrue(arrayContainsArray(new byte[] { 1, 3, 5, 4, 2 }, new byte[] { 5, 4 }));
    Assertions.assertTrue(arrayContainsArray(new byte[] { 9, -5, 13, 42, 5 }, new byte[] { 42, 5 }));
    Assertions.assertTrue(arrayContainsArray(new byte[] { 13, 86, 63, 51, -5 }, new byte[] { 13, 86 }));
    Assertions.assertTrue(arrayContainsArray(new byte[] { 9, -5, 21, 42, 3 }, new byte[] { 9, -5, 21, 42, 3 }));
    Assertions.assertFalse(arrayContainsArray(new byte[] { 1, 3, 5, 4, 2 }, new byte[] { 2, 1 }));
    Assertions.assertFalse(arrayContainsArray(new byte[] { 9, 8, 4, 6, 2 }, new byte[] { 5 }));
    Assertions.assertFalse(arrayContainsArray(new byte[] { 42, 41, 40 }, new byte[] { 40, 41 }));
    Assertions.assertFalse(arrayContainsArray(new byte[] { 40, 41 }, new byte[] { 41, 40 }));
  }

  public sealed interface Multipart permits Multipart.Value, Multipart.File {
    String getKey();
    @lombok.Value
    class Value implements Multipart {
      String key;
      String value;
    }

    @lombok.Value
    class File implements Multipart {
      String key;
      String filename;
      String contentType;
      byte[] contents;
    }

    public static class Builder {
      private Builder() {}
      private final ArrayList<Multipart> parts = new ArrayList<>();

      public Builder Value(String key, String value) {
        parts.add(new Value(key, value));
        return this;
      }

      public Builder File(String key, String filename, String contentType, byte[] contents) {
        parts.add(new File(key, filename, contentType, contents));
        return this;
      }

      public Multipart.Body build() {
        return buildMultipartBody(parts.toArray(new Multipart[0]));
      }
    }

    static Builder builder() { return new Builder(); }

    @lombok.Value
    class Body {
      String contentType;
      Iterable<byte[]> body;
    }
  }

  private static String findMultipartBoundary(Multipart[] parts) {
    while (true) {
      val boundaryCandidate = "----"+azstring(32);
      val candidateBytes = boundaryCandidate.getBytes(StandardCharsets.UTF_8);
      if (Arrays.stream(parts).allMatch(part -> {
        if (part.getKey().contains(boundaryCandidate)) return false;
        if (part instanceof Multipart.Value v) {
          if (v.getValue().contains(boundaryCandidate)) return false;
        } else if (part instanceof Multipart.File f) {
          if (f.filename.contains(boundaryCandidate)) return false;
          if (f.contentType.contains(boundaryCandidate)) return false;
          if (arrayContainsArray(f.contents, candidateBytes)) return false;
        }
        return true;
      })) {
        return boundaryCandidate;
      }
    }
  }

  public static Multipart.Body buildMultipartBody(Multipart... parts) {
    val boundary = findMultipartBoundary(parts);
    val preName = ("--"+boundary+"\r\nContent-Disposition: form-data; name=\"").getBytes(StandardCharsets.UTF_8);
    val postNameKV = "\"\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    val postNamePreFilename = "\"; filename=\"".getBytes(StandardCharsets.UTF_8);
    val postFilenamePreContentType = "\"\r\nContent-Type: ".getBytes(StandardCharsets.UTF_8);
    val postContentTypePreFile = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    val terminator = "\r\n".getBytes(StandardCharsets.UTF_8);
    val finalTerminator = ("--"+boundary+"--\r\n").getBytes(StandardCharsets.UTF_8);
    val result = new LinkedList<byte[]>();
    for (val part : parts) {
      result.add(preName);
      result.add(part.getKey().getBytes(StandardCharsets.UTF_8));
      if (part instanceof Multipart.Value v) {
        result.add(postNameKV);
        result.add(v.getValue().getBytes(StandardCharsets.UTF_8));
        result.add(terminator);
      } else if (part instanceof Multipart.File f) {
        result.add(postNamePreFilename);
        result.add(f.getFilename().getBytes(StandardCharsets.UTF_8));
        result.add(postFilenamePreContentType);
        result.add(f.getContentType().getBytes(StandardCharsets.UTF_8));
        result.add(postContentTypePreFile);
        result.add(f.getContents());
        result.add(terminator);
      } else { throw new IllegalStateException(); }
    }
    result.add(finalTerminator);
    return new Multipart.Body("multipart/form-data; boundary="+boundary, result);
  }
}
