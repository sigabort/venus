package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.Institute;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

/* test creating institute object and saving it to DB */
public class InstituteImplTest extends BaseImplTest {
  private Session sess;
  private VenusSession vs;
  
  @Before
  public void setUp() {
    vs = getVenusSession();
    sess = vs.getHibernateSession();
  }

  @Test
  public void testCreateInstitute() throws Exception {
   String name = "testCreateInst-" + getRandomString();
   Institute institute = createTestInstitute(name, vs);
   Transaction trans = sess.beginTransaction();
   sess.save(institute);
   trans.commit();
  }

  /** 
   * given name, create institute object 
   */
  public static Institute createTestInstitute(String name, VenusSession vs) {
    String code = name + "-code";
    String dispName = name + "-displayName";
    String desc = name + "-desc";
    String photoUrl = name + "-url";
    String email = name + "-email";

    Institute institute = new InstituteImpl();
    institute.setName(name);
    institute.setCode(code);
    institute.setDescription(desc);
    institute.setPhotoUrl(photoUrl);
    institute.setEmail(email);
    institute.setStatus(Status.Active.ordinal());
    institute.setCreated(new Date());
    institute.setLastModified(new Date());
    
    return institute;
  }

}