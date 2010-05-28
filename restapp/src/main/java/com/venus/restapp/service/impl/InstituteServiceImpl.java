package com.venus.restapp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import com.venus.model.Institute;
import com.venus.dal.InstituteOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.InstituteOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.service.InstituteService;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

@Service
public class InstituteServiceImpl implements InstituteService {
  private InstituteOperations iol = new InstituteOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession(null);
  private static final Logger log = Logger.getLogger(InstituteService.class);

  public Institute createUpdateInstitute(InstituteRequest req) throws ResponseException {
    String name = req.getName();
    Institute institute = null;
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", req.getCode());
    params.put("displayName", req.getDisplayName());    
    params.put("description", req.getDescription());
    params.put("photoUrl", req.getPhotoUrl());
    params.put("email", req.getEmail());

    /* see if the parent exists */
    String parent = req.getParent();
    if (!StringUtils.isBlank(parent)) {
      Institute parentInst = null;
      try {
        parentInst = iol.findInstituteByName(parent, null, vs);
      }
      catch (DataAccessException dae) {
        String errStr = "Error while getting parent institute : " + parent;
        log.error(errStr, dae);
        throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
      }
      if (parentInst == null) {
        String errStr = "Parent institute : " + parent + " doesn't exist";
        log.error(errStr);
        throw new ResponseException(HttpStatus.NOT_FOUND, errStr, null, null);        
      } 
      params.put("parent", parentInst);
    }
    
    /* create/update the institute now */
    try {
      institute  = iol.createUpdateInstitute(name, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while creating/updating institute with name: " + name;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return institute;
  }


  public Institute getInstitute(String name, BaseRequest request) throws ResponseException {
    Institute institute = null;
    try {
      institute = iol.findInstituteByName(name, null, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting institute with name: " + name;
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return institute;
  }

  /**
   * Get the institutes
   * @param request        The request parameter containing the optional parameters
   * @return               The list of institutes
   * @throws ResponseException if there is any error
   */
  public List<Institute> getInstitutes(BaseRequest request) throws ResponseException {
    int offset = request.getStartIndex();
    int maxRet = request.getItemsPerPage();
    String sortBy = request.getSortBy();
    String sortOrder = request.getSortOrder();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();

    /* Time to parse the query parameters */
    Map<String, Object> params = null;
    /* if the sortBy option is set, pass it to the DAL layer to 
     * sort depends on the requested option
     */
    if (!StringUtils.isBlank(sortBy)) {
      if (params == null) {
        params = new HashMap<String, Object>();
      }
      params.put("sortBy", sortBy);
      /* check whether the order is asc/desc. Default is 'asc' */
      if (StringUtils.equals("descending", sortOrder)) {
        params.put("isAscending", Boolean.FALSE);
      }
      else {
        params.put("isAscending", Boolean.TRUE);
      }
    }
    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      if (params == null) {
        params = new HashMap<String, Object>();
      }
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the institutes now */
    List<Institute> institutes = null;
    try {
      institutes = iol.getInstitutes(offset, maxRet, params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting institutes";
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return institutes;
  }

  /**
   * Get the institutes count
   * @param request        The request parameter containing the optional parameters
   * @return               The count of total institutes in the institute
   * @throws ResponseException if there is any error
   */
  public Integer getInstitutesCount(BaseRequest request) throws ResponseException {
   /* Time to parse the query parameters */
    Boolean onlyActive = request.getOnlyActive();
    String filterBy = request.getFilterBy();
    String filterValue = request.getFilterValue();
    String filterOp = request.getFilterOp();
    
    Map<String, Object> params = new HashMap<String, Object>();
    /* needed only count of active institutes? */
    params.put("onlyActive", onlyActive);

    /* if the filterBy option is set, pass it to the DAL layer to 
     * filter depends on the filterValue
     */
    if (!StringUtils.isBlank(filterBy) && !StringUtils.isBlank(filterValue)) {
      params.put("filterBy", filterBy);
      params.put("filterValue", filterValue);
      params.put("filterOp", filterOp);
    }

    /* get the institutes now */
    Integer count = null;
    try {
      count = iol.getInstitutesCount(params, vs);
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting institutes count";
      log.error(errStr, dae);
      throw new ResponseException(HttpStatus.INTERNAL_SERVER_ERROR, errStr, dae, null);
    }
    return count;
  }
  
  
  /**
   * Special API for the Admin Layer to create the parent institute. This shouldn't be used by others
   * Only APIs in 'InstituteAdminController' should use this operation
   * 
   * @param request      The {@link InstituteRequest request} containing the 
   *                     details of institute and other parameters
   * @return             The corresponding {@link Institute} object if 
   *                     created/updated with out any errors, null otherwise
   * @throws ResponseException thrown when there is any error
   */
  public Institute createUpdateParentInstitute(InstituteRequest request) throws ResponseException {
    return this.createUpdateInstitute(request);
  }

}
