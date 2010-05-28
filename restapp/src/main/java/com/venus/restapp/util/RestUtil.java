package com.venus.restapp.util;

import javax.servlet.http.HttpServletRequest;

import com.venus.model.Institute;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;


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
}