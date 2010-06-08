package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.Session;

public class BaseImplTest {

  /**
   * Generate 20-char random string
   */
  public static String getRandomString() {
    return RandomStringUtils.random(20, true, true); 
  }

  /**
   * Generate random number
   */
  public static int getRandomNumber() {
    return RandomUtils.nextInt(); 
  }

  /**
   * Get the venus session
   */
  public final VenusSession getVenusSession() {
    return VenusSessionFactory.getVenusSession();
  }
  
  @Test
  public void dummyTest() {}
}