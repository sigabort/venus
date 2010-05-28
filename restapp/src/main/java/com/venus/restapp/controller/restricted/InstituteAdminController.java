package com.venus.restapp.controller.restricted;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.InstituteService;
import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.InstituteResponse;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * Controller class for handling all parent institutes related requests.
 * Handles REST requests: create/update/delete/get
 * 
 * This should be only allowed to run from the localhost. Or infact, this code should not be
 * used in the deployment version at all. But, to run the tests, this can be included in the 
 * non-deployment versions.
 * 
 */
@Controller("instituteAdminController")
@RequestMapping(value="/restricted/institutes")
public class InstituteAdminController {
  @Autowired
  private InstituteService instituteService;
  
  private static final Logger log = Logger.getLogger(InstituteAdminController.class);

  /**
   * Create/Update the parent Institute
   * @param instituteRequest        The {@link InstituteRequest} object containing all parameters
   * @param result             The {@link BindingResult} object containing the errors if there
   *                           are any errors found while validating the request object
   * @return the {@link ModelAndView} object containing response of creation/updation of institute.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="create", method=RequestMethod.POST)
  public ModelAndView createParentInstitute(@Valid InstituteRequest instituteRequest, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("institutes/createInstitute", "response", re.getResponse());
    }
    
    log.info("Adding/Updating institute" + instituteRequest.getName());
    Object institute = null;
    try {
      institute = instituteService.createUpdateParentInstitute(instituteRequest);
    }
    catch (ResponseException re) {
      log.error("Can't create/update institute : " + instituteRequest.getName() + ", reason: " + re.getMessage());
      return new ModelAndView("institutes/createInstitute", "response", re.getResponse());
    }
    
    InstituteResponse resp = InstituteResponse.populateInstitute(institute);
    return new ModelAndView("institutes/institute", "response", resp);
  }
      
}
