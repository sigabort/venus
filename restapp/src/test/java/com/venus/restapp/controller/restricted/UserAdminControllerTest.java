package com.venus.restapp.controller.restricted;

import org.junit.Assert;


import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

import com.venus.restapp.response.UserResponse;
import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.util.RestUtil;

import com.venus.util.VenusSession;
import com.venus.model.Institute;

import com.venus.restapp.controller.AbstractControllerTest;
import com.venus.restapp.controller.InstituteControllerTest;

/**
 * unit tests for {@link UserAdminController}
 * @author sigabort
 */
public class UserAdminControllerTest extends AbstractControllerTest {

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private static UserAdminController controller;
  private static VenusSession vs;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link UserAdminController} class. 
   */
  @Before
  public void setUp() {
    super.setUp();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(UserAdminController.class);
    }
    Institute inst = InstituteControllerTest.createTestInstitute("usrAdminCTest-" + getRandomString());
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
   * Try to create user with admin as role
   * @throws Exception
   */
  @Test
  public void testCreateAdminUser() throws Exception {
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/restricted/users/create");

    String name = "tCAdminUser-" + getRandomString();
    /* create new request object */
    request.setParameter("username", name);

    /*
     * XXX: using HandlerAdapter.handle() for POST /createAdminUser requests is not working
     * The exception is regarding the @RequestMapping. 
     * The problem is related to : 
     * http://stackoverflow.com/questions/1401128/how-to-unit-test-a-spring-mvc-controller-using-pathvariable
     * So, using workaround:
     * 
     * calling the method directly using binder to validate the request
     */
    final UserRequest ur = new UserRequest();
    final WebDataBinder binder = new WebDataBinder(ur, "request");
    binder.bind(new MutablePropertyValues(request.getParameterMap()));

    final ModelAndView mav = controller.createAdminUser(ur, binder.getBindingResult(), request);

    //final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "users/user");
    final UserResponse resp = assertAndReturnModelAttributeOfType(mav, "response", UserResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
  }
 
}
