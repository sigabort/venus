package com.venus.restapp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.impl.CourseImpl;
import com.venus.model.impl.UserImpl;
import com.venus.model.impl.DepartmentImpl;
import com.venus.dal.CourseDal;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.CourseDalImpl;
import com.venus.util.VenusSession;

import com.venus.restapp.request.CourseRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.service.CourseService;
import com.venus.restapp.service.UserService;
import com.venus.restapp.service.DepartmentService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

@Service
public class CourseServiceImpl implements CourseService {
  @Autowired
  DepartmentService deptService;

  @Autowired
  UserService userService;
  
  private CourseDal iol = new CourseDalImpl();
  private static final Logger log = Logger.getLogger(CourseService.class);

  public CourseImpl createUpdateCourse(CourseRequest request, VenusSession vs) throws ResponseException {
    CourseImpl course = null;
    String code = request.getCode();
    String name = request.getName();
    String departmentName = request.getDepartment();
    String instructorName = request.getInstructor();
    String adminName = request.getAdmin();
    
    /* check if the department exists */
    DepartmentImpl department = (DepartmentImpl) deptService.getDepartment(departmentName, null, vs);
    if (department == null) {
      throw new IllegalArgumentException("Department " + departmentName + " doesn't exist");
    }
    
    /* check if the instructor / admin exist */
    UserImpl instructor = (UserImpl) userService.getUser(instructorName, null, vs);
    if (instructor == null) {
      throw new IllegalArgumentException("Instructor: " + instructorName  + " doesn't exist");
    }

    UserImpl admin = null;
    if (StringUtils.isNotEmpty(adminName)) {
      if(StringUtils.equals(adminName, instructorName)) {
        admin = instructor;
      }
      else {
        admin = (UserImpl) userService.getUser(adminName, null, vs);
      }
      if (admin == null) {
        throw new IllegalArgumentException("Admin: " + adminName + ", doesn't exist");
      }
    }
    Map<String, Object> params = courseReqToMap(request); 
    params.put("admin", admin);
    
    /* create/update the course now */
    try {
      course  = iol.createUpdateCourse(code, name, department, instructor, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while creating/updating course with name: " + name;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return course;
  }
  

//  public CourseImpl getCourse(String name, BaseRequest request, VenusSession vs) throws ResponseException {
//    VenusSession vs = VenusSessionFactory.getVenusSession(null);
//    CourseImpl course = null;
//    try {
//      course = iol.findCourseByName(name, null, vs);
//    }
//    catch (DataAccessException dae) {
//      String errStr = "Error while getting course with name: " + name;
//      log.error(errStr, dae);
//      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
//    }
//    return course;
//  }
//
//  /**
//   * Get the courses
//   * @param request        The request parameter containing the optional parameters
//   * @return               The list of courses
//   * @throws ResponseException if there is any error
//   */
//  public List<Course> getCourses(BaseRequest request, VenusSession vs) throws ResponseException {
//    VenusSession vs = VenusSessionFactory.getVenusSession(null);
//    int offset = request.getStartIndex();
//    int maxRet = request.getItemsPerPage();
//    String sortBy = request.getSortBy();
//    String sortOrder = request.getSortOrder();
//    String filterBy = request.getFilterBy();
//    String filterValue = request.getFilterValue();
//    String filterOp = request.getFilterOp();
//
//    /* Time to parse the query parameters */
//    Map<String, Object> params = null;
//    /* if the sortBy option is set, pass it to the DAL layer to 
//     * sort depends on the requested option
//     */
//    if (!StringUtils.isBlank(sortBy)) {
//      if (params == null) {
//        params = new HashMap<String, Object>();
//      }
//      params.put("sortBy", sortBy);
//      /* check whether the order is asc/desc. Default is 'asc' */
//      if (StringUtils.equals("descending", sortOrder)) {
//        params.put("isAscending", Boolean.FALSE);
//      }
//      else {
//        params.put("isAscending", Boolean.TRUE);
//      }
//    }
//    /* if the filterBy option is set, pass it to the DAL layer to 
//     * filter depends on the filterValue
//     */
//    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
//      if (params == null) {
//        params = new HashMap<String, Object>();
//      }
//      params.put("filterBy", filterBy);
//      params.put("filterValue", filterValue);
//      params.put("filterOp", filterOp);
//    }
//
//    /* get the courses now */
//    List<Course> courses = null;
//    try {
//      courses = iol.getCourses(offset, maxRet, params, vs);
//    }
//    catch (DataAccessException dae) {
//      String errStr = "Error while getting courses";
//      log.error(errStr, dae);
//      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
//    }
//    return courses;
//  }
//
//  /**
//   * Get the courses count
//   * @param request        The request parameter containing the optional parameters
//   * @return               The count of total courses in the course
//   * @throws ResponseException if there is any error
//   */
//  public Integer getCoursesCount(BaseRequest request, VenusSession vs) throws ResponseException {
//    VenusSession vs = VenusSessionFactory.getVenusSession(null);
//   /* Time to parse the query parameters */
//    Boolean onlyActive = request.getOnlyActive();
//    String filterBy = request.getFilterBy();
//    String filterValue = request.getFilterValue();
//    String filterOp = request.getFilterOp();
//    
//    Map<String, Object> params = new HashMap<String, Object>();
//    /* needed only count of active courses? */
//    params.put("onlyActive", onlyActive);
//
//    /* if the filterBy option is set, pass it to the DAL layer to 
//     * filter depends on the filterValue
//     */
//    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
//      params.put("filterBy", filterBy);
//      params.put("filterValue", filterValue);
//      params.put("filterOp", filterOp);
//    }
//
//    /* get the courses now */
//    Integer count = null;
//    try {
//      count = iol.getCoursesCount(params, vs);
//    }
//    catch (DataAccessException dae) {
//      String errStr = "Error while getting courses count";
//      log.error(errStr, dae);
//      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
//    }
//    return count;
//  }
//  
  
  private Map<String, Object> courseReqToMap(CourseRequest req) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("description", req.getDescription());
    params.put("photoUrl", req.getPhotoUrl());
    return params;
  }
  
}
