package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.UserService;
import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.UserResponse;
import com.venus.restapp.response.error.ResponseException;

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
   * @param userRequest   The UserRequest object containing all parameters
   * @param result             The BindingResult object containing the errors if there
   *                            are any errors found while validating the request object
   * @return the ModelAndView object containing response of creation/updation of user.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid UserRequest userRequest, BindingResult result) {
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
      user = userService.createUpdateUser(userRequest);
    }
    catch (ResponseException re) {
      log.error("Can't create/update user : " + userRequest.getUsername() + ", reason: " + re.getMessage());
      return new ModelAndView("users/createUser", "response", re.getResponse());
    }
    UserResponse resp = UserResponse.populateUser(user);
    return new ModelAndView("users/user", "response", resp);
  }
  
  /**
   * Send a particualr user details with given name in the institute.
   * @param name     The name of the user
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting user information.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="{username}", method=RequestMethod.GET)
  public ModelAndView getUser(@PathVariable String username, @Valid BaseRequest request, BindingResult result) {
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
      user = userService.getUser(username, request);
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
  public ModelAndView getUsers(@Valid BaseRequest request, BindingResult result) {
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
      users = userService.getUsers(request);
      /* get the total users count */
      totalCount = userService.getUsersCount(request);
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

}
