package com.venus.dal.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.venus.model.Program;
import com.venus.model.Status;
import com.venus.model.Department;
import com.venus.util.VenusSession;

import com.venus.model.impl.BaseImplTest;
import com.venus.model.impl.DepartmentImplTest;

public class ProgramOperationsImplTest extends BaseImplTest {
  private ProgramOperationsImpl pol;
  private Session sess;
  private VenusSession vs;
  private Department testDept;
  
  @Before
  public void setUp() {
    pol = new ProgramOperationsImpl();
    vs = getVenusSession();
    /* XXX: we need to do this after creating the institute */
    Integer randInt = new Random(new Random().nextLong()).nextInt();
    vs.setInstituteId(randInt);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
    Transaction txn = sess.beginTransaction();
    testDept = DepartmentImplTest.createTestDepartment("ProgramOpsImplTest-" + getUniqueName(), vs);
    sess.save(testDept);
    txn.commit();
  }

  /* create and test program */
  @Test
  public void testCreateProgram() throws Exception {
   String name = "testCreateProgram-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;
   
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertEquals("program status", (int)Status.Active.ordinal(), (int)program.getStatus());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program lastModified date", program.getLastModified());
  }

  /* create, update and test program */
  @Test
  public void testUpdateProgram() throws Exception {
   String name = "testUpdateProgram-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;

   /* create one program */
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program last modified date", program.getLastModified());

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newPrerequisites = name + "-nuPrerequisites";
   Integer newDuration = 10;

   /* update the program with new values */
   Program program1 = pol.createUpdateProgram(name, testDept, newCode, newDesc, newPrerequisites, newDuration, null, null, vs);
   Assert.assertNotNull(program1);
   
   Assert.assertEquals("program name", name, program1.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", newCode, program1.getCode());
   Assert.assertEquals("program description", newDesc, program1.getDescription());
   Assert.assertEquals("program prerequisites", newPrerequisites, program1.getPrerequisites());
   Assert.assertEquals("program duration", newDuration, program1.getDuration());
   Assert.assertNotNull("program created date", program1.getCreated());
   Assert.assertNotNull("program last modified date", program1.getLastModified());
  }
  
  /* create and find the program by its name */
  @Test
  public void testFindProgramByName() throws Exception {
   String name = "testFindProgramByName-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;
   
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program lastModified date", program.getLastModified());

   Program program1 = pol.findProgramByName(name, testDept, vs);
   Assert.assertNotNull(program1);
   Assert.assertEquals("The programs should be equal", program, program1);
  }

  /* create and find the program by its code */
  @Test
  public void testFindProgramByCode() throws Exception {
   String name = "testFindProgramByCode-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;
   
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program lastModified date", program.getLastModified());

   Program program1 = pol.findProgramByCode(code, vs);
   Assert.assertNotNull(program1);
   Assert.assertEquals("The programs should be equal", program, program1);
  }

  /* create, delete and update the same */
  @Test
  public void testUpdateDeletedProgram() throws Exception {
   String name = "testUpdateDeletedProgram-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;

   /* create one program */
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program last modified date", program.getLastModified());

   /* delete the program */
   pol.deleteProgram(program, vs);

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newPrerequisites = name + "-nuPrerequisites";
   Integer newDuration = 10;

   /* update the program with new values */
   Program program1 = pol.createUpdateProgram(name, testDept, newCode, newDesc, newPrerequisites, newDuration, null, null, vs);
   Assert.assertNotNull(program1);
   
   Assert.assertEquals("program name", name, program1.getName());
   Assert.assertEquals("program department", testDept, program1.getDepartment());
   Assert.assertEquals("program code", newCode, program1.getCode());
   Assert.assertEquals("program description", newDesc, program1.getDescription());
   Assert.assertEquals("program prerequisites", newPrerequisites, program1.getPrerequisites());
   Assert.assertEquals("program duration", newDuration, program1.getDuration());
   Assert.assertNotNull("program created date", program1.getCreated());
   Assert.assertNotNull("program last modified date", program1.getLastModified());
  }
 
  /* create, delete and test */
  @Test
  public void testDeleteProgram() throws Exception {
   String name = "testDeleteProgram-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;

   /* create one program */
   Program program = pol.createUpdateProgram(name, testDept, code, desc, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program);
   
   Assert.assertEquals("program name", name, program.getName());
   Assert.assertEquals("program department", testDept, program.getDepartment());
   Assert.assertEquals("program code", code, program.getCode());
   Assert.assertEquals("program description", desc, program.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program.getPrerequisites());
   Assert.assertEquals("program duration", duration, program.getDuration());
   Assert.assertNotNull("program created date", program.getCreated());
   Assert.assertNotNull("program last modified date", program.getLastModified());

   /* delete the program */
   pol.deleteProgram(program, vs);

   Program program1 = pol.findProgramByCode(code, vs);
   Assert.assertNull("program should not be found", program1);

   program1 = pol.findProgramByName(name, testDept, vs);
   Assert.assertNull("program should not be found", program1);
  }
 
  /* create some programs and fetch them */
  @Test
  public void testGetPrograms() throws Exception {
   String name = "testGetPrograms-" + getUniqueName();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String prerequisites = name + "-prerequisites";
   Integer duration = 5;

   /* create one program */
   Program program1 = pol.createUpdateProgram(name1, testDept, code1, desc1, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program1);
   
   Assert.assertEquals("program name", name1, program1.getName());
   Assert.assertEquals("program dept", testDept, program1.getDepartment());
   Assert.assertEquals("program code", code1, program1.getCode());
   Assert.assertEquals("program description", desc1, program1.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program1.getPrerequisites());
   Assert.assertEquals("program duration", duration, program1.getDuration());
   Assert.assertNotNull("program created date", program1.getCreated());
   Assert.assertNotNull("program last modified date", program1.getLastModified());

   /* create another program */
   Program program2 = pol.createUpdateProgram(name2, testDept, code2, desc2, prerequisites, duration, null, null, vs);
   Assert.assertNotNull(program2);
   
   Assert.assertEquals("program name", name2, program2.getName());
   Assert.assertEquals("program dept", testDept, program2.getDepartment());
   Assert.assertEquals("program code", code2, program2.getCode());
   Assert.assertEquals("program description", desc2, program2.getDescription());
   Assert.assertEquals("program prerequisites", prerequisites, program2.getPrerequisites());
   Assert.assertEquals("program duration", duration, program2.getDuration());
   Assert.assertNotNull("program created date", program2.getCreated());
   Assert.assertNotNull("program last modified date", program2.getLastModified());

   /* fetch the programs now */
   List<Program> list = pol.getPrograms(testDept, 0, 10, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should not be 2", 2, list.size());
  }

}