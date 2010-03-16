package com.venus.dal.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.model.impl.DepartmentImpl;
import com.venus.util.VenusSession;
import com.venus.dal.DepartmentOperations;
import com.venus.dal.DataAccessException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import org.apache.log4j.Logger;

/**
 * Implementation of operations on departments of an institute.
 * 
 * @author sigabort
 */
public class DepartmentOperationsImpl implements DepartmentOperations {
  private String FIND_DEPT_BY_NAME_STR = "findDepartmentByName";
  private String FIND_DEPT_BY_CODE_STR = "findDepartmentByCode";
  
  /* logger for logging */
  private final Logger log = Logger.getLogger(DepartmentOperationsImpl.class);
  
  /**
   * Create or Update department
   * @paramm name            The name of the department. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code: String</li><li>description: String</li><li>photoUrl: String</li>
   *   <li>email: String</li><li>status: Status</li><li>created: Date</li><li>lastModified: Date</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created/updated department object
   * @throws DataAccessException thrown when there is any error
   */
  public Department createUpdateDepartment(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    /* name is mandatory for the department create/update */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }

    /* see if the dept exists already */
    Department dept = findDepartmentByName(name, true, session);

    /* ok, department exists. Update it */
    if (dept != null) {
      /* if status is supplied, use it. Otherwise, we need to set it to Status.Active to make
       * sure we are updating the active object
       */
      Status status = OperationsUtilImpl.getStatus("status", optionalParams, Status.Active);
      if (optionalParams == null) {
	optionalParams = new HashMap<String, Object>(1);
      }
      optionalParams.put("status", (Object)status);

      /* ok, time to update department */
      dept = updateDepartment(dept, optionalParams, session);
    }
    else {
      /* object doesn't exist, create it */
      dept = createDepartment(name, optionalParams, session);
    }

