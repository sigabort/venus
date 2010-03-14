package com.venus.rest.test;

import org.wiztools.restclient.Response;

import com.venus.rest.VenusRestClient;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest {

  /* test getting default page - with out any parameters */
  @Test
  public void testGetDefaultPage() throws Exception {
    VenusRestClient client = new VenusRestClient();
    Response resp = client.getRequest(null, null);
    Assert.assertEquals("Response code", 200, resp.getStatusCode());
  }

  /* test getting departments page - with out any parameters */
  @Test
  public void testGetDepartmentsPage() throws Exception {
    VenusRestClient client = new VenusRestClient();
    Response resp = client.getRequest("/departments", null);
    Assert.assertEquals("Response code", 200, resp.getStatusCode());
  }
  
}
