package com.venus.model.impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Session;

public class BaseImplTest {
  
  protected final String getUniqueName() {
    return RandomStringUtils.randomAlphanumeric(25); 
  }

  protected final VenusSession getVenusSession() {
    return VenusSessionFactory.getVenusSession();
  }
  
  @Test
  public void dummyTest() {}
}