package com.venus.dal.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.User;
import com.venus.model.Status;
import com.venus.util.VenusSession;

import com.venus.model.impl.BaseImplTest;

public class UserOperationsImplTest extends BaseImplTest {
  private UserOperationsImpl uol;
  private Session sess;
  private VenusSession vs;
  private int rand;
  
  @Before
  public void setUp() {
    uol = new UserOperationsImpl();
    vs = getVenusSession();
    /* XXX: we need to do this after creating the institute */
    rand = getRandomNumber();
    vs.setInstituteId(rand);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
  }

  /**
   * Test creating a new user
   * @throws Exception
   */
  @Test
  public void testCreateUser() throws Exception {   
   String name = "testCU-" + getRandomString();
   Date created = new Date();
   Date lastModified = new Date();
   
   /* build test optional parameters */
   Map<String, Object> params = createTestUserParams(name); 

   /* add custom created/lastmodified dates */
   params.put("created", created);
   params.put("lastModified", lastModified);
   
   /* create a new user */
   User user = uol.createUpdateUser(name, params, vs);
   Assert.assertNotNull(user);
   
   /* test the created user details */
   Assert.assertEquals("User name", name, user.getUsername());
   Assert.assertEquals("User Institute Id", vs.getInstituteId(), (Integer)user.getInstituteId());
   Assert.assertEquals("User status", (int)Status.Active.ordinal(), (int)user.getStatus());
   Assert.assertEquals("user created date", created, user.getCreated());
   Assert.assertEquals("user last modified date", lastModified, user.getLastModified());   
   /* test optional parameters */
   testUserOptionalParams(user, params);
  }
  
  /**
   * Test creating user with out any optional parameters
   * @throws Exception
   */
  @Test
  public void testCreateUserWithNoOptionalParams() throws Exception {
    String name = "testCUWNOP-" + getRandomString();
    
    /* Create user with no optional params */
    User user = uol.createUpdateUser(name, null, vs);
    Assert.assertNotNull("User creation failed", user);
    
    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstituteId(), (Integer)user.getInstituteId());
    Assert.assertEquals("User status", (int)Status.Active.ordinal(), (int)user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());   
    /* test optional parameters */
    testUserOptionalParams(user, new HashMap<String, Object>());    
  }

  @Test
  public void testUpdateUser() throws Exception {
    String name = "testUpdateUsr-" + getRandomString();
    
    /* create optional parameters */
    Map<String, Object> params = createTestUserParams(name);

    /* create the user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);
    
    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstituteId(), (Integer)user.getInstituteId());
    Assert.assertEquals("User status", (int)Status.Active.ordinal(), (int)user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());   
    /* test optional parameters */
    testUserOptionalParams(user, params);    
    
    String newName = name + "-new";
    params = createTestUserParams(newName);
    /* update the user with new details */
    User newUser = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User updation failed", newUser);
    
    /* test the created user details */
    Assert.assertEquals("User name", name, newUser.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstituteId(), (Integer)newUser.getInstituteId());
    Assert.assertEquals("User status", (int)Status.Active.ordinal(), (int)newUser.getStatus());
    Assert.assertNotNull("user created date", newUser.getCreated());
    Assert.assertNotNull("user last modified date", newUser.getLastModified());   
    /* test new user optional parameters */
    testUserOptionalParams(newUser, params);    
  }
  
  @Test
  public void testFindUserByUsername() throws Exception {
   String name = "testFindUByUN-" + getRandomString();
   /* build optional test parameters */
   Map<String, Object> params = createTestUserParams(name);
   Assert.assertNotNull(params);

   /* create user */
   User user = uol.createUpdateUser(name, params, vs);
   Assert.assertNotNull("User creation failed", user);
   testUserOptionalParams(user, params);
   
   /* find the user by user name */
   User nuUser = uol.findUserByUsername(name, vs);
   Assert.assertNotNull(nuUser);
   Assert.assertEquals("User is not same", user, nuUser);
  }

  private Map<String, Object> createTestUserParams(String name) {
   String userId = name + "-userId";
   String password = name + "-passwd";
   String firstName = name + "-fName";
   String lastName = name + "-lName";
   String email = name + "-email";
   String url = name + "-url";
   String gender = "male";
   String phone = "1902091911";
   String address1 = name + "-address1";
   String address2 = name + "-address2";
   String city = name + "-city";
   String country = "India";
   String postalCode = "123131";
   String photoUrl = name + "-photoUrl";
   Date birthDate = new Date();
   Date joinDate = new Date();
   
   Map<String, Object> params = buildOptionalParams(userId, password, email, firstName, lastName, gender, url, phone, 
       address1, address2, city, country, postalCode, photoUrl, birthDate, joinDate, null, null, 
       null);
   return params;
  }
  
  private void testUserOptionalParams(User user, Map<String, Object> params) {
    Assert.assertEquals("UserId", (String)params.get("userId"), user.getUserId());
    Assert.assertEquals("Password", (String)params.get("password"), user.getPassword());
    Assert.assertEquals("FirstName", (String)params.get("firstName"), user.getFirstName());
    Assert.assertEquals("LastName", (String)params.get("lastName"), user.getLastName());
    Assert.assertEquals("Email", (String)params.get("email"), user.getEmail());
    Assert.assertEquals("Address1", (String)params.get("address1"), user.getAddress1());
    Assert.assertEquals("Address2", (String)params.get("address2"), user.getAddress2());
    Assert.assertEquals("city", (String)params.get("city"), user.getCity());
    Assert.assertEquals("country", (String)params.get("country"), user.getCountry());
    Assert.assertEquals("postalCode", (String)params.get("postalCode"), user.getPostalCode());
    Assert.assertEquals("photoUrl", (String)params.get("photoUrl"), user.getPhotoUrl());
    Assert.assertEquals("Gender", (String)params.get("gender"), user.getGender());
    Assert.assertEquals("Url", (String)params.get("url"), user.getUrl());
    Assert.assertEquals("BirthDate", (Date)params.get("birthDate"), user.getBirthDate());
    Assert.assertEquals("JoinDate", (Date)params.get("joinDate"), user.getJoinDate());
  }
  
  /**
   * build the map of optional parameters for the user
   */
  private Map<String, Object> buildOptionalParams(String userId, String password, String email,
              String firstName, String lastName, String gender, String url, String phone, String address1,
              String address2, String city, String country, String postalCode, String photoUrl, Date birthDate,
              Date joinDate, Status status, Date created, Date lastModified) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);
    params.put("password", password);
    params.put("email", email);
    params.put("firstName", firstName);
    params.put("lastName", lastName);
    params.put("gender", gender);
    params.put("url", url);
    params.put("phone", phone);
    params.put("address1", address1);
    params.put("address2", address2);
    params.put("city", city);
    params.put("country", country);
    params.put("postalCode", postalCode);
    params.put("photoUrl", photoUrl);
    params.put("birthDate", birthDate);
    params.put("joinDate", joinDate);
    params.put("status", status);
    params.put("created", created);
    params.put("lastModified", lastModified);
        
    return params;
  }

}