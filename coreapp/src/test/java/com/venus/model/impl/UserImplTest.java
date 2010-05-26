package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.Institute;
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
    String name = "UserImplTest-" + getRandomString();
    Institute institute = InstituteImplTest.createTestInstitute(name, vs);
    sess = vs.getHibernateSession();
    sess.save(institute);
    vs.setInstitute(institute);
  }

  @Test
  public void testCreateUser() throws Exception {
   Transaction trans = sess.beginTransaction();

   User user = new UserImpl();
   user.setUsername("ravi-" + getRandomString());
   user.setInstitute(vs.getInstitute());
   user.setFirstName("ravi");
   user.setLastName("ravi");
   user.setStatus(Status.Active.ordinal());
   user.setCreated(new Date());
   user.setLastModified(new Date());

   sess.save(user);
   trans.commit();
   
  }

}