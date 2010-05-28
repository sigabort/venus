package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.InstituteService;
import com.venus.restapp.request.InstituteRequest;
import com.venus.restapp.request.BaseInstituteRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.InstituteResponse;
import com.venus.restapp.response.error.ResponseException;

import org.apache.log4j.Logger;

/**
 * Controller class for handling all institutes related requests.
 * Handles REST requests: create/update/delete/get
 * 
 */
@Controller("instituteController")
@RequestMapping(value="/institutes")
public class InstituteController {
  @Autowired
  private InstituteService instituteService;
  private static final Logger log = Logger.getLogger(InstituteController.class);

  public InstituteService getInstituteService() {
    return this.instituteService;
  }
  
  /**
   * Get the Model object for creating the institute
   * This is mostly used by the UI clients when they want to create a institute
   * @param model        The model object for request
   * @return             The url location for view resolver to send the
   *                     proper view to the client with model object(InstituteRequest) added to request
   */
  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("instituteRequest", new InstituteRequest());
    return "institutes/createInstitute";
  }

  /**
   * Create/Update the Institute
   * @param instituteRequest   The InstituteRequest object containing all parameters
   * @param result             The BindingResult object containing the errors if there
   *                            are any errors found while validating the request object
   * @return the ModelAndView object containing response of creation/updation of institute.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid InstituteRequest instituteRequest, BindingResult result) {
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
      institute = instituteService.createUpdateInstitute(instituteRequest);
    }
    catch (ResponseException re) {
      return new ModelAndView("institutes/createInstitute", "response", re.getResponse());
    }
    InstituteResponse resp = InstituteResponse.populateInstitute(institute);
    return new ModelAndView("institutes/institute", "response", resp);
  }

  /**
   * Send a particualr institute details with given name in the institute.
   * @param name     The name of the institute
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting institute information.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView getInstitute(@PathVariable String name, @Valid BaseRequest request, BindingResult result) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("institutes/institute", "response", re.getResponse());
    }
    log.info("Fetching institute: " + name);
    Object institute = null;
    try {
      institute = instituteService.getInstitute(name, request);
    }
    catch (ResponseException re) {
      log.info("Error while finding Institute with name: " + name, re);
      return new ModelAndView("institutes/institute", "response", re.getResponse());
    }
    /* institute not found? throw 404 */
    if (institute == null) {
      log.info("Institute with name: " + name + " is not found...");
      ResponseException re = new ResponseException(HttpStatus.NOT_FOUND, "Institute with name: " + name + ", not found", null, null);
      return new ModelAndView("institutes/institute", "response", re.getResponse());
    }
    log.info("Got institute: " + name);
    /* populate the object */
    InstituteResponse resp = InstituteResponse.populateInstitute(institute);
    return new ModelAndView("institutes/institute", "response", resp);
  }

  /**
   * Send all of the institute results in the institute. This is the default request
   * for institutes page.
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting institutes.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getInstitutes(@Valid BaseRequest request, BindingResult result) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("institutes/home", "response", re.getResponse());
    }

    log.info("Fetching all institutes....");

    List institutes = null;
    Integer totalCount = null;

    try {
      institutes = instituteService.getInstitutes(request);
      /* get the total institutes count */
      totalCount = instituteService.getInstitutesCount(request);
    }
    catch (ResponseException re) {
      return new ModelAndView("institutes/home", "response", re.getResponse());
    }
    /* institutes not found? send empty response. So, the client can take care of
     * what to do next
     */
    if (institutes == null) {
      return new ModelAndView("institutes/home", "response", new BaseResponse());
    }
    log.info("Got institutes: " + institutes.size());
    
    /* populate the response object */
    InstituteResponse resp = InstituteResponse.populateInstitutes(institutes, totalCount);
    return new ModelAndView("institutes/home", "response", resp);
  }

}
