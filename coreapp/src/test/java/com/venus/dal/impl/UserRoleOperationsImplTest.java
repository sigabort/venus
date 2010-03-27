package com.venus.dal.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.model.Role;
import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;

import com.venus.model.impl.BaseImplTest;

/**
 * Class containing tests for {@link UserRoleOperations}
 * @author sigabort
 *
 */
public class UserRoleOperationsImplTest extends BaseImplTest {
  private UserRoleOperationsImpl uro;
  private Session sess;
  private VenusSession vs;
  private int rand;
  private User testUser;
  private Department testDept;
  
  @Before
  public void setUp() throws Exception {
    uro = new UserRoleOperationsImpl();
    vs = getVenusSession();
    /* XXX: we need to do this after creating the institute */
    rand = getRandomNumber();
    vs.setInstituteId(rand);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
    testUser = UserOperationsImplTest.createTestUser(getRandomString(), vs);
    testDept = DepartmentOperationsImplTest.createTestDepartment(getRandomString(), vs);
  }

  /**
   * Test creating a new user role
   * @throws Exception
   */
  @Test
  public void testCreateUserRole() throws Exception {   
   Date created = new Date();
   Date lastModified = new Date();
   
   /* build test optional parameters */
   Map<String, Object> params = new HashMap<String, Object>(); 

   /* add custom created/lastmodified dates */
   params.put("created", created);
   params.put("lastModified", lastModified);
   
   /* create a new user role with role: principal */
   UserRole userRole = uro.createUpdateUserRole(testUser, Role.PRINCIPAL, params, vs);
   Assert.assertNotNull(userRole);
   
   /* test the details now */
   Assert.assertEquals("The user role user", testUser, userRole.getUser());
   Assert.assertEquals("The role", (int)Role.PRINCIPAL.ordinal(), (int)userRole.getRole());
   Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole.getStatus());

