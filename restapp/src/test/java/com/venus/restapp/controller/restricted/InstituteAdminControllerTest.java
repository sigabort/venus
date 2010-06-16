package com.venus.restapp.controller.restricted;

import net.sf.oval.Validator;
import net.sf.oval.integration.spring.SpringValidator;

import org.junit.Assert;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import static org.springframework.test.web.ModelAndViewAssert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.MutablePropertyValues;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import com.venus.restapp.response.RestResponse;
import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.util.RestUtil;

import com.venus.util.VenusSession;
import com.venus.model.Institute;

import com.venus.restapp.controller.AbstractControllerTest;

/**
 * unit tests for {@link InstituteAdminController}
 * @author sigabort
 */
public class InstituteAdminControllerTest extends AbstractControllerTest {

  private static MockHttpServletRequest request;
  private static MockHttpServletResponse response;
  private static InstituteAdminController controller;
  private static VenusSession vs;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link InstituteAdminController} class. 
   */
  @Before
  public void setUp() {
    super.setUp();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(InstituteAdminController.class);
    }
    vs = RestUtil.createVenusSession(null);
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
   * Try to create parent institute
   * @throws Exception
   */
  @Test
  public void testCreateParentInstitute() throws Exception {
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/restricted/institutes/create");

    String name = "tCParentInstitute-" + getRandomString();
    /* create new request object */
    request.setParameter("name", name);

    /*
     * XXX: using HandlerAdapter.handle() for POST /createAdminInstitute requests is not working
     * The exception is regarding the @RequestMapping. 
     * The problem is related to : 
     * http://stackoverflow.com/questions/1401128/how-to-unit-test-a-spring-mvc-controller-using-pathvariable
     * So, using workaround:
     * 
     * calling the method directly using binder to validate the request
     */
    final InstituteRequest ur = new InstituteRequest();
    final WebDataBinder binder = new WebDataBinder(ur, "request");
    binder.bind(new MutablePropertyValues(request.getParameterMap()));

    final ModelAndView mav = controller.createParentInstitute(ur, binder.getBindingResult(), request);

    //final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "institutes/institute");
    final RestResponse resp = assertAndReturnModelAttributeOfType(mav, "response", RestResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
  }

  
  /**
   * Create test institute
   */
  public static Institute createTestInstitute(String name, VenusSession vs) {
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(InstituteAdminController.class);
      Assert.assertTrue("Handler class is not supported for invoking methods", handlerAdapter.supports(controller));
      
    }
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/restricted/institutes/create");

    String code = name + "-code";
    request.setParameter("name", name);
    request.setParameter("code", code);
    request.setParameter("displayName", name + "-displayName");    
    request.setParameter("description", name + "-description");
   
    final InstituteRequest ur = new InstituteRequest();
    final WebDataBinder binder = new WebDataBinder(ur, "request");
    binder.bind(new MutablePropertyValues(request.getParameterMap()));

    final ModelAndView mav = controller.createParentInstitute(ur, binder.getBindingResult(), request);
    
    //final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "institutes/institute");
    final RestResponse resp = assertAndReturnModelAttributeOfType(mav, "response", RestResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());

    try {
      return controller.getInstituteService().getInstitute(name, null);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
  }

 
}
