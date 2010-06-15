package com.venus.restapp.controller;

import org.junit.Assert;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

//import com.venus.restapp.response.CourseResponse;
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
    val inst = InstituteControllerTest.createTestInstitute("courseCTest-" + getRandomString(), null);
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
    val name: String = "testCreatCourse-" + getRandomString();
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
  }
  
}
