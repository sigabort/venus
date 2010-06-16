package com.venus.restapp.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.venus.model.UserRole;
import com.venus.model.User;
import com.venus.model.Department;
import com.venus.model.Role;
import com.venus.dal.UserRoleOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.UserRoleOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.service.UserRoleService;
import com.venus.restapp.service.DepartmentService;
import com.venus.restapp.service.UserService;
import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.BaseRequest;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;


/**
 * Service class for UserRoles. This class calls appropritate operations layer APIs
 * to do the actions. This layer reads the request parameters, and passes them
 * to operations layer as needed
 * 
 * @author sigabort
 *
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
  @Autowired
  private DepartmentService deptService;
  @Autowired
  private UserService userService;
  
  private UserRoleOperations uro = new UserRoleOperationsImpl();
  private static final Logger log = Logger.getLogger(UserRoleService.class);

  /**
   * Create/Update the {@link UserRole}(s).
   * 
   * @param request      The {@link UserRoleRequest request} containing the 
   *                     details of user, role(s) and other parameters
   * @return             The corresponding list of {@link UserRole} objects if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public List<UserRole> createUpdateUserRole(UserRoleRequest request, VenusSession vs) throws ResponseException {
    String username = request.getUsername();
    String[] roles = request.getRole();
    String deptName = request.getDepartmentName();
    List<UserRole> userRoles = null;
    Map<String, Object> params = null;

    /* see the department */
    if (!StringUtils.isEmpty(deptName)) {
      Department  department = null;
      try {
        department = deptService.getDepartment(deptName, new BaseRequest(), vs);
      }
      catch (RestResponseException rre) {
        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, rre.getMessage(), null, null);
      }
      if (department != null) {
        params = new HashMap<String, Object>();
        params.put("department", department);
      }
      else {
        String errStr = "Department with name: " + deptName + ", doesn't exist";
        log.error(errStr);
        throw new ResponseException(HttpStatus.NOT_FOUND, errStr, null, null);        
      }
    }
    /* see if the user is existing or not */
    User user = userService.getUser(username, new BaseRequest(), vs);
    if (user == null) {
      String errStr = "User with username: " + username + ", doesn't exist";
      log.error(errStr);
      throw new ResponseException(HttpStatus.NOT_FOUND, errStr, null, null);
    }
    if (roles != null) {
      log.info("i am going to create the user role now.....");
    }
    /* create all roles now */
    for (int idx = 0; idx < roles.length; idx++) {
      UserRole userRole = null;
      String role = StringUtils.stripToNull(roles[idx]);
      if (StringUtils.isEmpty(role)) {
        log.info("i am going to skip the user role: " + role.toUpperCase());
        continue;
      }
      try {
        log.info("i am going to create the user role: " + role.toUpperCase());
        userRole  = uro.createUpdateUserRole(user, Role.valueOf(role.toUpperCase()), params, vs);
      }
      catch (DataAccessException dae) {
        String errStr = "Error while creating/updating userRole with for user: " + username + ", role: " + role;
        log.error(errStr, dae);
        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
      }
      /* arguments are not proper */
      catch (IllegalArgumentException iae) {
        log.error(iae.getMessage());        
        throw new ResponseException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae, null);
      }
      if (userRole != null) {
        log.info("Created user role, with id: " + userRole.getID() + ", for user: " + username + ", role: " + role);
        /* if userRoles object is not created, create it */
        if (userRoles == null) {
          userRoles = new ArrayList<UserRole>(roles.length);
        }
        /* add newly creted object to the list */
        userRoles.add(userRole);
      }
    }
    return userRoles;
  }

  /**
   * Get the user roles for an user in an institute
   * 
   * @param user         The {@link User user} object for whom we need to fetch the roles
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link UserRole}s if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public List<UserRole> getUserRoles(User user, BaseRequest request, VenusSession vs) throws ResponseException {
    List<UserRole> userRoles = null;
    try{
      userRoles = uro.getUserRoles(user, null, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting user roles for user: " + user.getUsername() + ", in institute: " + 1;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return userRoles;
  }

  
  /**
   * Special API to create the role for Admin. This shouldn't be used by others
   * 
   * @param request      The {@link UserRoleRequest request} containing the 
   *                     details of user, role(s) and other parameters
   * @return             The corresponding list of {@link UserRole} objects if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public List<UserRole> createUpdateAdminUserRole(UserRoleRequest request, VenusSession vs) throws ResponseException {
    return createUpdateUserRole(request, vs);
  }

  
}
