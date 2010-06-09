package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.venus.util.VenusSession;
import org.hibernate.Session;

import java.util.Date;

class CourseImplTest extends AbstractImplTest {
  var sess: Session = _;
  var vs: VenusSession = _;
  var testInst: Institute = _;
  
  @Before def setUp() {
    vs = getVenusSession();
    sess = vs.getHibernateSession();
    val name = "CourseImplTest-" + getRandomString();
    sess.beginTransaction();
    testInst = InstituteImplTest.createTestInstitute(name, vs);
    sess.save(testInst);
    vs.setInstitute(testInst);
  }
  
  @Test def testCreateCourse() {
    val name:String = getRandomString();
    val course : CourseImpl = createTestCourse(name, vs);
    Assert.assertNotNull("The course object", course);
    sess.save(course);
    sess.getTransaction.commit;
  }  

  def createTestCourse(name: String, vs: VenusSession): CourseImpl = {
    val course: CourseImpl = new CourseImpl();
    val sess: Session = vs.getHibernateSession();

    course.setName(name);
    course.setCode(name + "-code");
    course.setDescription(name + "-desc");
    course.setContent(name + "-content");
    course.setPrerequisites(name + "-preReq");
    course.setDuration(getRandomNumber());
    val testDept = DepartmentImplTest.createTestDepartment(name, vs);
    sess.save(testDept);

    val testPgm = ProgramImplTest.createTestProgram(name, testDept, vs);
    sess.save(testPgm);

    val testUsr = UserImplTest.createTestUser(name, vs);
    sess.save(testUsr);
    
    course.setDepartment(testDept.asInstanceOf[DepartmentImpl]);
    course.setInstitute(testInst.asInstanceOf[InstituteImpl]);
    course.setAdmin(testUsr.asInstanceOf[UserImpl]);
    course.setInstructor(testUsr.asInstanceOf[UserImpl]);
    
    course.setStatus(Status.Active.ordinal());
    course.setCreated(new Date());
    course.setLastModified(new Date());
    course;
  }
  
}
