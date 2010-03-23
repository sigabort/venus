package com.venus.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.venus.model.User;
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
   * @paramm username        The username of the user. This should be unique in the institute
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
   * @return                 The created/updated department object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract User createUpdateUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;
  
  public abstract User findUserByUsername(String username, VenusSession session) throws DataAccessException;

//   public abstract User findUserByUserId(String userId, VenusSession session) throws DataAccessException;

//   public abstract User findUserByEmail(String email, VenusSession session) throws DataAccessException;

  public abstract List<User> getUsers(int offset, int maxRet, VenusSession vs) throws DataAccessException;
  
}
