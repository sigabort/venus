package com.venus.restapp.controller;

import net.sf.oval.exception.ConstraintsViolatedException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import org.springframework.test.web.ModelAndViewAssert._;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.WebDataBinder;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.dto.CourseDTO;
import com.venus.restapp.request.CourseRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.util.RestUtil;

import com.venus.model.impl.CourseImpl;
import com.venus.util.VenusSession;

/**
 * unit tests for {@link CourseController}
 * @author sigabort
 */
class CourseControllerTest extends ScalaHelperTest {

  var request: MockHttpServletRequest = _;
  var response: MockHttpServletResponse = _;
  var controller: CourseController = _;
  var vs: VenusSession = _;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link CourseController} class. 
   */
  @Before def setUp() {
    super.createSetup();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(classOf[CourseController]);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
    }
    val inst = InstituteControllerTest.createTestInstitute("courseCTest-" + getRandomString, null);
    vs = RestUtil.createVenusSession(inst);
    RestUtil.setVenusSession(request, vs);
  }

  /**
   * Clear the security context
   */
  @After def tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  /**
   * Try to create course with ROLE_ADMIN priviliges
   * @throws Exception
   */
  @Test def testCreateCourse() {
    val name: String = "testCreatCourse-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    /* set code parameter */
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name));

    /* fake admin login */
    fakeAdmin;
    
    /* create the course now*/
    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "courses/course");
    
    val resp: RestResponse = assertAndReturnModelAttributeOfType(mav, "response", classOf[RestResponse]);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError.toString.toBoolean);
    val dto : CourseDTO = resp.getEntry.asInstanceOf[CourseDTO];
    Assert.assertNotNull("The course dto", dto);
    
    verifyParamsArray(dto, Array[(String, Object)](("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name)));
  }

  
  @Test def testCreateCourseWithWrongCode() {
    val name: String = "testCreatCourseWrongCode-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("name", name), ("department", name), ("instructor", name), ("admin", name));

    val str: String = getRandomString(255+1);
    
    val codeParams = Array[String]("", null, str);    
    codeParams.foreach(s => {
      setReqParams(request, ("code", s));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });
  }
  
  @Test def testCreateCourseWithWrongName() {
    val name: String = "testCreatCourseWrongName-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("department", name), ("instructor", name), ("admin", name));
   
    val str: String = getRandomString(255+1);
    val nameParams = Array[String]("", null, str);    
    nameParams.foreach(s => {
      setReqParams(request, ("name", s));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });
  }
  
  @Test def testCreateCourseWithWrongDepartment() {
    val name: String = "testCreatCourseWrongDept-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("instructor", name), ("admin", name));
   
    /* check illegal arguments */
    val str: String = getRandomString(255+1);
    val deptParams = Array[String]("", null, str);    
    deptParams.foreach(s => {
      setReqParams(request, ("department", s));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });

    fakeAdmin;
    /* check with non-existing department */
    setReqParams(request, ("department", name + "-dummy"));
    /* create the course now*/
    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.NOT_FOUND.value());
  }

  @Test def testCreateCourseWithWrongInstructor() {
    val name: String = "testCreatCourseWrongInstructor-" + getRandomString;
    DepartmentControllerTest.createTestDepartment(name, vs);
    UserControllerTest.createTestUser(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("admin", name));
   
    /* check illegal arguments */
    val str: String = getRandomString(255+1);
    val instructorParams = Array[String]("", null, str);    
    instructorParams.foreach(s => {
      setReqParams(request, ("instructor", s));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });

    fakeAdmin;
    /* check with non-existing instructor */
    setReqParams(request, ("instructor", name + "-dummy"));
    /* create the course now*/
    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.NOT_FOUND.value());
  }


  @Test def testCreateCourseWithWrongAdmin() {
    val name: String = "testCreatCourseWrongAdmin-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name));
   
    /* check illegal arguments */
    val str: String = getRandomString(255+1);
    val adminParams = Array[String]("", null, str);    
    adminParams.foreach(s => {
      setReqParams(request, ("admin", s));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });

    fakeAdmin;
    /* check with non-existing admin */
    setReqParams(request, ("admin", name + "-dummy"));
    /* create the course now*/
    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.NOT_FOUND.value());
  }
  
  @Test def testCreateCourseWithWrongOptionalStringParams() {
    val name: String = "testCreatCourseWrongOptStrParams-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name));

    /* params array containing max length value for each field */
    val params = Array[(String, Int)](("description", 4096+1), ("photoUrl", 2048+1), ("content", 8192+1), ("prerequisites", 2048+1));
    
    /* set each parameter with corresponding strings of specified lengths */
    params.foreach(s => {
      val str: String = getRandomString(s._2);
      setReqParams(request, (s._1, str));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });
  }
  
  @Test def testCreateCourseWithWrongOptionalIntParams() {
    val name: String = "testCreatCourseWrongOptIntParams-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name));

    /* params array containing max length value for each field */
    val params = Array[(String, String)](("duration", "-11131"), ("duration", "afafafafa"));
    
    /* set each parameter with corresponding strings of specified lengths */
    params.foreach(s => {
      setReqParams(request, (s._1, s._2));
      /* create the course now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
    });
  }
  
  @Test def testCreateCourseWithoutAnySession() {
    val name: String = "testCreatCourseWOSession-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* remove session from the request */
    RestUtil.removeVenusSession(request);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name));

    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
  }

  @Test def testCreateCourseWithoutAnyInstituteInSession() {
    val name: String = "testCreatCourseWOInstInSession-" + getRandomString;
    UserControllerTest.createTestUser(name, vs);
    DepartmentControllerTest.createTestDepartment(name, vs);

    /* remove institute from session */
    vs = RestUtil.createVenusSession(null);
    RestUtil.setVenusSession(request, vs);
    
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("code", name), ("name", name), ("department", name), ("instructor", name), ("admin", name));

    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    checkForError(mav, "courses/create", HttpStatus.BAD_REQUEST.value());
  }

}
