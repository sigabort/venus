package com.venus.controller.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Department;
import com.venus.model.impl.DepartmentImpl;
import com.venus.dal.DepartmentOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.DepartmentOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.controller.request.DepartmentRequest;
import com.venus.controller.response.error.ResponseException;

import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Service
public class DepartmentService {
  private DepartmentOperations dol = new DepartmentOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession(new Integer(1));
  private static final Logger log = Logger.getLogger(DepartmentService.class);

  public Department getDepartment(String name) throws ResponseException {
    Department department = null;
    try {
      department = dol.findDepartmentByName(name, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting department with name: " + name;
      log.error(errStr, dae);
      throw new ResponseException(Response.Status.INTERNAL_SERVER_ERROR, null, errStr);
    }
    return department;
  }
  
  public Department createUpdateDepartment(DepartmentRequest req) throws ResponseException {
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
      throw new ResponseException(Response.Status.INTERNAL_SERVER_ERROR, null, errStr);
    }
    return dept;
  }

  public List<Department> getDepartments(int offset, int maxRet) throws ResponseException {
    List<Department> departments = null;
    try {
      departments = dol.getDepartments(offset, maxRet, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting departments";
      log.error(errStr, dae);
      throw new ResponseException(Response.Status.INTERNAL_SERVER_ERROR, null, errStr);
    }
    return departments;
  }


}
