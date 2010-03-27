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

import com.venus.restapp.service.UserRoleService;
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.validator.UserRoleValidator;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.UserRoleResponse;
import com.venus.restapp.response.error.ResponseException;

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
    model.addAttribute("userRoleRequest", new UserRoleRequest());
    return "userroles/createUserRole";
  }


  /**
   * Create/Update the User Role(s)
   * @param userRoleRequest        The {@link UserRoleRequest} object containing all parameters
   * @param result                 The {@link BindingResult} object containing the errors if there
   *                            are any errors found while validating the request object
   * @return the {@link ModelAndView} object containing response of creation/updation of user.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid UserRoleRequest userRoleRequest, BindingResult result, HttpServletRequest request) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("userroles/createUserRole", "response", re.getResponse());
    }
    
    UserController.validateUserRoleRequest(request, result, "userRoleRequest");
    if (result.hasErrors()) {
      log.error("--------Role has some error--------");
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("userroles/createUserRole", "response", re.getResponse());
    }
    
    List roles = null;
    try {
      roles = userRoleService.createUpdateUserRole(userRoleRequest);
    }
    catch (ResponseException re) {
      log.error("Can't create/update user roles for user: " + userRoleRequest.getUsername());
      return new ModelAndView("userroles/createUserRole", "response", re.getResponse());
    }
    if (roles != null) {
      if (roles.size() == 1) {
        UserRoleResponse resp = UserRoleResponse.populateUserRole(roles.get(0));
        return new ModelAndView("userroles/userRole", "response", resp);        
      }
      else {
        UserRoleResponse resp = UserRoleResponse.populateUserRoles(roles, roles.size());
        return new ModelAndView("userroles/userRole", "response", resp);
      }
    }
    log.error("--------Role creation has failed or didnt create--------");
    ResponseException re = new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, "Not able to create user roles", null, null);
    return new ModelAndView("userroles/createUserRole", "response", re.getResponse());
  }
  
}
