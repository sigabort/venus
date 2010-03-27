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
import com.venus.dal.OperationsUtil;
import com.venus.dal.DataAccessException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class UserOperationsImpl implements UserOperations {
  /** The named query name for finding user by userid */
  private static String FIND_USER_BY_USERID_STR = "findUserByUserId";
  /** The named query name for finding user by email */
  private static String FIND_USER_BY_EMAIL_STR = "findUserByEmail";
  
  private final Logger log = Logger.getLogger(UserOperationsImpl.class);
  
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
  /*
   * The unique keys on User object are: 
   * 1. (username, institute)
   * 2. (userid, institute)
   * 3. (email, institute)
   *
   * So, make sure when adding a new entry or updating an existing entry, we dont add duplicate keys 
   */
  public User createUpdateUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("createUpdate: Username must be supplied");
    }
    
    /* options to pass for finding user */
    Map<String, Object> opts = new HashMap<String, Object>(1);
    opts.put("onlyActive", Boolean.FALSE);
    
    /* see if the user with given username already exists or not */
    User user = findUserByUsername(username, opts, session);
    
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
    else { /* user doesn't exist, create a new user */
      user = createUser(username, optionalParams, session);
    }
    return user;
  }

  /**
   * Check if the email is already used by the other user
   * @param email          The email to check whether it is used by other users or not
   * @param currentUser    The current user to check if this user holds it or not. If 
   *                       currentUser is not null, and email is held by this user, returns false
   * @param session        The {@link VenusSession} object holding context
   * @return               true if email is already used by other users, false otherwise
   * @throws HibernateException        thrown when there is any error while fetching data
   * @throws IllegalArgumentException  thrown if the data passed is wrong
   */
  private boolean isEmailAlreadyUsed(String email, User currentUser, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (!StringUtils.isEmpty(email)) {
      /*
       * If the current user is provided, check if this is used by the current user. If so, return false.
       */
      if (currentUser != null) {
        if (email.equals(currentUser.getEmail())) {
          return false;
        }
      }
      /* we need to check the non-active users too */
      Map<String, Object> options = new HashMap<String, Object>();
      options.put("onlyActive", Boolean.FALSE);
      User existingUser = findUserByEmail(email, options, session);
      if (existingUser != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the userId is already used by the other user
   * @param userId         The userId to check whether it is used by other users or not
   * @param currentUser    The current user to check if this user holds it or not. If 
   *                       currentUser is not null, and userId is held by this user, returns false
   * @param session        The {@link VenusSession} object holding context
   * @return               true if userId is already used by other users, false otherwise
   * @throws HibernateException        thrown when there is any error while fetching data
   * @throws IllegalArgumentException  thrown if the data passed is wrong
   */
  private boolean isUserIdAlreadyUsed(String userId, User currentUser, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (!StringUtils.isEmpty(userId)) {
      /*
       * If the current user is provided, check if this is used by the current user. If so, return false.
       */
      if (currentUser != null) {
        if (userId.equals(currentUser.getUserId())) {
          return false;
        }
      }
      /* we need to check the non-active users too */
      Map<String, Object> options = new HashMap<String, Object>();
      options.put("onlyActive", Boolean.FALSE);
      User existingUser = findUserByUserId(userId, options, session);
      if (existingUser != null) {
        return true;
      }
    }
    return false;
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
   * @return                 The created/updated user object
   * @throws DataAccessException thrown when there is any error
   */
  private User createUser(String username, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("createUser: Username must be supplied");
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Creating User with name: " + username + ", for institute with id: " + session.getInstituteId());
    }
    
    /* check if the user is trying to set the email/userId which is already
     * used by other users. If so, throw an error:
     * we have unique keys on: email/institute, userId/institute, username/institute
     * Adding duplicate will result into constraint violation error.
     */      
    String email = OperationsUtilImpl.getStringValue("email", optionalParams, null);
    if (isEmailAlreadyUsed(email, null, session)) {
     throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    String userId = OperationsUtilImpl.getStringValue("userId", optionalParams, null);
    if (isUserIdAlreadyUsed(userId, null, session)) {
      throw new IllegalArgumentException("User with userId " + userId + " already exists");
    }
    /* all checks passed, go on and create user */

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
   * @return                 The created/updated user object
   * @throws DataAccessException thrown when there is any error
   */
  private User updateUser(User user, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (user == null) {
      throw new IllegalArgumentException("updateUser: user must be supplied");
    }
    boolean update = false;
    
    
    /* check if the user is trying to set the email/userId which is already
     * used by other users. If so, throw an error:
     * we have unique keys on: email/institute, userId/institute, username/institute
     * Adding duplicate will result into constraint violation error.
     */      
    String email = OperationsUtilImpl.getStringValue("email", optionalParams, null);
    if (isEmailAlreadyUsed(email, user, session)) {
     throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    String userId = OperationsUtilImpl.getStringValue("userId", optionalParams, null);
    if (isUserIdAlreadyUsed(userId, user, session)) {
      throw new IllegalArgumentException("User with userId " + userId + " already exists");
    }
    /* all checks passed, go on and update user */
    
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
        txn = sess.beginTransaction();
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
  
  /**
   * Find the user given the username in an institue. By default, returns
   * only active user if not specified.
   * @param username     The username of the user in the institute
   * @param options      The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public User findUserByUsername(String username, Map<String, Object> options, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("findUserByUsername: username must be supplied");
    }
    
    /* do we need to return only active user? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding user with username: " + username + ", for institute with id: " + vs.getInstituteId());
    }

    /* use naturalid restrictions to find the user here */
    try {
      Criteria criteria = vs.getHibernateSession().createCriteria(UserImpl.class);
      NaturalIdentifier naturalId = Restrictions.naturalId().set("username", username);
      naturalId.set("instituteId", vs.getInstituteId());
      
      criteria.add(naturalId);
      criteria.setCacheable(false);
      User user = (UserImpl) criteria.uniqueResult();
      /* check if only active user is needed */
      if (user != null && onlyActive) {
        if (user.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return user;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find user with username: " + username + ", in institute with id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  
  /**
   * Find the user given the userId. By default, returns
   * only active user if not specified
   * @param userId     The userId of the user in the institute
   * @param options    The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public User findUserByUserId(String userId, Map<String, Object> options, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    /* userId is null? throw error */
    if (userId == null) {
      throw new IllegalArgumentException("findUserByUserId: userId must be supplied");
    }

    /* do we need to return only active user? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding User with userId: " + userId + ", for institute with id: " + vs.getInstituteId());
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* find now using named query */
      Query query = sess.getNamedQuery(FIND_USER_BY_USERID_STR);
      query.setString(0, userId);
      query.setInteger(1, vs.getInstituteId());
      
      /* get the result */
      User user = (UserImpl) query.uniqueResult();
      /* check if only active user is needed */
      if (user != null && onlyActive) {
        if (user.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return user;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find user with userId: " + userId + ", in institute with id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
  
  /**
   * Find the user given the email. By default, returns
   * only active user if not specified
   * @param email     The email of the user in the institute
   * @param options    The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): If set to true, only active user will be returned. Defaults to true</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The user object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public User findUserByEmail(String email, Map<String, Object> options, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    /* email is null? throw error */
    if (email == null) {
      throw new IllegalArgumentException("findUserByEmail: email must be supplied");
    }

    /* do we need to return only active user? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding User with email: " + email + ", for institute with id: " + vs.getInstituteId());
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* find now using named query */
      Query query = sess.getNamedQuery(FIND_USER_BY_EMAIL_STR);
      query.setString(0, email);
      query.setInteger(1, vs.getInstituteId());
      
      /* get the result */
      User user = (UserImpl) query.uniqueResult();
      /* check if only active user is needed */
      if (user != null && onlyActive) {
        if (user.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return user;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find user with email: " + email + ", in institute with id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
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
  /* XXX: add filtering on the specified date (for example, get me users created in the given 
   * interval, joined in given interval, etc */
  public List<User> getUsers(int offset, int maxRet, Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* we will sort on 'created' by default for users. Why? - God knows */
    String sortBy = OperationsUtilImpl.getStringValue("sortBy", options, "created");
    Boolean isAscending = OperationsUtilImpl.getBoolean("isAscending", options, OperationsUtil.DEFAULT_SORT_ORDER);
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(UserImpl.class);

      /* set the institute id */
      c.add(Expression.eq("instituteId", vs.getInstituteId()));
      
      /* set the condition on status, if we need only active users */
      if (onlyActive) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      
      /* if sortBy is specified by, add order */
      if (StringUtils.isNotBlank(sortBy)) {
        c.addOrder(isAscending? Order.asc(sortBy) : Order.desc(sortBy));
      }
      
      /*XXX: Add filtering*/
      
      /* set pagination */
      c.setFirstResult(offset);
      c.setMaxResults(maxRet);

      /* return the list */
      return c.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get users, with institute id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
   

  /**
   * Set status of the user. This can be used to delete the User
   * @param user          The user object for which the status needs to be changed
   * @param status        The status to be set
   * @param vs            The session object
   * @throws DataAccessException thrown when there is any error
   */
  public void setStatus(User user, Status status, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    if (user == null || status == null) {
      throw new IllegalArgumentException("User and Status must be supplied");
    }
    
    /* check whether the passed status and current status are same */
    boolean update = false;
    if (status.ordinal() != user.getStatus()) {
      user.setStatus(status.ordinal());
      update = true;
    }

    /* delete if the status is different */
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Changing the status for user: " + user.getUsername() + ", in institute with id: " + user.getInstituteId());
      }
      try {
        /* change the last modified date */
        user.setLastModified(new Date());
        /* get the hibernate session */
        Session sess = vs.getHibernateSession();
        txn = sess.beginTransaction();
        sess.update(user);
      }
      catch (HibernateException he) {
        String errStr = "Unable to change the status for user: " + user.getUsername() + ", in institute with id: " + user.getInstituteId();
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
  }

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
  /* XXX: Need to allow filtering on the periods */
  public Integer getUsersCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    /* count only active users? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* filter options */
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(UserImpl.class);

      /* set the institute id */
      c.add(Expression.eq("instituteId", vs.getInstituteId()));
      
      /* set the condition on status, if we need only active users */
      if (onlyActive) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      /* set the projection for the row count */
      c.setProjection(Projections.rowCount());
      
      /*XXX: Add filtering*/

      /* return the count */
      return ((Number)c.uniqueResult()).intValue();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get users count, with institute id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  
}
