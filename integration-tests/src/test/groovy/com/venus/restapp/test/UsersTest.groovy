package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;

import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;

public class UsersTest extends AbstractTest {

  private VenusRestJSONClient client = null;
  
  @Before
  public void setUp() {
    client = new VenusRestJSONClient();
  }

  /**
   * Test to check whether an admin user can create the user or not
   */
  @Test
  public void testCreateUser() {
    createAdminUserAndLogin(client);
    
    def name = "testCU-" + getRandomString();
    def password = name + "-passwd";
    def params = [password:password];
	
    def resp = client.createUser(name, params);
    Assert.assertFalse("Can't create user", resp?.error);
    def user = resp?.entry;
    params['username'] = name;
    testUserDetails(user, params);

    /* logout now and try to crete */
    client.logout();

    /* check the error creating user with out credentials */
    resp = client.createUser(name, params);
    testError(resp, HttpStatus.SC_UNAUTHORIZED);
  }

  /**
   * Create user as admin, get the user and check the user info
   */
  @Test
  public void testGetUserAsAdmin() {    
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testGU-" + getRandomString();
    def params = buildUserOptionalParams(name);

    /* create the user and check the details */
    def resp = client.createUser(name, params);
    testNoErrors(resp);
    def user = resp?.entry;
    params['username'] = name;
    testUserDetails(user, params);
    
    /* get the created user info now */
    resp = client.getUser(name, null);
    testNoErrors(resp);
    user = resp?.entry;
    testUserDetails(user, params);
  }

  /**
   * Create user as admin, get the user as not logged in user and check the user info
   * For now, our server sends all information regarding to user. This test needs to be
   * changed when our server sends mini-info for not-logged in user
   */
  @Test
  public void testGetUserAsAnonymousUser() {    
    /* create an admin user and login as admin */
    createAdminUserAndLogin(client);
    def name = "testGUAAnonU-" + getRandomString();
    def params = buildUserOptionalParams(name);

    /* create the user and check the details */
    def resp = client.createUser(name, params);
    testNoErrors(resp);
    def user = resp?.entry;
    params['username'] = name;
    testUserDetails(user, params);

    /* logout now */
    resp = client.logout();
    testNoErrors(resp);
       
    /* get the created user info now as anonymous user */
    resp = client.getUser(name, null);
    testNoErrors(resp);
    user = resp?.entry;
    testUserDetails(user, params);
  }

  /**
   * create optional parameters for creating user, and build a map of the optional params
   * and return. The optional parameters are based on the 'name' sent as argument
   */
  public static Map buildUserOptionalParams(def name) {
    def password = name + "-passwd";
    def userId = name + "-userId";
    def email = name + "-email";
    def firstName = name + "-firstName";
    def lastName = name + "-lastName";
    def gender = 'male';
    def address1 = name + "-address1";
    def address2 = name + "-address2";
    def city = name + "-city";
    def country = name + "-country";
    def postalCode = '121121';
    def url = 'http://www.linked.in/sigabort';
    def photoUrl = 'http://www.linked.in/sigabort/pic';
    def joinDate = '11/11/2009';
    def birthDate = '06/06/1990';
    
    return [password:password, userId:userId, email:email, firstName:firstName, lastName:lastName, 
            gender:gender, address1:address1, address2:address2, city:city, country:country,
            postalCode:postalCode, url:url, photoUrl:photoUrl, joinDate:joinDate, birthDate:birthDate];
  }

  /**
   * Test the user details returned from the server
   */
  public static void testUserDetails(def user, def params) {
    if (user == null) {
      return;
    }
    Assert.assertNotNull("The user is not proper", user);
    if (params == null) {
      return;
    }
    Assert.assertEquals("The user's username", params['username'], user?.username);
    Assert.assertEquals("The user's password", params['password'], user?.password);
    Assert.assertEquals("The user's userId", params['userId'], user?.userId);
    Assert.assertEquals("The user's email", params['email'], user?.email);
    Assert.assertEquals("The user's firstName", params['firstName'], user?.firstName);
    Assert.assertEquals("The user's lastName", params['lastName'], user?.lastName);
    Assert.assertEquals("The user's gender", params['gender'], user?.gender);
    Assert.assertEquals("The user's url", params['url'], user?.url);
    Assert.assertEquals("The user's phone", params['phone'], user?.phone);
    Assert.assertEquals("The user's address1", params['address1'], user?.address1);
    Assert.assertEquals("The user's address2", params['address2'], user?.address2);
    Assert.assertEquals("The user's city", params['city'], user?.city);
    Assert.assertEquals("The user's country", params['country'], user?.country);
    Assert.assertEquals("The user's postalCode", params['postalCode'], user?.postalCode);
    Assert.assertEquals("The user's photoUrl", params['photoUrl'], user?.photoUrl);
    SimpleDateFormat df = new SimpleDateFormat('dd/MM/yyyy');

    /* server sends the dates in unix epoch timings, so convert them to client's style before
     * testing
     */
    if (user?.joinDate != null) {
      user.joinDate = df.format(new Date(user?.joinDate));
    }
    if (user?.birthDate != null) {
      user.birthDate = df.format(new Date(user?.birthDate));
    }
    
    Assert.assertEquals("The user's joinDate", params['joinDate'], user?.joinDate);
    Assert.assertEquals("The user's birthDate", params['birthDate'], user?.birthDate);
    
  }
    
}