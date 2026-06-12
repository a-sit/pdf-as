package at.gv.egiz.pdfas.web.store;

import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import io.micrometer.core.instrument.Statistic;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DBRequestStoreTest {
  @BeforeAll
  @SneakyThrows
  public static void configureH2Hibernate() {
    String config = """
        pdfas.dir=.
        request.db.timeout=600

        hibernate.props.hibernate.dialect=org.hibernate.dialect.H2Dialect
        hibernate.props.hibernate.connection.driver_class=org.h2.Driver
        hibernate.props.hibernate.connection.url=jdbc:h2:mem:pdfaswebdbtest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        hibernate.props.hibernate.connection.username=sa
        hibernate.props.hibernate.connection.password=
        hibernate.props.hibernate.connection.pool_size=1
        hibernate.props.hibernate.connection.autocommit=false
        hibernate.props.hibernate.hbm2ddl.auto=create-drop
        hibernate.props.hibernate.show_sql=false
        """;

    WebConfiguration.configure(
        new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));
  }

  public static String azstring(int length) {
    return
        new Random().ints(97,123).limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
  }

  @Test
  void signRequestRoundTrip() {
    val store = new DBRequestStore();

    val request = new PdfasSignRequest();
    val requestId = azstring(32);
    request.setRequestID(requestId);

    val event = new StatisticEvent();
    val filesize = (new Random().nextInt(42, 123456));
    event.setSource(StatisticEvent.Source.SOAP);
    event.setOperation(StatisticEvent.Operation.SIGN);
    event.setStatus(StatisticEvent.Status.OK);
    event.setDevice("mobile");
    event.setProfileId("SIGNATURBLOCK_DE_SMALL");
    event.setFilesize(filesize);
    event.setTimestampNow();

    val id = store.createNewStoreEntry(request, event);
    Assertions.assertNotNull(id);

    val fetchedPair = store.fetchStoreEntry(id);
    Assertions.assertNotNull(fetchedPair);

    val fetchedSignRequest = fetchedPair.getFirst();
    Assertions.assertNotNull(fetchedSignRequest);
    Assertions.assertEquals(requestId, fetchedSignRequest.getRequestID());

    val fetchedEvent = fetchedPair.getSecond();
    Assertions.assertNotNull(fetchedEvent);
    Assertions.assertEquals(StatisticEvent.Source.SOAP, fetchedEvent.getSource());
    Assertions.assertEquals(StatisticEvent.Operation.SIGN, fetchedEvent.getOperation());
    Assertions.assertEquals(StatisticEvent.Status.OK, fetchedEvent.getStatus());
    Assertions.assertEquals("mobile", fetchedEvent.getDevice());
    Assertions.assertEquals("SIGNATURBLOCK_DE_SMALL", fetchedEvent.getProfileId());
    Assertions.assertEquals(filesize, fetchedEvent.getFilesize());

    Assertions.assertNull(store.fetchStoreEntry(id));
  }
}
