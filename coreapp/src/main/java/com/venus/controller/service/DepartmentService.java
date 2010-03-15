package com.venus.controller.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

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
  private VenusSession vs = VenusSessionFactory.getVenusSession();
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
    String code = req.getCode();
    String desc = req.getDescription();
    String email = req.getEmail();
    String photoUrl = req.getPhotoUrl();
    Department dept = null;
    
    try {
      dept  = dol.createUpdateDepartment(name, code, desc, photoUrl, email, null, null, vs);
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
