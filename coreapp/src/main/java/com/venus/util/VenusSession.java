package com.venus.util;

import org.hibernate.Session;

public class VenusSession {
  private Session session;
  private Integer instituteId;
//   private Institute institute;
  
  public Session getHibernateSession() {
    return this.session;
  }
  public void setHibernateSession(Session session) {
    this.session = session;
  }

  public Integer getInstituteId() {
    return this.instituteId;
  }
  
  public void setInstituteId(Integer instituteId) {
    this.instituteId = instituteId;
  }

//   public Institute getInstitute() {
//     return this.institute;
//   }
  
//   public void setInstitute(Institute institute) {
//     this.institute = institute;
//   }
  
}
