package com.venus.dal.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
    sess = vs.getSession();
  }

  @Test
  public void testCreateUser() throws Exception {
   Transaction trans = sess.beginTransaction();
   
   String name = "testCU" + getUniqueName();
   String username = name + "-uname";
   String password = name + "-passwd";
   String firstName = name + "-fName";
   String lastName = name + "-lName";
   String email = name + "-email";
   String url = name + "-url";
   String gender = "male";
   String address = "India";
   Date birthDate = new Date();
   
   User user = createUser(username, password, firstName, lastName, email, address, gender, birthDate, url, vs);
   Assert.assertNotNull(user);
   trans.commit();
   
   testUserDetails(user, username, password, firstName, lastName, email, address, gender, birthDate, url);
  }
  
  @Test
  public void testFindUserByUsername() throws Exception {

   Transaction trans = sess.beginTransaction();

   String name = "testFindUByUN" + getUniqueName();
   User user = createTestUser(name, vs);
   Assert.assertNotNull(user);
   trans.commit();
   
   User nuUser = uol.findUserByUsername(name, vs);
   Assert.assertNotNull(nuUser);
   Assert.assertEquals("User is not same", user, nuUser);
  }

 
  private User createUser(String username, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url, VenusSession vs) {
    User user = uol.createUpdateUser(username, password, firstName, lastName, email, address, gender, birthDate, url, vs);
    return user;
  }

  private User createTestUser(String name, VenusSession vs) {
   String password = name + "-passwd";
   String firstName = name + "-fName";
   String lastName = name + "-lName";
   String email = name + "-email";
   String url = name + "-url";
   String gender = "male";
   String address = "India";
   Date birthDate = new Date();
   User user = uol.createUpdateUser(name, password, firstName, lastName, email, address, gender, birthDate, url, vs);
   return user;
  }
  
  private void testUserDetails(User user, String username, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url) {
    Assert.assertEquals("Username", username, user.getUsername());
    Assert.assertEquals("Password", password, user.getPassword());
    Assert.assertEquals("FirstName", firstName, user.getFirstName());
    Assert.assertEquals("LastName", lastName, user.getLastName());
    Assert.assertEquals("Email", email, user.getEmail());
    Assert.assertEquals("Address", address, user.getAddress());
    Assert.assertEquals("Gender", gender, user.getGender());
    Assert.assertEquals("Url", url, user.getUrl());
    Assert.assertEquals("BirthDate", birthDate, user.getBirthDate());
  }

}