package com.venus.rest.test;

import com.venus.rest.VenusRestClient;

import org.junit.Assert;
import org.junit.Test;

import org.apache.http.HttpResponse;

public class LoginTest {

  /* test login */
  @Test
  public void testLogin() throws Exception {
    VenusRestClient client = new VenusRestClient();
    HttpResponse resp = client.login("rod", "koala");
    Assert.assertTrue("Response code", resp.getStatusLine().getStatusCode() != 403);
  }
  
}