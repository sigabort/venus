/**
 * @file Default Controller
 */
package com.venus.restapp.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * Default Controller to handle default requests
 * @author sigabort
 *
 */
@Controller("defaultController")
@RequestMapping(value="/*")
public class DefaultController {

  private static final Logger log = Logger.getLogger(DefaultController.class);
  
  @RequestMapping(method=RequestMethod.GET)
  public void defaultHandler() {
    log.info("I am in default controller request handler....");
  }
  
}
