package com.venus.util;

import org.hibernate.Session;

import com.venus.model.Institute;

public class VenusSession {
  private Session session;
  private Institute institute;
  
  public Session getHibernateSession() {
    return this.session;
  }
  public void setHibernateSession(Session session) {
    this.session = session;
  }
 
  public Institute getInstitute() {
    return this.institute;
  }
  
  public void setInstitute(Institute institute) {
    this.institute = institute;
  }
  
}
