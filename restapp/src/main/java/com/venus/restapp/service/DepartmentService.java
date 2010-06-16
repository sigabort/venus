package com.venus.restapp.service;

import java.util.List;

import com.venus.model.Department;
import com.venus.util.VenusSession;

import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.RestResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service interface for Departments. The classes implementing this class should talk to
 * operations layer and call appropriate operations.
 * 
 * @author sigabort
 *
 */
public interface DepartmentService {
  /**
   * Create/Update the department. Only Admin can call this operation
   * 
   * @param request      The {@link DepartmentRequest request} containing the 
   *                     details of department and other parameters
   * @return             The corresponding {@link Department} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract Department createUpdateDepartment(DepartmentRequest request, VenusSession vs) throws RestResponseException;

  /**
   * Get the department. This operation can be called by any
   * @param name         The department name
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The corresponding {@link Department} object if found, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract Department getDepartment(String name, BaseRequest request, VenusSession vs) throws RestResponseException;

  /**
   * Get the departments in an institute. This operation can be called by any
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link Department}s if found, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract List<Department> getDepartments(BaseRequest request, VenusSession vs) throws RestResponseException;

  /**
   * Get the departments count in an institute. This operation can be called by any.
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The count of departments in an institute
   *                     (with filtering, if any filters provided in request)
   * @throws RestResponseException
   */
  public abstract Integer getDepartmentsCount(BaseRequest request, VenusSession vs) throws RestResponseException;
}
