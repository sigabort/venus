package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.Program;
import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

/* test creating program object and saving it to DB */
public class ProgramImplTest extends BaseImplTest {
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
  public void testCreateProgram() throws Exception {
   String name = "testCreateProgram-" + getRandomString();
   Transaction trans = sess.beginTransaction();
   Department dept = DepartmentImplTest.createTestDepartment(name, vs);
   sess.save(dept);
   trans.commit();
   trans = sess.beginTransaction();
   Program program = createTestProgram(name, dept, vs);
   sess.save(program);
   trans.commit();
  }

  /** 
   * given name, create program object 
   */
  public static Program createTestProgram(String name, Department dept, VenusSession vs) {
    String code = name + "-code";
    String desc = name + "-desc";
    String prerequisites = name + "-prerequisites";
    Integer duration = 4;

    Program program = new ProgramImpl();
    program.setName(name);
    program.setDepartment(dept);
    program.setCode(code);
    program.setDescription(desc);
    program.setPrerequisites(prerequisites);
    program.setDuration(duration);
    program.setStatus(Status.Active.ordinal());
    program.setCreated(new Date());
    program.setLastModified(new Date());
    
    return program;
  }

}