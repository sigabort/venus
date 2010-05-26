/**
 * @file This file contains the operations impl for User Roles.
 */

package com.venus.dal.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.venus.model.User;
import com.venus.model.Department;
import com.venus.model.Role;
import com.venus.model.UserRole;
import com.venus.model.impl.UserRoleImpl;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.OperationsUtil;
import com.venus.dal.UserRoleOperations;
import com.venus.dal.DataAccessException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Restrictions;

/**
 * This class contains the implementations for operations allowed on {@link UserRole user role}. The operations include
 * creation/updation/fetching/deletion of the user roles.
 * 
 * @author sigabort
 */
public class UserRoleOperationsImpl implements UserRoleOperations {
  private final Logger log = Logger.getLogger(UserRoleOperationsImpl.class);
  
  /**
   * Create or Update user role for a user
   * @param user             The {@link User user} object for which the roles need to be defined.
   * @param role             The {@link Role role} for a user
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>department({@link Department}):   The {@link Department department} user belongs to. If specified, the
   *                                role provided will be specific to this department.  For some roles, the department
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
  /*
   * The unique key on {@link UserRole} object is : combination of (user, role, department). So, when
   * adding a new entry or updating an existing entry, make sure we dont add duplicate keys 
   */
  public  UserRole createUpdateUserRole(User user, Role role, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (user == null || role == null) {
      throw new IllegalArgumentException("User and role should be supplied");
    }

    /* see if we need to set this for a department */
    Department dept = OperationsUtilImpl.getDepartment("department", optionalParams, null);

    /* check if the department is needed for this role */
    Boolean isDeptNeeded = role.isDepartmentRequired();    
    if (isDeptNeeded && dept == null) {
      throw new IllegalArgumentException("Department must be specified for role: " + role.toString());
    }
    
    /* options for finding the user role. We need to get non-active user role too. */
    Map<String, Object> options = new HashMap<String, Object>(2);
    options.put("onlyActive", Boolean.FALSE);    
    options.put("department", dept);

    /* see if we have user role with given details */
    UserRole ur = getUserRole(user, role, options, session);
    /* update the existing user role */
    if (ur != null) {
      /* if status is supplied, use it. Otherwise, we need to set it to Status.Active to make
       * sure we are creating/updating the active object
       */
      Status status = OperationsUtilImpl.getStatus("status", optionalParams, Status.Active);    
      if (optionalParams == null) {
        optionalParams = new HashMap<String, Object>(1);
      }
      optionalParams.put("status", (Object)status);

      ur = updateUserRole(ur, optionalParams, session);
    }
    /* user role doesn't exist, create it */
    else {
      ur = createUserRole(user, role, optionalParams, session);
    }
    return ur;
  }
  

  private UserRole createUserRole(User user, Role role, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (user == null || role == null) {
      throw new IllegalArgumentException("user and role must be supplied");
    }
    
    /* create a new object */
    UserRole ur = new UserRoleImpl();
    /* set user */
    ur.setUser(user);
    /* set role */
    ur.setRole(role.ordinal());
    /* set department */
    ur.setDepartment(OperationsUtilImpl.getDepartment("department", optionalParams, null));
    /* set proper status */
    ur.setStatus((OperationsUtilImpl.getStatus("status", optionalParams, Status.Active)).ordinal());
    
    /* set the created and lastmodified dates */
    ur.setCreated(OperationsUtilImpl.getDate("created", optionalParams, new Date()));
    ur.setLastModified(OperationsUtilImpl.getDate("lastModified", optionalParams, new Date()));    
    
    /* try to create the user now */
    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(ur);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create user role for user: " + user.getUsername() + ", role: " + role.toString() + " for institute: " + session.getInstitute().getName();
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
    return ur;    
  }
  
