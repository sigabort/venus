package com.venus.restapp.util;

import javax.servlet.http.HttpServletRequest;

import net.sf.oval.Validator;
import net.sf.oval.integration.spring.SpringValidator;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import com.venus.model.Institute;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.response.ResponseBuilder;


public class RestUtil {
  public static VenusSession getVenusSession(HttpServletRequest request) {
    return (VenusSession) request.getAttribute(RestParams.REST_REQUEST_SESSION_ATTR);
  }

  public static void setVenusSession(HttpServletRequest request, VenusSession vs) {
    request.setAttribute(RestParams.REST_REQUEST_SESSION_ATTR, vs);
  }

  public static void removeVenusSession(HttpServletRequest request) {
    request.removeAttribute(RestParams.REST_REQUEST_SESSION_ATTR);
  }
  
  public static VenusSession createVenusSession(Institute institute) {
    return VenusSessionFactory.getVenusSession(institute);
  }
  
  public static ModelAndView buildVenusResponse(String viewName, BaseResponse response) {
    return new ModelAndView(viewName, RestParams.VENUS_DEFAULT_RESP_ATTR, response);
  }
  
  /**
   * Validate the object using the oval's spring validator
   * @param request
   * @param httpReq
   * @param result
   */
  public static void validateRequest(Object request, HttpServletRequest httpReq, BindingResult result) {
    VenusSession vs = getVenusSession(httpReq);
    if (vs == null || vs.getInstitute() == null) {
      result.rejectValue(null, HttpStatus.BAD_REQUEST.toString(), "No session has been set");
      return;
    }
    /* get new spring validator using OVal's validator backing for validation */
    SpringValidator validator = new SpringValidator(new Validator());
    validator.validate(request, result);
  }

  public static ModelAndView buildErrorResponse(String view, RestResponseException rre, BindingResult result) {
    result.rejectValue(rre.getField(), rre.getErrorCode().toString(), rre.getMessage());
    BaseResponse resp = ResponseBuilder.createResponse(rre.getErrorCode(), result);
    return buildVenusResponse(view, resp);
  }
}