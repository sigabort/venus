package com.venus.dal.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.Institute;
import com.venus.model.User;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;
import com.venus.dal.UserOperations;

import com.venus.model.impl.BaseImplTest;
import com.venus.model.impl.InstituteImplTest;

public class UserOperationsImplTest extends BaseImplTest {
  private static UserOperationsImpl uol;
  private Session                   sess;
  private VenusSession              vs;
  private int                       rand;

  @Before
  public void setUp() {
    uol = new UserOperationsImpl();
    vs = getVenusSession();
    String name = "UsrOpsImplTest-" + getRandomString();
    Institute institute = InstituteImplTest.createTestInstitute(name, vs);
    sess = vs.getHibernateSession();
    sess.beginTransaction();
    sess.save(institute);
    vs.setInstitute(institute);
  }

  /**
   * Test creating a new user
   * 
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
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertEquals("user created date", created, user.getCreated());
    Assert.assertEquals("user last modified date", lastModified, user
        .getLastModified());
    /* test optional parameters */
    testUserOptionalParams(user, params);
  }

  /**
   * Test creating user with out any optional parameters
   * 
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
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test optional parameters */
    testUserOptionalParams(user, new HashMap<String, Object>());
  }

  /**
   * Test updating the existing user
   * 
   * @throws Exception
   */
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
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
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
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) newUser.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) newUser.getStatus());
    Assert.assertNotNull("user created date", newUser.getCreated());
    Assert.assertNotNull("user last modified date", newUser.getLastModified());
    /* test new user optional parameters */
    testUserOptionalParams(newUser, params);
  }

  /**
   * Test creating user with already used email by another user
   * 
   * @throws Exception
   */
  @Test
  public void testCreateUserWithUsedEmail() throws Exception {
    String name = "testCUWUE-" + getRandomString();
    String email = name + "-email";
    String userId = name + "-userId";

    /* create optional parameters */
    Map<String, Object> params = createTestUserParams(name);
    /* override the email, and userId values */
    params.put("email", email);
    params.put("userId", userId);

    /* create the user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);

    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test optional parameters */
    testUserOptionalParams(user, params);

    String newName = name + "-new";
    params = createTestUserParams(newName);
    /* override the email id with the one which is used by another user */
    params.put("email", email);

    /* try to create the user with new details (used email) */
    try {
      User newUser = uol.createUpdateUser(newName, params, vs);
      Assert.fail("Tried to create user with used email");
    } catch (IllegalArgumentException iae) {
      // test passed
    }

    /* override the userId with the one which is used by another user */
    params.put("email", null);
    params.put("userId", userId);

    /* try to create the user with new details (used userId) */
    try {
      User newUser = uol.createUpdateUser(newName, params, vs);
      Assert.fail("Tried to create user with used userId");
    } catch (IllegalArgumentException iae) {
      // test passed
    }
  }

  /**
   * Test updating user with already used userId by another user
   * 
   * @throws Exception
   */
  @Test
  public void testUpdateUserWithUsedUserId() throws Exception {
    String name = "testUUWUU-" + getRandomString();
    String email1 = name + "-email";
    String userId1 = name + "-userId";

    /* create optional parameters */
    Map<String, Object> params = createTestUserParams(name);
    /* override the email, and userId values */
    params.put("email", email1);
    params.put("userId", userId1);

    /* create the first user */
    User user1 = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user1);

    /* test the created user details */
    Assert.assertEquals("User name", name, user1.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user1.getInstitute().getID());

    /* test optional parameters */
    testUserOptionalParams(user1, params);

    /* create second user */
    String name2 = name + "-2";
    params = createTestUserParams(name2);

    User user2 = uol.createUpdateUser(name2, params, vs);
    Assert.assertNotNull("User creation failed", user2);

    /* test the created user details */
    Assert.assertEquals("User name", name2, user2.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user2.getInstitute().getID());

    /* override the email id with the one which is used by another user */
    params.put("email", email1);

    /* try to update the user with new details (used email) */
    try {
      User newUser = uol.createUpdateUser(name2, params, vs);
      Assert.fail("Tried to create user with used email");
    } catch (IllegalArgumentException iae) {
      // test passed
    }

    /* override the userId with the one which is used by another user */
    params.put("email", null);
    params.put("userId", userId1);

    /* try to update the user with new details (used userId) */
    try {
      User newUser = uol.createUpdateUser(name2, params, vs);
      Assert.fail("Tried to create user with used userId");
    } catch (IllegalArgumentException iae) {
      // test passed
    }

  }

  /**
   * Create/Update user with out any username
   * 
   * @throws Exception
   */
  @Test
  public void testCreateUpdateUserWithNoUsername() throws Exception {
    try {
      User user = uol.createUpdateUser(null, null, vs);
    } catch (IllegalArgumentException iae) {
      // test passed
      return;
    }
    Assert.fail("CreateUpdate with no username didn't return any exception");
  }

  /**
   * Test finding the user given the username
   * 
   * @throws Exception
   */
  @Test
  public void testFindUserByUsername() throws Exception {
    String name = "testFindUByUN-" + getRandomString();
    /* build optional test parameters */
    Map<String, Object> params = createTestUserParams(name);
    Assert.assertNotNull(params);

    /* create user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);

    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test the optional parameters */
    testUserOptionalParams(user, params);

    /* find the user by user name */
    User nuUser = uol.findUserByUsername(name, null, vs);
    Assert.assertNotNull(nuUser);
    Assert.assertEquals("User is not same", user, nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);
  }

  /**
   * Test 'findUserByUsername' with out passing name argument
   * 
   * @throws Exception
   */
  @Test
  public void testFindUserWithoutUsername() throws Exception {
    try {
      User user = uol.findUserByUsername(null, null, vs);
    } catch (IllegalArgumentException iae) {
      // test passed
      return;
    }
    Assert
        .fail("findUserByUsername didn't throw exception when username is not passed");
  }

  /**
   * Test finding the inactive user
   * 
   * @throws Exception
   */
  @Test
  public void testFindNonActiveUser() throws Exception {
    String name = "testFindNonActiveU-" + getRandomString();
    /* build optional test parameters */
    Map<String, Object> params = createTestUserParams(name);
    Assert.assertNotNull(params);
    params.put("status", Status.Unverified);
    String email = name + "-email";
    String userId = name + "-userId";
    params.put("email", email);
    params.put("userId", userId);

    /* create user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);

    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Unverified.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test the optional parameters */
    testUserOptionalParams(user, params);

    /* find the user by user name */
    User nuUser = uol.findUserByUsername(name, null, vs);
    Assert.assertNull("User found even the status is not-active", nuUser);

    /* build params with onlyActive set to false */
    params.put("onlyActive", Boolean.FALSE);
    nuUser = uol.findUserByUsername(name, params, vs);
    Assert.assertNotNull("Finding non-active suer failed", nuUser);
    Assert.assertEquals("The user found is not same as original user", user,
        nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);

    /* get the user by userId */
    nuUser = uol.findUserByUserId(userId, params, vs);
    Assert.assertNotNull("Finding non-active suer failed", nuUser);
    Assert.assertEquals("The user found is not same as original user", user,
        nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);

    /* get the user by email */
    nuUser = uol.findUserByEmail(email, params, vs);
    Assert.assertNotNull("Finding non-active suer failed", nuUser);
    Assert.assertEquals("The user found is not same as original user", user,
        nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);
  }

  /**
   * Test finding the user given the email
   * 
   * @throws Exception
   */
  @Test
  public void testFindUserByEmail() throws Exception {
    String name = "testFindUByEmail-" + getRandomString();
    /* build optional test parameters */
    Map<String, Object> params = createTestUserParams(name);
    Assert.assertNotNull(params);
    String email = name + "-email";
    params.put("email", email);

    /* create user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);

    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test the optional parameters */
    testUserOptionalParams(user, params);

    /* find the user by user name */
    User nuUser = uol.findUserByEmail(email, null, vs);
    Assert.assertNotNull("Can't find the user with given email", nuUser);
    Assert.assertEquals("User is not same", user, nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);
  }

  /**
   * Test finding the user given the userId
   * 
   * @throws Exception
   */
  @Test
  public void testFindUserByUserId() throws Exception {
    String name = "testFindUByUserId-" + getRandomString();
    /* build optional test parameters */
    Map<String, Object> params = createTestUserParams(name);
    Assert.assertNotNull(params);
    String userId = name + "-userId";
    params.put("userId", userId);

    /* create user */
    User user = uol.createUpdateUser(name, params, vs);
    Assert.assertNotNull("User creation failed", user);

    /* test the created user details */
    Assert.assertEquals("User name", name, user.getUsername());
    Assert.assertEquals("User Institute Id", vs.getInstitute().getID(),
        (Integer) user.getInstitute().getID());
    Assert.assertEquals("User status", (int) Status.Active.ordinal(),
        (int) user.getStatus());
    Assert.assertNotNull("user created date", user.getCreated());
    Assert.assertNotNull("user last modified date", user.getLastModified());
    /* test the optional parameters */
    testUserOptionalParams(user, params);

    /* find the user by user name */
    User nuUser = uol.findUserByUserId(userId, null, vs);
    Assert.assertNotNull("Can't find the user with given userId", nuUser);
    Assert.assertEquals("User is not same", user, nuUser);
    /* check optional parameters too */
    testUserOptionalParams(nuUser, params);
  }

  /**
   * Create users and get them
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsers() throws Exception {
    String name = "testGetUsers-" + getRandomString();
    String name1 = name + "-name1", name2 = name + "-name2";

    Map<String, Object> params1 = createTestUserParams(name1);
    Map<String, Object> params2 = createTestUserParams(name2);

    /* create one user */
    User user1 = uol.createUpdateUser(name1, params1, vs);
    Assert.assertNotNull("Creating user1 failed", user1);

    /* validate the values now */
    Assert.assertEquals("user name", name1, user1.getUsername());
    Assert.assertEquals("user institute Id", vs.getInstitute().getID(), user1
        .getInstitute().getID());
    Assert.assertNotNull("user created date", user1.getCreated());
    Assert.assertNotNull("user last modified date", user1.getLastModified());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user1, params1);

    /* create another user */
    User user2 = uol.createUpdateUser(name2, params2, vs);
    Assert.assertNotNull(user2);

    /* validate the values now */
    Assert.assertEquals("user name", name2, user2.getUsername());
    Assert.assertEquals("user institute Id", vs.getInstitute().getID(), user2
        .getInstitute().getID());
    Assert.assertNotNull("user created date", user2.getCreated());
    Assert.assertNotNull("user last modified date", user2.getLastModified());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user2, params2);

    /* fetch the users now */
    List<User> list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull(list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());
  }

  /**
   * Get the users with sorting on Id
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsersSortOnId() throws Exception {
    String name = "testGetUsrsSortOnId-" + getRandomString();
    String name1 = name + "-name1", name2 = name + "-name2";

    Map<String, Object> params1 = createTestUserParams(name1);
    Map<String, Object> params2 = createTestUserParams(name2);

    /* create one user */
    User user1 = uol.createUpdateUser(name1, params1, vs);
    Assert.assertNotNull("Creating user1 failed", user1);

    /* validate the values now */
    Assert.assertEquals("user name", name1, user1.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user1, params1);

    /* create another user */
    User user2 = uol.createUpdateUser(name2, params2, vs);
    Assert.assertNotNull("Creating user2 failed", user2);

    /* validate the values now */
    Assert.assertEquals("user name", name2, user2.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user2, params2);

    /* fetch the users now */
    List<User> list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());

    /* set order on "id" */
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("sortBy", "id");
    options.put("isAscending", Boolean.TRUE);

    /* get the users sorted by id, ascending */
    list = uol.getUsers(0, 10, options, vs);
    Assert.assertNotNull("didn't get any users", list);
    Assert.assertEquals("list should only contain 2 users", (new Integer(2))
        .intValue(), (int) list.size());
    User u1 = (User) list.get(0);
    Assert.assertEquals("first one should be equal to the one created first",
        user1, u1);
    User u2 = (User) list.get(1);
    Assert.assertEquals("second one should be equal to the one created later",
        user2, u2);

    /* get the users sorted by id, descending */
    options.put("isAscending", Boolean.FALSE);
    list = uol.getUsers(0, 10, options, vs);
    Assert.assertNotNull(list);
    Assert.assertEquals("list should only contain 2 users", (new Integer(2))
        .intValue(), (int) list.size());
    u1 = (User) list.get(0);
    Assert.assertEquals("first one should be equal to the one created later",
        user2, u1);
    u2 = (User) list.get(1);
    Assert.assertEquals("second one should be equal to the one created first",
        user1, u2);
  }

  /**
   * Get the users with sort on username
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsersSortOrderOnUsername() throws Exception {
    String name = "testGetUsrsSortOrdOnUN-" + getRandomString();
    String name1 = name + "-name1", name2 = name + "-name2";

    Map<String, Object> params1 = createTestUserParams(name1);
    Map<String, Object> params2 = createTestUserParams(name2);

    /* create one user */
    User user1 = uol.createUpdateUser(name1, params1, vs);
    Assert.assertNotNull("Creating user1 failed", user1);

    /* validate the values now */
    Assert.assertEquals("user name", name1, user1.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user1, params1);

    /* create another user */
    User user2 = uol.createUpdateUser(name2, params2, vs);
    Assert.assertNotNull("Creating user2 failed", user2);

    /* validate the values now */
    Assert.assertEquals("user name", name2, user2.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user2, params2);

    /* fetch the users now */
    List<User> list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());

    /* set order on "id" */
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("sortBy", "username");
    options.put("isAscending", Boolean.TRUE);

    /* get the users sorted by id, ascending */
    list = uol.getUsers(0, 10, options, vs);
    Assert.assertNotNull("didn't get any users", list);
    Assert.assertEquals("list should only contain 2 users", (new Integer(2))
        .intValue(), (int) list.size());
    User u1 = (User) list.get(0);
    Assert.assertEquals("first one should be equal to the one created first",
        user1, u1);
    User u2 = (User) list.get(1);
    Assert.assertEquals("second one should be equal to the one created later",
        user2, u2);

    /* get the users sorted by id, descending */
    options.put("isAscending", Boolean.FALSE);
    list = uol.getUsers(0, 10, options, vs);
    Assert.assertNotNull(list);
    Assert.assertEquals("list should only contain 2 users", (new Integer(2))
        .intValue(), (int) list.size());
    u1 = (User) list.get(0);
    Assert.assertEquals("first one should be equal to the one created later",
        user2, u1);
    u2 = (User) list.get(1);
    Assert.assertEquals("second one should be equal to the one created first",
        user1, u2);
  }

  /**
   * Get the users with sort on field which doesnt exist
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsersSortOrderOnNonExistingField() throws Exception {
    String name = "testGetUsrsSortOrdOnNEF-" + getRandomString();
    /* field name to sort on */
    String field = name;

    /* set order on "id" */
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("sortBy", field);
    options.put("isAscending", Boolean.TRUE);

    try {
      /* get the users sorted by non-existing field, ascending */
      List list = uol.getUsers(0, 10, options, vs);
    } catch (DataAccessException dae) {
      // test passed
      return;
    }

    Assert.fail("sorting unknown field didnt return DAE exception");
  }

  /**
   * Test getUsers() after some users' status set to non-Active.
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsersAfterDeletion() throws Exception {
    String name = "testGetUsrsAfterDel-" + getRandomString();
    String name1 = name + "-name1", name2 = name + "-name2";

    Map<String, Object> params1 = createTestUserParams(name1);
    Map<String, Object> params2 = createTestUserParams(name2);

    /* create one user */
    User user1 = uol.createUpdateUser(name1, params1, vs);
    Assert.assertNotNull("Creating user1 failed", user1);

    /* validate the values now */
    Assert.assertEquals("user name", name1, user1.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user1, params1);

    /* create another user */
    User user2 = uol.createUpdateUser(name2, params2, vs);
    Assert.assertNotNull("Creating user2 failed", user2);

    /* validate the values now */
    Assert.assertEquals("user name", name2, user2.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user2, params2);

    /* fetch the users now */
    List<User> list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());

    /* now delete first user */
    uol.setStatus(user1, Status.Deleted, vs);

    /* fetch the users now */
    list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 1 */
    Assert.assertEquals("list should contain only 1 users", 1, list.size());

    /* get the all - active and non-active now, sorted by id */
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("sortBy", "id");
    options.put("isAscending", Boolean.TRUE);
    options.put("onlyActive", Boolean.FALSE);

    /* fetch the users now */
    list = uol.getUsers(0, 10, options, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());

    /* check the users now */
    User u1 = (User) list.get(0);
    Assert.assertEquals("first one's status should be set to deleted",
        (int) Status.Deleted.ordinal(), (int) u1.getStatus());
    User u2 = (User) list.get(1);
    Assert.assertEquals("second one should be equal to the one created later",
        user2, u2);
  }

  /**
   * Test getUsersCount()
   * 
   * @throws Exception
   */
  @Test
  public void testGetUsersCount() throws Exception {
    String name = "testGetUsrsCount-" + getRandomString();
    String name1 = name + "-name1", name2 = name + "-name2";

    Map<String, Object> params1 = createTestUserParams(name1);
    Map<String, Object> params2 = createTestUserParams(name2);

    /* create one user */
    User user1 = uol.createUpdateUser(name1, params1, vs);
    Assert.assertNotNull("Creating user1 failed", user1);

    /* validate the values now */
    Assert.assertEquals("user name", name1, user1.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user1, params1);

    /* create another user */
    User user2 = uol.createUpdateUser(name2, params2, vs);
    Assert.assertNotNull("Creating user2 failed", user2);

    /* validate the values now */
    Assert.assertEquals("user name", name2, user2.getUsername());
    /* validate the optional fields of the user object */
    testUserOptionalParams(user2, params2);

    /* fetch the users now */
    List<User> list = uol.getUsers(0, 10, null, vs);
    Assert.assertNotNull("didn't get any users", list);
    /* should be only 2 */
    Assert.assertEquals("list should contain only 2 users", 2, list.size());

    /* get count */
    Integer count = uol.getUsersCount(null, vs);
    /* should be only 2 */
    Assert.assertEquals("users count should be 2", 2, (int) count);

    /* now delete first user */
    uol.setStatus(user1, Status.Deleted, vs);

    /* fetch the users count now */
    count = uol.getUsersCount(null, vs);
    /* should be only 1 */
    Assert.assertEquals("users count should be 1", 1, (int) count);

    /* get the all - active and non-active now, sorted by id */
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("sortBy", "id");
    options.put("isAscending", Boolean.TRUE);
    options.put("onlyActive", Boolean.FALSE);

    /* fetch the users now */
    count = uol.getUsersCount(options, vs);
    /* should be only 2 */
    Assert.assertEquals("users count should be 2", 2, (int) count);
  }

  public static User createTestUser(String name, VenusSession vsess)
      throws Exception {
    Map<String, Object> params = createTestUserParams(name);
    UserOperations uo = new UserOperationsImpl();
    return uo.createUpdateUser(name, params, vsess);
  }

  private static Map<String, Object> createTestUserParams(String name) {
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

    Map<String, Object> params = buildOptionalParams(userId, password, email,
        firstName, lastName, gender, url, phone, address1, address2, city,
        country, postalCode, photoUrl, birthDate, joinDate, null, null, null);
    return params;
  }

  private void testUserOptionalParams(User user, Map<String, Object> params) {
    Assert.assertEquals("UserId", (String) params.get("userId"), user
        .getUserId());
    Assert.assertEquals("Password", (String) params.get("password"), user
        .getPassword());
    Assert.assertEquals("FirstName", (String) params.get("firstName"), user
        .getFirstName());
    Assert.assertEquals("LastName", (String) params.get("lastName"), user
        .getLastName());
    Assert.assertEquals("Email", (String) params.get("email"), user.getEmail());
    Assert.assertEquals("Address1", (String) params.get("address1"), user
        .getAddress1());
    Assert.assertEquals("Address2", (String) params.get("address2"), user
        .getAddress2());
    Assert.assertEquals("city", (String) params.get("city"), user.getCity());
    Assert.assertEquals("country", (String) params.get("country"), user
        .getCountry());
    Assert.assertEquals("postalCode", (String) params.get("postalCode"), user
        .getPostalCode());
    Assert.assertEquals("photoUrl", (String) params.get("photoUrl"), user
        .getPhotoUrl());
    Assert.assertEquals("Gender", (String) params.get("gender"), user
        .getGender());
    Assert.assertEquals("Url", (String) params.get("url"), user.getUrl());
    Assert.assertEquals("BirthDate", (Date) params.get("birthDate"), user
        .getBirthDate());
    Assert.assertEquals("JoinDate", (Date) params.get("joinDate"), user
        .getJoinDate());
  }

  /**
   * build the map of optional parameters for the user
   */
  private static Map<String, Object> buildOptionalParams(String userId,
      String password, String email, String firstName, String lastName,
      String gender, String url, String phone, String address1,
      String address2, String city, String country, String postalCode,
      String photoUrl, Date birthDate, Date joinDate, Status status,
      Date created, Date lastModified) {
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