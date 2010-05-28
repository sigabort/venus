package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;
import com.venus.restapp.VenusRestResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import net.sf.json.JSONObject;

/**
 * Utililty class extended by all other tests
 */
public class AbstractTest {
  /**
   * Generate 20-char random string
   * @return The generated string
   */
  public static String getRandomString() {
    return RandomStringUtils.random(20, true, true);
  }
  
  /**
   * Generate random number
   * @return the random number
   */
  public static int getRandomNumber() {
    return RandomUtils.nextInt();
  }
  
  @Test
  public void dummyTest() {}

  /**
   * Create an admin user and login as that user
   * @param client      The {@link VenusRestJSONClient} used for sending requests
   */
  public static void createAdminUserAndLogin(VenusRestJSONClient client) {
    String name = getRandomString() + "-" + getRandomNumber();
    /* login as admin before creating department */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("password", name);
    JSONObject resp = client.createAdminUser(name, params);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("Response code", resp.getBoolean("error"));

    /* login using admin user/passwd */
    resp = client.login(name, name, null);
    Assert.assertFalse("Response code", resp.getBoolean("error"));
  }

  /**
   * Test whether the response is proper or not
   * @param response    The {JSONObject response} object to check
   */
  public static void testNoErrors(JSONObject response) {
    Assert.assertNotNull(response);
    Boolean error = response.getBoolean("error");
    Assert.assertFalse("Response error, expected: false. Found: " + error, error);
    Assert.assertEquals("Response error code", (int) HttpStatus.SC_OK, (int)response.getInt("httpErrorCode"));
  }

  /**
   * Test whether the response contains expected error or not
   * @param response    The {JSONObject response} object to check
   * @param errorCode   The expected error code
   */
  public static void testError(JSONObject response, Integer errorCode) {
    Assert.assertNotNull(response);
    Boolean error = response.getBoolean("error");
    Assert.assertTrue("Response error, expected: true. Found: " + error, error);
    Assert.assertEquals("Response error code", (int) errorCode, (int)response.getInt("httpErrorCode"));
  }

 }

