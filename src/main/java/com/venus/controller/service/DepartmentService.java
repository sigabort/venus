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

import org.springframework.stereotype.Service;


@Service
public class DepartmentService {
  private DepartmentOperations dol = new DepartmentOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession();

  public Department getDepartment(String name) {
    Department department = null;
    try {
      department = dol.findDepartmentByName(name, vs);
    }
    catch (DataAccessException dae) {
      throw new RuntimeException("Error while getting dept: " + name, dae);
    }
    return department;
  }
  
  public Department createUpdateDepartment(DepartmentRequest req) {
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
      throw new RuntimeException("Error while creating/updating dept: " + name, dae);
    }
    if (dept == null) {
      throw new RuntimeException("Unable to create/update user");
    }
    return dept;
  }

  public List<Department> getDepartments(int offset, int maxRet) {
    List<Department> departments = null;
    try {
      departments = dol.getDepartments(offset, maxRet, vs);
    }
    catch (DataAccessException dae) {
      throw new RuntimeException("Error while getting departments", dae);
    }
    
    return departments;
  }


}