  private UserRole updateUserRole(UserRole ur, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (ur == null) {
      throw new IllegalArgumentException("UserRole must be supplied");
    }
    
    boolean update = false;
    if (optionalParams != null) {
      /* check if department needs to be changed */
      Department department = OperationsUtilImpl.getDepartment("department", optionalParams, null);
      if (department != null && !department.equals(ur.getDepartment())) {
        ur.setDepartment(department);
        update = true;
      }
      
      /* check if status needs to be changed */
      Status status = OperationsUtilImpl.getStatus("status", optionalParams, null);
      if (status != null && status.ordinal() != ur.getStatus()) {
        ur.setStatus(status.ordinal());
        update = true;
      }
    }
    
    /* if update is needed, update */
    if (update) {
      Transaction txn = null;
      if (log.isDebugEnabled()) {
        log.debug("Updating user role for user: " + ur.getUser().getUsername() + ", role: " + ur.getRole() + " in institute : " + session.getInstitute().getName());
      }
      try {
        /* set last modified date */
        ur.setLastModified(new Date());
        Session sess = session.getHibernateSession();
        txn = sess.beginTransaction();
        sess.update(ur);
      }
      catch (HibernateException he) {
        String errStr = "Unable to update user role for user: " + ur.getUser().getUsername()  + ", role: " + ur.getRole() + ", in institute: " + session.getInstitute().getName();
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
    return ur;
  }
  
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
  public UserRole getUserRole(User user, Role role, Map<String, Object> options, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (user == null || role == null) {
      throw new IllegalArgumentException("getUserRole: user and role must be supplied");
    }
    
    /* do we need to return only active user? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    Department department = OperationsUtilImpl.getDepartment("department", options, null);

    if (log.isDebugEnabled()) {
      log.debug("Finding user role with username: " + user.getUsername() + ", role: " + role.toString() + ", for institute with id: " + session.getInstitute().getName());
    }

    /* use naturalid restrictions to find the user role here */
    try {
      Criteria criteria = session.getHibernateSession().createCriteria(UserRoleImpl.class);
      /* create naturalId restrictions */
      NaturalIdentifier naturalId = Restrictions.naturalId().set("user", user);
      naturalId.set("role", role.ordinal());
      /* only set if department specific role is asked for */
      if (department != null) {
        naturalId.set("department", department);
      }
      criteria.add(naturalId);
      
      /* set status if only active result is to be returned? */
      if (onlyActive) {
        criteria = criteria.add(Restrictions.eq("status", Status.Active.ordinal()));
      }
      criteria.setCacheable(false);
      /* get the user role */
      UserRole userRole = (UserRoleImpl) criteria.uniqueResult();
      return userRole;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find user role with username: " + user.getUsername() + ", role: " + role.toString() + ", in institute with id: " + session.getInstitute().getName();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  } 
  
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
  public List<UserRole> getUserRoles(User user, Map<String, Object> options, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (user == null) {
      throw new IllegalArgumentException("getUserRole: user and role must be supplied");
    }
    
    /* do we need to return only active user? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    Department department = OperationsUtilImpl.getDepartment("department", options, null);

    if (log.isDebugEnabled()) {
      log.debug("Finding user roles with username: " + user.getUsername() + ", for institute with id: " + session.getInstitute().getName());
    }

    /* use naturalid restrictions to find the user roles here */
    try {
      Criteria criteria = session.getHibernateSession().createCriteria(UserRoleImpl.class);
      /* create naturalId restrictions */
      NaturalIdentifier naturalId = Restrictions.naturalId().set("user", user);
      /* only set if department specific roles is asked for */
      if (department != null) {
        naturalId.set("department", department);
      }
      criteria.add(naturalId);
      
      /* set status if only active result is to be returned? */
      if (onlyActive) {
        criteria = criteria.add(Restrictions.eq("status", Status.Active.ordinal()));
      }
      criteria.setCacheable(false);
      /* get the user roles */
      return criteria.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find user roles with username: " + user.getUsername() + ", in institute with id: " + session.getInstitute().getName();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  } 
  
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
   * @throws IllegalArgumentException    thrown when the invalid data is passed
   */
  public int setStatus(User user, Status status, Map<String, Object> options, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    if (user == null || status == null) {
      throw new IllegalArgumentException("User and status must be specified");
    }
    
    /* get the department, and roles to be set the status */
    Department department = OperationsUtilImpl.getDepartment("department", options, null);
    List<Role> roles = OperationsUtilImpl.getRoles("roles", options, null);
    
    log.debug("Updating status for roles of user: " + user.getUsername());
    
    Session session = vs.getHibernateSession();
    StringBuilder str = new StringBuilder("update UserRoleImpl set status=:status where user=:user ");

    /* if the department is specified, set the status to only the specified departments */
    if (department != null) {
      str.append(" and department=:department ");
    }
    /* if the set of roles are specified, only set the status to those roles */
    if (roles != null && roles.size() > 0) {
      str.append(" and role in (");
      int count = 0;
      for (Role role : roles) {
        String delim = (count == 0)? "" : ", ";
        str.append(delim + role.ordinal());
        count++;
      }
      str.append(" ) ");
    }

    /* try to set the status now */
    Transaction txn = null;
    int count = 0;
    try {
      /* begin the transaction */
      txn = session.beginTransaction();
      /* create the query */
      Query query = session.createQuery(str.toString());
      /* set the elements */
      query.setEntity("user", user);
      query.setInteger("status", status.ordinal());
      if (department != null) {
        query.setEntity("department", department);
      }
      /* execute the query */
      count = query.executeUpdate();
    }
    catch (HibernateException he) {
      String errStr = "Error while setting status to user: " + user.getUsername() + ", status: " + status.toString() + ", in institute: " + vs.getInstitute().getName();
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
    return count;
  }
  
  
  /**
   * Set status of the user role. This can be used to delete a particular role of a user
   * @param userRole             The {@link UserRole userRole} object whose status need to be set
   * @param status        The {@link Status status} to be set
   * @param vs            The {@link VenusSession session} object
   * @throws DataAccessException thrown when there is any error
   * @throws IllegalArgumentException thrown when invalid data is passed
   */
  public void setStatus(UserRole userRole, Status status, VenusSession vs) throws DataAccessException, IllegalArgumentException {
    if (userRole == null || status == null) {
      throw new IllegalArgumentException("UserRole and Status must be supplied");
    }
    
    /* check whether the passed status and current status are same */
    boolean update = false;
    if (status.ordinal() != userRole.getStatus()) {
      userRole.setStatus(status.ordinal());
      update = true;
    }

    /* delete if the status is different */
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Changing the status of UserRole, for user: " + userRole.getUser().getUsername() + ", role: " + userRole.getRole() +", in institute with id: " + vs.getInstitute().getName());
      }
      try {
        /* change the last modified date */
        userRole.setLastModified(new Date());
        /* get the hibernate session */
        Session sess = vs.getHibernateSession();
        txn = sess.beginTransaction();
        sess.update(userRole);
      }
      catch (HibernateException he) {
        String errStr = "Unable to change the status of UserRole for user: " + userRole.getUser().getUsername() + ", role: " + userRole.getRole() + ", in institute with id: " + vs.getInstitute().getName();
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

  
}