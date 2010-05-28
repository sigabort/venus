package com.venus.restapp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.User;
import com.venus.dal.UserOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.UserOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;


/**
 * Service class for Users. This class calls appropritate operations layer APIs
 * to do the actions. This layer reads the request parameters, and passes them
 * to operations layer as needed
 * 
 * @author sigabort
 *
 */
@Service
public class UserServiceImpl implements UserService {
  private UserOperations uo = new UserOperationsImpl();
  private static final Logger log = Logger.getLogger(UserService.class);

  /**
   * Create/Update the user.
   * 
   * @param req          The {@link UserRequest request} containing the 
   *                     details of user and other parameters
   * @return             The corresponding {@link User} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public User createUpdateUser(UserRequest req, VenusSession vs) throws ResponseException {
    String username = req.getUsername();
    User user = null;
    Map<String, Object> params = new HashMap<String, Object>();

    params.put("email", req.getEmail());
    params.put("userId", req.getUserId());
    params.put("password", req.getPassword());
    params.put("firstName", req.getFirstName());
    params.put("lastName", req.getLastName());
    params.put("gender", req.getGender());
    params.put("url", req.getUrl());
    params.put("phone", req.getPhone());
    params.put("address1", req.getAddress1());
    params.put("address2", req.getAddress2());
    params.put("city", req.getCity());
    params.put("country", req.getCountry());
    params.put("postalCode", req.getPostalCode());
    params.put("photoUrl", req.getPhotoUrl());
    params.put("birthDate", req.getBirthDate());
    params.put("joinDate", req.getJoinDate());
    params.put("created", req.getCreated());
    params.put("lastModified", req.getLastModified());
        
    try {
      user  = uo.createUpdateUser(username, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while creating/updating user with username: " + username;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    /* arguments are not proper */
    catch (IllegalArgumentException iae) {
      throw new ResponseException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae, null);
    }
    return user;
  }

  /**
   * Get the user
   * @param username         The user name
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The corresponding {@link User} object if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public User getUser(String username, BaseRequest request, VenusSession vs) throws ResponseException {
    User user = null;
    try {
      user = uo.findUserByUsername(username, null, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting user with username: " + username;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return user;
  }

  /**
   * Get the users
   * @param request        The request parameter containing the optional parameters
   * @return               The list of users
   * @throws ResponseException if there is any error
   */
  public List<User> getUsers(BaseRequest request, VenusSession vs) throws ResponseException {
    int offset = request.getStartIndex();
    int maxRet = request.getItemsPerPage();
    String sortBy = request.getSortBy();
    String sortOrder = request.getSortOrder();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();

    /* Time to parse the query parameters */
    Map<String, Object> params = null;
    /* if the sortBy option is set, pass it to the DAL layer to 
     * sort depends on the requested option
     */
    if (!StringUtils.isBlank(sortBy)) {
      if (params == null) {
        params = new HashMap<String, Object>();
      }
      params.put("sortBy", sortBy);
      /* check whether the order is asc/desc. Default is 'asc' */
      if (StringUtils.equals("descending", sortOrder)) {
        params.put("isAscending", Boolean.FALSE);
      }
      else {
        params.put("isAscending", Boolean.TRUE);
      }
    }
    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      if (params == null) {
        params = new HashMap<String, Object>();
      }
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the users now */
    List<User> users = null;
    try {
      users = uo.getUsers(offset, maxRet, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting users";
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return users;
  }

  /**
   * Get the users count in the institute
   * @param request        The request parameter containing the optional parameters
   * @return               The count of total users in the institute
   * @throws ResponseException if there is any error
   */
  public Integer getUsersCount(BaseRequest request, VenusSession vs) throws ResponseException {
   /* Time to parse the query parameters */
    Boolean onlyActive = request.getOnlyActive();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();
    
    Map<String, Object> params = new HashMap<String, Object>();
    /* needed only count of active users? */
    params.put("onlyActive", onlyActive);

    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the users now */
    Integer count = null;
    try {
      count = uo.getUsersCount(params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting users count";
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return count;
  }
  
  
  /**
   * Special API for the Admin Layer to create the admin user. This shouldn't be used by others
   * 
   * @param request      The {@link UserRequest request} containing the 
   *                     details of user and other parameters
   * @return             The corresponding {@link User} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public User createUpdateAdminUser(UserRequest request, VenusSession vs) throws ResponseException {
    return createUpdateUser(request, vs);
  }

}
