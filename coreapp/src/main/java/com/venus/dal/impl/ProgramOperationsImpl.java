package com.venus.dal.impl;

import java.util.Date;
import java.util.List;

import com.venus.model.Department;
import com.venus.model.Program;
import com.venus.model.Status;
import com.venus.model.impl.ProgramImpl;
import com.venus.util.VenusSession;
import com.venus.dal.ProgramOperations;
import com.venus.dal.DataAccessException;

import org.hibernate.Criteria;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Expression;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import org.apache.log4j.Logger;

public class ProgramOperationsImpl implements ProgramOperations {
  private String FIND_PROGRAM_BY_CODE_STR = "findProgramByCode";

  private final Logger log = Logger.getLogger(ProgramOperationsImpl.class);
  
  /**
   * Create / update program
   */
  public Program createUpdateProgram(String name, Department dept, String code, String description, String prerequisites, Integer duration, Date created, Date lastModified, VenusSession session) throws DataAccessException {
    /* name is mandatory for the program create/update */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }

    /* see if the program exists already */
    Program program = findProgramByName(name, dept, true, session);
    /* ok, program exists. Update it */
    if (program != null) {
      program = updateProgram(program, code, description, prerequisites, duration, Status.Active, session);
    }
    else {
      program = createProgram(name, dept, code, description, prerequisites, duration, created, lastModified, session);
    }

    return program;
  }

  /* create new program */
  private Program createProgram(String name, Department dept, String code, String description, String prerequisites, Integer duration, Date created, Date lastModified, VenusSession session) throws DataAccessException {
    /* name is mandatory for the program create */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }
    if (dept == null) {
      throw new IllegalArgumentException("department must be supplied");
    }
    
    Program program = new ProgramImpl();
    program.setName(name);
    program.setDepartment(dept);

    if (code != null) {
      program.setCode(code);
    }

    if (description != null) {
      program.setDescription(description);
    }

    if (prerequisites != null) {
      program.setPrerequisites(prerequisites);
    }

    if (duration != null) {
      program.setDuration(duration);
    }
    
    program.setStatus(Status.Active.ordinal());
    program.setCreated((created != null)? created : new Date());
    program.setLastModified((lastModified != null)? lastModified : new Date());

    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(program);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create program: " + name + ", for dept: " + dept.getName();
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
    return program;
  }

  /* update the existing program */
  private Program updateProgram(Program program, String code, String description, String prerequisites, Integer duration, Status status, VenusSession session) throws DataAccessException {
    if (program == null) {
      return null;
    }
    boolean update = false;

    String oldCode = program.getCode();
    if (oldCode == null || !oldCode.equals(code)) {
      program.setCode(code);
      update = true;
    }

    String oldDescription = program.getDescription();
    if (oldDescription == null || !oldDescription.equals(description)) {
      program.setDescription(description);
      update = true;
    }

    String oldPrerequisites = program.getPrerequisites();
    if (oldPrerequisites == null || !oldPrerequisites.equals(prerequisites)) {
      program.setPrerequisites(prerequisites);
      update = true;
    }

    Integer oldDuration = program.getDuration();
    if (oldDuration == null || !oldDuration.equals(duration)) {
      program.setDuration(duration);
      update = true;
    }

    Integer oldStatus = program.getStatus();
    if (oldStatus == null || !oldStatus.equals(status.ordinal())) {
      program.setStatus(status.ordinal());
      update = true;
    }

    if (update) {
      Transaction txn = null;
      try {
	program.setLastModified(new Date());
	Session sess = session.getHibernateSession();
	txn = sess.beginTransaction();
	sess.update(program);
      }
      catch (HibernateException he) {
	String errStr = "Unable to update program: " + program.getName() + ", in dept: " + program.getDepartment().getName();
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
    return program;
  }

  /**
   * Find the program given the name
   */
  public Program findProgramByName(String name, Department dept, boolean includeDeleted, VenusSession vs)  throws DataAccessException {
    /* name/dept is null? return null */
    if (name == null || dept == null) {
      return null;
    }

    try {
      Criteria criteria = vs.getHibernateSession().createCriteria(ProgramImpl.class);
      NaturalIdentifier naturalId = Restrictions.naturalId().set("name", name);
      naturalId.set("department", dept);
      
      criteria.add(naturalId);
      criteria.setCacheable(false);
      Program program = (ProgramImpl) criteria.uniqueResult();
      if (program != null && !includeDeleted) {
	if (program.getStatus() == Status.Deleted.ordinal()) {
	  return null;
	}
      }
      return program;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find program with name: " + name + ", in dept: " + dept.getName();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the program given the name
   */
  public Program findProgramByName(String name, Department dept, VenusSession vs)  throws DataAccessException {
    /* name/dept is null? return null */
    if (name == null || dept == null) {
      return null;
    }
    return findProgramByName(name, dept, false, vs);
  }

  /**
   * Find the program given the code
   */
  public Program findProgramByCode(String code, VenusSession vs) throws DataAccessException  {
    /* code is null? return null */
    if (code == null) {
      return null;
    }
    
    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* find now */
      Query query = sess.getNamedQuery(FIND_PROGRAM_BY_CODE_STR);
      query.setString(0, code);
      return (Program)query.uniqueResult();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find program with code: " + code;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Delete the program
   */
  public void deleteProgram(Program program, VenusSession vs) throws DataAccessException  {
    if (program == null) {
      return;
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Deleting program: " + program.getName());
    }
    
    /* set the status to deleted */
    program.setStatus(Status.Deleted.ordinal());

    Transaction txn = null;
    try {
      /* get the hibernate session */
      Session sess = vs.getHibernateSession();
      txn = sess.beginTransaction();
      sess.update(program);
    }
    catch (HibernateException he) {
      String errStr = "Unable to delete program: " + program.getName();
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
   * Get all the programs in the institute
   */
  public List<Program> getPrograms(Department dept, int offset, int maxRet, VenusSession vs)  throws DataAccessException {
    try {
      Session sess = vs.getHibernateSession();
      Query query = sess.createQuery("FROM ProgramImpl program WHERE program.department=:dept AND program.status=:status");
      query.setInteger("status", Status.Active.ordinal());
      query.setEntity("dept", dept);
      query.setFirstResult(offset);
      query.setMaxResults(maxRet);
      
      return query.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get programs for dept: " + dept.getName();
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  

}
