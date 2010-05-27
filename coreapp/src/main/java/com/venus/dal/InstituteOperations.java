package com.venus.dal;

import java.util.List;
import java.util.Map;

import com.venus.model.Institute;
import com.venus.model.Status;
import com.venus.util.VenusSession;

/**
 * Operations on institutes
 * 
 * @author sigabort
 */
public interface InstituteOperations {
  
  /**
   * Create or Update institute
   * @param name            The name of the institute. This should be unique
   * @param optionalParams   The map of optional parameters. The list include:
   * <ul>
   *   <li>code(String): The code for the institute. If not
   *       specified, name will be used as code 
   *   </li>
   *   <li>displayName(String): The name of the institute used for displaying on web pages</li>
   *   <li>description(String): The description of the institute</li>
   *   <li>photoUrl(String): The photourl for the institute</li>
   *   <li>email(String): The email of the institute (may be admin mail-id)</li>
   *   <li>parent({@link Department}): The parent of the institute</li>
   *   <li>status(Status): The status of the institute</li>
   *   <li>created(Date): The created date of the institute</li>
   *   <li>lastModified(Date): The last modified date of the institute</li>
   * </ul>
   * @param session          The venus session object consisting of institute, hibernate session
   * @return                 The created/updated institute object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Institute createUpdateInstitute(String name, Map<String, Object> optionalParams, VenusSession session) throws DataAccessException;
  
  /**
   * Find the institute given the name. By default, returns
   * only active institute if not specified
   * @param name     The name of the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): if set to true, only active institute is returned</li>
   * </ul>
   * @param session       The venus session object
   * @return         The institute object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract Institute findInstituteByName(String name, Map<String, Object> options, VenusSession session) throws DataAccessException;

  /**
   * Find the institute given the code. By default, returns
   * only active institute if not specified
   * @param code     The code of the institute
   * @param options  The map of optional parameters. The list include:
   * <ul>
   *   <li>onlyActive(Boolean): if set to true, only active institute is returned</li>
   * </ul>
   * @param session       The venus session object
   * @return         The institute object if found, null otherwise
   * @throws DataAccessException thrown when there is any exception
   */
  public abstract Institute findInstituteByCode(String code, Map<String, Object> options, VenusSession session) throws DataAccessException;
  
  /**
   * Set status of the institute. This can be used to delete the Institute
   * @param institute          The institute object to be deleted
   * @param status        The status to be set
   * @param session       The session object
   * @throws DataAccessException thrown when there is any error
   */
  public abstract void setStatus(Institute institute, Status status, VenusSession session) throws DataAccessException;


  /**
   * Get all the institutes
   * @param offset        The paging offset in the list
   * @param maxRet        Maximum number of objects to be returned
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active institutes, defaults to true</li>
   *   <li>parent({@link Department}): return only parent's child institutes, defaults to null</li>
   *   <li>sortBy(String): if specified, the restults will be sorted by this field, defaults to created</li>
   *   <li>isAscending(Boolean): sort by ascending/descending, defaults to true</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the list of institutes in an institute
   * @throws DataAccessException thrown when there is any error
   */
  public abstract List<Institute> getInstitutes(int offset, int maxRet, Map<String, Object> options, VenusSession vs) throws DataAccessException;

  /**
   * Get total number of institutes
   * @param options       The oprional parameters, including:
   * <ul>
   *   <li>onlyActive(Boolean): return only active institutes, defaults to true</li>
   *   <li>parent({@link Department}): count only parent's child institutes, defaults to null</li>
   *   <li>filterBy(String): filter the output by this field name</li>
   *   <li>filterValue(String): filter the output based on this value for the given field. This will be effective, only if filterBy field is set</li>
   *   <li>filterOp(String): filter operation, can be : contains, equals, startsWith, present. Defaults to contains</li>
   * </ul>
   * @param vs           The venus session object
   * @return the total count of institutes
   * @throws DataAccessException thrown when there is any error
   */
  public abstract Integer getInstitutesCount(Map<String, Object> options, VenusSession vs)  throws DataAccessException;  
}
