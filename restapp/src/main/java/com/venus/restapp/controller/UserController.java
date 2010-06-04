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
@Controller("userController")
@RequestMapping(value="/users")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRoleService userRoleService;
  
  private static final Logger log = Logger.getLogger(UserController.class);

  /**
   * Get the Model object for creating the user
   * This is mostly used by the UI clients when they want to create a user
   * @param model        The model object for request
   * @return             The url location for view resolver to send the
   *                     proper view to the client with model object(UserRequest) added to request
   */
  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("userRequest", new UserRequest());
    return "users/createUser";
  }


  /**
   * Create/Update the User
   * @param userRequest        The {@link UserRequest} object containing all parameters
   * @param result             The {@link BindingResult} object containing the errors if there
   *                           are any errors found while validating the request object
   * @param request            The {@link HttpServletRequest} object corresponding to this request
   * @return the {@link ModelAndView} object containing response of creation/updation of user.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid UserRequest userRequest, BindingResult result, HttpServletRequest request) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      for (ObjectError err: result.getAllErrors()) {
        log.error("Adding/Updating user " + userRequest.getUsername() + ", err : " + err.toString());         
      }
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("users/createUser", "response", re.getResponse());
    }

    /* see if role(s) provided */
    String[] role = userRequest.getRole();
    UserRoleRequest urr = null;
    if (!ArrayUtils.isEmpty(role)) {
      validateUserRoleRequest(request, result, "userRequest");
      if (result.hasErrors()) {
        log.error("--------Role has some error--------");
        ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
        return new ModelAndView("users/createUser", "response", re.getResponse());
      }
      /* build UserRoleRequest for creating roles after creating user */
      urr = getUserRoleRequest(userRequest);
    }
    
    log.info("Adding/Updating user" + userRequest.getUsername());
    Object user = null;
    try {
      user = userService.createUpdateUser(userRequest, RestUtil.getVenusSession(request));
    }
    catch (ResponseException re) {
      log.error("Can't create/update user : " + userRequest.getUsername() + ", reason: " + re.getMessage());
      return new ModelAndView("users/createUser", "response", re.getResponse());
    }
    
    /* if roles are also provided, try to create the roles */
    if (urr != null) {
      try {
        Object ur = userRoleService.createUpdateUserRole(urr, RestUtil.getVenusSession(request));
      }
      catch (ResponseException re) {
        log.error("Can't create/update user role: " + role[0] + ", for user: " + userRequest.getUsername());
        return new ModelAndView("users/createUser", "response", re.getResponse());
      }
    }
    UserResponse resp = UserResponse.populateUser(user);
    return new ModelAndView("users/user", "response", resp);
  }
  
  /**
   * Send a particualr user details with given name in the institute.
   * @param username     The username of the user
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting user information.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="{username}", method=RequestMethod.GET)
  public ModelAndView getUser(@PathVariable String username, @Valid BaseRequest request, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("users/user", "response", re.getResponse());
    }

    log.info("Fetching user: " + username);
    Object user = null;
    try {
      user = userService.getUser(username, request, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      log.info("Error while finding User with name: " + username, re);
      return new ModelAndView("users/user", "response", re.getResponse());
    }
    /* user not found? throw 404 */
    if (user == null) {
      log.info("User with name: " + username + " is not found...");
      ResponseException re = new ResponseException(HttpStatus.NOT_FOUND, "User with name: " + username + ", not found", null, null);
      return new ModelAndView("users/user", "response", re.getResponse());
    }
    log.info("Got user: " + username);
    /* populate the object */
    UserResponse resp = UserResponse.populateUser(user);
    return new ModelAndView("users/user", "response", resp);
  }

  /**
   * Send all of the user results in the institute. This is the default request
   * for users page.
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting users.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getUsers(@Valid BaseRequest request, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("users/home", "response", re.getResponse());
    }

    log.info("Fetching all users....");

    List users = null;
    Integer totalCount = null;

    try {
      users = userService.getUsers(request, RestUtil.getVenusSession(req));
      /* get the total users count */
      totalCount = userService.getUsersCount(request, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      return new ModelAndView("users/home", "response", re.getResponse());
    }
    /* users not found? send empty response. So, the client can take care of
     * what to do next
     */
    if (users == null) {
      return new ModelAndView("users/home", "response", new BaseResponse());
    }
    log.info("Got users: " + users.size());
    
    /* populate the response object */
    UserResponse resp = UserResponse.populateUsers(users, totalCount);
    return new ModelAndView("users/home", "response", resp);
  }

  /**
   * Get the {@link UserRoleRequest} object from the {@ink UserRequest} object
   * @param req         The {@link UserRequest} object
   * @return            The {@link UserRoleRequest} object
   */
  private UserRoleRequest getUserRoleRequest(UserRequest req) {
    UserRoleRequest urr = new UserRoleRequest(req.getUsername(), req.getRole());
    urr.setDepartmentName(req.getDepartmentName());
    return urr;
  }
  
  /**
   * Validate the UserRole Request. We use custom validator {@link UserRoleValidator} to validate
   * the user role request. Validate here, and add the errors to the BindingResult object passed in
   * (if any)
   * @param request           The {@link HttpServletRequest} object for getting all the input values
   * @param result            The {@link BindingResult} object which can be used to add the errors
   *                          if found
   * @param requestAttr       The request attribute used for this request
   */
  public static void validateUserRoleRequest(HttpServletRequest request, BindingResult result, String requestAttr) {
    WebDataBinder binder = new WebDataBinder(new UserRoleRequest(), requestAttr);
    binder.setValidator(new UserRoleValidator());
    binder.bind(new MutablePropertyValues(request.getParameterMap()));
    binder.validate();
    BindingResult res = binder.getBindingResult();
    if (res.hasErrors()) {
      for (ObjectError err: res.getAllErrors()) {
        result.addError(err);
      }
    }
  }
  
}
