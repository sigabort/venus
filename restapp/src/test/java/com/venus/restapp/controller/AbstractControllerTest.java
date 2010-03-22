package com.venus.restapp.controller;

import org.junit.Test;

import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Abstract controller test to handle the stuff which is generic to
 * all controller tests
 */
public class AbstractControllerTest {
  /** The main config file for the application context */
  public static final String WEB_CONFIG_FILE = "config/restapp-config.xml";

  /** handler adapter used to execute the requests */
  static HandlerAdapter handlerAdapter;
  /** application context */
  static ClassPathXmlApplicationContext appContext;
  
  /**
   * Generate 10-char random string
   */
  public String getRandomString() {
    return RandomStringUtils.random(15, true, true);
  }
  
  /**
   * Generate random number
   */
  public long getRandomNumber() {
    return RandomUtils.nextLong();
  }
  
  /**
   * Create the setup:
   *   create the application context, create the handler bean, etc.
   */
  public void setUp() {
    if (appContext == null) {
      /*
       * Get the application context by reading the config file.
       */
      appContext = new ClassPathXmlApplicationContext(new String[] {WEB_CONFIG_FILE});
    }
    if (handlerAdapter == null) {
      /*
       * TODO: I couldn't use getBean(HandlerAdapter.class), because it seems the config
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
    }
  }
  
  @Test
  public void dummyTest() {}

}