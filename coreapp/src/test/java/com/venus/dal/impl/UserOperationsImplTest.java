package com.venus.dal.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.User;
import com.venus.util.VenusSession;

import com.venus.model.impl.BaseImplTest;

public class UserOperationsImplTest extends BaseImplTest {
  private UserOperationsImpl uol;
  private Session sess;
  private VenusSession vs;
  
  @Before
  public void setUp() {
    uol = new UserOperationsImpl();
    vs = getVenusSession();
    /* XXX: we need to do this after creating the institute */
    vs.setInstituteId(1);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
  }

  @Test
  public void testCreateUser() throws Exception {   
   String name = "testCU" + getUniqueName();
   String username = name + "-uname";
   String userId = name + "-userId";
   String password = name + "-passwd";
   String firstName = name + "-fName";
   String lastName = name + "-lName";
   String email = name + "-email";
   String url = name + "-url";
   String gender = "male";
   String address = "India";
   Date birthDate = new Date();
   
   User user = createUser(username, userId, password, firstName, lastName, email, gender, url, address, birthDate, vs);
   Assert.assertNotNull(user);
   
   testUserDetails(user, username, userId, password, firstName, lastName, email, address, gender, birthDate, url);
  }
  
  @Test
  public void testFindUserByUsername() throws Exception {

   String name = "testFindUByUN" + getUniqueName();
   User user = createTestUser(name, vs);
   Assert.assertNotNull(user);
   
   User nuUser = uol.findUserByUsername(name, vs);
   Assert.assertNotNull(nuUser);
   Assert.assertEquals("User is not same", user, nuUser);
  }

 
  private User createUser(String username, String userId, String password, String firstName, String lastName, String email, String gender, String url, String address1, Date birthDate, VenusSession vs) throws Exception {
    User user = uol.createUpdateUser(username, userId, password, firstName, lastName, email, gender, url, null, address1, null, null, null, null, null, birthDate, null, null, null, vs);
    return user;
  }

  private User createTestUser(String name, VenusSession vs) throws Exception {
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
   User user = uol.createUpdateUser(name, userId, password, firstName, lastName, email, gender, url, phone, address1, address2, city, country, postalCode, photoUrl, birthDate, joinDate, null, null, vs);
   return user;
  }
  
  private void testUserDetails(User user, String username, String userId, String password, String firstName, String lastName, String email, String address1, String gender, Date birthDate, String url) {
    Assert.assertEquals("Username", username, user.getUsername());
    Assert.assertEquals("UserId", userId, user.getUserId());
    Assert.assertEquals("Password", password, user.getPassword());
    Assert.assertEquals("FirstName", firstName, user.getFirstName());
    Assert.assertEquals("LastName", lastName, user.getLastName());
    Assert.assertEquals("Email", email, user.getEmail());
    Assert.assertEquals("Address", address1, user.getAddress1());
    Assert.assertEquals("Gender", gender, user.getGender());
    Assert.assertEquals("Url", url, user.getUrl());
    Assert.assertEquals("BirthDate", birthDate, user.getBirthDate());
  }

}