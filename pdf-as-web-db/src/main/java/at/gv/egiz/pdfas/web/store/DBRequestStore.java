package at.gv.egiz.pdfas.web.store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.pdfas.api.processing.PdfasSignRequest;
import at.gv.egiz.pdfas.api.processing.PdfasSignResponse;
import at.gv.egiz.pdfas.web.config.WebConfiguration;
import at.gv.egiz.pdfas.web.stats.StatisticEvent;
import at.gv.egiz.pdfas.web.store.db.Request;
import at.gv.egiz.pdfas.web.store.db.Response;

public class DBRequestStore implements IRequestStore {

  private static final Logger logger = LoggerFactory
      .getLogger(DBRequestStore.class);

  private final SessionFactory sessions;

  public DBRequestStore() {
    final Configuration cfg = new Configuration();
    cfg.addAnnotatedClass(Request.class);
    cfg.addAnnotatedClass(Response.class);
    cfg.setProperties(WebConfiguration.getHibernateProps());

    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                cfg.getProperties()).build();

    sessions = cfg.buildSessionFactory(serviceRegistry);
  }

  private void cleanOldRequests() {
    final int seconds = WebConfiguration.getDBTimeout();
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, -1 * seconds);
    final Date date = calendar.getTime();
    final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    logger.info("Clearing Entries before: " + dt.format(date));
      Transaction tx = null;
      try (Session session = sessions.openSession()) {
          tx = session.beginTransaction();
          final MutationQuery query = session.createMutationQuery("delete from Request as req"
                  + " where req.created < :date");
          query.setParameter("date", calendar.getTime());
          query.executeUpdate();
          tx.commit();
      } catch (final Throwable e) {
          logger.error("Failed to save Request", e);
          if (tx != null) tx.rollback();
      }
  }

  public void cleanOldRequestException() {
    final int seconds = WebConfiguration.getDBTimeout();
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, -1 * seconds);
    final Date date = calendar.getTime();
    final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    logger.info("Clearing Entries before: " + dt.format(date));
      try (Session session = sessions.openSession()) {
          final MutationQuery query = session.createMutationQuery("delete from Request as req"
                  + " where req.created < :date");
          query.setParameter("date", calendar.getTime());
          query.executeUpdate();

          final MutationQuery queryResponse = session.createMutationQuery("delete from Response as req"
                  + " where req.created < :date");
          queryResponse.setParameter("date", calendar.getTime());
          queryResponse.executeUpdate();

      }
  }

  @Override
  public String createNewStoreEntry(PdfasSignRequest request,
      StatisticEvent event) {
    // Clean Old Requests
    this.cleanOldRequests();
      Transaction tx = null;
      try (Session session = sessions.openSession()) {
          tx = session.beginTransaction();
          final Request dbRequest = new Request();
          dbRequest.setSignRequest(request);
          dbRequest.setStatisticEvent(event);
          dbRequest.setCreated(Calendar.getInstance().getTime());
          session.persist(dbRequest);

          tx.commit();
          return dbRequest.getId();
      } catch (final Throwable e) {
          logger.error("Failed to save Request", e);
          if (tx != null) tx.rollback();
          return null;
      }
  }

  @Override
  public Pair<PdfasSignRequest, StatisticEvent> fetchStoreEntry(String id) {
    // Clean Old Requests
    this.cleanOldRequests();

      Transaction tx = null;
      try (Session session = sessions.openSession()) {
          tx = session.beginTransaction();
          final Request dbRequest = session.get(Request.class, id);
          if (dbRequest == null) return null;

          final PdfasSignRequest request = dbRequest.getSignRequest();
          final StatisticEvent event = dbRequest.getStatisticEvent();
          session.remove(dbRequest);

          tx.commit();
          return new Pair<>(request, event);
      } catch (final Throwable e) {
          logger.error("Failed to fetch Request", e);
          if (tx != null) tx.rollback();
          return null;
      }

  }

  @Override
  public String createNewResponseEntry(PdfasSignResponse response) {
    // Clean Old Requests
    this.cleanOldRequests();
      Transaction tx = null;
      try (Session session = sessions.openSession()) {
          tx = session.beginTransaction();
          final Response dbRequest = new Response();
          dbRequest.setSignedResponse(response);
          dbRequest.setCreated(Calendar.getInstance().getTime());
          session.persist(dbRequest);

          tx.commit();
          return dbRequest.getId();
      } catch (final Throwable e) {
          logger.error("Failed to save Request", e);
          if (tx != null) tx.rollback();
          return null;
      }
  }

  @Override
  public PdfasSignResponse fetchStoreResponse(String id) {
    // Clean Old Requests
    this.cleanOldRequests();

      Transaction tx = null;
      try (Session session = sessions.openSession()) {
          tx = session.beginTransaction();
          final Response dbResponse = session.get(Response.class, id);

          final PdfasSignResponse request = dbResponse.getSignedResponse();

          session.remove(dbResponse);

          tx.commit();
          return request;
      } catch (final Throwable e) {
          logger.error("Failed to fetch Response", e);
          if (tx != null) tx.rollback();
          return null;
      }
  }
}
