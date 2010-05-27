package com.venus.dal.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Institute;
import com.venus.model.Status;
import com.venus.model.impl.InstituteImpl;
import com.venus.util.VenusSession;
import com.venus.dal.InstituteOperations;
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
 * Implementation of operations on institutes .
 * 
 * @author sigabort
 */
public class InstituteOperationsImpl implements InstituteOperations {

  /* logger for logging */
  private final Logger log = Logger.getLogger(InstituteOperationsImpl.class);
  
  /**
   * Create or Update institute
   * @param name            The name of the institute. This should be unique
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the institute. If not
   *       specified, name will be used as code 
   *   </li>
   *   <li>displayName(String): The name of the institute used for displaying on web pages</li>
   *   <li>description(String): The description of the institute</li>
   *   <li>photoUrl(String): The photourl for the institute</li>
   *   <li>email(String): The email of the institute (may be admin mail-id)</li>
   *   <li>parent({@link Institute}): The parent of the institute</li>
   *   <li>status(Status): The status of the institute</li>
   *   <li>created(Date): The created date of the institute</li>
   *   <li>lastModified(Date): The last modified date of the institute</li>
   * </ul>
   * @param session          The venus session object consisting of institute, hibernate session
   * @return                 The created/updated institute object
   * @throws DataAccessException thrown when there is any error
   */
  public Institute createUpdateInstitute(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    /* name is mandatory for the institute create/update */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }

    /* options to pass for finding institute */
    Map<String, Object> opts = new HashMap<String, Object>(1);
    opts.put("onlyActive", Boolean.FALSE);

    /* see if the institute exists already */
    Institute institute = findInstituteByName(name, opts, session);
    
    /* ok, institute exists. Update it */
    if (institute != null) {
      /* if status is supplied, use it. Otherwise, we need to set it to Status.Active to make
       * sure we are updating the active object
       */
      Status status = OperationsUtilImpl.getStatus("status", optionalParams, Status.Active);
      if (optionalParams == null) {
        optionalParams = new HashMap<String, Object>(1);
      }
      optionalParams.put("status", (Object)status);

      /* ok, time to update institute */
      institute = updateInstitute(institute, optionalParams, session);
    }
    else {
      /* object doesn't exist, create it */
      institute = createInstitute(name, optionalParams, session);
    }