   /* test optional params now */
   testUserRoleParams(userRole, params);
  }

  /**
   * Test updating a user role
   * @throws Exception
   */
  @Test
  public void testUpdateUserRole() throws Exception {   
   /* create a new user role with role: principal */
   UserRole userRole = uro.createUpdateUserRole(testUser, Role.PRINCIPAL, null, vs);
   Assert.assertNotNull(userRole);
   
   /* test the details now */
   Assert.assertEquals("The user role user", testUser, userRole.getUser());
   Assert.assertEquals("The role", (int)Role.PRINCIPAL.ordinal(), (int)userRole.getRole());
   Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole.getStatus());

   /* add department as optional parameter */
   Map<String, Object> params = new HashMap<String, Object>();
   params.put("department", testDept);
 
   /* update the user role by setting a department */
   UserRole nuUserRole = uro.createUpdateUserRole(testUser, Role.INSTRUCTOR, params, vs);
   Assert.assertNotNull(nuUserRole);
   
   /* test the details now */
   Assert.assertEquals("The user role user", testUser, nuUserRole.getUser());
   Assert.assertEquals("The role", (int)Role.INSTRUCTOR.ordinal(), (int)nuUserRole.getRole());
   Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)nuUserRole.getStatus());
   
   /* test optional params now */
   testUserRoleParams(nuUserRole, params);   
  }

  
  /**
   * Test creating different roles for same user in same department 
   * @throws Exception
   */
  @Test
  public void testCreateUserRolesForSameDept() throws Exception {   
    /* add department as optional parameter */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    
    /* create a new user role with role: Instructor */
    UserRole userRole = uro.createUpdateUserRole(testUser, Role.INSTRUCTOR, params, vs);
    Assert.assertNotNull(userRole);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole.getUser());
    Assert.assertEquals("The role", (int)Role.INSTRUCTOR.ordinal(), (int)userRole.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole.getStatus());
    /* test optional params now */
    testUserRoleParams(userRole, params);   
 
    /* create a new user role in same department */
    UserRole nuUserRole = uro.createUpdateUserRole(testUser, Role.HEADOFDEPARTMENT, params, vs);
    Assert.assertNotNull(nuUserRole);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, nuUserRole.getUser());
    Assert.assertEquals("The role", (int)Role.HEADOFDEPARTMENT.ordinal(), (int)nuUserRole.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)nuUserRole.getStatus());
   
    /* test optional params now */
    testUserRoleParams(nuUserRole, params);   
  }

  /**
   * Test creating/updating user role with status as non-active 
   * @throws Exception
   */
  @Test
  public void testUpdateNonActiveUserRole() throws Exception {   
    /* add department as optional parameter */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("status", Status.Deleted);
    
    /* create a new user role with role: Instructor */
    UserRole userRole = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole.getRole());
    /* test optional params now */
    testUserRoleParams(userRole, params);   
 
    /* Update the same user role */
    UserRole nuUserRole = uro.createUpdateUserRole(testUser, Role.STAFF, null, vs);
    Assert.assertNotNull(nuUserRole);
    
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, nuUserRole.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)nuUserRole.getRole());
    /* test optional params now, the status should be active */
    params.put("status", Status.Active);
    testUserRoleParams(userRole, params);   
  }

  /**
   * Test fetching user role with status as non-active 
   * @throws Exception
   */
  @Test
  public void testGetNonActiveUserRole() throws Exception {   
    /* add department as optional parameter */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("status", Status.Deleted);
    
    /* create a new user role with role: Instructor */
    UserRole userRole = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole.getRole());
    /* test optional params now */
    testUserRoleParams(userRole, params);   
 
    /* get the same user role */
    UserRole nuUserRole = uro.getUserRole(testUser, Role.STAFF, null, vs);
    Assert.assertNull("user role found, even if it is deleted", nuUserRole);

    params.put("onlyActive", Boolean.FALSE);
    /* get the same user role even if the userrole is inactive */
    nuUserRole = uro.getUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull("user role not found, even after asking for non-active user role", nuUserRole);
    
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, nuUserRole.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)nuUserRole.getRole());
    /* test optional params now, the status should be active */
    testUserRoleParams(userRole, params);   
  }

  
  /**
   * Test creating roles with out department where it is required 
   * @throws Exception
   */
  @Test
  public void testCreateUserRolesWithOutDept1() throws Exception {   
    /* roles for which department is needed */
    Role[] roles = new Role[] {Role.INSTRUCTOR, Role.HEADOFDEPARTMENT, Role.STUDENT};

    for (int idx = 0; idx < roles.length; idx++) {
      try {
        /* create a new user role with role */
        UserRole userRole = uro.createUpdateUserRole(testUser, roles[idx], null, vs);
        Assert.fail("Created user role even with out department for role: " + roles[idx].toString());
      }
      catch (IllegalArgumentException iae) {
        //test passed
      }
    }
  }

  /**
   * Test creating roles with out department where it is not required 
   * @throws Exception
   */
  @Test
  public void testCreateUserRolesWithOutDept2() throws Exception {   
    /* roles for which department is not needed */
    Role[] roles = new Role[] {Role.ADMIN, Role.PRINCIPAL, Role.STAFF};

    for (int idx = 0; idx < roles.length; idx++) {
      try {
        /* create a new user role with role */
        UserRole userRole = uro.createUpdateUserRole(testUser, roles[idx], null, vs);
        Assert.assertNotNull("User role", userRole);
        /* test the details now */
        Assert.assertEquals("The user role user", testUser, userRole.getUser());
        Assert.assertEquals("The role", (int)roles[idx].ordinal(), (int)userRole.getRole());
        Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole.getStatus());
      }
      catch (IllegalArgumentException iae) {
        Assert.fail("Creation of user role failed, even when the department is not needed. Role: " + roles[idx].toString());
      }
    }
  }

  /**
   * Test getGetUserRoles() with out department
   * @throws Exception
   */
  @Test
  public void testGetUserRolesNoDept() throws Exception {   
    /* create a new user role with role: Staff */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, null, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole1.getStatus());
 
    /* create another user role for the same user */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, null, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole2.getStatus());
  
    /* get the user roles and check the count */
    List<UserRole> roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 2, roles.size());

    /* create another user role for the same user */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.PRINCIPAL, null, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.PRINCIPAL.ordinal(), (int)userRole3.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole3.getStatus());
    
    /* get the user roles and check the count */
    roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 3, roles.size());
  }
  
  /**
   * Test getGetUserRoles() with and without department
   * @throws Exception
   */
  @Test
  public void testGetUserRolesWithAndWithOutDept() throws Exception {   
    /* add department as optional parameter */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    params.put("status", Status.Active);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params);
 
    /* create another user role for the same user */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, params, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    testUserRoleParams(userRole2, params);    
  
    /* get the user roles and check the count */
    List<UserRole> roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 2, roles.size());

    /* create another user role for the same user with out department */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.PRINCIPAL, null, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.PRINCIPAL.ordinal(), (int)userRole3.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole3.getStatus());
    
    /* get the user roles and check the count */
    roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 3, roles.size());
  }

  
  /**
   * Test getGetUserRoles() with department
   * @throws Exception
   */
  @Test
  public void testGetUserRolesWithDept() throws Exception {   
    /* add department as optional parameter */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    params.put("status", Status.Active);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params);
 
    /* create another user role for the same user */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, params, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    testUserRoleParams(userRole2, params);    
  
    /* get the user roles and check the count */
    List<UserRole> roles = uro.getUserRoles(testUser, params, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 2, roles.size());

    /* create another user role for the same user with out department */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.PRINCIPAL, null, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.PRINCIPAL.ordinal(), (int)userRole3.getRole());
    Assert.assertEquals("The status of user role", (int)Status.Active.ordinal(), (int)userRole3.getStatus());
    
    /* get the user roles and check the count, pass the department. So, the count should be 2 only */
    roles = uro.getUserRoles(testUser, params, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of user roles", 2, roles.size());
  }

  /**
   * Test getGetUserRoles() - get non-active user roles
   * @throws Exception
   */
  @Test
  public void testGetNonActiveUserRoles() throws Exception {   
    /* add department and status as optional parameters */
    Map<String, Object> params1 = new HashMap<String, Object>();
    params1.put("department", testDept);
    params1.put("status", Status.Unverified);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params1, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params1);
 
    /* add only status as optional parameter - no department */
    Map<String, Object> params2 = new HashMap<String, Object>();
    params2.put("status", Status.Unverified);

    /* create another user role for the same user - with out department */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, params2, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    testUserRoleParams(userRole2, params2);
  
    /* get the user roles - should be no roles returned as the roles are not active */
    List<UserRole> roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertTrue("List of user roles", (roles == null || roles.size() == 0));

    Map<String, Object> options = new HashMap<String, Object>();
    options.put("onlyActive", Boolean.FALSE);
    
    /* get the non-active roles now */
    roles = uro.getUserRoles(testUser, options, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of inactive user roles", 2, roles.size());
    
    /* get the non-active roles now  with department as filter - should get only one */
    options.put("department", testDept);
    roles = uro.getUserRoles(testUser, options, vs);
    Assert.assertNotNull("List of user roles", roles);
    Assert.assertEquals("The count of inactive user roles", 1, roles.size());
    
  }

  /**
   * Test setStatus for multiple roles with out passing roles/dept
   * @throws Exception
   */
  @Test
  public void testSetStatusWithOutDeptAndRoles() throws Exception {   
    /* add department and status as optional parameters */
    Map<String, Object> params1 = new HashMap<String, Object>();
    params1.put("department", testDept);
    params1.put("status", Status.Unverified);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params1, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params1);
 
    /* add only status as optional parameter - no department */
    Map<String, Object> params2 = new HashMap<String, Object>();
    params2.put("status", Status.Unverified);

    /* create another user role for the same user - with out department */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, params2, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    testUserRoleParams(userRole2, params2);
  
    /* get the user roles - should be no roles returned as the roles are not active */
    List<UserRole> roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertTrue("List of user roles", (roles == null || roles.size() == 0));

    /* now, set the status to Active */
    int count = uro.setStatus(testUser, Status.Active, null, vs);
    
    Assert.assertEquals("The changed status count should be 2", 2, count);
    
    
    /* get the list now with out any params, they should be retrievable */
    roles = uro.getUserRoles(testUser, null, vs);
    Assert.assertEquals("List of user roles after setting status", 2, roles.size());    
  }

  
  /**
   * Test setStatus for multiple roles passing Roles
   * @throws Exception
   */
  @Test
  public void testSetStatusWithRoles() throws Exception {   
    /* add department and status as optional parameters */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    params.put("status", Status.Active);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params);
 
    /* create another user role for the same user - with out department */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, params, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
    testUserRoleParams(userRole2, params);
  
    /* create another user role for the same user - with out department */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.HEADOFDEPARTMENT, params, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.HEADOFDEPARTMENT.ordinal(), (int)userRole3.getRole());
    testUserRoleParams(userRole3, params);

    List<Role> roles = new ArrayList<Role>(2);
    roles.add(Role.STAFF);
    roles.add(Role.PRINCIPAL);
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("roles", roles);
 
    /* now, set the status to Deleted for 2 roles(staff, principal)
     * But, role-principal doesn't exist for this user. So, the total
     * deleted count should be only 1
     */
    int count = uro.setStatus(testUser, Status.Deleted, options, vs);
    Assert.assertEquals("The changed status count should be 1", 1, count);
    
    /* get the list now with out any params, they should be retrievable */
    List<UserRole> userRoles = uro.getUserRoles(testUser, null, vs);
    Assert.assertEquals("List of user roles after setting status", 2, userRoles.size());    
  }

  /**
   * Test setStatus for multiple roles passing department as option
   * @throws Exception
   */
  @Test
  public void testSetStatusWithDept() throws Exception {   
    /* add department and status as optional parameters */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    params.put("status", Status.Active);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params);
 
    /* create another user role for the same user - with out department */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, null, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
  
    /* create another user role for the same user - with out department */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.HEADOFDEPARTMENT, params, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.HEADOFDEPARTMENT.ordinal(), (int)userRole3.getRole());
    testUserRoleParams(userRole3, params);
 
    /*
     * Set the status to only user roles corresponding to the specified department  
     */
    int count = uro.setStatus(testUser, Status.Deleted, params, vs);
    Assert.assertEquals("The changed status count should be 2", 2, count);
    
    /* get the list now with out any params, they should be retrievable */
    List<UserRole> userRoles = uro.getUserRoles(testUser, null, vs);
    Assert.assertEquals("List of user roles after setting status", 1, userRoles.size());
  }

  /**
   * Test setStatus for a single User Role
   * @throws Exception
   */
  @Test
  public void testSetStatusSingleUserRole() throws Exception {   
    /* add department and status as optional parameters */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("department", testDept);
    params.put("status", Status.Active);
 
    /* create a new user role with role: Instructor */
    UserRole userRole1 = uro.createUpdateUserRole(testUser, Role.STAFF, params, vs);
    Assert.assertNotNull(userRole1);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole1.getUser());
    Assert.assertEquals("The role", (int)Role.STAFF.ordinal(), (int)userRole1.getRole());
    testUserRoleParams(userRole1, params);
 
    /* create another user role for the same user - with out department */
    UserRole userRole2 = uro.createUpdateUserRole(testUser, Role.ADMIN, null, vs);
    Assert.assertNotNull(userRole2);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole2.getUser());
    Assert.assertEquals("The role", (int)Role.ADMIN.ordinal(), (int)userRole2.getRole());
  
    /* create another user role for the same user - with out department */
    UserRole userRole3 = uro.createUpdateUserRole(testUser, Role.HEADOFDEPARTMENT, params, vs);
    Assert.assertNotNull(userRole3);
   
    /* test the details now */
    Assert.assertEquals("The user role user", testUser, userRole3.getUser());
    Assert.assertEquals("The role", (int)Role.HEADOFDEPARTMENT.ordinal(), (int)userRole3.getRole());
    testUserRoleParams(userRole3, params);
 
    /* delete UserRole-1 */
    uro.setStatus(userRole1, Status.Deleted, vs);
    
    /* get the list now with out any params, they should be retrievable */
    List<UserRole> userRoles = uro.getUserRoles(testUser, null, vs);
    Assert.assertEquals("List of user roles after setting status", 2, userRoles.size());
    
    /* delete UserRole-2 */
    uro.setStatus(userRole2, Status.Deleted, vs);
    
    /* get the list now with out any params, they should be retrievable */
    userRoles = uro.getUserRoles(testUser, null, vs);
    Assert.assertEquals("List of user roles after setting status", 1, userRoles.size());
    Assert.assertEquals("The non-active userRole after 2 deletions", (UserRole)userRole3, (UserRole)userRoles.get(0));
  }

  
  /**
   * Test the optional parameters of the user role
   * @param ur       The user role object to be tested
   * @param params   The list of parameters to check
   */
  private void testUserRoleParams(UserRole ur, Map<String, Object> params) {
    if (params != null && ur != null) {
      Assert.assertEquals("User Role Department", params.get("department"), ur.getDepartment());
      Date created = (Date)params.get("created");
      if (created != null) {
        Assert.assertEquals("User Role's Created date", created, ur.getCreated());
      }
      Date lastModified = (Date)params.get("lastModified");
      if (lastModified != null) {
        Assert.assertEquals("User Role's Last Modified date", lastModified, ur.getLastModified());
      }
      Status status = (Status) params.get("status");
      if (status != null) {
        Assert.assertEquals("User Role's Status", (int)status.ordinal(), (int)ur.getStatus());
      }
    }
  }
  
  
}