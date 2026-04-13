package at.gv.egiz.pdfas.web.stats.impl;

import at.gv.egiz.pdfas.web.stats.StatisticBackend;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class StatisticMicrometerBackend implements StatisticBackend {
  /** bridge between ServiceLoader component and Boot's beans */
  @Component
  public static class SpringContextProxy implements ApplicationContextAware {
    private static volatile ApplicationContext applicationContext;
    @Override public void setApplicationContext(@NonNull ApplicationContext ctx) { applicationContext = ctx; }
    public static <T> T getBean(Class<T> type) {
      try {
        return (applicationContext != null) ? applicationContext.getBean(type) : null;
      } catch (BeansException ex) {
        log.warn("Spring MeterRegistry not available, skipped micrometer metric logging", ex);
        return null;
      }
    }
  }
  public static final String NAME = "StatisticMicrometerBackend";
  @Override public String getName() { return NAME; }

  @Override
  public void storeEvent(StatisticEvent e) {
    if (e == null) return;

    MeterRegistry registry = SpringContextProxy.getBean(MeterRegistry.class);
    if (registry == null) return;

    Tags baseTags = Tags.of(
        "operation", safeName(e.getOperation(), v -> v.getName()),
        "status", safeName(e.getStatus(), v -> v.getName()),
        "source", safeName(e.getSource(), v -> v.getName()),
        "device", safeString(e.getDevice()),
        "profile", safeString(e.getProfileId())
    );

    Timer.builder("pdfas_requests")
        .description("Duration of PDF-AS operations")
        .tags(baseTags)
        .publishPercentileHistogram()
        .register(registry)
        .record(Math.max(0, e.getDuration()), TimeUnit.MILLISECONDS);

    if (e.getStatus() == StatisticEvent.Status.ERROR) {
      String whichException = safeName(e.getException(), it -> it.getClass().getSimpleName());
      Counter.builder("pdfas_errors")
          .description("Failed PDF-AS operations")
          .tags(baseTags.and("exception", whichException))
          .register(registry)
          .increment();
    }
  }

  private static @NonNull String safeString(String str) {
    return ((str == null) || str.isBlank()) ? "unknown" : str;
  }

  private static <T> @NonNull String safeName(T v, @NonNull Function<T, String> op) {
    return safeString((v != null) ? op.apply(v) : null);
  }
}
