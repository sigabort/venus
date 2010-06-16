package com.venus.restapp.service;

import java.util.List;

import com.venus.model.Institute;

import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.RestResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service interface for Institutes. The classes implementing this class should talk to
 * operations layer and call appropriate operations.
 * 
 * @author sigabort
 *
 */
public interface InstituteService {
  /**
   * Create/Update the institute. Only Admin can call this operation
   * 
   * @param request      The {@link InstituteRequest request} containing the 
   *                     details of institute and other parameters
   * @return             The corresponding {@link Institute} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract Institute createUpdateInstitute(InstituteRequest request) throws RestResponseException;

  /**
   * Get the institute. This operation can be called by any
   * @param name         The institute name
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The corresponding {@link Institute} object if found, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract Institute getInstitute(String name, BaseRequest request) throws RestResponseException;

  /**
   * Get the institutes. This operation can be called by any
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The list of {@link Institute}s if found, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  public abstract List<Institute> getInstitutes(BaseRequest request) throws RestResponseException;

  /**
   * Get the institutes count. This operation can be called by any.
   * 
   * @param request      The base {@link BaseRequest request} containing all optional parameters
   * @return             The count of institutes in an institute
   *                     (with filtering, if any filters provided in request)
   * @throws RestResponseException
   */
  public abstract Integer getInstitutesCount(BaseRequest request) throws RestResponseException;
  
  /**
   * Special API for the Admin Layer to create the parent institute. This shouldn't be used by others
   * Only APIs in 'InstituteAdminController' should use this operation
   * 
   * @param request      The {@link InstituteRequest request} containing the 
   *                     details of institute and other parameters
   * @return             The corresponding {@link Institute} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws RestResponseException thrown when there is any error
   */
  //@PreAuthorize("hasIpAddress('127.0.0.1/24')")
  public abstract Institute createUpdateParentInstitute(InstituteRequest request) throws RestResponseException;

}
