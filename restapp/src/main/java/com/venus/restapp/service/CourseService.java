package com.venus.restapp.service;

import java.util.List;

import com.venus.model.impl.CourseImpl;
import com.venus.util.VenusSession;

import com.venus.restapp.request.CourseRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.RestResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service interface for Courses. The classes implementing this class should talk to
 * operations layer and call appropriate operations.
 * 
 * @author sigabort
 *
 */
public interface CourseService {
  /**
   * Create/Update the course. Only Admin can call this operation
   * 
   * @param request      The {@link CourseRequest request} containing the 
   *                     details of course and other parameters
   * @param vs           The {@link VenusSession} object containing sessions, institute info, etc
   * @return             The corresponding {@link Course} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract CourseImpl createUpdateCourse(CourseRequest request, VenusSession vs) throws RestResponseException;
//
//  /**
//   * Get the course. This operation can be called by any
//   * @param name         The course code
//   * @param request      The base {@link BaseRequest request} containing all optional parameters
//   * @param vs           The {@link VenusSession} object containing sessions, institute info, etc
//   * @return             The corresponding {@link Course} object if found, null otherwise
//   * @throws ResponseException thrown when there is any error
//   */
//  public abstract CourseImpl getCourse(String code, BaseRequest request, VenusSession vs) throws ResponseException;
//
//  /**
//   * Get the courses. This operation can be called by any
//   * 
//   * @param request      The base {@link BaseRequest request} containing all optional parameters
//   * @param vs           The {@link VenusSession} object containing sessions, institute info, etc
//   * @return             The list of {@link Course}s if found, null otherwise
//   * @throws ResponseException thrown when there is any error
//   */
//  public abstract List<CourseImpl> getCourses(BaseRequest request, VenusSession vs) throws ResponseException;
//
//  /**
//   * Get the courses count. This operation can be called by any.
//   * 
//   * @param request      The base {@link BaseRequest request} containing all optional parameters
//   * @param vs           The {@link VenusSession} object containing sessions, institute info, etc
//   * @return             The count of courses in an institute
//   *                     (with filtering, if any filters provided in request)
//   * @throws ResponseException
//   */
//  public abstract Integer getCoursesCount(BaseRequest request, VenusSession vs) throws ResponseException;
}
