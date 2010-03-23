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
import com.venus.dal.OperationsUtil;
import com.venus.dal.DataAccessException;

import org.hibernate.Criteria;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Implementation of operations on departments of an institute.
 * 
 * @author sigabort
 */
public class DepartmentOperationsImpl implements DepartmentOperations {

  /* logger for logging */
  private final Logger log = Logger.getLogger(DepartmentOperationsImpl.class);
  
  /**
   * Create or Update department
   * @paramm name            The name of the department. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the department in the institute. If not
   *       specified, concat(name, '-', institute id) will be used as code 
   *   </li>
   *   <li>description(String): The description of the department</li>
   *   <li>photoUrl(String): The photourl for the department</li>
   *   <li>email(String): The email of the department (may be admin mail-id)</li>
   *   <li>status(Status): The status of the department</li>
   *   <li>created(Date): The created date of the department</li>
   *   <li>lastModified(Date): The last modified date of the department</li>
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

    /* options to pass for finding department */
    Map<String, Object> opts = new HashMap<String, Object>(1);
    opts.put("onlyActive", Boolean.FALSE);

    /* see if the dept exists already */
    Department dept = findDepartmentByName(name, opts, session);

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
   *   <li>code(String): The code for the department in the institute. If not
   *       specified, concat(name, '-', institute id) will be used as code 
   *   </li>
   *   <li>description(String): The description of the department</li>
   *   <li>photoUrl(String): The photourl for the department</li>
   *   <li>email(String): The email of the department (may be admin mail-id)</li>
   *   <li>status(Status): The status of the department</li>
   *   <li>created(Date): The created date of the department</li>
   *   <li>lastModified(Date): The last modified date of the department</li>
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
   *   <li>code(String): The code for the department in the institute. If not
   *       specified, concat(name, '-', institute id) will be used as code 
   *   </li>
   *   <li>description(String): The description of the department</li>
   *   <li>photoUrl(String): The photourl for the department</li>
   *   <li>email(String): The email of the department (may be admin mail-id)</li>
   *   <li>status(Status): The status of the department</li>
   *   <li>created(Date): The created date of the department</li>
   *   <li>lastModified(Date): The last modified date of the department</li>
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
      if (log.isDebugEnabled()) {
	log.debug("Updating Department with name: " + dept.getName() + ", for institute with id: " + dept.getInstituteId());
      }
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
   * Find the department given the name in an institue. By default, returns
   * only active department if not specified
   * @param name     The name of the department in the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive: Boolean</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The department object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public Department findDepartmentByName(String name, Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    /* name is null? return null */
    if (name == null) {
      return null;
    }

    /* do we need to return only active department? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding Department with name: " + name + ", for institute with id: " + vs.getInstituteId());
    }

    /* use naturalid restrictions to find the department here */
    try {
      Criteria criteria = vs.getHibernateSession().createCriteria(DepartmentImpl.class);
      NaturalIdentifier naturalId = Restrictions.naturalId().set("name", name);
      naturalId.set("instituteId", vs.getInstituteId());
      
      criteria.add(naturalId);
      criteria.setCacheable(false);
      Department dept = (DepartmentImpl) criteria.uniqueResult();
      /* check if only active department is needed */
      if (dept != null && onlyActive) {
	if (dept.getStatus() != Status.Active.ordinal()) {
	  return null;
	}
      }
      return dept;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with name: " + name + ", in institute with id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the department given the code in an institue. By default, returns
   * only active department if not specified
   * @param code     The code of the department in the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive: Boolean</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The department object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public Department findDepartmentByCode(String code, Map<String, Object> options, VenusSession vs) throws DataAccessException  {
    /* code is null? return null */
    if (code == null) {
      return null;
    }

    /* do we need to return only active department? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding Department with code: " + code + ", for institute with id: " + vs.getInstituteId());
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* create query */
      Query query = sess.createQuery("from DepartmentImpl dept where dept.code=:code and dept.instituteId=:instituteId ");
      query.setString("code", code);
      query.setInteger("instituteId", vs.getInstituteId());

      /* find now */
      Department dept = (Department)query.uniqueResult();
      /* check if only active department is needed */
      if (dept != null && onlyActive) {
	if (dept.getStatus() != Status.Active.ordinal()) {
	  return null;
	}
      }
      return dept;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find department with code: " + code + ", in institute with id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Set status of the department. This can be used to delete the Department
   * @param dept          The department object to be deleted
   * @param status        The status to be set
   * @param vs            The session object
   * @throws DataAccessException thrown when there is any error
   */
  public void setStatus(Department dept, Status status, VenusSession vs) throws DataAccessException  {
    if (dept == null || status == null) {
      return;
    }
    
    boolean update = false;
    if (status.ordinal() != dept.getStatus()) {
      dept.setStatus(status.ordinal());
      update = true;
    }

    /* delete if the status is different */
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
	log.debug("Changing the status for department: " + dept.getName() + ", in institute with id: " + dept.getInstituteId());
      }
      try {
	/* get the hibernate session */
	Session sess = vs.getHibernateSession();
	txn = sess.beginTransaction();
	sess.update(dept);
      }
      catch (HibernateException he) {
	String errStr = "Unable to change the status for department: " + dept.getName() + ", in institute with id: " + dept.getInstituteId();
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
   * Get all the departments in the institute
   * @param offset        The paging offset in the list
   * @param maxRet        Maximum number of objects to be returned
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active departments, defaults to true</li>
   *   <li>sortBy(String): if specified, the restults will be sorted by this field, defaults to created</li>
   *   <li>isAscending(Boolean): sort by ascending/descending, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the list of departments in an institute
   * @throws DataAccessException thrown when there is any error
   */
  public List<Department> getDepartments(int offset, int maxRet, Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* we will sort on 'created' by default for departments. Why? - God knows */
    String sortBy = OperationsUtilImpl.getStringValue("sortBy", options, "created");
    Boolean isAscending = OperationsUtilImpl.getBoolean("isAscending", options, OperationsUtil.DEFAULT_SORT_ORDER);
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(DepartmentImpl.class);

      /* set the institute id */
      c.add(Expression.eq("instituteId", vs.getInstituteId()));
      
      /* set the condition on status, if we need only active departments */
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
      String errStr = "Unable to get departments, with institute id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  

  /**
   * Get departments count in the institute
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active departments, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the total count of departments in the institute
   * @throws DataAccessException thrown when there is any error
   */
  public Integer getDepartmentsCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    /* count only active departments? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* filter options */
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(DepartmentImpl.class);

      /* set the institute id */
      c.add(Expression.eq("instituteId", vs.getInstituteId()));
      
      /* set the condition on status, if we need only active departments */
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
      String errStr = "Unable to get departments, with institute id: " + vs.getInstituteId();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

}
