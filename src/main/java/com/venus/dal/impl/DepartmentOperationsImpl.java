package com.venus.dal.impl;

import java.util.Date;
import java.util.List;

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.model.impl.DepartmentImpl;
import com.venus.util.VenusSession;
import com.venus.dal.DepartmentOperations;

import org.hibernate.Session;
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
  public Department createUpdateDepartment(String name, String code, String description, String photoUrl, String email, Date created, Date lastModified, VenusSession session) {
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
  private Department createDepartment(String name, String code, String description, String photoUrl, String email, Date created, Date lastModified, VenusSession session) {
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

    Session sess = session.getSession();
    sess.save(dept);
    return dept;
  }

  /* update the existing department */
  private Department updateDepartment(Department dept, String code, String description, String photoUrl, String email, Status status, VenusSession session) {
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
      dept.setLastModified(new Date());
      Session sess = session.getSession();
      sess.update(dept);
    }
    return dept;
  }

  /**
   * Find the department given the name
   */
  public Department findDepartmentByName(String name, boolean includeDeleted, VenusSession vs) {
    /* name is null? return null */
    if (name == null) {
      return null;
    }
    /*  */
    if (!includeDeleted) {
      return findDepartmentByName(name, vs);
    }

    /* get hibernate session */
    Session sess = vs.getSession();
    /* create query */
    Query query = sess.createQuery("from DepartmentImpl dept where dept.name=:name ");
    query.setString("name", name);
    /* find now */
    return (Department)query.uniqueResult();
  }

  /**
   * Find the department given the code
   */
  public Department findDepartmentByCode(String code, boolean includeDeleted, VenusSession vs) {
    /* code is null? return null */
    if (code == null) {
      return null;
    }
    /*  */
    if (!includeDeleted) {
      return findDepartmentByCode(code, vs);
    }

    /* get hibernate session */
    Session sess = vs.getSession();
    /* create query */
    Query query = sess.createQuery("from DepartmentImpl dept where dept.code=:code ");
    query.setString("code", code);
    /* find now */
    return (Department)query.uniqueResult();
  }

  /**
   * Find the department given the name
   */
  public Department findDepartmentByName(String name, VenusSession vs) {
    /* name is null? return null */
    if (name == null) {
      return null;
    }
    /* get hibernate session */
    Session sess = vs.getSession();
    /* find now */
    Query query = sess.getNamedQuery(FIND_DEPT_BY_NAME_STR);
    query.setString(0, name);
    return (Department)query.uniqueResult();
  }

  /**
   * Find the department given the code
   */
  public Department findDepartmentByCode(String code, VenusSession vs) {
    /* code is null? return null */
    if (code == null) {
      return null;
    }
    /* get hibernate session */
    Session sess = vs.getSession();
    /* find now */
    Query query = sess.getNamedQuery(FIND_DEPT_BY_CODE_STR);
    query.setString(0, code);
    return (Department)query.uniqueResult();
  }

  /**
   * Delete the department
   */
  public void deleteDepartment(Department dept, VenusSession vs) {
    if (dept == null) {
      return;
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Removing department: " + dept.getName());
    }
    
    /* set the status to deleted */
    dept.setStatus(Status.Deleted.ordinal());

    /* get the hibernate session */
    Session sess = vs.getSession();
    sess.update(dept);
  }

  /**
   * Get all the departments in the institute
   */
  public List<Department> getDepartments(int offset, int maxRet, VenusSession vs) {
    Session sess = vs.getSession();
    Query query = sess.createQuery("from DepartmentImpl dept where dept.status=:status");
    query.setInteger("status", Status.Active.ordinal());
    query.setFirstResult(offset);
    query.setMaxResults(maxRet);

    return query.list();
  }
  

}
