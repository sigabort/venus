package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;

import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Map;

/**
 * Department related tests
 */
public class DepartmentsTest extends AbstractTest {

  private VenusRestJSONClient client = null;
  
  @Before
  public void setUp() {
    client = new VenusRestJSONClient('DeptTest-' + getRandomString());
  }
  
  
  /* test create department, fetch and check */
  @Test
  public void testCreateDepartment() {
    VenusRestJSONClient client = new VenusRestJSONClient();
    createAdminUserAndLogin(client);

    String name = "testCreateDept-" + getRandomString();
    def params = buildDepartmentOptionalParams(name);

    /* create department */
    def resp = client.createDepartment(name, params);
    testNoErrors(resp);
    def dept = resp?.entry;
    params['name'] = name;
    testDepartmentDetails(dept, params);

    /* get the department now and check */
    resp = client.getDepartment(name, null);
    testNoErrors(resp);
    dept = resp?.entry;
    testDepartmentDetails(dept, params);
  }

  /**
   * Create one test department and return that department
   * This can be used by other tests to create a department quickly
   */
  public static Object createTestDepartment(myClient, name) {
    
    def params = buildDepartmentOptionalParams(name);

    /* create department */
    def resp = myClient.createDepartment(name, params);
    testNoErrors(resp);
    def dept = resp?.entry;
    params['name'] = name;
    testDepartmentDetails(dept, params);

    return dept;
  }

  /**
   * @param name  The name of the department
   * @return      The map of the optional parameters
   */
  public static Map buildDepartmentOptionalParams(name) {
    def code = name + "-code";
    def desc = name + "-desc";
    def photoUrl = "http://venus.com/pics/121";
    def email = "sigabort@venus.com";
    def params = [code:code, description:desc, photoUrl:photoUrl, email:email];
    return params;
  }
  
  /**
   * Test the department details
   * @param dept        The department object to test
   * @param params      The parameters containing the expected results
   */
  public static void testDepartmentDetails(dept, params) {
    Assert.assertEquals("The department name", params['name'], dept?.name);
    Assert.assertEquals("The department code", params['code'], dept?.code);
    Assert.assertEquals("The department description", params['description'], dept?.description);
    Assert.assertEquals("The department photoUrl", params['photoUrl'], dept?.photoUrl);
    Assert.assertEquals("The department email", params['email'], dept?.email);
  }
  
}
