package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.User;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;


public class UserImplTest extends BaseImplTest {
  private Session sess;
  private VenusSession vs;
  
  @Before
  public void setUp() {
    vs = getVenusSession();
    /* XXX: we need to set this by creating actual institute */
    vs.setInstituteId(1);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
  }

  @Test
  public void testCreateUser() throws Exception {
   Transaction trans = sess.beginTransaction();

   User user = new UserImpl();
   user.setUsername("ravi-" + getRandomString());
   user.setInstituteId(vs.getInstituteId());
   user.setFirstName("ravi");
   user.setLastName("ravi");
   user.setStatus(Status.Active.ordinal());
   user.setCreated(new Date());
   user.setLastModified(new Date());

   sess.save(user);
   trans.commit();
   
  }

}