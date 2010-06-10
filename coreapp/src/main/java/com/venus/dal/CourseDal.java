package com.venus.dal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.venus.model.impl.DepartmentImpl;
import com.venus.model.impl.ProgramImpl;
import com.venus.model.impl.InstituteImpl;
import com.venus.model.impl.CourseImpl;
import com.venus.model.impl.UserImpl;
import com.venus.util.VenusSession;
import com.venus.model.Status;

public interface CourseDal {

  /**
   * Create or Update a course
   * 
   * @param code
   *          The code of the course.This should be unique in the institute
   * @param name
   *          The name of the course.
   * @param department
   *          The {@link Department department} to which this course belongs to
   * @param instructor
   *          The {@link User instructor} of the course
   * @param optionalParams
   *          The map of optional parameters. The list include:
   *          <ul>
   *          <li>program({@link Program}): The program to which this course
   *          belongs to
   *          <li>admin(@link UserImpl): Admin of the course. If not specified,
   *          instructor will be used.
   *          <li>description(String): The description of the course</li>
   *          <li>photoUrl(String): The photourl for the course</li>
   *          <li>content(String): The content of the course</li>
   *          <li>prerequisites(String): Prerequisites for the course</li>
   *          <li>content(String): The content of the course</li>
   *          <li>duration(Integer): The duration of the course</li>
   *          <li>status(Status): The status of the course</li>
   *          <li>created(Date): The created date of the course</li>
   *          <li>lastModified(Date): The last modified date of the course</li>
   *          </ul>
   * @param session
   *          The venus session object consisting of institute, hibernate
   *          session
   * @return The created/updated course object
   * @throws DataAccessException
   *           thrown when there is any error
   */
  public abstract CourseImpl createUpdateCourse(String code, String name,
      DepartmentImpl dept, UserImpl instructor,
      Map<String, Object> optionalParams, VenusSession session)
      throws DataAccessException;

  /**
   * Find the course given the code. By default, returns only active course if
   * not specified, otherwise
   * 
   * @param code
   *          The code of the course
   * @param options
   *          The map of optional parameters. The list include:
   *          <ul>
   *          <li>onlyActive: Boolean</li>
   *          </ul>
   * @param vs
   *          The venus session object
   * @return The course object if found, null otherwise
   * @throws DataAccessException
   *           thrown when there is any exception
   */
  public abstract CourseImpl findCourse(String code,
      Map<String, Object> options, VenusSession vs) throws DataAccessException;


  /**
   * Get all the courses
   * 
   * @param offset
   *          The paging offset in the list
   * @param maxRet
   *          Maximum number of objects to be returned
   * @param options
   *          The oprional parameters, including:
   *          <ul>
   *          <li>sortList(List<{@link VenusSortImpl}>): List of objects to be
   *          sorted on
   *          <li>filterList(List<{@link VenusFilterImpl}>): List of objects to
   *          filter
   *          </ul>
   * @param vs
   *          The venus session object
   * @return the list of courses in an institute
   * @throws DataAccessException
   *           thrown when there is any error
   */
  public abstract List<CourseImpl> getCourses(int offset, int maxRet,
      Map<String, Object> options, VenusSession vs) throws DataAccessException;

  
  
  /**
   * Get the number of courses in an institute
   * 
   * @param options
   *          The oprional parameters, including:
   *          <ul>
   *          <li>filterList(List<{@link VenusFilterImpl}>): List of objects to
   *          filter
   *          </ul>
   * @param vs
   *          The venus session object
   * @return the count courses in an institute
   * @throws DataAccessException
   *           thrown when there is any error
   */
  public abstract Integer getCoursesCount(Map<String, Object> options, VenusSession vs) throws DataAccessException;

  /**
   * Set status of the course. This can be used to delete the course
   * @param course        The course object for which the status needs to be changed
   * @param status        The status to be set
   * @param vs            The session object
   * @throws DataAccessException thrown when there is any error
   */
   public abstract void setStatus(CourseImpl course, Status status, VenusSession vs) throws DataAccessException;
  
}
