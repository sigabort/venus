package com.venus.util;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;


public class VenusSessionFactory {
  
  public static VenusSession getVenusSession() {
    SessionFactory sf = new Configuration().configure().buildSessionFactory();
    Session session = sf.openSession();
    VenusSession vs = new VenusSession();
    vs.setHibernateSession(session);
    return vs;
  }
  
}