    return dept;
  }

  /**
   * Create department
   * @paramm name            The name of the department. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code: String</li><li>description: String</li><li>photoUrl: String</li>
   *   <li>email: String</li><li>status: Status</li><li>created: Date</li><li>lastModified: Date</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created department object
   * @throws DataAccessException thrown when there is any error
   */
  private Department createDepartment(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    /* name is mandatory for the department create */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Creating Department with name: " + name + ", for institute with id: " + session.getInstituteId());
    }

    /* create new object */
    Department dept = new DepartmentImpl();
    dept.setName(name);

    /* set the institute Id */
    dept.setInstituteId(session.getInstituteId());

    /****** set the optional parameters now *******/
    /* code - uniquely identifying the department. If not set, use name+"-"+InstituteId for now */
    dept.setCode(OperationsUtilImpl.getStringValue("code", optionalParams, name + "-" + session.getInstituteId()));
    /* description of the department */
    dept.setDescription(OperationsUtilImpl.getStringValue("description", optionalParams, null));
    dept.setPhotoUrl(OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null));
    dept.setEmail(OperationsUtilImpl.getStringValue("email", optionalParams, null));
    /* status should be active if not specified */
    dept.setStatus(OperationsUtilImpl.getStatus("status", optionalParams, Status.Active).ordinal());
    /* set the created and lastmodified dates */
    dept.setCreated(OperationsUtilImpl.getDate("created", optionalParams, new Date()));
    dept.setLastModified(OperationsUtilImpl.getDate("lastModified", optionalParams, new Date()));    

    /* begin the transaction and save the object */
    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(dept);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create department: " + name + ", for institute: " + session.getInstituteId();
      if (txn != null && txn.isActive()) {
	txn.rollback();
      }
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
    finally {
      if (txn != null && txn.isActive()) {
	txn.commit();
      }
    }
    return dept;
  }

  /**
   * Update department
   * @paramm dept            The department object to be updated
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code: String</li><li>description: String</li><li>photoUrl: String</li>
   *   <li>email: String</li><li>status: Status</li><li>created: Date</li><li>lastModified: Date</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The updated department object
   * @throws DataAccessException thrown when there is any error
   */
  private Department updateDepartment(Department dept, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (dept == null) {
      return null;
    }
    boolean update = false;

    if (optionalParams != null) {
      /* check the code */
      String newCode = OperationsUtilImpl.getStringValue("code", optionalParams, null);
      if (newCode != null && !newCode.equals(dept.getCode())) {
	dept.setCode(newCode);
	update = true;
      }
      
      /* check the description */
      String newDescription = OperationsUtilImpl.getStringValue("description", optionalParams, null);
      if (newDescription != null && !newDescription.equals(dept.getDescription())) {
	dept.setDescription(newDescription);
	update = true;
      }

      /* check the photoUrl */
      String newPhotoUrl = OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null);
      if (newPhotoUrl != null && !newPhotoUrl.equals(dept.getPhotoUrl())) {
	dept.setPhotoUrl(newPhotoUrl);
	update = true;
      }
      
      /* check the email */
      String newEmail = OperationsUtilImpl.getStringValue("email", optionalParams, null);
      if (newEmail != null && !newEmail.equals(dept.getEmail())) {
	dept.setEmail(newEmail);
	update = true;
      }
      
      /* check the Status */
      Status newStatus = OperationsUtilImpl.getStatus("status", optionalParams, null);
      if (newStatus != null && newStatus.ordinal() != dept.getStatus()) {
	dept.setStatus(newStatus.ordinal());
	update = true;
      }
    }

    /* is update needed? */
    if (update) {
      Transaction txn = null;
      try {
	/* set the last modified date */
	dept.setLastModified(new Date());
	Session sess = session.getHibernateSession();
	txn = sess.beginTransaction();
	sess.update(dept);
      }
      catch (HibernateException he) {
	String errStr = "Unable to update department: " + dept.getName() + ", for institute: " + dept.getInstituteId();
	if (txn != null && txn.isActive()) {
	  txn.rollback();
	}
	log.error(errStr, he);
	throw new DataAccessException(errStr, he);
      }
      finally {
	if (txn != null && txn.isActive()) {
	txn.commit();
	}
      }
    }
    return dept;
  }

  /**
   * Find the department given the name
   */
  public Department findDepartmentByName(String name, boolean includeDeleted, VenusSession vs)  throws DataAccessException {
    /* name is null? return null */
    if (name == null) {
      return null;
    }
    /*  */
    if (!includeDeleted) {
      return findDepartmentByName(name, vs);
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* create query */
      Query query = sess.createQuery("from DepartmentImpl dept where dept.name=:name ");
      query.setString("name", name);
      /* find now */
      return (Department)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with name: " + name;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the department given the code
   */
  public Department findDepartmentByCode(String code, boolean includeDeleted, VenusSession vs) throws DataAccessException  {
    /* code is null? return null */
    if (code == null) {
      return null;
    }
    /*  */
    if (!includeDeleted) {
      return findDepartmentByCode(code, vs);
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* create query */
      Query query = sess.createQuery("from DepartmentImpl dept where dept.code=:code ");
      query.setString("code", code);
      /* find now */
      return (Department)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with code: " + code;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the department given the name
   */
  public Department findDepartmentByName(String name, VenusSession vs)  throws DataAccessException {
    /* name is null? return null */
    if (name == null) {
      return null;
    }
   
    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* find now */
      Query query = sess.getNamedQuery(FIND_DEPT_BY_NAME_STR);
      query.setString(0, name);
      return (Department)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with name: " + name;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the department given the code
   */
  public Department findDepartmentByCode(String code, VenusSession vs) throws DataAccessException  {
    /* code is null? return null */
    if (code == null) {
      return null;
    }
    
    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* find now */
      Query query = sess.getNamedQuery(FIND_DEPT_BY_CODE_STR);
      query.setString(0, code);
      return (Department)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with code: " + code;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Delete the department
   */
  public void deleteDepartment(Department dept, VenusSession vs) throws DataAccessException  {
    if (dept == null) {
      return;
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Deleting department: " + dept.getName());
    }
    
    /* set the status to deleted */
    dept.setStatus(Status.Deleted.ordinal());

    Transaction txn = null;
    try {
      /* get the hibernate session */
      Session sess = vs.getHibernateSession();
      txn = sess.beginTransaction();
      sess.update(dept);
    }
    catch (HibernateException he) {
      String errStr = "Unable to delete department: " + dept.getName();
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

  /**
   * Get all the departments in the institute
   */
  public List<Department> getDepartments(int offset, int maxRet, VenusSession vs)  throws DataAccessException {
    try {
      Session sess = vs.getHibernateSession();
      Query query = sess.createQuery("from DepartmentImpl dept where dept.status=:status");
      query.setInteger("status", Status.Active.ordinal());
      query.setFirstResult(offset);
      query.setMaxResults(maxRet);
      
      return query.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get departments";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  

}
