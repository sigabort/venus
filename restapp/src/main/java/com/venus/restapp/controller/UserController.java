package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.UserDTO;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.util.VenusSession;

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
    model.addAttribute("user", new UserRequest());
    return "users/create";
  }


  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@ModelAttribute("user") UserRequest userRequest, BindingResult result, HttpServletRequest request) {
    RestUtil.validateRequest(userRequest, request, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("users/create", resp);
    }

    /* see if role(s) provided */
    String[] role = userRequest.getRole();
    UserRoleRequest urr = null;
    if (!ArrayUtils.isEmpty(role)) {
      validateUserRoleRequest(request, result, "user");
      /* if there is any error, build the response and send over */
      if (result.hasErrors()) {
        BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
        return RestUtil.buildVenusResponse("users/create", resp);
      }
      /* build UserRoleRequest for creating roles after creating user */
      urr = getUserRoleRequest(userRequest);
    }
    
    VenusSession vs = RestUtil.getVenusSession(request);
    User user = null;
    try {
      user = userService.createUpdateUser(userRequest, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("users/create", re, result);
    }
    
    /* if roles are also provided, try to create the roles */
    if (urr != null) {
      try {
        List urList = userRoleService.createUpdateUserRole(urr, vs);
      }
      catch (RestResponseException re) {
        return RestUtil.buildErrorResponse("users/create", re, result);
      }
    }
    RestResponse resp = ResponseBuilder.createResponse(user, new UserDTO());
    return RestUtil.buildVenusResponse("users/user", resp);
  }
  
  @RequestMapping(value="{username}", method=RequestMethod.GET)
  public ModelAndView getUser(@PathVariable String username, BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("users/user", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    User user = null;

    try {
      user = userService.getUser(username, request, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("users/user", re, result);
    }
    /* user not found? throw 404 */
    if (user == null) {
      RestResponseException re = new RestResponseException(null, HttpStatus.NOT_FOUND, "User with name: " + username + ", not found");
      return RestUtil.buildErrorResponse("users/user", re, result);
    }

    RestResponse resp = ResponseBuilder.createResponse(user, new UserDTO());
    return RestUtil.buildVenusResponse("users/user", resp);
  }

  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getUsers(BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("users/home", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    List users = null;
    Integer totalCount = null;

    try {
      users = userService.getUsers(request, vs);
      /* get the total users count */
      totalCount = userService.getUsersCount(request, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("users/home", re, result);
    }

    /* populate the response object */
    RestResponse resp = ResponseBuilder.createResponse(users, totalCount, new UserDTO());
    return RestUtil.buildVenusResponse("users/home", resp);
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
