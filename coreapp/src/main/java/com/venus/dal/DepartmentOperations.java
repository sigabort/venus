package com.venus.dal;

import java.util.List;
import java.util.Map;

import com.venus.model.Department;
import com.venus.util.VenusSession;

/**
 * Operations on departments of an institute.
 * 
 * @author sigabort
 */
public interface DepartmentOperations {
  
  /**
   * Create or Update department
   * @paramm name            The name of the department. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code: String</li><li>description: String</li><li>photoUrl: String</li>
   *   <li>email: String</li><li>status: Status</li><li>created: Date</li><li>lastModified: Date</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created/updated department object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Department createUpdateDepartment(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;
  
  public abstract Department findDepartmentByName(String name, VenusSession session) throws DataAccessException;

  public abstract Department findDepartmentByCode(String code, VenusSession session) throws DataAccessException;

  public abstract void deleteDepartment(Department dept, VenusSession session) throws DataAccessException;

  public abstract List<Department> getDepartments(int offset, int maxRet, VenusSession vs) throws DataAccessException;
  
}
