package com.venus.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.model.Role;
import com.venus.model.Status;
import com.venus.model.Department;
import com.venus.util.VenusSession;

/**
 * This class specifies the operations allowed on {@link UserRole user role}. The operations include
 * creation/updation/fetching/deletion of the user roles.
 * 
 * @author sigabort
 */
public interface UserRoleOperations {
  
  /**
   * Create or Update user role for a user
   * @param user             The {@link User user} object for which the roles need to be defined.
   * @param role             The {@link Role role} for a user
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>department({@link Department}):   The {@link Department department} user belongs to. If specified, the
   *                                role provided will be specific to this department. For some roles, the department
   *                                is mandatory. See {@link Role} for more details
   *   </li>
   *   <li>status({@link Status}): The status of the user role to be set</li>
   *   <li>created(Date): The created date of this object</li>
   *   <li>lastModified(Date): The last modified date of this object</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created/updated user role object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract UserRole createUpdateUserRole(User user, Role role, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;

  /**
   * Get the {@link UserRole user role} for a specific user and with a specific role
   * @param user                  The {@link User user} for whom we need to get the {@link UserRole} object
   * @param role                  The {@link Role role} get only with this role
   * @param options               The optional parameters, including:
   * <ul>
   *   <li>onlyActive({@link Boolean}): If specified as true, only user roles with status as {@link Status Active}
   *                              are returned. Defaults to true
   *   </li>
   *   <li>department({@link Department}): If specified, only user role with this department is returned</li>
   * </ul>
   * @param session     The {@link VenusSession session} object containing the context of request
   * @return            The corresponding {@link UserRole user role} if found, null otherwise
   * @throws DataAccessException  thrown when there is any exception
   * @throws IllegalArgumentException thrown when the data is not correct
   */
  public abstract UserRole getUserRole(User user, Role role, Map<String, Object> options, VenusSession session) throws DataAccessException, IllegalArgumentException;

  /**
   * Get the roles of a user
   * @param user         The {@link User user} for whom the roles need to be fetched
   * @param options      The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): Return only active roles</li>
   *   <li>department({@link Department}): Return only roles in this department </li>
   * </ul>
   * @param session       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
//  public abstract List<UserRole> getUserRoles(User user, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Set status of the user roles. This can be used to delete the roles of a user
   * @param user          The {@link User user} object for whom the roles' status need to be set
   * @param status        The {@link Status status} to be set
   * @param optionalParams       The list of optional parameters. The list include:
   * <ul>
   *   <li>department({@link Department}): Set the specified status to the roles for only this department </li>
   *   <li>roles(List<{@link Role}>): Only set the status the specified roles </li>
   * </ul>
   * @param vs            The {@link VenusSession session} object
   * @throws DataAccessException thrown when there is any error
   */
//  public abstract void setStatus(User user, Status status, Map<String, Object> optionalParams, VenusSession vs) throws DataAccessException;
  
}
