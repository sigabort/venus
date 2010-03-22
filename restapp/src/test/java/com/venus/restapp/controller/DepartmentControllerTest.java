package com.venus.restapp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.springframework.test.web.ModelAndViewAssert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerAdapter;

/**
 * unit tests for {@link DepartmentController}
 * @author sigabort
 */
public class DepartmentControllerTest {
  /** The main config file for the application context */
  public static final String WEB_CONFIG_FILE = "config/restapp-config.xml";

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private HandlerAdapter handlerAdapter;
  private DepartmentController controller;

  @Before
  public void setUp() {
    /* create mock requests, responses */
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    /*
     * Get the application context by reading the config file.
     */
    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {WEB_CONFIG_FILE});
    
    /*
     * XXX: I couldn't use getBean(HandlerAdapter.class), because it seems the config
     * contains 2 types of AnnotationMethodHandlerAdapter classes. So, until we figure
     * out the problem in the config, I am taking the first handler adapter in the following mannaer.
     */
    Map map = appContext.getBeansOfType(HandlerAdapter.class, false, true);
    String beanName = null;
    /* just read the first bean name */
    for (Object entry: map.keySet()) {
      beanName = (String) entry;
      break;
    }

    /* get the handler adapter bean from the map */
    handlerAdapter = (HandlerAdapter) map.get(beanName);

    // Get the controller from the context
    controller = appContext.getBean(DepartmentController.class);
  }

  /**
   * Test for getDepartments and default view
   */
  @Test
  public void testGet() throws Exception {
    request.setMethod("GET");
    final ModelAndView mav = handlerAdapter.handle(request, response, controller);
    assertViewName(mav, "departments/home");
  }
  
}
