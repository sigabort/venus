package com.venus.dal.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Status;
import com.venus.model.impl.CourseImpl;
import com.venus.model.impl.ProgramImpl;
import com.venus.model.impl.UserImpl;
import com.venus.model.impl.DepartmentImpl;
import com.venus.model.impl.InstituteImpl;
import com.venus.util.VenusSession;
import com.venus.dal.CourseDal;
import com.venus.dal.OperationsUtil;
import com.venus.dal.DataAccessException;

import org.apache.log4j.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Implementation of DAL Operations on courses.
 * 
 * @author sigabort
 */
public class CourseDalImpl implements CourseDal {

  private final static String COURSE_IMPL_CLASS = "com.venus.model.impl.CourseImpl";
  
  /* logger for logging */
  private final Logger log = Logger.getLogger(CourseDalImpl.class);
  private final AbstractDalImpl adi = new AbstractDalImpl();
  
  public CourseImpl createUpdateCourse(String code, String name,
      DepartmentImpl dept, UserImpl instructor,
      Map<String, Object> optionalParams, VenusSession vs)
      throws DataAccessException {

    if (StringUtils.isEmpty(code) || vs.getInstitute() == null) {
      throw new IllegalArgumentException("Code and Institute should be provided");
    }
    
    /* set the natural identifier parameters */
    Map<String, Object> niParams = new HashMap<String, Object>(2);
    niParams.put("code", code);
    niParams.put("institute", vs.getInstitute());

    CourseImpl course = null;
    
    try {
      /* Try to find if the object already exists with the given NI params */
      /* options to pass for finding object */
      Map<String, Object> opts = new HashMap<String, Object>(1);
      opts.put("onlyActive", Boolean.FALSE);

      course = (CourseImpl) adi.find(COURSE_IMPL_CLASS, niParams, opts, vs.getHibernateSession());

      /* set the mandatory parameters */
      Map<String, Object> mandatoryParams = new HashMap<String, Object>(3);
      mandatoryParams.put("name", name);
      mandatoryParams.put("department", dept);
      mandatoryParams.put("instructor", instructor);

      /* update the course if already existing */
      if (course != null) {
        course = (CourseImpl) adi.update(course, mandatoryParams, optionalParams, vs.getHibernateSession());
      }
      else {
        /* check if the mandatory params set or not */
        if (ArrayUtils.isEmpty(new Object[] {code, vs.getInstitute(), name, dept, instructor})) {
          throw new IllegalArgumentException("Mandatory parameter(s) missing");
        }
        /* if admin is not set, use instructor as admin */
        optionalParams = OperationsUtilImpl.setIfNotExists("admin", optionalParams, instructor);
        course = (CourseImpl) adi.create(COURSE_IMPL_CLASS, niParams, mandatoryParams, optionalParams, vs.getHibernateSession());
      }
    }
    catch (NoSuchMethodException nse) {
      throw new DataAccessException(nse);
    }
    catch (IllegalAccessException iae) {
      throw new DataAccessException(iae);
    }
    catch (InvocationTargetException ite) {
      throw new DataAccessException(ite);
    }
    catch (ClassNotFoundException cne) {
      throw new DataAccessException(cne);
    }
    catch (InstantiationException ie) {
      throw new DataAccessException(ie);
    }
    return course;
  }
  
  /**
   * Find the course given the code. By default, returns
   * only active course if not specified, otherwise
   * @param code     The code of the course 
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive: Boolean</li>
   * </ul>
   * @param vs       The venus session object
   * @return         The course object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public CourseImpl findCourse(String code, Map<String, Object> options, VenusSession vs)
      throws DataAccessException {
    /* code is null? throw error */
    if (StringUtils.isEmpty(code) || vs.getInstitute() == null) {
      throw new IllegalArgumentException("Code and Institute should be provided");
    }
    
    /* set the natural identifier parameters */
    Map<String, Object> niParams = new HashMap<String, Object>(2);
    niParams.put("code", code);
    niParams.put("institute", vs.getInstitute());
    
    CourseImpl course = null;
    
    try {
      course = (CourseImpl) adi.find(COURSE_IMPL_CLASS, niParams, options, vs.getHibernateSession());
    }
    catch (ClassNotFoundException cne) {
      throw new DataAccessException(cne);
    }
    return course;
  }
  
  
}