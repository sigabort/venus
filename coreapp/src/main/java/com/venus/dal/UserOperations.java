package com.venus.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.venus.model.User;
import com.venus.model.Status;
import com.venus.util.VenusSession;

/**
 * This class specifies the operations allowed on user. The operations include
 * creation/updation/fetching/deletion of the user.
 * @author sigabort
 *
 */
public interface UserOperations {
  
  /**
   * Create or Update user
   * @param username        The username of the user. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>userId(String):   The userId can be used as user's roll number, etc 
   *   </li>
   *   <li>password(String): The password for the user, used while logging into system</li>
   *   <li>email(String):    The email-id of the user. This will be used to login too</li>
   *   <li>firstName(String): The first name of the user</li>
   *   <li>lastName(String):  The last name of the user</li>
   *   <li>gender(String): The gender the user</li>
   *   <li>url(String): The profile url for the user</li>
   *   <li>phone(String): The phone number of the user</li>
   *   <li>address1(String): The first part of address of the user</li>
   *   <li>address2(String): The second part of address of the user</li>
   *   <li>city(String): The city</li>
   *   <li>country(String): The country</li>
   *   <li>postalCode(String): The postalcode/zipcode</li>
   *   <li>photoUrl(String): The Photo URL of the User. This can be internal url or external</li>
   *   <li>birthDate(Date): The date of birth of the user</li>
   *   <li>joinDate(Date): The date of joining in the institute</li>
   *   <li>status(Status): The status of the user </li>
   *   <li>created(Date): The created date of this object</li>
   *   <li>lastModified(Date): The last modified date of this object</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created/updated user object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract User createUpdateUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;

  /**
   * Find the user given the username in an institue. By default, returns
   * only active user if not specified.
   * @param username     The username of the user in the institute
   * @param options      The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param session       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract User findUserByUsername(String username, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Find the user given the userId. By default, returns
   * only active user if not specified
   * @param userId     The userId of the user in the institute
   * @param options    The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param session       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract User findUserByUserId(String userId, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Find the user given the email. By default, returns
   * only active user if not specified
   * @param email     The email of the user in the institute
   * @param options    The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param session       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract User findUserByEmail(String email, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Get all the users in the institute (Allowing filtering)
   * @param offset        The paging offset in the list
   * @param maxRet        Maximum number of objects to be returned
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active users, defaults to true</li>
   *   <li>sortBy(String): if specified, the restults will be sorted by this field, defaults to created</li>
   *   <li>isAscending(Boolean): sort by ascending/descending, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the list of users in an institute
   * @throws DataAccessException thrown when there is any error
   */
  public abstract List<User> getUsers(int offset, int maxRet, Map<String, Object> options, VenusSession vs) throws DataAccessException;
  
  /**
   * Set status of the user. This can be used to delete the User
   * @param user          The user object for which the status needs to be changed
   * @param status        The status to be set
   * @param vs            The session object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract void setStatus(User user, Status status, VenusSession vs) throws DataAccessException;
  
  
  /**
   * Get users count in the institute
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active users, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the total count of users in the institute
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Integer getUsersCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException;
  
}
