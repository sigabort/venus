package com.venus.dal.impl;

import org.scalatest.junit.JUnitSuite;
import org.scalatest.junit.ShouldMatchersForJUnit;

import scala.collection.mutable.ListBuffer;

import org.junit.{Test, Before, Assert};

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.venus.model.{BaseModel, Institute, Department};
import com.venus.model.impl._;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;

import com.venus.model.impl.AbstractImplTest;
import com.venus.model.impl.InstituteImplTest;
import scala.collection.jcl.HashMap;

import scala.collection.mutable.Map;

import java.util.{Date};

class CourseDalImplTest extends AbstractImplTest {
  val cdi = new CourseDalImpl();
  var sess: Session = _;
  var vs: VenusSession = _;
  var testInst: InstituteImpl = _;
  var testDept: DepartmentImpl = _;
  var testPgm: ProgramImpl = _;
  var testUsr: UserImpl = _;
  
  
  @Before def setUp() {
    vs = getVenusSession();
    sess = vs.getHibernateSession();
    val name = "CourseDalImplTest-" + getRandomString();
    testInst = InstituteImplTest.createTestInstitute(name, vs).asInstanceOf[InstituteImpl];
    sess.beginTransaction();
    sess.save(testInst);
    vs.setInstitute(testInst);
    
    testDept = DepartmentImplTest.createTestDepartment(name, vs).asInstanceOf[DepartmentImpl];
    sess.save(testDept);
    testPgm = ProgramImplTest.createTestProgram(name, testDept, vs).asInstanceOf[ProgramImpl];
    sess.save(testPgm);
    testUsr = UserImplTest.createTestUser(name, vs).asInstanceOf[UserImpl];
    sess.save(testUsr);
  }
  
  /**
   * Create the course and test the object
   * @return
   */
  @Test def testCreateCourse() {
    val code: String = "testCreateCourse-" + getRandomString();
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", name), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters */
    verifyParamsMap(course, optionalParams);
  }

