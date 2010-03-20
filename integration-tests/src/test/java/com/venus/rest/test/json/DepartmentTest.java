package com.venus.rest.test.json;

import com.venus.rest.VenusRestJSONClient;
import com.venus.rest.VenusRestResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import net.sf.json.JSONObject;

/**
 * Department related tests
 */
public class DepartmentTest {

  private int randInt;
  @Before
  public void setUp() {
    randInt = new Random(new Random().nextLong()).nextInt();
  }

  /* test create department */
  @Test
  public void testCreateDepartment() throws Exception {
    String name = "testCreateDept" + randInt;
    String code = name + "-code";
    VenusRestJSONClient client = new VenusRestJSONClient();

    /* login as admin before creating department */
    VenusRestResponse r = client.login("rod", "koala");
    Assert.assertNotNull("Login response is not proper", r);

    /* create department */
    JSONObject resp = client.createDepartment(name, code, null);
    Assert.assertNotNull("Didn't get the response", resp);
    JSONObject response = resp.getJSONObject("response");
    Assert.assertNotNull("Didn't get the response", response);
    Assert.assertFalse("Response code", response.getBoolean("error"));

    /* get the department now */
    resp = client.getDepartment(name, null);
    Assert.assertNotNull("Didn't get the response", resp);    
    Assert.assertNotNull("Didn't get the response", resp);
    response = resp.getJSONObject("response");
    Assert.assertNotNull("Didn't get the response", response);
    Assert.assertFalse("Response code", response.getBoolean("error"));
  }

  /* test getting departments page - with out any parameters */
  @Test
  public void testGetDepartmentsPage() throws Exception {
    VenusRestJSONClient client = new VenusRestJSONClient();
    JSONObject resp = client.getDepartments(null);
    Assert.assertNotNull("Get Departments response is not proper", resp);
  }
}
