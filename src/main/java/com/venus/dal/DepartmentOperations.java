package com.venus.dal;

import java.util.Date;
import java.util.List;

import com.venus.model.Department;
import com.venus.util.VenusSession;

public interface DepartmentOperations {
  
  /**
   * Create / update department
   */
  public abstract Department createUpdateDepartment(String name, String code, String desc, String photoUrl, String email, Date created, Date lastModified, VenusSession session) throws DataAccessException;
  
  public abstract Department findDepartmentByName(String name, VenusSession session) throws DataAccessException;

  public abstract Department findDepartmentByCode(String code, VenusSession session) throws DataAccessException;

  public abstract void deleteDepartment(Department dept, VenusSession session) throws DataAccessException;

  public abstract List<Department> getDepartments(int offset, int maxRet, VenusSession vs) throws DataAccessException;
  
}
