package com.venus.dal.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.venus.model.impl.BaseModelImpl;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;
import com.venus.model.Status;
import com.venus.model.impl.VenusFilterImpl;
import com.venus.model.impl.VenusSortImpl;
import com.venus.model.Institute;


public class AbstractDalImpl {
  /* logger for logging */
  private final Logger log = Logger.getLogger(AbstractDalImpl.class);
  
  public Object createUpdate(String clName, Map<String, Object> niParams,
        Map<String, Object> mandatoryParams, Map<String, Object> optionalParams, Session session) throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException, DataAccessException, ClassNotFoundException, InstantiationException {

    /* Try to find if the object already exists with the given NI params */
    /* options to pass for finding object */
    Map<String, Object> opts = new HashMap<String, Object>(1);
    opts.put("onlyActive", Boolean.FALSE);

    /* check if the object exists or not */
    Object obj = find(clName, niParams, opts, session);
    
    if (obj != null) {
      obj = update(obj, mandatoryParams, optionalParams, session);
    }
    else {
      obj = create(clName, niParams, mandatoryParams, optionalParams, session);
    }
    return obj;
  }

  /* merge maps to one */
  private Map<String, Object> combineMaps(Map<String, Object>[] list) {
    Map<String, Object> params = new HashMap<String, Object>();
    for (Map<String, Object> map: list) {
      if (map != null) {
        params.putAll(map);
      }
    }
    return params;
  }
  
