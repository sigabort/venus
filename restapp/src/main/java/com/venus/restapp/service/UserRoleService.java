package com.venus.restapp.service;

import java.util.List;

import com.venus.model.UserRole;

import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service interface for UserRoles. The classes implementing this class should talk to
 * operations layer and call appropriate operations.
 * 
 * @author sigabort
 *
 */
public interface UserRoleService {
  /**
   * Create/Update the {@link UserRole}(s). Only Admin can call this operation
   * 
   * @param request      The {@link UserRoleRequest request} containing the 
   *                     details of user, role(s) and other parameters
   * @return             The corresponding list of {@link UserRole} objects if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract List<UserRole> createUpdateUserRole(UserRoleRequest request) throws ResponseException;

  /**
   * Get the userRole. This operation can be called by any
   * @param userRolename     The userRole name
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The corresponding {@link UserRole} object if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  //public abstract UserRole getUserRole(String userRolename, BaseRequest request) throws ResponseException;

  /**
   * Get the userRoles in an institute. This operation can be called by any
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link UserRole}s if found, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  //public abstract List<UserRole> getUserRoles(BaseRequest request) throws ResponseException;
}