package com.venus.util;

import org.hibernate.Session;

public class VenusSession {
  private Session session;
  
  public Session getHibernateSession() {
    return this.session;
  }
  public void setHibernateSession(Session session) {
    this.session = session;
  }
}
