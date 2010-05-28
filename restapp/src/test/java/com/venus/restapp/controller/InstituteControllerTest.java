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

import com.venus.restapp.response.InstituteResponse;
import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;

import com.venus.model.Institute;

/**
 * unit tests for {@link InstituteController}
 * @author sigabort
 */
public class InstituteControllerTest extends AbstractControllerTest {

  private static MockHttpServletRequest request;
  private static MockHttpServletResponse response;
  private static InstituteController controller;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link InstituteController} class. 
   */
  @Before
  public void setUp() {
    super.setUp();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(InstituteController.class);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
    }
  }
  
  /**
   * Clear the security context
   */
  @After
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  /** 
   * Test getting institutes home page
   * @throws Exception
   */
  @Test
  public void testGet() throws Exception {
    request.setMethod(HttpMethod.GET.toString());
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "institutes/home");
    final InstituteResponse dr = assertAndReturnModelAttributeOfType(mav, "response", InstituteResponse.class);
    Assert.assertNotNull("Didn't get the response", dr);
    Assert.assertFalse("The error", dr.getError());
  }

  /** 
   * Test getting institute which is not existing
   * @throws Exception
   */
  @Test
  public void testGetNonExistingInstitute() throws Exception {
    request.setMethod(HttpMethod.GET.toString());
    String name = "tGNED-" + getRandomString();

    /*
     * XXX: using HandlerAdapter.handle() for GET /institute requests is not working
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
    
    final ModelAndView mav = controller.getInstitute(name, br, binder.getBindingResult());
    
    //final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "institutes/institute");
    final BaseResponse resp = assertAndReturnModelAttributeOfType(mav, "response", BaseResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertTrue("The error", resp.getError());
    Assert.assertEquals("The error code", new Integer(404), (Integer)resp.getHttpErrorCode());
  }
  
  /**
   * Test creating institute with out logged in as admin
   * @throws Exception
   */
  @Test
  public void testCreateInstituteAsNonAdmin() throws Exception {
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("name", getRandomString());
    request.setParameter("code", getRandomString());
    
    /* try to create institute with out any credentials */
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
   * Try to create institute with ROLE_ADMIN priviliges
   * @throws Exception
   */
  @Test
  public void testCreateInstituteWithCredentials() throws Exception {
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
   
    /* create/update the institute now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "institutes/institute");
    final InstituteResponse dr = assertAndReturnModelAttributeOfType(mav, "response", InstituteResponse.class);
    Assert.assertNotNull("Didn't get the response", dr);
    Assert.assertFalse("The error", dr.getError());
  }
  
  /**
   * Test creating institute with logged in as normal user
   * @throws Exception
   */
  @Test
  public void testCreateInstituteAsNormalUser() throws Exception {
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
    
    /* try to create institute with out any credentials */
    try {
      final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    }
    catch (AccessDeniedException ade) {
      //Test passed
      return;
    }
    Assert.fail();
  }
  
  
  /**
   * Create test institute
   */
  public static Institute createTestInstitute(String name) {
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(InstituteController.class);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
    }

    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    /* create new request object */
    request.setRequestURI("/create");

    String code = name + "-code";
    request.setParameter("name", name);
    request.setParameter("code", code);
    request.setParameter("displayName", name + "-displayName");    
    request.setParameter("description", name + "-description");
   
    /* create/update the institute now*/
    try {
      final ModelAndView mav = handlerAdapter.handle(request, response, controller);
      assertViewName(mav, "institutes/institute");
      final InstituteResponse dr = assertAndReturnModelAttributeOfType(mav, "response", InstituteResponse.class);
      //after creating the institute, clear the ctx
      SecurityContextHolder.clearContext();
      Assert.assertNotNull("Didn't get the response", dr);
      Assert.assertFalse("The error", dr.getError());
    

      return controller.getInstituteService().getInstitute(name, null);
    }
    catch (Exception e) {
      return null;
    }
  }
  
}
