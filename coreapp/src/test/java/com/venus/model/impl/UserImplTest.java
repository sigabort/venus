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
    sess.beginTransaction();
    sess.save(institute);
    vs.setInstitute(institute);
  }

  @Test
  public void testCreateUser() throws Exception {
    Transaction trans = sess.beginTransaction();
    User user = createTestUser(getRandomString(), vs);
    sess.save(user);
    trans.commit();
  }
  
  public static User createTestUser(String name, VenusSession vs) throws Exception {
   User user = new UserImpl();
   user.setUsername(name + "-name");
   user.setInstitute(vs.getInstitute());
   user.setFirstName(name + "-fName");
   user.setLastName(name + "-lName");
   user.setStatus(Status.Active.ordinal());
   user.setCreated(new Date());
   user.setLastModified(new Date());
   return user;
  }

}