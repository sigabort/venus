package com.venus.util;

import org.hibernate.Session;

public class VenusSession {
  private Session session;
  
  public Session getSession() {
    return this.session;
  }
  public void setSession(Session session) {
    this.session = session;
  }
}
