package com.venus.restapp.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.venus.model.Institute;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.restapp.response.BaseResponse;

public class RestUtil {
  public static VenusSession getVenusSession(HttpServletRequest request) {
    return (VenusSession) request.getAttribute(RestParams.REST_REQUEST_SESSION_ATTR);
  }

  public static void setVenusSession(HttpServletRequest request, VenusSession vs) {
    request.setAttribute(RestParams.REST_REQUEST_SESSION_ATTR, vs);
  }
  
  public static VenusSession createVenusSession(Institute institute) {
    return VenusSessionFactory.getVenusSession(institute);
  }
  
  public static ModelAndView buildVenusResponse(String viewName, BaseResponse response) {
    return new ModelAndView(viewName, RestParams.VENUS_DEFAULT_RESP_ATTR, response);
  }
}