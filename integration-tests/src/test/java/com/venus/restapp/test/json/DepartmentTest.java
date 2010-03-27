package com.venus.restapp.test.json;

import com.venus.restapp.VenusRestJSONClient;
import com.venus.restapp.VenusRestResponse;
import com.venus.restapp.test.AbstractTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import net.sf.json.JSONObject;

/**
 * Department related tests
 */
public class DepartmentTest extends AbstractTest {

  private int randInt;
  @Before
  public void setUp() {
    randInt = getRandomNumber();
  }
  
  
  /* test create department */
  @Test
  public void testCreateDepartment() throws Exception {
    String name = "testCreateDept" + randInt;
    String code = name + "-code";
    VenusRestJSONClient client = new VenusRestJSONClient();

    /* create admin user and login as that user */
    createAdminUserAndLogin(client);
    
    /* create department */
    JSONObject resp = client.createDepartment(name, code, null);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("Response code", resp.getBoolean("error"));

    /* get the department now */
    resp = client.getDepartment(name, null);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertFalse("Response code", resp.getBoolean("error"));
  }

  /* test getting departments page - with out any parameters */
  @Test
  public void testGetDepartmentsPage() throws Exception {
    VenusRestJSONClient client = new VenusRestJSONClient();
    JSONObject resp = client.getDepartments(null);
    Assert.assertNotNull("Get Departments response is not proper", resp);
  }
}
