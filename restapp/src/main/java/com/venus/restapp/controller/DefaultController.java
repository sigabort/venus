/**
 * @file Default Controller
 */
package com.venus.restapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.InstituteDTO;
import com.venus.restapp.util.RestUtil;

import com.venus.util.VenusSession;

/**
 * Default Controller to handle default requests
 * @author sigabort
 *
 */
@Controller("defaultController")
@RequestMapping(value="/*")
public class DefaultController {

  private static final Logger log = Logger.getLogger(DefaultController.class);

  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView defaultHandler(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
    log.info("I am in default controller request handler, name: " + name + "....");
    VenusSession vs = RestUtil.getVenusSession(request);
    RestResponse resp = ResponseBuilder.createResponse(vs.getInstitute(), new InstituteDTO());
    return RestUtil.buildVenusResponse(name, resp);
  }
}
