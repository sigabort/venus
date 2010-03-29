/**
 * @file Default Controller
 */
package com.venus.restapp.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import com.venus.restapp.response.BaseResponse;

/**
 * Default Controller to handle default requests
 * @author sigabort
 *
 */
@Controller("defaultController")
@RequestMapping(value="/*")
public class DefaultController {

  private static final Logger log = Logger.getLogger(DefaultController.class);

  /**
   * Send Home response. This will be used for login/logout too.
   * So, lets send basic information in the response attribute
   */
  @RequestMapping(value="home", method=RequestMethod.GET)
  public ModelAndView homeHandler() {
    return new ModelAndView("home", "response", new BaseResponse());
  }
  
  @RequestMapping(method=RequestMethod.GET)
  public void defaultHandler() {
    log.info("I am in default controller request handler....");
  }
}
