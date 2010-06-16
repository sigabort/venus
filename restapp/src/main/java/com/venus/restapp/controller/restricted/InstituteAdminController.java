package com.venus.restapp.controller.restricted;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.oval.Validator;
import net.sf.oval.integration.spring.SpringValidator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.InstituteDTO;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.log4j.Logger;

import com.venus.util.VenusSession;
import com.venus.model.Institute;

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
  
  public InstituteService getInstituteService() {
    return this.instituteService;
  }
  
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
  public ModelAndView createParentInstitute(@ModelAttribute("institute") InstituteRequest instituteRequest, BindingResult result, HttpServletRequest req) {
    validateRequest(instituteRequest, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("institutes/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    Institute institute = null;
    try {
      institute = instituteService.createUpdateParentInstitute(instituteRequest);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("institutes/create", re, result);
    }

    RestResponse resp = ResponseBuilder.createResponse(institute, new InstituteDTO());
    return RestUtil.buildVenusResponse("institutes/institute", resp);
  }
  
  
  
  private void validateRequest(Object request, HttpServletRequest httpReq, BindingResult result) {
    VenusSession vs = RestUtil.getVenusSession(httpReq);
    /* get new spring validator using OVal's validator backing for validation */
    SpringValidator validator = new SpringValidator(new Validator());
    validator.validate(request, result);
  }

}
