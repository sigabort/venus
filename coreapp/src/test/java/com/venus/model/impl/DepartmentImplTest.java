package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

/* test creating department object and saving it to DB */
public class DepartmentImplTest extends BaseImplTest {
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
  public void testCreateDepartment() throws Exception {
   String name = "testCreateDept-" + getRandomString();
   Department dept = createTestDepartment(name, vs);
   Transaction trans = sess.beginTransaction();
   sess.save(dept);
   trans.commit();
  }

  /** 
   * given name, create department object 
   */
  public static Department createTestDepartment(String name, VenusSession vs) {
    String code = name + "-code";
    String desc = name + "-desc";
    String photoUrl = name + "-url";
    String email = name + "-email";

    Department dept = new DepartmentImpl();
    dept.setName(name);
    dept.setInstituteId(vs.getInstituteId());
    dept.setCode(code);
    dept.setDescription(desc);
    dept.setPhotoUrl(photoUrl);
    dept.setEmail(email);
    dept.setStatus(Status.Active.ordinal());
    dept.setCreated(new Date());
    dept.setLastModified(new Date());
    
    return dept;
  }

}