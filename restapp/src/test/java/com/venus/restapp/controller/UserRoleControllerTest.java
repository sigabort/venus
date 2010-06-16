package com.venus.restapp.controller;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
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

import com.venus.restapp.response.RestResponse;
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.util.RestUtil;

import com.venus.util.VenusSession;
import com.venus.model.Institute;

/**
 * unit tests for {@link UserRoleController}
 * @author sigabort
 */
public class UserRoleControllerTest extends AbstractControllerTest {

  private static MockHttpServletRequest request;
  private static MockHttpServletResponse response;
  private static UserRoleController controller;
  private static UserController userController;
  private static VenusSession vs;

  /**
   * Create the setup for each request.
   * Calls super() to make sure the appContext is created before creating mock objects.
   * After getting appContext, creates mock request, mock response objects and gets the bean for
   * {@link UserRoleController} class. 
   */
  @Before
  public void setUp() {
    super.setUp();
    /* create mock requests, responses - different for each test */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    if (controller == null) {
      // Get the controller from the context
      controller = appContext.getBean(UserRoleController.class);
    }
    if (userController == null) {
      // Get the controller from the context
      userController = appContext.getBean(UserController.class);
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
   * Try to create user, and add role for that user
   * @throws Exception
   */
  @Test
  public void testCreateUserRole() throws Exception {
    /* Create the user first */
    String username = "tCreateUR-" + getRandomString();
    UserControllerTest.createTestUser(username, vs);
    
    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("username", username);
    request.setParameter("role", new String[] {"principal", "admin"});
   
    /* create/update the user now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "userRoles/home");
    final RestResponse resp = assertAndReturnModelAttributeOfType(mav, "response", RestResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
    Assert.assertEquals("The error code", (int)200, (int)resp.getHttpErrorCode());
    List entries = resp.getEntries();
    Assert.assertNotNull("The user roles", entries);    
    Assert.assertEquals("The number of roles for user", 2, entries.size());
  }

  /**
   * Try to create user, and add invalid role(s) for that user
   * @throws Exception
   */
  @Test
  public void testCreateUserAndInvalidUserRole() throws Exception {
    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    String name = "tCUWC-" + getRandomString();
    /* create new request object */
    request.setParameter("username", name);
    request.setParameter("role", new String[] {"xyz", "dude"});
   
    /* create/update the user now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "userRoles/create");
    final BaseResponse br = assertAndReturnModelAttributeOfType(mav, "response", BaseResponse.class);
    Assert.assertNotNull("Didn't get the response", br);
    Assert.assertTrue("The error", br.getError());
    Assert.assertEquals("The error code", (int)400, (int)br.getHttpErrorCode());    
  }

  /**
   * Try to create user, add roles with out departments which require
   * department as mandatory parameter
   * @throws Exception
   */
  @Test
  public void testCreateUserRolesWithOutMandatoryDept() throws Exception {
    /* Create the user first */
    String name = "tCURWOMD-" + getRandomString();
    UserControllerTest.createTestUser(name, vs);

    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("username", name);
    request.setParameter("role", "Student");
   
    /* create/update the user now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "userRoles/create");
    final BaseResponse br = assertAndReturnModelAttributeOfType(mav, "response", BaseResponse.class);
    Assert.assertNotNull("Didn't get the response", br);
    Assert.assertTrue("The error", br.getError());
    Assert.assertEquals("The error code", (int)400, (int)br.getHttpErrorCode());    
  }
  
  /**
   * Test creating user with logged in as normal user
   * @throws Exception
   */
  @Test
  public void testCreateUserRoleAsNormalUser() throws Exception {
    /* Login in as user who has role 'ROLE_USER' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("role", "principal");
    request.setParameter("username", getRandomString());    
    
    /* try to create user with out any credentials */
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
   * Try to get user roles
   * @throws Exception
   */
  @Test
  public void testGetUserRoles() throws Exception {
    /* Create the user first */
    String name = "tGetURs-" + getRandomString();
    UserControllerTest.createTestUser(name, vs);

    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("username", name);
    request.setParameter("role", new String[] {"Admin", "staff"});
   
    /* create/update the user now*/
    ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "userRoles/home");
    RestResponse resp = assertAndReturnModelAttributeOfType(mav, "response", RestResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
    Assert.assertEquals("The error code", (int)200, (int)resp.getHttpErrorCode());
    List entries = resp.getEntries();
    Assert.assertNotNull("The user roles", entries);    
    Assert.assertEquals("The number of roles for user", 2, entries.size());
    
    /* get user roles now */
    /*
     * XXX: using HandlerAdapter.handle() for GET /username requests is not working
     * The exception is regarding the @PathVariable. 
     * The problem is related to : 
     * http://stackoverflow.com/questions/1401128/how-to-unit-test-a-spring-mvc-controller-using-pathvariable
     * So, using workaround:
     * 
     * calling the method directly using binder to validate the request
     */
    BaseRequest br = new BaseRequest();
    WebDataBinder binder = new WebDataBinder(br, "request");
    binder.bind(new MutablePropertyValues(request.getParameterMap()));

    mav = controller.getUserRoles(name, br, binder.getBindingResult(), request);
    resp = assertAndReturnModelAttributeOfType(mav, "response", RestResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
    Assert.assertEquals("The error code", (int)200, (int)resp.getHttpErrorCode());
    entries = resp.getEntries();
    Assert.assertNotNull("The user roles", entries);    
    Assert.assertEquals("The number of roles for user", 2, entries.size());
  }

  
  
}
