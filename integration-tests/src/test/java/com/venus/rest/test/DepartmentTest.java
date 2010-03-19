package com.venus.rest.test;

import com.venus.rest.VenusRestClient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Random;
import org.apache.http.HttpResponse;


public class DepartmentTest {

  private int randInt;
  @Before
  public void setUp() {
    randInt = new Random(new Random().nextLong()).nextInt();
  }

  /* test login */
  @Test
  public void testCreateDepartment() throws Exception {
    String name = "testCreateDept" + randInt;
    String code = name + "-code";
    VenusRestClient client = new VenusRestClient();
    HttpResponse resp = client.login("rod", "koala");
    resp = client.createDepartment(name, code, null);
    Assert.assertTrue("Response code", resp.getStatusLine().getStatusCode() != 403);
  }

  /* test getting departments page - with out any parameters */
  @Test
  public void testGetDepartmentsPage() throws Exception {
    VenusRestClient client = new VenusRestClient();
    HttpResponse resp = client.getRequest("/departments", null);
    Assert.assertEquals("Response code", 200, resp.getStatusLine().getStatusCode());
  }

  
}