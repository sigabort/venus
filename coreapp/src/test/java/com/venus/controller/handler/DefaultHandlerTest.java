package com.venus.controller.handler;

import org.springframework.web.servlet.ModelAndView;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class DefaultHandlerTest {

  @Test public void dummyTest() {}

  @Test
  public void testHandleDefaultRequestView() throws Exception {		
    DefaultHandler controller = new DefaultHandler();
    ModelAndView modelAndView = controller.defaultHandler();		
    Assert.assertEquals("home", modelAndView.getViewName());
  }

  @Test
  public void testHandleResourceRequestView() throws Exception {		
    DefaultHandler controller = new DefaultHandler();
    ModelAndView modelAndView = controller.resourceHandler("home");		
    Assert.assertEquals("home", modelAndView.getViewName());
  }

}