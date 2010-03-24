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

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;

import com.venus.model.impl.BaseImplTest;

public class DepartmentOperationsImplTest extends BaseImplTest {
  private DepartmentOperationsImpl dol;
  private Session sess;
  private VenusSession vs;
  
  @Before
  public void setUp() {
    dol = new DepartmentOperationsImpl();
    vs = getVenusSession();
    /* XXX: we need to do this after creating the institute */
    Integer randInt = new Random(new Random().nextLong()).nextInt();
    vs.setInstituteId(randInt);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
  }

  /* create and test department */
  @Test
  public void testCreateDepartment() throws Exception {
   String name = "testCreateDept-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Date created = new Date();
   Date lastModified = new Date();
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, Status.Deleted, created, lastModified);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Deleted.ordinal(), (int)dept.getStatus());
   Assert.assertEquals("department created date", created, dept.getCreated());
   Assert.assertEquals("department last modified date", lastModified, dept.getLastModified());
  }

  /* create with only mandatory parameters and test department */
  @Test
  public void testCreateDepartmentWithNoOptionalParams() throws Exception {
   String name = "testCreateDept-" + getRandomString();
   
   Department dept = dol.createUpdateDepartment(name, null, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   /* As we set the code even if it is not passed, lets add it to the optional params */
   Map<String, Object> params = buildOptionalParams(name + "-" + vs.getInstituteId(), null, null, null, null, null, null);
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Active.ordinal(), (int)dept.getStatus());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department last modified date", dept.getLastModified());
  }

  /* create, update and test department */
  @Test
  public void testUpdateDepartment() throws Exception {
   String name = "testUpdateDept-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Date created = new Date();
   Date lastModified = new Date();
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, created, lastModified);

   /* create one department */
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Active.ordinal(), (int)dept.getStatus());
   Assert.assertEquals("department created date", created, dept.getCreated());
   Assert.assertEquals("department lastModified date", lastModified, dept.getLastModified());

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newPhotoUrl = name + "-nuPhotoUrl";
   String newEmail = name + "-nuEmail";
   Map<String, Object> newParams = buildOptionalParams(newCode, newDesc, newPhotoUrl, newEmail, null, null, null);

   /* update the department with new values */
   Department dept1 = dol.createUpdateDepartment(name, newParams, vs);
   Assert.assertNotNull(dept1);
   
   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(newParams, dept1);

   Assert.assertEquals("department name", name, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Active.ordinal(), (int)dept1.getStatus());
   Assert.assertEquals("department created date", created, dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());
  }
  
  /**
   * Create/Update department with out any name
   * @throws Exception
   */
  @Test
  public void testCreateUpdateDepartmentWithoutName() throws Exception {
    try {
      Department dept = dol.createUpdateDepartment(null, null, vs);
    }
    catch (IllegalArgumentException iae) {
      //test passed
      return;
    }
    Assert.fail("CreateUpdate with no name didn't return any exception");
  }
  
  /* create and find the department by its name */
  @Test
  public void testFindDepartmentByName() throws Exception {
   String name = "testFindDeptByName-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Active.ordinal(), (int)dept.getStatus());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department lastModified date", dept.getLastModified());

   Department dept1 = dol.findDepartmentByName(name, null, vs);
   Assert.assertNotNull(dept1);
   Assert.assertEquals("The departments should be equal", dept, dept1);
  }
  
  /**
   * Test 'findDepartmentByName' with out passing name argument
   * @throws Exception
   */
  @Test
  public void testFindDepartmentWithoutName() throws Exception {
    try {
      Department dept = dol.findDepartmentByName(null, null, vs);
    }
    catch (IllegalArgumentException iae) {
      //test passed
      return;
    }
    Assert.fail("findDepartmentByName didn't throw exception when name is not passed");
  }

  /* Create non-active departments and try to find */
  @Test
  public void testFindNonActiveDepartments() throws Exception {
   String name = "testFindDeptByName-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, Status.Suspended, null, null);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Suspended.ordinal(), (int)dept.getStatus());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department lastModified date", dept.getLastModified());

   /* The department is non-active. So it should be null */
   Department dept1 = dol.findDepartmentByName(name, null, vs);
   Assert.assertNull(dept1);

   /* Try to find the non-active department */
   params.put("onlyActive", Boolean.FALSE);
   Department dept2 = dol.findDepartmentByName(name, params, vs);
   Assert.assertNotNull(dept2);

   Assert.assertEquals("department name", name, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Suspended.ordinal(), (int)dept2.getStatus());

   /* check with findDepartmentByCode */
   dept1 = dol.findDepartmentByCode(code, null, vs);
   Assert.assertNull(dept1);

   /* Try to find the non-active department */
   params.put("onlyActive", Boolean.FALSE);
   dept2 = dol.findDepartmentByCode(code, params, vs);
   Assert.assertNotNull(dept2);

   Assert.assertEquals("department name", name, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());
   Assert.assertEquals("department status", (int)Status.Suspended.ordinal(), (int)dept2.getStatus());
  }

  /* create and find the department by its code */
  @Test
  public void testFindDepartmentByCode() throws Exception {
   String name = "testFindDeptByCode-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);

   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department lastModified date", dept.getLastModified());

   Department dept1 = dol.findDepartmentByCode(code, null, vs);
   Assert.assertNotNull(dept1);
   Assert.assertEquals("The departments should be equal", dept, dept1);
  }

  /* create, delete and update the same */
  @Test
  public void testUpdateDeletedDepartment() throws Exception {
   String name = "testUpdateDeletedDept-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);

   /* create one department */
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department last modified date", dept.getLastModified());

   /* delete the department */
   dol.setStatus(dept, Status.Deleted, vs);

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newPhotoUrl = name + "-nuPhotoUrl";
   String newEmail = name + "-nuEmail";
   Map<String, Object> newParams = buildOptionalParams(newCode, newDesc, newPhotoUrl, newEmail, null, null, null);

   /* update the department with new values */
   Department dept1 = dol.createUpdateDepartment(name, newParams, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(newParams, dept1);
   
   Assert.assertEquals("department name", name, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertNotNull("department created date", dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());
  }
 
  /* create, delete and test */
  @Test
  public void testDeleteDepartment() throws Exception {
   String name = "testDeleteDept-" + getRandomString();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);

   /* create one department */
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params, dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department last modified date", dept.getLastModified());

   /* delete the department */
   dol.setStatus(dept, Status.Deleted, vs);

   Department dept1 = dol.findDepartmentByCode(code, null, vs);
   Assert.assertNull("department should not be found", dept1);

   dept1 = dol.findDepartmentByName(name, null, vs);
   Assert.assertNull("department should not be found", dept1);
  }
 
  /* create some departments and fetch them */
  @Test
  public void testGetDepartments() throws Exception {
   String name = "testGetDepts-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params1, dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertNotNull("department created date", dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params2, dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());
   Assert.assertNotNull("department created date", dept2.getCreated());
   Assert.assertNotNull("department last modified date", dept2.getLastModified());

   /* fetch the departments now */
   List<Department> list = dol.getDepartments(0, 10, null, vs);
   Assert.assertNotNull(list);
   /* should be only 2, when we create new institute per test */
   Assert.assertTrue("list should not be less than 2", list.size() >= 2);
  }


  /* create some departments and fetch them with sortOrder, sortBy specified */
  @Test
  public void testGetDepartmentsSortOnId() throws Exception {
   String name = "testGetDeptsSortOnId-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();
   /* Set the new institue ID */
   vs.setInstituteId(randInt);

   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params1, dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params2, dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());

   /* fetch the departments now */
   List<Department> list = dol.getDepartments(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   
   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "id");
   options.put("isAscending", Boolean.TRUE);

   /* get the departments sorted by id, ascending */
   list = dol.getDepartments(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   Department d1 = (Department) list.get(0);
   Assert.assertEquals("first one should be equal to the one created first", dept1, d1);
   Department d2 = (Department) list.get(1);
   Assert.assertEquals("second one should be equal to the one created later", dept2, d2);

   /* get the departments sorted by id, descending */
   options.put("isAscending", Boolean.FALSE);
   list = dol.getDepartments(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   d1 = (Department) list.get(0);
   Assert.assertEquals("first one should be equal to the one created later", dept2, d1);
   d2 = (Department) list.get(1);
   Assert.assertEquals("second one should be equal to the one created first", dept1, d2);
   
  }

  /**
   * Test getDepartments() with sorting on name
   * @throws Exception
   */
  @Test
  public void testGetDepartmentsSortOnName() throws Exception {
   String name = "testGetDeptsSortOnId-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();
   /* Set the new institue ID */
   vs.setInstituteId(randInt);

   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params1, dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params2, dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());

   /* fetch the departments now */
   List<Department> list = dol.getDepartments(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   
   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "name");
   options.put("isAscending", Boolean.TRUE);

   /* get the departments sorted by id, ascending */
   list = dol.getDepartments(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   Department d1 = (Department) list.get(0);
   Assert.assertEquals("first one should be equal to the one created first", dept1, d1);
   Department d2 = (Department) list.get(1);
   Assert.assertEquals("second one should be equal to the one created later", dept2, d2);

   /* get the departments sorted by id, descending */
   options.put("isAscending", Boolean.FALSE);
   list = dol.getDepartments(0, 10, options, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   d1 = (Department) list.get(0);
   Assert.assertEquals("first one should be equal to the one created later", dept2, d1);
   d2 = (Department) list.get(1);
   Assert.assertEquals("second one should be equal to the one created first", dept1, d2);
   
  }
  
  
  /**
   * Test getDepartments() with sorting on non-existing field
   * @throws Exception
   */
  @Test
  public void testGetDepartmentsSortOnNonExistingField() throws Exception {
   String name = "testGetDeptsSortOnNEF-" + getRandomString();
   String field = name;

   /* set order on "id" */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", field);
   options.put("isAscending", Boolean.TRUE);

   try {
     /* get the departments sorted by non-existing field, ascending */
     List list = dol.getDepartments(0, 10, options, vs);
   }
   catch (DataAccessException dae) {
     //test passed
     return;
   }
   Assert.fail("getDepartments didn't return DAE exception even after sorting on non-existing field");
  }
  
  /** create some departments, delete and check again 
   * @throws Exception
   */
  @Test
  public void testGetDepartmentsAfterDeletion() throws Exception {
   String name = "testGetDeptsAftDel-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();
   /* Set the new institue ID */
   vs.setInstituteId(randInt);

   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params1, dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params2, dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());

   /* fetch the departments now */
   List<Department> list = dol.getDepartments(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());

   /* set the status of first department to deleted */
   dol.setStatus(dept1, Status.Deleted, vs);
   /* now try to get the departments again. This should return only second one */
   list = dol.getDepartments(0, 10, null, vs);
   Assert.assertNotNull(list);
   Assert.assertEquals("list should only contain only 1 department", (new Integer(1)).intValue(), (int)list.size());
   Assert.assertEquals("The department should be the second one", dept2, (Department)list.get(0));

   /* get the all - active and non-active now, sorted by id */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("sortBy", "id");
   options.put("isAscending", Boolean.TRUE);
   options.put("onlyActive", Boolean.FALSE);

   list = dol.getDepartments(0, 100, options, vs);
   Assert.assertNotNull(list);   
   Assert.assertEquals("list should only contain 2 departments", (new Integer(2)).intValue(), (int)list.size());
   Department d1 = (Department) list.get(0);
   Assert.assertEquals("first one's status should be deleted", (int)Status.Deleted.ordinal(), (int)d1.getStatus());
   Department d2 = (Department) list.get(1);
   Assert.assertEquals("second one should be equal to the one created later", dept2, d2);
  }


  /* create some departments, check the count */
  @Test
  public void testGetDepartmentsCount() throws Exception {
   String name = "testGetDeptsCount-" + getRandomString();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Integer randInt = new Random(new Random().nextLong()).nextInt();
   /* Set the new institue ID */
   vs.setInstituteId(randInt);

   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params1, dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);

   /* validate the optional fields of the dept object */
   validateDepartmentOptionalFields(params2, dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());

   /* get the departments count now */
   Integer count = dol.getDepartmentsCount(null, vs);
   Assert.assertEquals("list should only contain 2 departments", new Integer(2), count);

   /* set the status of first department to deleted */
   dol.setStatus(dept1, Status.Deleted, vs);
   /* now try to get the departments count again. This should return only one */
   count = dol.getDepartmentsCount(null, vs);
   Assert.assertEquals("list should only contain only 1 department", new Integer(1), count);

   /* get the all - active and non-active now, the count should be 2 */
   Map<String, Object> options = new HashMap<String, Object>();
   options.put("onlyActive", Boolean.FALSE);

   count = dol.getDepartmentsCount(options, vs);
   Assert.assertEquals("list should only contain 2 departments", new Integer(2), count);
  }

  /**
   * build the map of optional parameters for the department
   */
  private Map<String, Object> buildOptionalParams(String code, String description, String photoUrl,
						  String email, Status status, Date created, Date lastModified) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", code);
    params.put("description", description);
    params.put("photoUrl", photoUrl);
    params.put("email", email);
    params.put("status", status);
    params.put("created", created);
    params.put("lastModified", lastModified);
    
    return params;
  }

  /**
   * Validate the department object against the parameters map
   */
  private void validateDepartmentOptionalFields(Map<String, Object> params, Department dept) {
    Assert.assertEquals("department code", OperationsUtilImpl.getStringValue("code", params, null), dept.getCode());
    Assert.assertEquals("department description", OperationsUtilImpl.getStringValue("description", params, null), dept.getDescription());
    Assert.assertEquals("department photoUrl", OperationsUtilImpl.getStringValue("photoUrl", params, null), dept.getPhotoUrl());
    Assert.assertEquals("department email", OperationsUtilImpl.getStringValue("email", params, null), dept.getEmail());
  }

}