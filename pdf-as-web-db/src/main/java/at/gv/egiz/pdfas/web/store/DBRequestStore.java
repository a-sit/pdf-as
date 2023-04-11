package at.gv.egiz.pdfas.web.store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
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
import at.gv.egiz.pdfas.web.store.db.StatisticRequest;

public class DBRequestStore implements IRequestStore {

  private static final Logger logger = LoggerFactory
      .getLogger(DBRequestStore.class);

  private final SessionFactory sessions;
  private final ServiceRegistry serviceRegistry;

  public DBRequestStore() {
    final Configuration cfg = new Configuration();
    cfg.addAnnotatedClass(Request.class);
    cfg.addAnnotatedClass(Response.class);
    cfg.addAnnotatedClass(StatisticRequest.class);
    cfg.setProperties(WebConfiguration.getHibernateProps());

    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
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
    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final Query query = session.createQuery("delete from Request as req"
          + " where req.created < :date");
      query.setCalendar("date", calendar);
      query.executeUpdate();
      tx.commit();
    } catch (final Throwable e) {
      logger.error("Failed to save Request", e);
      tx.rollback();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public void cleanOldRequestException() {
    final int seconds = WebConfiguration.getDBTimeout();
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, -1 * seconds);
    final Date date = calendar.getTime();
    final SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    logger.info("Clearing Entries before: " + dt.format(date));
    Session session = null;
    try {
      session = sessions.openSession();
      final Query query = session.createQuery("delete from Request as req"
          + " where req.created < :date");
      query.setCalendar("date", calendar);
      query.executeUpdate();

      final Query queryStat = session.createQuery("delete from StatisticRequest as req"
          + " where req.created < :date");
      queryStat.setCalendar("date", calendar);
      queryStat.executeUpdate();

      final Query queryResponse = session.createQuery("delete from Response as req"
          + " where req.created < :date");
      queryResponse.setCalendar("date", calendar);
      queryResponse.executeUpdate();
      
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  @Override
  public String createNewStoreEntry(PdfasSignRequest request,
      StatisticEvent event) {
    // Clean Old Requests
    this.cleanOldRequests();
    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final Request dbRequest = new Request();
      dbRequest.setSignRequest(request);
      dbRequest.setCreated(Calendar.getInstance().getTime());
      session.save(dbRequest);

      final StatisticRequest statisticRequest = new StatisticRequest();
      statisticRequest.setStatisticEvent(event);
      statisticRequest.setCreated(Calendar.getInstance().getTime());
      session.save(statisticRequest);

      tx.commit();
      return dbRequest.getId();
    } catch (final Throwable e) {
      logger.error("Failed to save Request", e);
      tx.rollback();
      return null;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  @Override
  public PdfasSignRequest fetchStoreEntry(String id) {
    // Clean Old Requests
    this.cleanOldRequests();

    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final Request dbRequest = session.get(Request.class, id);

      final PdfasSignRequest request = dbRequest.getSignRequest();

      session.delete(dbRequest);

      tx.commit();
      return request;
    } catch (final Throwable e) {
      logger.error("Failed to fetch Request", e);
      tx.rollback();
      return null;
    } finally {
      if (session != null) {
        session.close();
      }
    }

  }

  @Override
  public StatisticEvent fetchStatisticEntry(String id) {
    // Clean Old Requests
    this.cleanOldRequests();

    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final StatisticRequest dbRequest = session.get(
          StatisticRequest.class, id);

      final StatisticEvent request = dbRequest.getStatisticEvent();

      session.delete(dbRequest);

      tx.commit();
      return request;
    } catch (final Throwable e) {
      logger.error("Failed to fetch Request", e);
      tx.rollback();
      return null;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  @Override
  public String createNewResponseEntry(PdfasSignResponse response) {
    // Clean Old Requests
    this.cleanOldRequests();
    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final Response dbRequest = new Response();
      dbRequest.setSignedResponse(response);
      dbRequest.setCreated(Calendar.getInstance().getTime());
      session.save(dbRequest);

      tx.commit();
      return dbRequest.getId();
    } catch (final Throwable e) {
      logger.error("Failed to save Request", e);
      tx.rollback();
      return null;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  @Override
  public PdfasSignResponse fetchStoreResponse(String id) {
    // Clean Old Requests
    this.cleanOldRequests();

    Session session = null;
    Transaction tx = null;
    try {
      session = sessions.openSession();
      tx = session.beginTransaction();
      final Response dbResponse = session.get(Response.class, id);

      final PdfasSignResponse request = dbResponse.getSignedResponse();

      session.delete(dbResponse);

      tx.commit();
      return request;
    } catch (final Throwable e) {
      logger.error("Failed to fetch Response", e);
      tx.rollback();
      return null;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
