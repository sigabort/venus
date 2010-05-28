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

import static org.springframework.test.web.ModelAndViewAssert.*;
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

import com.venus.restapp.response.DepartmentResponse;
import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;

import com.venus.restapp.util.RestUtil;

import com.venus.util.VenusSession;
import com.venus.model.Institute;


/**
 * unit tests for {@link DepartmentController}
 * @author sigabort
 */
public class DepartmentControllerTest extends AbstractControllerTest {

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private static DepartmentController controller;
  private static VenusSession vs;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link DepartmentController} class. 
   */
  @Before
  public void setUp() {
    super.setUp();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(DepartmentController.class);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
    }
    Institute inst = InstituteControllerTest.createTestInstitute("userRoleCTest-" + getRandomString(), null);
    vs = RestUtil.createVenusSession(inst);
    RestUtil.setVenusSession(request, vs);

  }
  
  /**
   * Clear the security context
   */
  @After
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  /** 
   * Test getting departments home page
   * @throws Exception
   */
  @Test
  public void testGet() throws Exception {
    request.setMethod(HttpMethod.GET.toString());
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "departments/home");
    final DepartmentResponse dr = assertAndReturnModelAttributeOfType(mav, "response", DepartmentResponse.class);
    Assert.assertNotNull("Didn't get the response", dr);
    Assert.assertFalse("The error", dr.getError());
  }

  /** 
   * Test getting department which is not existing
   * @throws Exception
   */
  @Test
  public void testGetNonExistingDepartment() throws Exception {
    request.setMethod(HttpMethod.GET.toString());
    String name = "tGNED-" + getRandomString();

    /*
     * XXX: using HandlerAdapter.handle() for GET /department requests is not working
     * The exception is regarding the @PathVariable. 
     * The problem is related to : 
     * http://stackoverflow.com/questions/1401128/how-to-unit-test-a-spring-mvc-controller-using-pathvariable
     * So, using workaround:
     * 
     * calling the method directly using binder to validate the request
     */
    final BaseRequest br = new BaseRequest();
    final WebDataBinder binder = new WebDataBinder(br, "request");
    binder.bind(new MutablePropertyValues(request.getParameterMap()));
    
    final ModelAndView mav = controller.getDepartment(name, br, binder.getBindingResult(), request);
    
    //final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "departments/department");
    final BaseResponse resp = assertAndReturnModelAttributeOfType(mav, "response", BaseResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertTrue("The error", resp.getError());
    Assert.assertEquals("The error code", new Integer(404), (Integer)resp.getHttpErrorCode());
  }
  
  /**
   * Test creating department with out logged in as admin
   * @throws Exception
   */
  @Test
  public void testCreateDepartmentAsNonAdmin() throws Exception {
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("name", getRandomString());
    request.setParameter("code", getRandomString());
    
    /* try to create department with out any credentials */
    try {
      final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    }
    catch (AuthenticationCredentialsNotFoundException acnf) {
      //Test passed
      return;
    }
    Assert.fail();
  }
  
  /**
   * Try to create department with ROLE_ADMIN priviliges
   * @throws Exception
   */
  @Test
  public void testCreateDepartmentWithCredentials() throws Exception {
    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    String name = "tCDWC-" + getRandomString();
    String code = name + "-code";
    /* create new request object */
    request.setParameter("name", name);
    request.setParameter("code", code);
   
    /* create/update the department now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "departments/department");
    final DepartmentResponse dr = assertAndReturnModelAttributeOfType(mav, "response", DepartmentResponse.class);
    Assert.assertNotNull("Didn't get the response", dr);
    Assert.assertFalse("The error", dr.getError());
  }
  
  /**
   * Test creating department with logged in as normal user
   * @throws Exception
   */
  @Test
  public void testCreateDepartmentAsNormalUser() throws Exception {
    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("name", getRandomString());
    request.setParameter("code", getRandomString());
    
    /* try to create department with out any credentials */
    try {
      final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    }
    catch (AccessDeniedException ade) {
      //Test passed
      return;
    }
    Assert.fail();
  }
  
}
