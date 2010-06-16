package com.venus.restapp.service;

import java.util.List;

import com.venus.model.UserRole;
import com.venus.model.User;
import com.venus.util.VenusSession;

import com.venus.restapp.request.UserRoleRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.RestResponseException;

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
   * @throws RestResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract List<UserRole> createUpdateUserRole(UserRoleRequest request, VenusSession vs) throws RestResponseException;

  /**
   * Get the user roles for an user in an institute. This operation can be called by any
   * 
   * @param user         The {@link User user} object for whom we need to fetch the roles
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link UserRole}s if found, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract List<UserRole> getUserRoles(User user, BaseRequest request, VenusSession vs) throws RestResponseException;

  /**
   * Special API to create the role for Admin. This shouldn't be used by others.
   * Only APIs in 'UserAdminController' should use this operation
   * 
   * @param request      The {@link UserRoleRequest request} containing the 
   *                     details of user, role(s) and other parameters
   * @return             The corresponding list of {@link UserRole} objects if 
   *                     created/updated with out any errors, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract List<UserRole> createUpdateAdminUserRole(UserRoleRequest request, VenusSession vs) throws RestResponseException;

}