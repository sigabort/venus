package com.venus.dal.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.venus.model.User;
import com.venus.model.Status;
import com.venus.model.impl.UserImpl;
import com.venus.util.VenusSession;
import com.venus.dal.UserOperations;
import com.venus.dal.DataAccessException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import org.apache.log4j.Logger;

public class UserOperationsImpl implements UserOperations {
  private String FIND_USER_BY_USERNAME_STR = "findUserByUsername";
  private Logger log = Logger.getLogger(UserOperationsImpl.class);
  
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
  public User createUpdateUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    User user = findUserByUsername(username, session);
    if (user != null) {
      /* if status is supplied, use it. Otherwise, we need to set it to Status.Active to make
       * sure we are updating the active object
       */
      Status status = OperationsUtilImpl.getStatus("status", optionalParams, Status.Active);
      if (optionalParams == null) {
        optionalParams = new HashMap<String, Object>(1);
      }
      optionalParams.put("status", (Object)status);

      /* ok, time to update the existing user */
      user = updateUser(user, optionalParams, session);
    }
    else {
      user = createUser(username, optionalParams, session);
    }
    return user;
  }

 
  /**
   * Create a new user
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
  private User createUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Creating User with name: " + username + ", for institute with id: " + session.getInstituteId());
    }

    /* create object */
    User user = new UserImpl();

    /* set user name */
    user.setUsername(username);
    /* set the institute Id */
    user.setInstituteId(session.getInstituteId());

    /****** set the optional parameters now *******/
    
    /* userId - can be user's roll number, employee id, etc */
    user.setUserId(OperationsUtilImpl.getStringValue("userId", optionalParams, null));
    /* set the password - if it needs to be encrypted, it should be done at the service layer */
    user.setPassword(OperationsUtilImpl.getStringValue("password", optionalParams, null));
    /* set the email */
    user.setEmail(OperationsUtilImpl.getStringValue("email", optionalParams, null));
    /* set the firstName */
    user.setFirstName(OperationsUtilImpl.getStringValue("firstName", optionalParams, null));
    /* set the lastName */
    user.setLastName(OperationsUtilImpl.getStringValue("lastName", optionalParams, null));
    /* set the gender */
    user.setGender(OperationsUtilImpl.getStringValue("gender", optionalParams, null));
    /* set the url */
    user.setUrl(OperationsUtilImpl.getStringValue("url", optionalParams, null));
    /* set the phone */
    user.setPhone(OperationsUtilImpl.getStringValue("phone", optionalParams, null));
    /* set the address1 */
    user.setAddress1(OperationsUtilImpl.getStringValue("address1", optionalParams, null));
    /* set the address2 */
    user.setAddress2(OperationsUtilImpl.getStringValue("address2", optionalParams, null));
    /* set the city */
    user.setCity(OperationsUtilImpl.getStringValue("city", optionalParams, null));
    /* set the country */
    user.setCountry(OperationsUtilImpl.getStringValue("country", optionalParams, null));
    /* set the postalCode */
    user.setPostalCode(OperationsUtilImpl.getStringValue("postalCode", optionalParams, null));
    /* set the photoUrl */
    user.setPhotoUrl(OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null));
    /* set the birthDate */
    user.setBirthDate(OperationsUtilImpl.getDate("birthDate", optionalParams, null));
    /* set the joinDate */
    user.setJoinDate(OperationsUtilImpl.getDate("joinDate", optionalParams, null));
    /* status should be active if not specified */
    user.setStatus((OperationsUtilImpl.getStatus("status", optionalParams, Status.Active)).ordinal());
    /* set the created and lastmodified dates */
    user.setCreated(OperationsUtilImpl.getDate("created", optionalParams, new Date()));
    user.setLastModified(OperationsUtilImpl.getDate("lastModified", optionalParams, new Date()));    
    
    /* try to create the user now */
    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(user);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create user: " + username + " for institute: " + session.getInstituteId();
      log.error(errStr, he);
      if (txn != null && txn.isActive()) {
        txn.rollback();
      }
      throw new DataAccessException(errStr, he);
    }
    finally {
      if (txn != null && txn.isActive()) {
        txn.commit();
      }
    }
    return user;
  }

  /**
   * Update an existing user
   * @paramm User        The user {@link User} object of the user to be updated
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
  private User updateUser(User user, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (user == null) {
      return null;
    }
    boolean update = false;
    
    if (optionalParams != null) {
      /* check the userId */
      String newUserId = OperationsUtilImpl.getStringValue("userId", optionalParams, null);
      if (newUserId != null && !newUserId.equals(user.getUserId())) {
        user.setUserId(newUserId);
        update = true;
      }
      
      /* check the password */
      String newPassword = OperationsUtilImpl.getStringValue("password", optionalParams, null);
      if (newPassword != null && !newPassword.equals(user.getPassword())) {
        user.setPassword(newPassword);
        update = true;
      }
      
      /* check the email */
      String newEmail = OperationsUtilImpl.getStringValue("email", optionalParams, null);
      if (newEmail != null && !newEmail.equals(user.getEmail())) {
        user.setEmail(newEmail);
        update = true;
      }
      
      /* check the firstName */
      String newFirstName = OperationsUtilImpl.getStringValue("firstName", optionalParams, null);
      if (newFirstName != null && !newFirstName.equals(user.getFirstName())) {
        user.setFirstName(newFirstName);
        update = true;
      }
      
      /* check the lastName */
      String newLastName = OperationsUtilImpl.getStringValue("lastName", optionalParams, null);
      if (newLastName != null && !newLastName.equals(user.getLastName())) {
        user.setLastName(newLastName);
        update = true;
      }
      
      /* check the gender */
      String newGender = OperationsUtilImpl.getStringValue("gender", optionalParams, null);
      if (newGender != null && !newGender.equals(user.getGender())) {
        user.setGender(newGender);
        update = true;
      }
      
      /* check the url */
      String newUrl = OperationsUtilImpl.getStringValue("url", optionalParams, null);
      if (newUrl != null && !newUrl.equals(user.getUrl())) {
        user.setUrl(newUrl);
        update = true;
      }
      
      /* check the phone */
      String newPhone = OperationsUtilImpl.getStringValue("phone", optionalParams, null);
      if (newPhone != null && !newPhone.equals(user.getPhone())) {
        user.setPhone(newPhone);
        update = true;
      }
      
      /* check the address1 */
      String newAddress1 = OperationsUtilImpl.getStringValue("address1", optionalParams, null);
      if (newAddress1 != null && !newAddress1.equals(user.getAddress1())) {
        user.setAddress1(newAddress1);
        update = true;
      }
      
      /* check the address2 */
      String newAddress2 = OperationsUtilImpl.getStringValue("address2", optionalParams, null);
      if (newAddress2 != null && !newAddress2.equals(user.getAddress2())) {
        user.setAddress2(newAddress2);
        update = true;
      }
      
      /* check the city */
      String newCity = OperationsUtilImpl.getStringValue("city", optionalParams, null);
      if (newCity != null && !newCity.equals(user.getCity())) {
        user.setCity(newCity);
        update = true;
      }
      
      /* check the country */
      String newCountry = OperationsUtilImpl.getStringValue("country", optionalParams, null);
      if (newCountry != null && !newCountry.equals(user.getCountry())) {
        user.setCountry(newCountry);
        update = true;
      }
      
      /* check the postalCode */
      String newPostalCode = OperationsUtilImpl.getStringValue("postalCode", optionalParams, null);
      if (newPostalCode != null && !newPostalCode.equals(user.getPostalCode())) {
        user.setPostalCode(newPostalCode);
        update = true;
      }
      
      /* check the photoUrl */
      String newPhotoUrl = OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null);
      if (newPhotoUrl != null && !newPhotoUrl.equals(user.getPhotoUrl())) {
        user.setPhotoUrl(newPhotoUrl);
        update = true;
      }
      
      /* check the birthDate */
      Date newBirthDate = OperationsUtilImpl.getDate("birthDate", optionalParams, null);
      if (newBirthDate != null && !newBirthDate.equals(user.getBirthDate())) {
        user.setBirthDate(newBirthDate);
        update = true;
      }
      
      /* check the joinDate */
      Date newJoinDate = OperationsUtilImpl.getDate("joinDate", optionalParams, null);
      if (newJoinDate != null && !newJoinDate.equals(user.getJoinDate())) {
        user.setJoinDate(newJoinDate);
        update = true;
      }
      
      /* check the status */
      Status newStatus = OperationsUtilImpl.getStatus("status", optionalParams, null);
      if (newStatus != null && newStatus.ordinal() != user.getStatus()) {
        user.setStatus(newStatus.ordinal());
        update = true;
      }
      
      /* check the created */
      Date newCreated = OperationsUtilImpl.getDate("created", optionalParams, null);
      if (newCreated != null && !newCreated.equals(user.getCreated())) {
        user.setCreated(newCreated);
        update = true;
      }
      
    }
    
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Updating user: " + user.getUsername()+ ", in institute : " + session.getInstituteId());
      }
      try{
        user.setLastModified(new Date());
        Session sess = session.getHibernateSession();
        sess.update(user);
      }
      catch (HibernateException he) {
        String errStr = "Unable to update user: " + user.getUsername() + " in institute: " + session.getInstituteId();
        log.error(errStr, he);
        if (txn != null && txn.isActive()) {
          txn.rollback();
        }
        throw new DataAccessException(errStr, he);
      }
      finally {
        if (txn != null && txn.isActive()) {
          txn.commit();
        }
      }
    }
    return user;
  }

  public User findUserByUsername(String username, VenusSession vs) throws DataAccessException {
    if (username == null) {
      return null;
    }
    try {
      Session sess = vs.getHibernateSession();
      Query query = sess.getNamedQuery(FIND_USER_BY_USERNAME_STR);
      query.setString(0, username);
      return (User)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find the user: " + username;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
    
  }

  public List<User> getUsers(int offset, int maxRet, VenusSession vs) throws DataAccessException {
    try {
      Session sess = vs.getHibernateSession();
      Query query = sess.createQuery("from UserImpl");
      query.setFirstResult(offset);
      query.setMaxResults(maxRet);
      return query.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get the users";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  

}
