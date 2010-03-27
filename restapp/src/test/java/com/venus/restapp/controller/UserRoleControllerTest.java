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

import com.venus.restapp.response.UserRoleResponse;
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.response.UserResponse;
import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;


/**
 * unit tests for {@link UserRoleController}
 * @author sigabort
 */
public class UserRoleControllerTest extends AbstractControllerTest {

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private static UserRoleController controller;
  private static UserController userController;  

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
    createTestUser(username);
    
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
    assertViewName(mav, "userroles/userRole");
    final UserRoleResponse resp = assertAndReturnModelAttributeOfType(mav, "response", UserRoleResponse.class);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("The error", resp.getError());
    Assert.assertEquals("The error code", (int)200, (int)resp.getHttpErrorCode());
    Assert.assertNotNull("The user object of the response", resp.getEntries());
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
    assertViewName(mav, "userroles/createUserRole");
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
   * Creates a test user with given name as username.
   * IMP: Before calling this function, Setup() method should be called
   * to make sure the controller bean is created.
   * @param name    The username
   */
  public void createTestUser(String name) throws Exception {
    /* Login in as user who has role 'ROLE_ADMIN' */
    Authentication authRequest = new UsernamePasswordAuthenticationToken("ignored", 
        "ignored", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    SecurityContextHolder.getContext().setAuthentication(authRequest);
    
    /* should be POST method, with uri : /create */
    request.setMethod(HttpMethod.POST.toString());
    request.setRequestURI("/create");

    /* create new request object */
    request.setParameter("username", name);
   
    /* create/update the user now*/
    final ModelAndView mav = handlerAdapter.handle(request, response, userController);
    assertViewName(mav, "users/user");
    final UserResponse ur = assertAndReturnModelAttributeOfType(mav, "response", UserResponse.class);
    Assert.assertNotNull("Didn't get the response", ur);
    Assert.assertFalse("The error", ur.getError());
    SecurityContextHolder.clearContext();
  }
  
  
}
