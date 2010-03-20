package com.venus.rest.test;

import com.venus.rest.VenusRestClient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;


/**
 * Department related tests
 */
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
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertEquals("Response code", HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());

    /* get the department now */
    resp = client.getDepartment(name, null);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertEquals("Response code", HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());
    
  }

  /* test getting departments page - with out any parameters */
  @Test
  public void testGetDepartmentsPage() throws Exception {
    VenusRestClient client = new VenusRestClient();
    HttpResponse resp = client.getRequest("/departments", null);
    Assert.assertEquals("Response code", HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());
  }

  
}