    return institute;
  }

  /**
   * Check if the code is already used by another institute
   * @param code           The code to check whether it is used by other institute or not
   * @param currentInstitute    The current institute to check if this institute holds it or not. If 
   *                       current institute is not null, and code is held by this institute, returns false
   * @param session        The {@link VenusSession} object holding context
   * @return               true if code is already used by other institute, false otherwise
   * @throws HibernateException        thrown when there is any error while fetching data
   * @throws IllegalArgumentException  thrown if the data passed is wrong
   */
  private boolean isCodeAlreadyUsed(String code, Institute currentInstitute, VenusSession session) throws DataAccessException, IllegalArgumentException {
    if (!StringUtils.isEmpty(code)) {
      /*
       * If the current institute is provided, check if this is used by the current institute. If so, return false.
       */
      if (currentInstitute != null) {
        if (code.equals(currentInstitute.getCode())) {
          return false;
        }
      }
      /* we need to check the non-active institutes too */
      Map<String, Object> options = new HashMap<String, Object>();
      options.put("onlyActive", Boolean.FALSE);
      Institute existingInstitute = findInstituteByCode(code, options, session);
      if (existingInstitute != null) {
        return true;
      }
    }
    return false;
  }

  
  /**
   * Create institute
   * @param name            The name of the institute. This should be unique
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the institute. If not
   *       specified, name will be used as code 
   *   </li>
   *   <li>displayName(String): The name of the institute used for displaying on web pages</li>
   *   <li>description(String): The description of the institute</li>
   *   <li>photoUrl(String): The photourl for the institute</li>
   *   <li>email(String): The email of the institute (may be admin mail-id)</li>
   *   <li>parent({@link Institute}): The parent of the institute</li>
   *   <li>status(Status): The status of the institute</li>
   *   <li>created(Date): The created date of the institute</li>
   *   <li>lastModified(Date): The last modified date of the institute</li>
   * </ul>
   * @param session          The venus session object consisting of institute, hibernate session
   * @return                 The created/updated institute object
   * @throws DataAccessException thrown when there is any error
   */
  private Institute createInstitute(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    /* name is mandatory for the institute create */
    if (name == null) {
      throw new IllegalArgumentException("name must be supplied");
    }
    
    if (log.isDebugEnabled()) {
      log.debug("Creating Institute with name: " + name);
    }

    /* create new object */
    Institute institute = new InstituteImpl();
    institute.setName(name);

    /* code - uniquely identifying the institute. If not set, use name for now */
    String code = OperationsUtilImpl.getStringValue("code", optionalParams, name);
    /* check if the institute is trying to set the code which is already
     * used by other users. If so, throw an error:
     * we have unique keys on: name, code
     * Adding duplicate will result into constraint violation error.
     */      
    if (isCodeAlreadyUsed(code, null, session)) {
     throw new IllegalArgumentException("Institute with code " + code + " already exists");
    }

    /****** set the optional parameters now *******/
    institute.setCode(code);
    /* description of the institute */
    institute.setDisplayName(OperationsUtilImpl.getStringValue("displayName", optionalParams, null));
    institute.setDescription(OperationsUtilImpl.getStringValue("description", optionalParams, null));
    institute.setPhotoUrl(OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null));
    institute.setEmail(OperationsUtilImpl.getStringValue("email", optionalParams, null));
    institute.setParent((Institute) OperationsUtilImpl.getObject("parent", optionalParams, null));
    /* status should be active if not specified */
    institute.setStatus(OperationsUtilImpl.getStatus("status", optionalParams, Status.Active).ordinal());
    /* set the created and lastmodified dates */
    institute.setCreated(OperationsUtilImpl.getDate("created", optionalParams, new Date()));
    institute.setLastModified(OperationsUtilImpl.getDate("lastModified", optionalParams, new Date()));    

    /* begin the transaction and save the object */
    Transaction txn = null;
    try {
      Session sess = session.getHibernateSession();
      txn = sess.beginTransaction();
      sess.save(institute);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create institute: " + name;
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
    return institute;
  }

  /**
   * @param name            The name of the institute. This should be unique
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the institute. If not
   *       specified, name will be used as code 
   *   </li>
   *   <li>displayName(String): The name of the institute used for displaying on web pages</li>
   *   <li>description(String): The description of the institute</li>
   *   <li>photoUrl(String): The photourl for the institute</li>
   *   <li>email(String): The email of the institute (may be admin mail-id)</li>
   *   <li>parent({@link Institute}): The parent of the institute</li>
   *   <li>status(Status): The status of the institute</li>
   *   <li>created(Date): The created date of the institute</li>
   *   <li>lastModified(Date): The last modified date of the institute</li>
   * </ul>
   * @param session          The venus session object consisting of institute, hibernate session
   * @return                 The created/updated institute object
   * @throws DataAccessException thrown when there is any error
   */
  private Institute updateInstitute(Institute institute, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException {
    if (institute == null) {
      throw new IllegalArgumentException("Update: Institute must be supplied");
    }
    boolean update = false;

    String code = OperationsUtilImpl.getStringValue("code", optionalParams, null);
    /* check if the institute is trying to set the code which is already
     * used by other users. If so, throw an error:
     * we have unique keys on: name, code
     * Adding duplicate will result into constraint violation error.
     */      
    if (isCodeAlreadyUsed(code, institute, session)) {
     throw new IllegalArgumentException("Institute with code " + code + " already exists");
    }
    if (code != null && !code.equals(institute.getCode())) {
      institute.setCode(code);
      update = true;
    }

    if (optionalParams != null) {
      /* check the displayName */
      String newDisplayName = OperationsUtilImpl.getStringValue("displayName", optionalParams, null);
      if (newDisplayName != null && !newDisplayName.equals(institute.getDisplayName())) {
        institute.setDisplayName(newDisplayName);
        update = true;
      }

      /* check the description */
      String newDescription = OperationsUtilImpl.getStringValue("description", optionalParams, null);
      if (newDescription != null && !newDescription.equals(institute.getDescription())) {
        institute.setDescription(newDescription);
        update = true;
      }

      /* check the photoUrl */
      String newPhotoUrl = OperationsUtilImpl.getStringValue("photoUrl", optionalParams, null);
      if (newPhotoUrl != null && !newPhotoUrl.equals(institute.getPhotoUrl())) {
        institute.setPhotoUrl(newPhotoUrl);
        update = true;
      }
      
      /* check the email */
      String newEmail = OperationsUtilImpl.getStringValue("email", optionalParams, null);
      if (newEmail != null && !newEmail.equals(institute.getEmail())) {
        institute.setEmail(newEmail);
        update = true;
      }
      
      /* check the Status */
      Status newStatus = OperationsUtilImpl.getStatus("status", optionalParams, null);
      if (newStatus != null && newStatus.ordinal() != institute.getStatus()) {
        institute.setStatus(newStatus.ordinal());
        update = true;
      }
    }

    /* is update needed? */
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Updating Institute with name: " + institute.getName());
      }
      Transaction txn = null;
      try {
        /* set the last modified date */
        institute.setLastModified(new Date());
        Session sess = session.getHibernateSession();
        txn = sess.beginTransaction();
        sess.update(institute);
      }
      catch (HibernateException he) {
        String errStr = "Unable to update institute: " + institute.getName();
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
    return institute;
  }
  
  /**
   * Find the institute given the name. By default, returns
   * only active institute if not specified
   * @param name     The name of the institute 
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive: Boolean</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The institute object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public Institute findInstituteByName(String name, Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    /* name is null? throw error */
    if (name == null) {
      throw new IllegalArgumentException("findInstituteByName: institute name must be supplied");
    }

    /* do we need to return only active institute? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding Institute with name: " + name);
    }

    /* use naturalid restrictions to find the institute here */
    try {
      Criteria criteria = vs.getHibernateSession().createCriteria(InstituteImpl.class);
      NaturalIdentifier naturalId = Restrictions.naturalId().set("name", name);
      
      criteria.add(naturalId);
      criteria.setCacheable(false);
      Institute institute = (InstituteImpl) criteria.uniqueResult();
      /* check if only active institute is needed */
      if (institute != null && onlyActive) {
        if (institute.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return institute;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find institute with name: " + name;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Find the institute given the code. By default, returns
   * only active institute if not specified
   * @param code     The code of the institute 
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive: Boolean</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The institute object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public Institute findInstituteByCode(String code, Map<String, Object> options, VenusSession vs) throws DataAccessException  {
    /* code is null? throw error */
    if (code == null) {
      throw new IllegalArgumentException("findInstituteByCode: institute code must be supplied");
    }

    /* do we need to return only active institute? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    if (log.isDebugEnabled()) {
      log.debug("Finding Institute with code: " + code);
    }

    try {
      /* get hibernate session */
      Session sess = vs.getHibernateSession();
      /* create query */
      Query query = sess.createQuery("from InstituteImpl institute where institute.code=:code ");
      query.setString("code", code);

      /* find now */
      Institute institute = (Institute)query.uniqueResult();
      /* check if only active institute is needed */
      if (institute != null && onlyActive) {
        if (institute.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return institute;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find institute with code: " + code;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }

  /**
   * Set status of the institute. This can be used to delete the Institute
   * @param institute          The institute object for which the status needs to be changed
   * @param status        The status to be set
   * @param vs            The session object
   * @throws DataAccessException thrown when there is any error
   */
  public void setStatus(Institute institute, Status status, VenusSession vs) throws DataAccessException  {
    if (institute == null || status == null) {
      throw new IllegalArgumentException("Institute and Status must be supplied");
    }
    
    boolean update = false;
    if (status.ordinal() != institute.getStatus()) {
      institute.setStatus(status.ordinal());
      update = true;
    }

    /* delete if the status is different */
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Changing the status for institute: " + institute.getName());
      }
      try {
        institute.setLastModified(new Date());
        /* get the hibernate session */
        Session sess = vs.getHibernateSession();
        txn = sess.beginTransaction();
        sess.update(institute);
      }
      catch (HibernateException he) {
        String errStr = "Unable to change the status for institute: " + institute.getName();
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
   * Get all the institutes
   * @param offset        The paging offset in the list
   * @param maxRet        Maximum number of objects to be returned
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active institutes, defaults to true</li>
   *   <li>parent({@link Department}): return only parent's child institutes, defaults to null</li>
   *   <li>sortBy(String): if specified, the restults will be sorted by this field, defaults to created</li>
   *   <li>isAscending(Boolean): sort by ascending/descending, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the list of institutes in an institute
   * @throws DataAccessException thrown when there is any error
   */
  public List<Institute> getInstitutes(int offset, int maxRet, Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* we will sort on 'created' by default for institutes. Why? - God knows */
    String sortBy = OperationsUtilImpl.getStringValue("sortBy", options, "created");
    Boolean isAscending = OperationsUtilImpl.getBoolean("isAscending", options, OperationsUtil.DEFAULT_SORT_ORDER);
    /* see if the parent institute is set. If yes, only the children of parent institute are sent */
    Institute parent = (Institute) OperationsUtilImpl.getObject("parent", options, null);
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(InstituteImpl.class);

      /* set the condition on status, if we need only active institutes */
      if (onlyActive) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      
      /* if sortBy is specified by, add order */
      if (StringUtils.isNotBlank(sortBy)) {
        c.addOrder(isAscending? Order.asc(sortBy) : Order.desc(sortBy));
      }
      /* see if only parent institute's children are to be returned */
      if (parent != null) {
        c.add(Expression.eq("parent", parent));
      }
      
      /*XXX: Add filtering*/
      
      /* set pagination */
      c.setFirstResult(offset);
      c.setMaxResults(maxRet);

      /* return the list */
      return c.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get institutes";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  

  /**
   * Get institutes count 
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active institutes, defaults to true</li>
   *   <li>parent({@link Department}): return only parent's child institutes, defaults to null</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the total count of institutes 
   * @throws DataAccessException thrown when there is any error
   */
  public Integer getInstitutesCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException {
    /* count only active institutes? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    /* filter options */
    String filterBy = OperationsUtilImpl.getStringValue("filterBy", options, null);
    String filterValue = OperationsUtilImpl.getStringValue("filterValue", options, null);
    String filterOp = OperationsUtilImpl.getStringValue("filterOp", options, OperationsUtil.DEFAULT_FILTER_OP);
    /* see if the parent institute is set. If yes, only the children count of parent institute is sent */
    Institute parent = (Institute) OperationsUtilImpl.getObject("parent", options, null);

    try {
      /* use criteria for efficiency */
      Session sess = vs.getHibernateSession();
      Criteria c = sess.createCriteria(InstituteImpl.class);
      
      /* set the condition on status, if we need only active institutes */
      if (onlyActive) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      /* see if only parent institute's children count is to be returned */
      if (parent != null) {
        c.add(Expression.eq("parent", parent));
      }
      
      /* set the projection for the row count */
      c.setProjection(Projections.rowCount());
      
      /*XXX: Add filtering*/

      /* return the count */
      return ((Number)c.uniqueResult()).intValue();
    }
    catch (HibernateException he) {
      String errStr = "Unable to get institutes count" + ((parent != null)? (" for institute: " + parent.getName()) : "");
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }


}