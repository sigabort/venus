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
import com.venus.restapp.response.dto.DepartmentDTO;
import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.util.RestUtil;

import com.venus.model.Department;
import com.venus.util.VenusSession;

/**
 * unit tests for {@link DepartmentController}
 * @author sigabort
 */
class DepartmentsTest extends ScalaHelperTest {

  var request: MockHttpServletRequest = _;
  var response: MockHttpServletResponse = _;
  var controller: DepartmentController = _;
  var vs: VenusSession = _;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link DepartmentController} class. 
   */
  @Before def setUp() {
    super.createSetup();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(classOf[DepartmentController]);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
    }
    val inst = InstituteControllerTest.createTestInstitute("departmentCTest-" + getRandomString, null);
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
   * Try to create department with ROLE_ADMIN priviliges
   * @throws Exception
   */
  @Test def testCreateDepartment() {
    val name: String = "testCreatDepartment-" + getRandomString;
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    /* set code parameter */
    setReqParams(request, ("name", name));

    /* fake admin login */
    fakeAdmin;
    
    /* create the department now*/
    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "departments/department");
    
    val resp: RestResponse = assertAndReturnModelAttributeOfType(mav, "response", classOf[RestResponse]);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError.toString.toBoolean);
    val dto : DepartmentDTO = resp.getEntry.asInstanceOf[DepartmentDTO];
    Assert.assertNotNull("The department dto", dto);
    
    verifyParamsArray(dto, Array[(String, Object)](("name", name)));
  }

  
  @Test def testCreateDepartmentWithWrongName() {
    val name: String = "testCreatDepartmentWrongName-" + getRandomString;
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("name", name));

    val str: String = getRandomString(255+1);
    
    val codeParams = Array[String]("", null, str);    
    codeParams.foreach(s => {
      setReqParams(request, ("name", s));
      /* create the department now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "departments/create", HttpStatus.BAD_REQUEST.value());
    });
  }
  
  @Test def testCreateDepartmentWithWrongOptionalStringParams() {
    val name: String = "testCreatDepartmentWrongOptStrParams-" + getRandomString;
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("name", name));

    /* params array containing max length value for each field */
    val params = Array[(String, Int)](("code", 255+1), ("description", 4096+1), ("photoUrl", 2048+1), ("email", 2048+1));
    
    /* set each parameter with corresponding strings of specified lengths */
    params.foreach(s => {
      val str: String = getRandomString(s._2);
      setReqParams(request, (s._1, str));
      /* create the department now*/
      val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
      checkForError(mav, "departments/create", HttpStatus.BAD_REQUEST.value());
    });
  }
    
  @Test def testCreateDepartmentWithoutAnySession() {
    val name: String = "testCreatDepartmentWOSession-" + getRandomString;
    DepartmentControllerTest.createTestDepartment(name, vs);
    /* remove session from the request */
    RestUtil.removeVenusSession(request);
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("name", name));

    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    checkForError(mav, "departments/create", HttpStatus.BAD_REQUEST.value());
  }

  @Test def testCreateDepartmentWithoutAnyInstituteInSession() {
    val name: String = "testCreatDepartmentWOInstInSession-" + getRandomString;

    /* remove institute from session */
    vs = RestUtil.createVenusSession(null);
    RestUtil.setVenusSession(request, vs);
    
    /* should be POST method, with uri : /create */
    setReqMethodAndURI(request, HttpMethod.POST, "/create");
    setReqParams(request, ("name", name));

    val mav: ModelAndView = handlerAdapter.handle(request, response, controller);
    checkForError(mav, "departments/create", HttpStatus.BAD_REQUEST.value());
  }

}
