package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.InstituteDTO;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.log4j.Logger;

import com.venus.util.VenusSession;
import com.venus.model.Institute;

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
    model.addAttribute("institute", new InstituteRequest());
    return "institutes/create";
  }

  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@ModelAttribute("institute") InstituteRequest instituteRequest, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(instituteRequest, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("institutes/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    Institute institute = null;
    try {
      institute = instituteService.createUpdateInstitute(instituteRequest);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("institutes/create", re, result);
    }

    RestResponse resp = ResponseBuilder.createResponse(institute, new InstituteDTO());
    return RestUtil.buildVenusResponse("institutes/institute", resp);
  }

  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView getInstitute(@PathVariable String name, BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("institutes/institute", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    Institute institute = null;

    try {
      institute = instituteService.getInstitute(name, request);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("institutes/institute", re, result);
    }
    if (institute == null) {
      RestResponseException re = new RestResponseException(null, HttpStatus.NOT_FOUND, "Institute with name: " + name + ", not found");
      return RestUtil.buildErrorResponse("institutes/institute", re, result);
    }

    RestResponse resp = ResponseBuilder.createResponse(institute, new InstituteDTO());
    return RestUtil.buildVenusResponse("institutes/institute", resp);

  }

  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getInstitutes(BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("institutes/home", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    List institutes = null;
    Integer totalCount = null;

    try {
      institutes = instituteService.getInstitutes(request);
      /* get the total institutes count */
      totalCount = instituteService.getInstitutesCount(request);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("institutes/home", re, result);
    }
    /* populate the response object */
    RestResponse resp = ResponseBuilder.createResponse(institutes, totalCount, new InstituteDTO());
    return RestUtil.buildVenusResponse("institutes/home", resp);
  }

}
