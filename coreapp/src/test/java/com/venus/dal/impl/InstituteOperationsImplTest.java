package com.venus.dal.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.Institute;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;
import com.venus.dal.InstituteOperations;

import com.venus.model.impl.BaseImplTest;
import com.venus.model.impl.InstituteImplTest;

public class InstituteOperationsImplTest extends BaseImplTest {
  private static InstituteOperationsImpl iol;
  private Session sess;
  private VenusSession vs;
  
  @Before
  public void setUp() {
    iol = new InstituteOperationsImpl();
    vs = getVenusSession();
    sess = vs.getHibernateSession();
  }

  /* create and test institute */
  @Test
  public void testCreateInstitute() throws Exception {
   String name = "testCreateInstitute-" + getRandomString();
   String code = name + "-code";
   String displayName = name + "-displayName";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Date created = new Date();
   Date lastModified = new Date();
   Map<String, Object> params = buildOptionalParams(code, displayName, desc, photoUrl, email, Status.Deleted, created, lastModified);
   
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertEquals("institute status", (int)Status.Deleted.ordinal(), (int)institute.getStatus());
   Assert.assertEquals("institute created date", created, institute.getCreated());
   Assert.assertEquals("institute last modified date", lastModified, institute.getLastModified());
  }

  /* create with only mandatory parameters and test institute */
  @Test
  public void testCreateInstituteWithNoOptionalParams() throws Exception {
   String name = "testCreateInstitute-" + getRandomString();
   
   Institute institute = iol.createUpdateInstitute(name, null, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   /* As we set the code even if it is not passed, lets add it to the optional params */
   Map<String, Object> params = buildOptionalParams(name, null, null, null, null, null, null, null);
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertEquals("institute status", (int)Status.Active.ordinal(), (int)institute.getStatus());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute last modified date", institute.getLastModified());
  }

  /* create, update and test institute */
  @Test
  public void testUpdateInstitute() throws Exception {
   String name = "testUpdateInstitute-" + getRandomString();
   String code = name + "-code";
   String displayName = name + "-displayName";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Date created = new Date();
   Date lastModified = new Date();
   Map<String, Object> params = buildOptionalParams(code, displayName, desc, photoUrl, email, null, created, lastModified);

   /* create one institute */
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertEquals("institute status", (int)Status.Active.ordinal(), (int)institute.getStatus());
   Assert.assertEquals("institute created date", created, institute.getCreated());
   Assert.assertEquals("institute lastModified date", lastModified, institute.getLastModified());

   String newCode = name + "-nuCode";
   String newDispName = name + "-nuDispName";
   String newDesc = name + "-nuDesc";
   String newPhotoUrl = name + "-nuPhotoUrl";
   String newEmail = name + "-nuEmail";
   Map<String, Object> newParams = buildOptionalParams(newCode, newDispName, newDesc, newPhotoUrl, newEmail, null, null, null);

   /* update the institute with new values */
   Institute institute1 = iol.createUpdateInstitute(name, newParams, vs);
   Assert.assertNotNull(institute1);
   
   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(newParams, institute1);

   Assert.assertEquals("institute name", name, institute1.getName());
   Assert.assertEquals("institute status", (int)Status.Active.ordinal(), (int)institute1.getStatus());
   Assert.assertEquals("institute created date", created, institute1.getCreated());
   Assert.assertNotNull("institute last modified date", institute1.getLastModified());
  }
  
  /**
   * Create/Update institute with out any name
   * @throws Exception
   */
  @Test
  public void testCreateUpdateInstituteWithoutName() throws Exception {
    try {
      Institute institute = iol.createUpdateInstitute(null, null, vs);
    }
    catch (IllegalArgumentException iae) {
      //test passed
      return;
    }
    Assert.fail("CreateUpdate with no name didn't return any exception");
  }
  
  /* create and find the institute by its name */
  @Test
  public void testFindInstituteByName() throws Exception {
   String name = "testFindInstituteByName-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String dispName = name + "-displayName";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, null, null, null);
   
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertEquals("institute status", (int)Status.Active.ordinal(), (int)institute.getStatus());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute lastModified date", institute.getLastModified());

   Institute institute1 = iol.findInstituteByName(name, null, vs);
   Assert.assertNotNull(institute1);
   Assert.assertEquals("The institutes should be equal", institute, institute1);
  }
  
  /**
   * Test 'findInstituteByName' with out passing name argument
   * @throws Exception
   */
  @Test
  public void testFindInstituteWithoutName() throws Exception {
    try {
      Institute institute = iol.findInstituteByName(null, null, vs);
    }
    catch (IllegalArgumentException iae) {
      //test passed
      return;
    }
    Assert.fail("findInstituteByName didn't throw exception when name is not passed");
  }

  /* Create non-active institutes and try to find */
  @Test
  public void testFindNonActiveInstitutes() throws Exception {
   String name = "testFindInstituteByName-" + getRandomString();
   String code = name + "-code";
   String dispName = name + "-dispName";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, Status.Suspended, null, null);
   
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertEquals("institute status", (int)Status.Suspended.ordinal(), (int)institute.getStatus());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute lastModified date", institute.getLastModified());

   /* The institute is non-active. So it should be null */
   Institute institute1 = iol.findInstituteByName(name, null, vs);
   Assert.assertNull(institute1);

   /* Try to find the non-active institute */
   params.put("onlyActive", Boolean.FALSE);
   Institute institute2 = iol.findInstituteByName(name, params, vs);
   Assert.assertNotNull(institute2);

   Assert.assertEquals("institute name", name, institute2.getName());
   Assert.assertEquals("institute status", (int)Status.Suspended.ordinal(), (int)institute2.getStatus());

   /* check with findInstituteByCode */
   institute1 = iol.findInstituteByCode(code, null, vs);
   Assert.assertNull(institute1);

   /* Try to find the non-active institute */
   params.put("onlyActive", Boolean.FALSE);
   institute2 = iol.findInstituteByCode(code, params, vs);
   Assert.assertNotNull(institute2);

   Assert.assertEquals("institute name", name, institute2.getName());
   Assert.assertEquals("institute status", (int)Status.Suspended.ordinal(), (int)institute2.getStatus());
  }

