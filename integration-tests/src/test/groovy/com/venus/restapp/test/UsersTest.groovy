package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;

import org.junit.Test;
import org.junit.Assert;

public class UsersTest extends AbstractTest {

  @Test
  public void testCreateUser() {
	VenusRestJSONClient client = new VenusRestJSONClient();
	createAdminUserAndLogin(client);
	
	def username = "testCU-" + getRandomString();
	def resp = client.createUser(username, [password:username]);
	Assert.assertFalse("Can't create user", resp?.error);
	def user = resp?.entry;
	Assert.assertEquals("User Name", username, user?.username);
	
	client.logout();
  }
}