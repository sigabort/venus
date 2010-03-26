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

import com.venus.model.User;
import com.venus.model.UserRole;
import com.venus.model.Role;
import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;

import com.venus.model.impl.BaseImplTest;

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
   
   /* create a new user role with role: Instructor */
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
   /* create a new user role with role: Instructor */
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
 
    /* update the user role by setting a department */
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
    /* get the same user role */
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
   * Test the optional parameters of the user role
   * @param ur
   * @param params
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