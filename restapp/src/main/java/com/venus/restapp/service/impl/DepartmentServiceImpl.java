package com.venus.restapp.service.impl;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Department;
import com.venus.dal.DepartmentOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.DepartmentOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.service.DepartmentService;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

@Service
public class DepartmentServiceImpl implements DepartmentService {
  private DepartmentOperations dol = new DepartmentOperationsImpl();
  private static final Logger log = Logger.getLogger(DepartmentService.class);

  public Department createUpdateDepartment(DepartmentRequest req, VenusSession vs) throws RestResponseException {
    String name = req.getName();
    Department dept = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", req.getCode());
    params.put("description", req.getDescription());
    params.put("photoUrl", req.getPhotoUrl());
    params.put("email", req.getEmail());
    
    try {
      dept  = dol.createUpdateDepartment(name, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while creating/updating department with name: " + name;
      log.error(errStr, dae);
      throw new RestResponseException(null, HttpStatus.INTERNAL_SERVER_ERROR, errStr);
    }
    catch (IllegalArgumentException iae) {
      throw new RestResponseException(null, HttpStatus.BAD_REQUEST, iae.getMessage());      
    }
    return dept;
  }


  public Department getDepartment(String name, BaseRequest request, VenusSession vs) throws RestResponseException {
    Department department = null;
    try {
      department = dol.findDepartmentByName(name, null, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting department with name: " + name;
      log.error(errStr, dae);
      throw new RestResponseException(null, HttpStatus.INTERNAL_SERVER_ERROR, errStr);
    }
    catch (IllegalArgumentException iae) {
      throw new RestResponseException(null, HttpStatus.BAD_REQUEST, iae.getMessage());
    }
    return department;
  }

  /**
   * Get the departments
   * @param request        The request parameter containing the optional parameters
   * @return               The list of departments
   * @throws RestResponseException if there is any error
   */
  public List<Department> getDepartments(BaseRequest request, VenusSession vs) throws RestResponseException {
    int offset = request.getStartIndex();
    int maxRet = request.getItemsPerPage();
    String sortBy = request.getSortBy();
    String sortOrder = request.getSortOrder();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();

    /* Time to parse the query parameters */
    Map<String, Object> params = null;
    /* if the sortBy option is set, pass it to the DAL layer to 
     * sort depends on the requested option
     */
    if (!StringUtils.isBlank(sortBy)) {
      if (params == null) {
	params = new HashMap<String, Object>();
      }
      params.put("sortBy", sortBy);
      /* check whether the order is asc/desc. Default is 'asc' */
      if (StringUtils.equals("descending", sortOrder)) {
	params.put("isAscending", Boolean.FALSE);
      }
      else {
	params.put("isAscending", Boolean.TRUE);
      }
    }
    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      if (params == null) {
	params = new HashMap<String, Object>();
      }
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the departments now */
    List<Department> departments = null;
    try {
      departments = dol.getDepartments(offset, maxRet, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting departments";
      log.error(errStr, dae);
      throw new RestResponseException(null, HttpStatus.INTERNAL_SERVER_ERROR, errStr);
    }    
    catch (IllegalArgumentException iae) {
      throw new RestResponseException(null, HttpStatus.BAD_REQUEST, iae.getMessage());
    }

    return departments;
  }

  /**
   * Get the departments count in the institute
   * @param request        The request parameter containing the optional parameters
   * @return               The count of total departments in the institute
   * @throws RestResponseException if there is any error
   */
  public Integer getDepartmentsCount(BaseRequest request, VenusSession vs) throws RestResponseException {
   /* Time to parse the query parameters */
    Boolean onlyActive = request.getOnlyActive();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();
    
    Map<String, Object> params = new HashMap<String, Object>();
    /* needed only count of active departments? */
    params.put("onlyActive", onlyActive);

    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the departments now */
    Integer count = null;
    try {
      count = dol.getDepartmentsCount(params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting departments count";
      log.error(errStr, dae);
      throw new RestResponseException(null, HttpStatus.INTERNAL_SERVER_ERROR, errStr);
    }
    catch (IllegalArgumentException iae) {
      throw new RestResponseException(null, HttpStatus.BAD_REQUEST, iae.getMessage());
    }

    return count;
  }
}
