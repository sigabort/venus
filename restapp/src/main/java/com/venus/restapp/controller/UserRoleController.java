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
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.validator.UserRoleValidator;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.UserRoleDTO;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.util.VenusSession;

import org.apache.log4j.Logger;

/**
 * Controller class for handling all user roles related requests.
 * Handles REST requests: create/update/delete/get
 * 
 */
@Controller("userRoleController")
@RequestMapping(value="/userroles")
public class UserRoleController {
  @Autowired
  private UserRoleService userRoleService;
  @Autowired
  private UserService userService;  
  
  private static final Logger log = Logger.getLogger(UserRoleController.class);

  /**
   * Get the Model object for creating the user role(s)
   * This is mostly used by the UI clients when they want to create a user role
   * @param model        The model object for request
   * @return             The url location for view resolver to send the
   *                     proper view to the client with model object(UserRoleRequest) added to request
   */
  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("userRole", new UserRoleRequest());
    return "userroles/create";
  }


  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@ModelAttribute("userRole") UserRoleRequest userRoleRequest, BindingResult result, HttpServletRequest request) {
    RestUtil.validateRequest(userRoleRequest, request, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("userRoles/create", resp);
    }
    
    /* we need to validate the user roles separately as there may be invalid roles
     * coming in. Do that, and send BAD_REQUEST if the roles are not proper
     */
    UserController.validateUserRoleRequest(request, result, "userRole");
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("userRoles/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(request);
    
    /* add/update the user roles now */
    List roles = null;
    try {
      roles = userRoleService.createUpdateUserRole(userRoleRequest, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("userRoles/create", re, result);
    }
    
    /* populate the response object */
    RestResponse resp = ResponseBuilder.createResponse(roles, null, new UserRoleDTO());
    return RestUtil.buildVenusResponse("userRoles/home", resp);
  }
  
  
  @RequestMapping(value="{username}", method=RequestMethod.GET)
  public ModelAndView getUserRoles(@PathVariable String username, BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("userRoles/home", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    List users = null;
    Integer totalCount = null;

    User user = null;
    try {
      user = userService.getUser(username, request, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("userRoles/home", re, result);
    }
    /* user not found? throw 404 */
    if (user == null) {
      RestResponseException re = new RestResponseException(null, HttpStatus.NOT_FOUND, "User with name: " + username + ", not found");
      return RestUtil.buildErrorResponse("userRoles/home", re, result);
    }
    log.info("Got user: " + username);
    
    List roles = null;
    
    try {
      roles = userRoleService.getUserRoles(user, request, vs);
    }
    catch (RestResponseException re) {
      RestUtil.buildErrorResponse("userRoles/home", re, result);
    }
    log.info("Got roles for user: " + username);

    /* populate the response object */
    RestResponse resp = ResponseBuilder.createResponse(roles, null, new UserRoleDTO());
    return RestUtil.buildVenusResponse("userRoles/home", resp);
  }

  
}
