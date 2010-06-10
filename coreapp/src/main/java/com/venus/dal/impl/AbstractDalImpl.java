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
import org.hibernate.criterion.Restrictions;

import com.venus.model.BaseModel;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;
import com.venus.model.Status;
import com.venus.model.impl.VenusFilterImpl;
import com.venus.model.impl.VenusSortImpl;


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
      BaseModel obj = (BaseModel) criteria.uniqueResult();
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
  
  
  public List<Object> get(String clName, int offset, int maxReturn, Map<String, Object> options, Session session)  throws DataAccessException, ClassNotFoundException {
    /* do we need to return only active institute? */
    Boolean onlyActive = OperationsUtilImpl.getBoolean("onlyActive", options, Boolean.TRUE);
    List<VenusSortImpl> sortList = (List<VenusSortImpl>) OperationsUtilImpl.getObject("sortList", options, null);
    List<VenusFilterImpl> filterList = (List<VenusFilterImpl>) OperationsUtilImpl.getObject("filterList", options, null);
    
    Class classType = Class.forName(clName);

    /* use naturalid restrictions to find the institute here */
    try {
      session.beginTransaction();
      Criteria c = session.createCriteria(classType);
      
      /* set the condition on status, if we need only active institutes */
      if (onlyActive) {
        c.add(Expression.eq("status", Status.Active.ordinal()));
      }

      /* add Sort Orders */
      if (sortList != null) {
        for (VenusSortImpl sort: sortList) {
          if (sort != null) {
            String sortBy = sort.getSortBy();
            if (StringUtils.isNotBlank(sortBy)) {
              c.addOrder(sort.getIsAscending()? Order.asc(sortBy) : Order.desc(sortBy));
            }
          }
        }
      }
      
      /* add filters */
      if (filterList != null) {
        for (VenusFilterImpl filter: filterList) {
          if (filter != null) {
            String filterBy = filter.getFilterBy();
            if (StringUtils.isNotBlank(filterBy)) {
              String filterOp = filter.getFilterOp();
              Object filterValue = filter.getFilterValue();
              if (StringUtils.equals("equals", filterOp)) {
                /* see if the filter value is null. If yes,return only items with filterby as null */
                if (filterValue == null) {
                  c.add(Restrictions.isNull(filterBy));
                }
                else {
                  c.add(Restrictions.eq(filterBy, filterValue));
                }
              }
              else if (filterValue != null) {
                c.add(Restrictions.like(filterBy, "%" + filterValue + "%"));
              }
            }
          } 
        }
      }
      /* set cachable to false for now */
      c.setCacheable(false);
      
      /* set pagination */
      c.setFirstResult(offset);
      c.setMaxResults(maxReturn);

      /* return the list */
      return c.list();
    }
    catch (HibernateException he) {
      String errStr = "Unable to find object";
      log.error(errStr, he);
      throw new DataAccessException(errStr, he);
    }
  }
  
  
  
  
  private String getSetMethodName(String param) {
    return "set" + StringUtils.capitalize(param);
  }
  private String getGetMethodName(String param) {
    return "get" + StringUtils.capitalize(param);
  }
  
}