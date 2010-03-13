package com.venus.dal.impl;

import java.util.Date;
import java.util.List;

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
import org.hibernate.SQLQuery;

import org.apache.log4j.Logger;

public class DepartmentOperationsImpl implements DepartmentOperations {
  private String FIND_DEPT_BY_NAME_STR = "findDepartmentByName";
  private String FIND_DEPT_BY_CODE_STR = "findDepartmentByCode";

  private final Logger log = Logger.getLogger(DepartmentOperationsImpl.class);
  
  /**
   * Create / update department
   */
  public Department createUpdateDepartment(String name, String code, String description, String photoUrl, String email, Date created, Date lastModified, VenusSession session) throws DataAccessException {
    /* name is mandatory for the department create/update */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }

    /* see if the dept exists already */
    Department dept = findDepartmentByName(name, true, session);
    /* ok, department exists. Update it */
    if (dept != null) {
      dept = updateDepartment(dept, code, description, photoUrl, email, Status.Active, session);
    }
    else {
      dept = createDepartment(name, code, description, photoUrl, email, created, lastModified, session);
    }

    return dept;
  }

  /* create new department */
  private Department createDepartment(String name, String code, String description, String photoUrl, String email, Date created, Date lastModified, VenusSession session) throws DataAccessException {
    /* name is mandatory for the department create */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }

    Department dept = new DepartmentImpl();
    if (name != null) {
      dept.setName(name);
    }

    if (code != null) {
      dept.setCode(code);
    }
    /* code is not supplied, name will be used as code */
    else {
      dept.setCode(name);
    }

    if (description != null) {
      dept.setDescription(description);
    }

    if (photoUrl != null) {
      dept.setPhotoUrl(photoUrl);
    }

    if (email != null) {
      dept.setEmail(email);
    }
    
    dept.setStatus(Status.Active.ordinal());
    dept.setCreated((created != null)? created : new Date());
    dept.setLastModified((lastModified != null)? lastModified : new Date());

    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(dept);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create department: " + name;
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

  /* update the existing department */
  private Department updateDepartment(Department dept, String code, String description, String photoUrl, String email, Status status, VenusSession session) throws DataAccessException {
    if (dept == null) {
      return null;
    }
    boolean update = false;

    String oldCode = dept.getCode();
    if (oldCode == null || !oldCode.equals(code)) {
      dept.setCode(code);
      update = true;
    }

    String oldDescription = dept.getDescription();
    if (oldDescription == null || !oldDescription.equals(description)) {
      dept.setDescription(description);
      update = true;
    }

    String oldPhotoUrl = dept.getPhotoUrl();
    if (oldPhotoUrl == null || !oldPhotoUrl.equals(photoUrl)) {
      dept.setPhotoUrl(photoUrl);
      update = true;
    }

    String oldEmail = dept.getEmail();
    if (oldEmail == null || !oldEmail.equals(email)) {
      dept.setEmail(email);
      update = true;
    }

    Integer oldStatus = dept.getStatus();
    if (oldStatus == null || !oldStatus.equals(status.ordinal())) {
      dept.setStatus(status.ordinal());
      update = true;
    }

    if (update) {
      Transaction txn = null;
      try {
	dept.setLastModified(new Date());
	Session sess = session.getHibernateSession();
	txn = sess.beginTransaction();
	sess.update(dept);
      }
      catch (HibernateException he) {
	String errStr = "Unable to update department: " + dept.getName();
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
