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
    client = new VenusRestJSONClient('URoleTest-' + getRandomString());
  }

  /**
   * Test creating user with user roles
   */
  @Test
  public void testCreateUserWithRole() {
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testCreateUserWRole" + getRandomString();
    def params = UsersTest.buildUserOptionalParams(name);

    /* set roles */
    def roles = ['ADMIN', 'PRINCIPAL'];
    params['role'] = roles;
    
    /* create user with roles */
    def resp = client.createUser(name, params);
    testNoErrors(resp);
    def user = resp?.entry;
    Assert.assertNotNull("user is not proper", user);
    params['username'] = name;
    UsersTest.testUserDetails(user, params);
    
    /* get the user roles now */
    resp = client.getUserRoles(name, null);
    testNoErrors(resp);
    def userRoles = resp?.entries;
    Assert.assertNotNull("user roles not proper", userRoles);
    testUserRoles(userRoles, params);
  }

  /**
   * Test creating user, add roles, fetch and check
   */
  @Test
  public void testCreateUserAndAddRoles() {
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testCreateUserAAddRole-" + getRandomString();

    def user = UsersTest.createTestUser(client, name);
    
    /* set roles */
    def params = [role:['ADMIN', 'STAFF']];
    
    /* create user with roles */
    def resp = client.createUserRoles(name, params);
    testNoErrors(resp);
    def userRoles = resp?.entries;
    Assert.assertNotNull("user roles not proper", userRoles);
    params['username'] = name;
    testUserRoles(userRoles, params);

    /* log out as admin */
    resp = client.logout();  
    testNoErrors(resp);

    /* fetch the roles of the user and double check */
    resp = client.getUserRoles(name, null);
    testNoErrors(resp);
    userRoles = resp?.entries;
    Assert.assertNotNull("user roles not proper", userRoles);
    params['username'] = name;
    testUserRoles(userRoles, params);    
  }

  
  /**
   * Test fetching user roles with non-existing user
   */
  @Test
  public void testGetNonExistingUserRoles() {
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testGetNEUR-" + getRandomString();

    /* get the user roles now */
    def resp = client.getUserRoles(name, null);
    testError(resp, HttpStatus.SC_NOT_FOUND);  //user not found
  }

  /**
   * Test creating user roles with non-existing user
   */
  @Test
  public void testCreateNonExistingUserRoles() {
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testCreateNEUR-" + getRandomString();

    /* get the user roles now */
    def resp = client.createUserRoles(name, [role:['admin']]);
    testError(resp, HttpStatus.SC_NOT_FOUND);  //user not found
  }

  /**
   * Test creating user roles with department
   */
  @Test
  public void testCreateUserRolesWithDept() {
    def name = "testCURWD-" + getRandomString();
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def dept = DepartmentsTest.createTestDepartment(client, name);
    def user = UsersTest.createTestUser(client, name);
    
    def params = [role:['ADMIN', 'STAFF'], departmentName:dept?.name];
    
    def resp = client.createUserRoles(user?.username, params);
    testNoErrors(resp);
    def userRoles = resp?.entries;
    params['username'] = user?.username;
    testUserRoles(userRoles, params);
    
    /* log out as admin */
    resp = client.logout();  
    testNoErrors(resp);
  }
  
  /**
   * Test the user role details
   * @param userRoles   The list of roles which the response should contain
   * @param params      The parameters containing the expected results
   */
  public static void testUserRoles(userRoles, params) {
    def roles = params['role'];
    Assert.assertEquals("The roles length", roles?.size(), userRoles?.size());
    for (def userRole : userRoles) {
      Assert.assertTrue("The role of the user role", roles.contains(userRole?.role));
      Assert.assertEquals("The department name for the user role", params['departmentName'], userRole?.department);      
      Assert.assertEquals("The user for the user role", params['username'], userRole?.username);
    }
  }
  
}