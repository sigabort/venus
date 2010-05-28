package com.venus.restapp.service;

import java.util.List;

import com.venus.model.User;
import com.venus.util.VenusSession;

import com.venus.restapp.request.UserRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service interface for Users. The classes implementing this class should talk to
 * operations layer and call appropriate operations.
 * 
 * @author sigabort
 *
 */
public interface UserService {
  /**
   * Create/Update the user. Only Admin can call this operation
   * 
   * @param request      The {@link UserRequest request} containing the 
   *                     details of user and other parameters
   * @return             The corresponding {@link User} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract User createUpdateUser(UserRequest request, VenusSession vs) throws ResponseException;

  /**
   * Get the user. This operation can be called by any
   * @param username     The user name
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The corresponding {@link User} object if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public abstract User getUser(String username, BaseRequest request, VenusSession vs) throws ResponseException;

  /**
   * Get the users in an institute. This operation can be called by any
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link User}s if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public abstract List<User> getUsers(BaseRequest request, VenusSession vs) throws ResponseException;

  /**
   * Get the users count in an institute. This operation can be called by any.
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The count of users in an institute
   *                     (with filtering, if any filters provided in request)
   * @throws ResponseException
   */
  public abstract Integer getUsersCount(BaseRequest request, VenusSession vs) throws ResponseException;
  
  /**
   * Special API for the Admin Layer to create the admin user. This shouldn't be used by others
   * Only APIs in 'AdminController' should use this operation
   * 
   * @param request      The {@link UserRequest request} containing the 
   *                     details of user and other parameters
   * @return             The corresponding {@link User} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public abstract User createUpdateAdminUser(UserRequest request, VenusSession vs) throws ResponseException;
  
}