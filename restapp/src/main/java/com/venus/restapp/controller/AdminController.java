package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.UserService;
import com.venus.restapp.service.UserRoleService;
import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.validator.UserRoleValidator;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.UserResponse;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * Controller class for handling all users related requests.
 * Handles REST requests: create/update/delete/get
 * 
 */
@Controller("adminController")
@RequestMapping(value="/admin")
public class AdminController {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRoleService userRoleService;
  
  private static final Logger log = Logger.getLogger(AdminController.class);

  /**
   * Create/Update the admin User
   * @param userRequest        The {@link UserRequest} object containing all parameters
   * @param result             The {@link BindingResult} object containing the errors if there
   *                           are any errors found while validating the request object
   * @return the {@link ModelAndView} object containing response of creation/updation of user.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="createAdminUser", method=RequestMethod.POST)
  public ModelAndView createAdminUser(@Valid UserRequest userRequest, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("users/createUser", "response", re.getResponse());
    }
    
    log.info("Adding/Updating user" + userRequest.getUsername());
    Object user = null;
    try {
      user = userService.createUpdateAdminUser(userRequest, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      log.error("Can't create/update user : " + userRequest.getUsername() + ", reason: " + re.getMessage());
      return new ModelAndView("users/createUser", "response", re.getResponse());
    }
    
    /* create user role request for admin user */
    UserRoleRequest urr = getAdminUserRoleRequest(userRequest);
    
    /* if roles are also provided, try to create the roles */
    if (urr != null) {
      try {
        Object ur = userRoleService.createUpdateAdminUserRole(urr, RestUtil.getVenusSession(req));
      }
      catch (ResponseException re) {
        log.error("Can't create/update admin role for user: " + userRequest.getUsername());
        return new ModelAndView("users/createUser", "response", re.getResponse());
      }
    }
    UserResponse resp = UserResponse.populateUser(user);
    return new ModelAndView("users/user", "response", resp);
  }
  

  private UserRoleRequest getAdminUserRoleRequest(UserRequest req) {
    String[] roles = new String[] {"ADMIN"};
    UserRoleRequest urr = new UserRoleRequest(req.getUsername(), roles);
    return urr;
  }
    
}
