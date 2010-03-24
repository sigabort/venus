package com.venus.dal;

import java.util.List;
import java.util.Map;

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;

/**
 * Operations on departments of an institute.
 * 
 * @author sigabort
 */
public interface DepartmentOperations {
  
  /**
   * Create or Update department
   * @param name            The name of the department. This should be unique in the institute
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the department in the institute. If not
   *       specified, concat(name, '-', institute id) will be used as code 
   *   </li>
   *   <li>description(String): The description of the department</li>
   *   <li>photoUrl(String): The photourl for the department</li>
   *   <li>email(String): The email of the department (may be admin mail-id)</li>
   *   <li>status(Status): The status of the department</li>
   *   <li>created(Date): The created date of the department</li>
   *   <li>lastModified(Date): The last modified date of the department</li>
   * </ul>
   * @param session          The venus session object consisting of instituteId, hibernate session
   * @return                 The created/updated department object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Department createUpdateDepartment(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;
  
  /**
   * Find the department given the name in an institue. By default, returns
   * only active department if not specified
   * @param name     The name of the department in the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): if set to true, only active department is returned</li>
   * </ul>
   * @param session       The venus session object
   * @return         The department object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract Department findDepartmentByName(String name, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Find the department given the code in an institue. By default, returns
   * only active department if not specified
   * @param code     The code of the department in the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): if set to true, only active department is returned</li>
   * </ul>
   * @param session       The venus session object
   * @return         The department object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract Department findDepartmentByCode(String code, Map<String, Object> options, VenusSession session) throws DataAccessException;
  
  /**
   * Set status of the department. This can be used to delete the Department
   * @param dept          The department object to be deleted
   * @param status        The status to be set
   * @param session       The session object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract void setStatus(Department dept, Status status, VenusSession session) throws DataAccessException;


  /**
   * Get all the departments in the institute
   * @param offset        The paging offset in the list
   * @param maxRet        Maximum number of objects to be returned
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active departments, defaults to true</li>
   *   <li>sortBy(String): if specified, the restults will be sorted by this field, defaults to created</li>
   *   <li>isAscending(Boolean): sort by ascending/descending, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the list of departments in an institute
   * @throws DataAccessException thrown when there is any error
   */
  public abstract List<Department> getDepartments(int offset, int maxRet, Map<String, Object> options, VenusSession vs) throws DataAccessException;

  /**
   * Get departments count in the institute
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active departments, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the total count of departments in the institute
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Integer getDepartmentsCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException;  
}