  /**
   * Create the course with out optional parameters
   * @return
   */
  @Test def testCreateCourseWithOutOptParameters() {
    val code: String = "testCCWOOP-" + getRandomString();
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, null, vs);
    Assert.assertNotNull("Created Course", course);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", name), ("department", testDept), ("instructor", testUsr)));
    course.getStatus should be          (Status.Active.ordinal());
    course.getCreated should not        (be (null));
    course.getLastModified should not   (be (null));
    course.getAdmin should  equal       (testUsr);
  }

  /**
   * Create the course with out mandatory parameters
   * @return
   */
  @Test { val expected = classOf[ IllegalArgumentException ] }
  def testCreateCourseWithOutMandateParams() {
    val code: String = "testCCWOMandatePs-" + getRandomString();
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse(null, null, null, null, null, vs);    
  }

  
    /**
   * Create the course with mandatory parameters as empty
   * @return
   */
  @Test { val expected = classOf[ IllegalArgumentException ] }
  def testCreateCourseWithMandateParamsAsEmpty() {
    val code: String = "testCCWMPAE-" + getRandomString();
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse("", "", null, null, null, vs);    
  }
  
  /**
   * Create the course, update it and test the object
   * @return
   */
  @Test def testUpdateCourse() {
    val code: String = "testUpdateCourse-" + getRandomString();
    val course: CourseImpl = createTestCourse(code, vs);
    
    val newName: String = code + "-newName";
    testDept = DepartmentImplTest.createTestDepartment(code, vs).asInstanceOf[DepartmentImpl];
    sess.save(testDept);
    testUsr = UserImplTest.createTestUser(code, vs).asInstanceOf[UserImpl];
    sess.save(testUsr);

    val optionalParams = getCourseOptionalParams(code + "-New");
    
    /* update the course with new parameters now */
    val newCourse: CourseImpl = cdi.createUpdateCourse(code, newName, testDept, testUsr, optionalParams.underlying, vs);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", newName), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters */
    verifyParamsMap(course, optionalParams);
  }

  /**
   * Create the course, update it with out optional params
   * @return
   */
  @Test def testUpdateCourseWithOutOptParams() {
    val code: String = "testUpdateCourseWOOptParams-" + getRandomString();
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    val name: String = code + "-name";
    
    /* create a course */
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);
    
    val newName: String = code + "-newName";
    testDept = DepartmentImplTest.createTestDepartment(code, vs).asInstanceOf[DepartmentImpl];
    sess.save(testDept);
    testUsr = UserImplTest.createTestUser(code, vs).asInstanceOf[UserImpl];
    sess.save(testUsr);

    /* update the course with new parameters now */
    val newCourse: CourseImpl = cdi.createUpdateCourse(code, newName, testDept, testUsr, null, vs);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", newName), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters - should be old params */
    verifyParamsMap(course, optionalParams);
  }
  
  /**
   * Create the course, update it with out mandatory params
   * @return
   */
  @Test def testUpdateCourseWithOutMandatoryParams() {
    val code: String = "testUpdateCourseWOMandParams-" + getRandomString();
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    val name: String = code + "-name";
    
    /* create a course */
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);
    
    /* update the course with new parameters now */
    val newCourse: CourseImpl = cdi.createUpdateCourse(code, null, null, null, null, vs);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", name), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters - should be old params */
    verifyParamsMap(course, optionalParams);
  }

  /**
   * Test finding a course
   * @return
   */
  @Test def testFindCourse() {
    val code: String = "testFindCourse-" + getRandomString();
    val course: CourseImpl = createTestCourse(code, vs);
    
    val c: CourseImpl = cdi.findCourse(code, null, vs);
    c should not be (null);
    c should equal (course);
  }

  /**
   * Test finding a course which is not existing
   * @return
   */
  @Test def testFindNonExistingCourse() {
    val code: String = "testFindNonExistingCourse-" + getRandomString();
    
    val c: CourseImpl = cdi.findCourse(code, null, vs);
    c should  be (null);
  }

  /**
   * Test finding a course which is not active
   * @return
   */
  @Test def testFindInActiveCourse() {
    val code: String = "testFindInactiveCourse-" + getRandomString();
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    optionalParams.put("status", Status.Deleted);
    val name: String = code + "-name";
    
    /* create a course */
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);

    val c: CourseImpl = cdi.findCourse(code, null, vs);
    c should be (null);
  }

  
  /**
   * Test finding a course which is not active asking returning even nonactive course
   * @return
   */
  @Test def testFindInActiveCourseWhichShouldReturn() {
    val code: String = "testFindInactiveCourseWSR-" + getRandomString();
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    optionalParams.put("status", Status.Deleted);
    val name: String = code + "-name";
    
    /* create a course */
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);

    val options: HashMap[String, Object] = new HashMap[String, Object];
    options  += "onlyActive" -> false.asInstanceOf[Object];
    val c: CourseImpl = cdi.findCourse(code, options.underlying, vs);
    c should not be (null);
    c should equal (course);
  }

  /**
   * Test finding course with out code
   * @return
   */
  @Test { val expected = classOf[ IllegalArgumentException ] }
  def testFindWithoutMandatoryParams() {
    val options: HashMap[String, Object] = new HashMap[String, Object];
    val c: CourseImpl = cdi.findCourse(null, options.underlying, vs);
  }

  
  def getCourseOptionalParams(code: String): HashMap[String, Object] = {
    val columns = Array[(String, Object)](
          ("description", code+"-desc"), 
          ("program", testPgm), ("admin", testUsr), ("photoUrl", code + "-photoUrl"),
          ("prerequisites", code + "-preReq"), ("content", code + "-content"), 
          ("duration", getRandomNumber().asInstanceOf[Object]), ("status", Status.Active),
          ("created", new Date()), ("lastModified", new Date())
          );
    val params: HashMap[String, Object] = new HashMap[String, Object];
    columns.foreach(s => params += s._1 -> s._2);
    params;
  }
  
  def createTestCourse(code: String, vs: VenusSession): CourseImpl = {
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, vs);
    Assert.assertNotNull("Created Course", course);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", name), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters */
    verifyParamsMap(course, optionalParams);

    course;
  }
  
  
}
