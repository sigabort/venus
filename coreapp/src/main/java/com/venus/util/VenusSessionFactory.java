package com.venus.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import org.hibernate.transaction.JOTMTransactionManagerLookup;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.venus.model.Institute;

public class VenusSessionFactory {
  /**
   * see if we are run in test mode or not
   */
  static Boolean testMode = Boolean.getBoolean("venus.test.mode");

  public static VenusSession getVenusSession() {
    SessionFactory sf = null;
    /*
     * If not in test mode, try to get the initial context using lookup 
     */
    if (!testMode) {
      try {
        sf = (SessionFactory) new InitialContext()
            .lookup("java:comp/env/hibernate/SessionFactory");
      } catch (NamingException ne) {
        return null;
      }
    }

    if (sf == null) {
      sf = SessionFactoryUtil.getInstance();
    }
    /*
     * XXX: Instead of getCurrentSession(), we use openSession(). Because, we still 
     * commit the transactions in the dal/impl layer. If we use current session and commit transactions in the
     * dal layer, session will be closed when ever we commit. That will cause problems for other operations.
     * So, lets create new session for each call when we are run in test mode.
     */
    Session session = sf.openSession();
    if (session == null || !session.isOpen()) {
      session = sf.openSession();
    }
    VenusSession vs = new VenusSession();
    vs.setHibernateSession(session);
    return vs;
  }

  public static VenusSession getVenusSession(Institute institute) {
    VenusSession vs = getVenusSession();
    vs.setInstitute(institute);
    return vs;
  }

}
