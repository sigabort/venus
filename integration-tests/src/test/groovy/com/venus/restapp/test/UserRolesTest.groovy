package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;

import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Map;
import java.util.Date;

/**
 * Test class for testing the user role REST APIs
 */
public class UserRolesTest extends AbstractTest {

  private VenusRestJSONClient client = null;
  
  @Before
  public void setUp() {
    client = new VenusRestJSONClient();
  }

  @Test
  public void testCreateUserRole() {
    //fill in with stuff
  }
}