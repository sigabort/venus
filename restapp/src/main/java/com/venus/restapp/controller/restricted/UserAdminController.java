package com.venus.restapp.controller.restricted;

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
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;
import com.venus.restapp.response.dto.UserDTO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.util.VenusSession;

/**
 * Controller class for handling all admin users related requests.
 * Handles REST requests: create/update/delete/get
 * 
 * This should be only allowed to run from the localhost. Or infact, this code should not be
 * used in the deployment version at all. But, to run the tests, this can be included in the 
 * non-deployment versions.
 * 
 */
@Controller("userAdminController")
@RequestMapping(value="/restricted/users")
public class UserAdminController {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRoleService userRoleService;
  
  private static final Logger log = Logger.getLogger(UserAdminController.class);

  @RequestMapping(value="create", method=RequestMethod.POST)
  public ModelAndView createAdminUser(@ModelAttribute("user") UserRequest userRequest, BindingResult result, HttpServletRequest request) {
    RestUtil.validateRequest(userRequest, request, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("users/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(request);
    User user = null;
    try {
      user = userService.createUpdateAdminUser(userRequest, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("users/create", re, result);
    }

    /* create user role request for admin user */
    UserRoleRequest urr = getAdminUserRoleRequest(userRequest);
    /* if roles are also provided, try to create the roles */
    if (urr != null) {
      try {
        List urList = userRoleService.createUpdateAdminUserRole(urr, vs);
      }
      catch (RestResponseException re) {
        return RestUtil.buildErrorResponse("users/create", re, result);
      }
    }
    
    RestResponse resp = ResponseBuilder.createResponse(user, new UserDTO());
    return RestUtil.buildVenusResponse("users/user", resp);
  }
  

  private UserRoleRequest getAdminUserRoleRequest(UserRequest req) {
    String[] roles = new String[] {"ADMIN"};
    UserRoleRequest urr = new UserRoleRequest(req.getUsername(), roles);
    return urr;
  }
    
}
