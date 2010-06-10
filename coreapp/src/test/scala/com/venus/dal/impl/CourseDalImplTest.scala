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
import scala.collection.jcl.{HashMap, ArrayList};

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

  /* test getting courses in an institute */
  @Test def testGetCourses() {
    val code: String = "testGetCourses-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val course1 : CourseImpl = createTestCourse(code + "-1", vs);
    val course2 : CourseImpl = createTestCourse(code + "-2", vs);

    val courses = cdi.getCourses(0, 10, null, vs);
    courses should not be (null);
    courses.size should equal (2);
  }

  /* test getting count of courses in an institute */
  @Test def testGetCoursesCount() {
    val code: String = "testGetCoursesCount-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val course1 : CourseImpl = createTestCourse(code + "-1", vs);
    val course2 : CourseImpl = createTestCourse(code + "-2", vs);

    val count = cdi.getCoursesCount(null, vs);
    count should equal (2);
  }

  
  /* test getting courses in an institute */
  @Test def testGetCoursesWithSort() {
    val code: String = "testGetCoursesSort-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val course2 : CourseImpl = createTestCourse(code + "-2", vs);
    val course3 : CourseImpl = createTestCourse(code + "-3", vs);
    val course1 : CourseImpl = createTestCourse(code + "-1", vs);

    val sort1: VenusSortImpl = new VenusSortImpl("code", true);
    val list = new ArrayList[VenusSortImpl];
    list.add(sort1);
    val options = new HashMap[String, Object];
    options += "sortList" -> list.underlying.asInstanceOf[Object];
    
    val courses = cdi.getCourses(0, 10, options.underlying, vs);
    courses should not be (null);
    courses.size should equal (3);
    courses.get(0).asInstanceOf[CourseImpl] should equal (course1);
    courses.get(1).asInstanceOf[CourseImpl] should equal (course2);
    courses.get(2).asInstanceOf[CourseImpl] should equal (course3);
  }

  
  /* test getting courses in an institute */
  @Test def testGetCoursesWithFilter() {
    val code: String = "testGetCoursesFilter-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val course2 : CourseImpl = createTestCourse(code + "-xxx2", vs);
    val course3 : CourseImpl = createTestCourse(code + "-xxx3", vs);
    val course1 : CourseImpl = createTestCourse(code + "-xxx1", vs);

    val sort1: VenusSortImpl = new VenusSortImpl("code", true);
    val list1 = new ArrayList[VenusSortImpl];
    list1.add(sort1);
    val options = new HashMap[String, Object];
    options += "sortList" -> list1.underlying.asInstanceOf[Object];

    val filter1 = new VenusFilterImpl("code", "-xxx2", "contains");
    val list2 = new ArrayList[VenusFilterImpl];
    list2.add(filter1);
    options += "filterList" -> list2.underlying.asInstanceOf[Object];

    /* get the list of courses now */
    val courses = cdi.getCourses(0, 10, options.underlying, vs);
    courses should not be (null);
    courses.size should equal (1);
    courses.get(0).asInstanceOf[CourseImpl] should equal (course2);

    /* check the count */
    val count = cdi.getCoursesCount(options.underlying, vs);
    count should equal (1);
  }

  /* test getting courses in an institute with filters on program/dept */
  @Test def testGetCoursesWithFiltersOnDept() {
    val code: String = "testGetCoursesFOD-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val n1 = code + "-1";  val n2 = code + "-2"; val n3 = code + "-3";

    val p1 = getCourseOptionalParams(n1);
    val p2 = getCourseOptionalParams(n2);
    val p3 = getCourseOptionalParams(n3);
    
    val d1 = DepartmentImplTest.createTestDepartment(n1, vs).asInstanceOf[DepartmentImpl];
    sess.save(d1);
    val d2 = DepartmentImplTest.createTestDepartment(n2, vs).asInstanceOf[DepartmentImpl];
    sess.save(d2);

    
    /* use d1 as department for first 2 courses */
    val c1 = cdi.createUpdateCourse(n1, n1, d1, testUsr, p1.underlying, vs);
    c1 should not be (null);
    val c2 = cdi.createUpdateCourse(n2, n2, d1, testUsr, p2.underlying, vs);
    c2 should not be (null);

    /* use d2 as department for 3rd course */
    val c3 = cdi.createUpdateCourse(n3, n3, d2, testUsr, p3.underlying, vs);
    c3 should not be (null);

    /* Get all courses in this institute first - should be 3 */
    val courses = cdi.getCourses(0, 10, null, vs);
    courses should not be (null);
    courses.size should equal (3);

    /* Get all courses in the institute which belong to department-1 */
    val filter1 = new VenusFilterImpl("department", d1, "equals");
    val list1 = new ArrayList[VenusFilterImpl];
    list1.add(filter1);
    val options = new HashMap[String, Object];
    options += "filterList" -> list1.underlying.asInstanceOf[Object];
    
    val courses1 = cdi.getCourses(0, 10, options.underlying, vs).asInstanceOf[java.util.ArrayList[CourseImpl]];
    courses1 should not be (null);
    courses1.size should equal (2);
    val cList = new ArrayList[CourseImpl](courses1);
    cList.foreach(s => s should (equal(c1) or equal(c2)));
    
    /* check the count too */
    val count = cdi.getCoursesCount(options.underlying, vs);
    count should equal (2);
  }

  
  /* test getting courses in an institute with filters on program/dept */
  @Test def testGetCoursesWithFiltersOnPgm() {
    val code: String = "testGetCoursesFOP-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val n1 = code + "-1";  val n2 = code + "-2"; val n3 = code + "-3";

    val p1 = getCourseOptionalParams(n1);
    val p2 = getCourseOptionalParams(n2);
    val p3 = getCourseOptionalParams(n3);
    
    val pgm1 = ProgramImplTest.createTestProgram(n1, testDept, vs).asInstanceOf[ProgramImpl];
    sess.save(pgm1);
    val pgm2 = ProgramImplTest.createTestProgram(n2, testDept, vs).asInstanceOf[ProgramImpl];
    sess.save(pgm2);

    /* use pgm2 as pgm for 3rd course */
    p3 += "program"-> pgm2;
    val c3 = cdi.createUpdateCourse(n3, n3, testDept, testUsr, p3.underlying, vs);
    c3 should not be (null);

    p1 += "program" -> pgm1;
    p2 += "program" -> pgm1;
    /* use pgm1 as pgm for first 2 courses */
    val c1 = cdi.createUpdateCourse(n1, n1, testDept, testUsr, p1.underlying, vs);
    c1 should not be (null);
    val c2 = cdi.createUpdateCourse(n2, n2, testDept, testUsr, p2.underlying, vs);
    c2 should not be (null);


    /* Get all courses in this institute first - should be 3 */
    val courses = cdi.getCourses(0, 10, null, vs);
    courses should not be (null);
    courses.size should equal (3);

    /* Get all courses in the institute which belong to program-1 */
    val filter1 = new VenusFilterImpl("program", pgm1, "equals");
    val list1 = new ArrayList[VenusFilterImpl];
    list1.add(filter1);
    val options = new HashMap[String, Object];
    options += "filterList" -> list1.underlying.asInstanceOf[Object];
    
    val courses1 = cdi.getCourses(0, 10, options.underlying, vs).asInstanceOf[java.util.ArrayList[CourseImpl]];
    courses1 should not be (null);
    courses1.size should equal (2);
    val cList = new ArrayList[CourseImpl](courses1);
    cList.foreach(s => s should (equal(c1) or equal(c2)));
    
    val count = cdi.getCoursesCount(options.underlying, vs);
    count should equal (2);
  }

  /* test getting only active courses */
  @Test def testGetOnlyActiveCourses() {
    val code: String = "testGetOnlyActiveCs-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val n1 = code + "-1";  val n2 = code + "-2"; val n3 = code + "-3";

    val p1 = getCourseOptionalParams(n1);
    val p2 = getCourseOptionalParams(n2);
    val p3 = getCourseOptionalParams(n3);
    
    /* use deleted as status for 3rd course */
    p3 += "status"-> Status.Deleted;
    val c3 = cdi.createUpdateCourse(n3, n3, testDept, testUsr, p3.underlying, vs);
    c3 should not be (null);

    /* use deleted as status for 1st course */
    p1 += "status"-> Status.Suspended;
    /* use pgm1 as pgm for first 2 courses */
    val c1 = cdi.createUpdateCourse(n1, n1, testDept, testUsr, p1.underlying, vs);
    c1 should not be (null);
    val c2 = cdi.createUpdateCourse(n2, n2, testDept, testUsr, p2.underlying, vs);
    c2 should not be (null);

    /* Get all courses in this institute first - should be 3 */
    val courses = cdi.getCourses(0, 10, null, vs);
    courses should not be (null);
    courses.size should equal (1);
    
    val count = cdi.getCoursesCount(null, vs);
    count should equal (1);
  }

    /* test getting only active courses */
  @Test def testGetNonActiveCourses() {
    val code: String = "testGetNonActiveCs-" + getRandomString();
    val inst = InstituteImplTest.createTestInstitute(code, vs).asInstanceOf[InstituteImpl];
    sess.save(inst);
    vs.setInstitute(inst);
    val n1 = code + "-1";  val n2 = code + "-2"; val n3 = code + "-3";

    val p1 = getCourseOptionalParams(n1);
    val p2 = getCourseOptionalParams(n2);
    val p3 = getCourseOptionalParams(n3);
    
    /* use deleted as status for 3rd course */
    p3 += "status"-> Status.Deleted;
    val c3 = cdi.createUpdateCourse(n3, n3, testDept, testUsr, p3.underlying, vs);
    c3 should not be (null);

    /* use deleted as status for 1st course */
    p1 += "status"-> Status.Suspended;
    /* use pgm1 as pgm for first 2 courses */
    val c1 = cdi.createUpdateCourse(n1, n1, testDept, testUsr, p1.underlying, vs);
    c1 should not be (null);
    val c2 = cdi.createUpdateCourse(n2, n2, testDept, testUsr, p2.underlying, vs);
    c2 should not be (null);

    val list = createFilterList(("status", Status.Active, "notEquals"));
    val options = new HashMap[String, Object];
    options += "filterList" -> list.underlying.asInstanceOf[Object];
    
    /* Get all non active courses in this institute - should be 2 */
    val courses = cdi.getCourses(0, 10, options.underlying, vs);
    courses should not be (null);
    courses.size should equal (2);
    
    val count = cdi.getCoursesCount(options.underlying, vs);
    count should equal (2);
  }

  /* check the status of course after setting them */
  @Test def testSetStatus() {
    val code = "testSetStat-" + getRandomString();
    val course = createTestCourse(code, vs);
    course should not be (null);
    course.getStatus should equal (Status.Active.ordinal);
    
    /* set status and test */ 
    for (st <- Status.values) {
      cdi.setStatus(course, st, vs);
      course.getStatus should equal (st.ordinal);
    }
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
  
  def createTestCourse(code: String, venusSess: VenusSession): CourseImpl = {
    val optionalParams: HashMap[String, Object] = getCourseOptionalParams(code);
    val name: String = code + "-name";
    
    val course: CourseImpl = cdi.createUpdateCourse(code, name, testDept, testUsr, optionalParams.underlying, venusSess);
    Assert.assertNotNull("Created Course", course);
    
    /* test the mandatory parameters */
    verifyParamsArray(course, Array[(String, Object)](("code", code), ("name", name), ("department", testDept), ("instructor", testUsr)));
    
    /* test the optional parameters */
    verifyParamsMap(course, optionalParams);

    course;
  }
  
  
  def createFilterList(args: (String, Object, String)*): ArrayList[VenusFilterImpl] = {
    var arr: ArrayList[VenusFilterImpl] = null;
    if (args != null) {
      arr = new ArrayList[VenusFilterImpl];
      args.foreach(s => arr += new VenusFilterImpl(s._1, s._2, s._3));
    }
    arr;
  }
  
}