  /* create and find the institute by its code */
  @Test
  public void testFindInstituteByCode() throws Exception {
   String name = "testFindInstituteByCode-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String dispName = name + "-dispName";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, null, null, null);
   
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);
   
   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);

   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute lastModified date", institute.getLastModified());

   Institute institute1 = iol.findInstituteByCode(code, null, vs);
   Assert.assertNotNull(institute1);
   Assert.assertEquals("The institutes should be equal", institute, institute1);
  }

  /* create, delete and update the same */
  @Test
  public void testUpdateDeletedInstitute() throws Exception {
   String name = "testUpdateDeletedInstitute-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String dispName = name + "-dispName";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute last modified date", institute.getLastModified());

   /* delete the institute */
   iol.setStatus(institute, Status.Deleted, vs);

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newDispName = name + "-newDispName";
   String newPhotoUrl = name + "-nuPhotoUrl";
   String newEmail = name + "-nuEmail";
   Map<String, Object> newParams = buildOptionalParams(newCode, newDispName, newDesc, newPhotoUrl, newEmail, null, null, null);

   /* update the institute with new values */
   Institute institute1 = iol.createUpdateInstitute(name, newParams, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(newParams, institute1);
   
   Assert.assertEquals("institute name", name, institute1.getName());
   Assert.assertNotNull("institute created date", institute1.getCreated());
   Assert.assertNotNull("institute last modified date", institute1.getLastModified());
  }
 
  /* create, delete and test */
  @Test
  public void testDeleteInstitute() throws Exception {
   String name = "testDeleteInstitute-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String dispName = name + "-dispName";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute = iol.createUpdateInstitute(name, params, vs);
   Assert.assertNotNull(institute);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params, institute);
   
   Assert.assertEquals("institute name", name, institute.getName());
   Assert.assertNotNull("institute created date", institute.getCreated());
   Assert.assertNotNull("institute last modified date", institute.getLastModified());

   /* delete the institute */
   iol.setStatus(institute, Status.Deleted, vs);

   Institute institute1 = iol.findInstituteByCode(code, null, vs);
   Assert.assertNull("institute should not be found", institute1);

   institute1 = iol.findInstituteByName(name, null, vs);
   Assert.assertNull("institute should not be found", institute1);
  }
 
  /* create some institutes and fetch them */
  @Test
  public void testGetInstitutes() throws Exception {
   String name = "testGetInstitutes-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String dispName1 = name + "-dispName1", dispName2 = name + "-dispName2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params1 = buildOptionalParams(code1, dispName1, desc1, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute1 = iol.createUpdateInstitute(name1, params1, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params1, institute1);
   
   Assert.assertEquals("institute name", name1, institute1.getName());
   Assert.assertNotNull("institute created date", institute1.getCreated());
   Assert.assertNotNull("institute last modified date", institute1.getLastModified());

   /* create another institute */
   Map<String, Object> params2 = buildOptionalParams(code2, dispName2, desc2, photoUrl, email, null, null, null);
   Institute institute2 = iol.createUpdateInstitute(name2, params2, vs);
   Assert.assertNotNull(institute2);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params2, institute2);
   
   Assert.assertEquals("institute name", name2, institute2.getName());
   Assert.assertNotNull("institute created date", institute2.getCreated());
   Assert.assertNotNull("institute last modified date", institute2.getLastModified());

   /* fetch the institutes now */
   List<Institute> list = iol.getInstitutes(0, 10, null, vs);
   Assert.assertNotNull(list);
   /* should be only 2, when we create new institute per test */
   Assert.assertTrue("list should not be less than 2", list.size() >= 2);
  }


  /* create some institutes and fetch them with sortOrder, sortBy specified */
  @Test
  public void testGetInstitutesSortOnId() throws Exception {
   String name = "testGetInstitutesSortOnId-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String dispName1 = name + "-dispName1", dispName2 = name + "-dispName2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";

   Map<String, Object> params1 = buildOptionalParams(code1, dispName1, desc1, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute1 = iol.createUpdateInstitute(name1, params1, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params1, institute1);
   
   Assert.assertEquals("institute name", name1, institute1.getName());

   /* create another institute */
   Map<String, Object> params2 = buildOptionalParams(code2, dispName2, desc2, photoUrl, email, null, null, null);
   Institute institute2 = iol.createUpdateInstitute(name2, params2, vs);
   Assert.assertNotNull(institute2);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params2, institute2);
   
   Assert.assertEquals("institute name", name2, institute2.getName());

   /* fetch the institutes now */
   List<Institute> list = iol.getInstitutes(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should contain at least 2 institutes", list.size() >= 2);
   
   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "id");
   options.put("isAscending", Boolean.TRUE);

   /* get the institutes sorted by id, ascending */
   list = iol.getInstitutes(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should contain at least 2 institutes", list.size() >= 2);
  }

  /**
   * Test getInstitutes() with sorting on name
   * @throws Exception
   */
  @Test
  public void testGetInstitutesSortOnName() throws Exception {
   String name = "testGetInstitutesSortOnId-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String dispName1 = name + "-dispName1", dispName2 = name + "-dispName2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();

   Map<String, Object> params1 = buildOptionalParams(code1, dispName1, desc1, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute1 = iol.createUpdateInstitute(name1, params1, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params1, institute1);
   
   Assert.assertEquals("institute name", name1, institute1.getName());

   /* create another institute */
   Map<String, Object> params2 = buildOptionalParams(code2, dispName2, desc2, photoUrl, email, null, null, null);
   Institute institute2 = iol.createUpdateInstitute(name2, params2, vs);
   Assert.assertNotNull(institute2);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params2, institute2);
   
   Assert.assertEquals("institute name", name2, institute2.getName());

   /* fetch the institutes now */
   List<Institute> list = iol.getInstitutes(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should at least contain 2 institutes", list.size() >= 2);
   
   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "name");
   options.put("isAscending", Boolean.TRUE);

   /* get the institutes sorted by id, ascending */
   list = iol.getInstitutes(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should contain at least 2 institutes", list.size() >= 2);   
  }
  
  
  /**
   * Test getInstitutes() with sorting on non-existing field
   * @throws Exception
   */
  @Test
  public void testGetInstitutesSortOnNonExistingField() throws Exception {
   String name = "testGetInstitutesSortOnNEF-" + getRandomString();
   String field = name;

   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", field);
   options.put("isAscending", Boolean.TRUE);

   try {
     /* get the institutes sorted by non-existing field, ascending */
     List list = iol.getInstitutes(0, 10, options, vs);
   }
   catch (DataAccessException dae) {
     //test passed
     return;
   }
   Assert.fail("getInstitutes didn't return DAE exception even after sorting on non-existing field");
  }
  
  /** create some institutes, delete and check again 
   * @throws Exception
   */
  @Test
  public void testGetInstitutesAfterDeletion() throws Exception {
   String name = "testGetInstitutesAftDel-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String dispName1 = name + "-dispName1", dispName2 = name + "-dispName2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();

   Map<String, Object> params1 = buildOptionalParams(code1, dispName1, desc1, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute1 = iol.createUpdateInstitute(name1, params1, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params1, institute1);
   
   Assert.assertEquals("institute name", name1, institute1.getName());

   /* create another institute */
   Map<String, Object> params2 = buildOptionalParams(code2, dispName2, desc2, photoUrl, email, null, null, null);
   Institute institute2 = iol.createUpdateInstitute(name2, params2, vs);
   Assert.assertNotNull(institute2);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params2, institute2);
   
   Assert.assertEquals("institute name", name2, institute2.getName());

   /* fetch the institutes now */
   List<Institute> list = iol.getInstitutes(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should atleast contain 2 institutes", list.size() >=  2);

   /* set the status of first institute to deleted */
   iol.setStatus(institute1, Status.Deleted, vs);
   /* now try to get the institutes again. This should return only second one */
   list = iol.getInstitutes(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should at least contain 1 institute", list.size() >= 1);

   /* get the all - active and non-active now, sorted by id */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "id");
   options.put("isAscending", Boolean.TRUE);
   options.put("onlyActive", Boolean.FALSE);

   list = iol.getInstitutes(0, 100, options, vs);
   Assert.assertNotNull(list);   
   Assert.assertTrue("list should at least contain 2 institutes", list.size() >= 2);
  }


  /* create some institutes, check the count */
  @Test
  public void testGetInstitutesCount() throws Exception {
   String name = "testGetInstitutesCount-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String dispName1 = name + "-dispName1", dispName2 = name + "-dispName2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();

   Map<String, Object> params1 = buildOptionalParams(code1, dispName1, desc1, photoUrl, email, null, null, null);

   /* create one institute */
   Institute institute1 = iol.createUpdateInstitute(name1, params1, vs);
   Assert.assertNotNull(institute1);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params1, institute1);
   
   Assert.assertEquals("institute name", name1, institute1.getName());
   
   /* create another institute */
   Map<String, Object> params2 = buildOptionalParams(code2, dispName2, desc2, photoUrl, email, null, null, null);
   Institute institute2 = iol.createUpdateInstitute(name2, params2, vs);
   Assert.assertNotNull(institute2);

   /* validate the optional fields of the institute object */
   validateInstituteOptionalFields(params2, institute2);
   
   Assert.assertEquals("institute name", name2, institute2.getName());
   
   /* get the institutes count now */
   Integer count = iol.getInstitutesCount(null, vs);
   Assert.assertTrue("list should at least contain 2 institutes", count >= 2);

   /* set the status of first institute to deleted */
   iol.setStatus(institute1, Status.Deleted, vs);
   /* now try to get the institutes count again. This should return only one */
   Integer newCount = iol.getInstitutesCount(null, vs);
   Assert.assertTrue("list should contain at least 1 institute", newCount >= 1);   
   Assert.assertEquals("count is not decreased even after deletion", (long)(count - 1), (long)newCount);

   /* get the all - active and non-active now, the count should be 2 */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("onlyActive", Boolean.FALSE);

   newCount = iol.getInstitutesCount(options, vs);
   Assert.assertTrue("list should at least contain 2 institutes", newCount >= 2);
   Assert.assertTrue("list should at least contain institutes as earlier (before deletion)", newCount >= count);   
  }

  public static Institute createTestInstitute(String name, VenusSession vs) throws Exception {
    Map<String, Object> params = createTestInstituteParams(name);
    InstituteOperations doi = new InstituteOperationsImpl();
    Institute institute = doi.createUpdateInstitute(name, params, vs);
    return institute;
  }

  private static Map<String, Object> createTestInstituteParams(String name) {
    String code = name + "-code";
    String dispName = name + "-displayName";    
    String desc = name + "-desc";
    String photoUrl = name + "-url";
    String email = name + "-email";
    Date created = new Date();
    Date lastModified = new Date();
    Map<String, Object> params = buildOptionalParams(code, dispName, desc, photoUrl, email, Status.Active, created, lastModified);
    return params;
  }
  
  /**
   * build the map of optional parameters for the institute
   */
  private static Map<String, Object> buildOptionalParams(String code, String displayName, String description, String photoUrl,
              String email, Status status, Date created, Date lastModified) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", code);
    params.put("displayName", displayName);
    params.put("description", description);
    params.put("photoUrl", photoUrl);
    params.put("email", email);
    params.put("status", status);
    params.put("created", created);
    params.put("lastModified", lastModified);
    
    return params;
  }

  /**
   * Validate the institute object against the parameters map
   */
  private void validateInstituteOptionalFields(Map<String, Object> params, Institute institute) {
    Assert.assertEquals("institute code", OperationsUtilImpl.getStringValue("code", params, null), institute.getCode());
    Assert.assertEquals("institute displayName", OperationsUtilImpl.getStringValue("displayName", params, null), institute.getDisplayName());
    Assert.assertEquals("institute description", OperationsUtilImpl.getStringValue("description", params, null), institute.getDescription());
    Assert.assertEquals("institute photoUrl", OperationsUtilImpl.getStringValue("photoUrl", params, null), institute.getPhotoUrl());
    Assert.assertEquals("institute email", OperationsUtilImpl.getStringValue("email", params, null), institute.getEmail());
  }

}