  public Object create(String clName, Map<String, Object> niParams,
      Map<String, Object> mandatoryParams, Map<String, Object> optionalParams, Session session) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, DataAccessException, ClassNotFoundException, InstantiationException {

    /* set the status object to Active if not provided */
    Status status = (Status) OperationsUtilImpl.getObject("status", optionalParams, Status.Active);
    if (optionalParams == null) {
      optionalParams = new HashMap<String, Object>();
    }
    optionalParams.put("status", status.ordinal());

    /* set created and lastModified dates if not provided */
    optionalParams = OperationsUtilImpl.setIfNotExists("created", optionalParams, new Date());
    optionalParams = OperationsUtilImpl.setIfNotExists("lastModified", optionalParams, new Date());

    
    /* Merge all the maps to one map */
    Map<String, Object>[] array = (Map<String, Object>[]) Array.newInstance(Map.class, 3);
    array[0] = niParams;
    array[1] = mandatoryParams;
    array[2] = optionalParams;
    Map<String, Object> params = combineMaps(array);

    /* Get the class's constructor object */
    Class classType = Class.forName(clName);
    Constructor cr = classType.getConstructor(null);
    Object clObj = cr.newInstance(null);
    
    if (params != null) {
      for (Map.Entry entry: params.entrySet()) {
        //System.out.println("The name: " + entry.getKey() + ", val: " + entry.getValue());
        String name = (String)entry.getKey();
        Object value = entry.getValue();
        Method m = classType.getMethod(getSetMethodName(name), new Class[] {value.getClass()});
        m.invoke(clObj, new Object[] {entry.getValue()});
        m = classType.getMethod(getGetMethodName(name), null);
        Object ret = m.invoke(clObj, null);
        //System.out.println("My value " + ret);
      }
    }
    
    /* begin the transaction and save the object */
    Transaction txn = null;
    try {
      txn = session.beginTransaction();
      session.save(clObj);
    }
    catch (HibernateException he) {
      String errStr = "Unable to create object";
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
    return clObj;
  }
  
  public Object update(Object clObj, Map<String, Object> mandatoryParams, Map<String, Object> optionalParams, Session session) throws NoSuchMethodException,
    IllegalAccessException, InvocationTargetException, DataAccessException {
    if (clObj == null) {
      throw new IllegalArgumentException("The class object should be provided");
    }
    
    /* set the status object to Active if not provided */
    Status status = (Status) OperationsUtilImpl.getObject("status", optionalParams, Status.Active);
    if (optionalParams == null) {
      optionalParams = new HashMap<String, Object>();
    }
    optionalParams.put("status", status.ordinal());
    
    boolean update = false;
    
    Map<String, Object>[] array = (Map<String, Object>[]) Array.newInstance(Map.class, 2);
    array[0] = mandatoryParams;
    array[1] = optionalParams;
    Map<String, Object> params = combineMaps(array);
    
    if(params != null) {
      
      for (Map.Entry entry: params.entrySet()) {
        //System.out.println("[Update]The name: " + entry.getKey() + ", val: " + entry.getValue());
        String name = (String)entry.getKey();
        Object value = entry.getValue();
        Method m = clObj.getClass().getMethod(getGetMethodName(name), null);
        Object ret = m.invoke(clObj, null);

        if (value != null && !value.equals(ret)) {
          //System.out.println("[Update] setting value.. " + value);
          m = clObj.getClass().getMethod(getSetMethodName(name),  new Class[] {value.getClass()});
          m.invoke(clObj, new Object[] {entry.getValue()});
        }
      }
    }
    
    /* is update needed? */
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Updating Obj");
      }
      Transaction txn = null;
      try {
        /* set the last modified date */
        Method m = clObj.getClass().getMethod(getSetMethodName("lastModified"), new Class[] {Date.class});
        m.invoke(clObj, new Object[] {new Date()});
        
        txn = session.beginTransaction();
        session.update(clObj);
      }
      catch (HibernateException he) {
        String errStr = "Unable to update Object ";
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
    
    return clObj;
  }
  
  
  public Object find(String clName, Map<String, Object> niParams, Map<String, Object> options, Session session)  throws DataAccessException, ClassNotFoundException {
    /* do we need to return only active institute? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);

    Class classType = Class.forName(clName);

    /* use naturalid restrictions to find the institute here */
    try {
      session.beginTransaction();
      Criteria criteria = session.createCriteria(classType);
      NaturalIdentifier naturalId = null;
      if (niParams != null) {
        for (Map.Entry entry: niParams.entrySet()) {
          if (naturalId != null) {
            naturalId = naturalId.set((String)entry.getKey(), entry.getValue());
          }
          else {
            naturalId = Restrictions.naturalId().set((String)entry.getKey(), entry.getValue());
          }
        }
      }
      if (naturalId != null) {
        criteria.add(naturalId);
      }
      criteria.setCacheable(false);
      BaseModelImpl obj = (BaseModelImpl) criteria.uniqueResult();
      /* check if only active obj is needed */
      if (obj != null && onlyActive) {
        if (obj.getStatus() != Status.Active.ordinal()) {
          return null;
        }
      }
      return obj;
    }
    catch (HibernateException he) {
      String errStr = "Unable to find object";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
  private void addSortOrderToCriteria(Criteria c, List<VenusSortImpl> sortList) {
    if (sortList != null && c != null) {
      for (VenusSortImpl sort: sortList) {
        if (sort != null) {
          String sortBy = sort.getSortBy();
          if (StringUtils.isNotBlank(sortBy)) {
            System.out.println("Sorting on : " + sortBy + ", order: " + sort.getIsAscending());
            c.addOrder(sort.getIsAscending()? Order.asc(sortBy) : Order.desc(sortBy));
          }
        }
      }
    }
  }
  
  private boolean addFiltersToCriteria(Criteria c, List<VenusFilterImpl> filterList) {
    /* to check if the status is provided in the filter list or not */
    boolean statusSet = false;

    /* add filters */
    if (filterList != null && c != null) {
      for (VenusFilterImpl filter: filterList) {
        if (filter != null) {
          String filterBy = filter.getFilterBy();
          if (StringUtils.isNotBlank(filterBy)) {
            String filterOp = filter.getFilterOp();
            Object filterValue = filter.getFilterValue();
            /* status needs special treatment, as we dont have backing object
             * for that in the DB */
            if (StringUtils.equals("status", filterBy)) {
              statusSet = true;
              filterValue = ((Status)filterValue).ordinal();
            }
            System.out.println("Got the op: " + filterOp + ", value: " + filterValue + ", by: " + filterBy);
            if (StringUtils.equals("equals", filterOp)) {
              /* see if the filter value is null. If yes,return only items with filterby as null */
              if (filterValue == null) {
                c.add(Restrictions.isNull(filterBy));
              }
              else {
                c.add(Restrictions.eq(filterBy, filterValue));
              }
            }
            else if (StringUtils.equals("notEquals", filterOp)) {
              /* see if the filter value is null. If yes,return only items with filterby as null */
              if (filterValue == null) {
                c.add(Restrictions.isNotNull(filterBy));
              }
              else {
                c.add(Restrictions.not(Expression.eq(filterBy, filterValue)));
              }                
            }
            else if (filterValue != null) {
              c.add(Restrictions.like(filterBy, "%" + filterValue + "%"));
            }
          }
        } 
      }
    }
    return statusSet;
  }
   
  public List get(String clName, int offset, int maxReturn, Map<String, Object> options, Session session)  throws DataAccessException, ClassNotFoundException {
    /* get the sort order list */
    List<VenusSortImpl> sortList = (List<VenusSortImpl>) OperationsUtilImpl.getObject("sortList", options, null);
    /* get the filters list */
    List<VenusFilterImpl> filterList = (List<VenusFilterImpl>) OperationsUtilImpl.getObject("filterList", options, null);
    
    Class classType = Class.forName(clName);

    /* use naturalid restrictions to find the institute here */
    try {
      session.beginTransaction();
      Criteria c = session.createCriteria(classType);
      
      /* add Sort Orders */
      addSortOrderToCriteria(c, sortList);
      /* add filters to the criteria (if provided) and check if the status is added too or not */
      boolean statusSet = addFiltersToCriteria(c, filterList);
      
 
      /* if objects not requested with particular status, return only active objects */
      if (!statusSet) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      
      /* set cachable to false for now */
      c.setCacheable(false);
      
      /* set pagination */
      c.setFirstResult(offset);
      c.setMaxResults(maxReturn);

      System.out.println("QUERY: " + c.toString());
      
      /* return the list */
      return c.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find object";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
  public Integer count(String clName, Map<String, Object> options, Session session)  throws DataAccessException, ClassNotFoundException {
    /* get the sort order list */
    List<VenusSortImpl> sortList = (List<VenusSortImpl>) OperationsUtilImpl.getObject("sortList", options, null);
    /* get the filters list */
    List<VenusFilterImpl> filterList = (List<VenusFilterImpl>) OperationsUtilImpl.getObject("filterList", options, null);
    
    Class classType = Class.forName(clName);

    /* use naturalid restrictions to find the institute here */
    try {
      session.beginTransaction();
      Criteria c = session.createCriteria(classType);
      
      /* add Sort Orders */
      addSortOrderToCriteria(c, sortList);
      /* add filters to the criteria (if provided) and check if the status is added too or not */
      boolean statusSet = addFiltersToCriteria(c, filterList);
      
 
      /* if objects not requested with particular status, return only active objects */
      if (!statusSet) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }
      
      /* set cachable to false for now */
      c.setCacheable(false);
      /* set the projection for the row count */
      c.setProjection(Projections.rowCount());
      
      System.out.println("COUNT QUERY: " + c.toString());
      
      /* return the list */
      return ((Number)c.uniqueResult()).intValue();
    }
    catch (HibernateException he) {
      String errStr = "Unable to count the number of objects for : " + clName;
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
  public void setStatus(BaseModelImpl obj, Status status, Session sess) throws DataAccessException  {
    if (obj == null || status == null) {
      throw new IllegalArgumentException("obj and Status must be supplied");
    }
    
    boolean update = false;
    if (status.ordinal() != obj.getStatus()) {
      obj.setStatus(status.ordinal());
      update = true;
    }

    /* delete if the status is different */
    Transaction txn = null;
    if (update) {
      if (log.isDebugEnabled()) {
        log.debug("Changing the status for obj: " + obj.getClass().getName());
      }
      try {
        obj.setLastModified(new Date());
        txn = sess.beginTransaction();
        sess.update(obj);
      }
      catch (HibernateException he) {
        String errStr = "Unable to change the status for obj: " + obj.getClass().getName();
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

  
  private String getSetMethodName(String param) {
    return "set" + StringUtils.capitalize(param);
  }
  private String getGetMethodName(String param) {
    return "get" + StringUtils.capitalize(param);
  }
  
}