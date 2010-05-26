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
   * @param session          The venus session object consisting of institute, hibernate session
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
   * @param session       The {@link VenusSession session} object
   * @return              The list of {@link UserRole role} objects if found, null/empty list otherwise
   * @throws DataAccessException thrown when there is any exception
   * @throws IllegalArgumentException thrown when wrong data passed
   */
  public abstract List<UserRole> getUserRoles(User user, Map<String, Object> options, VenusSession session) throws DataAccessException, IllegalArgumentException;

  /**
   * Set status of the user role. This can be used to delete a particular role of a user
   * @param userRole             The {@link UserRole userRole} object whose status need to be set
   * @param status        The {@link Status status} to be set
   * @param vs            The {@link VenusSession session} object
   * @throws DataAccessException thrown when there is any error
   * @throws IllegalArgumentException thrown when invalid data is passed
   */
  public abstract void setStatus(UserRole userRole, Status status, VenusSession vs) throws DataAccessException, IllegalArgumentException;
 
  /**
   * Set the status for user roles belonging to one user
   * @param user         the {@link User user} object for which the roles' status need to be changed 
   * @param status       The {@link Status status} to be set
   * @param options      The optional parameters, including:
   * <ul>
   *   <li>department({@link Department}): When passed, only roles' status corresponding to this department are changed</li>
   *   <li>roles(List<{@link Role}>): When passed, only these roles' status are changed</li>
   * </ul>
   * @param vs           The {@link VenusSession session} object containing context
   * @return             The count of user roles whose status is changed
   * @throws DataAccessException         thrown when there is any error while doing operation
   * @throws IllegalArgumentException    thrown when invalid data is passed
   */  
  public abstract int setStatus(User user, Status status, Map<String, Object> options, VenusSession vs) throws DataAccessException, IllegalArgumentException;
}
