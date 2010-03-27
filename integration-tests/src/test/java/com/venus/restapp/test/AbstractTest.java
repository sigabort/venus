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
  public String getRandomString() {
    return RandomStringUtils.random(20, true, true);
  }
  
  /**
   * Generate random number
   * @return the random number
   */
  public int getRandomNumber() {
    return RandomUtils.nextInt();
  }
  
  @Test
  public void dummyTest() {}
  
  public void createAdminUserAndLogin(VenusRestJSONClient client) {
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
}