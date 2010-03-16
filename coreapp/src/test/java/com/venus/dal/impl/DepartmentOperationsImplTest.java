package com.venus.dal.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.hibernate.Session;

import com.venus.model.Department;
import com.venus.model.Status;
import com.venus.util.VenusSession;

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
    vs.setInstituteId(1);
//     vs.setInstitute(institute);
    sess = vs.getHibernateSession();
  }

  /* create and test department */
  @Test
  public void testCreateDepartment() throws Exception {
   String name = "testCreateDept-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Date created = new Date();
   Date lastModified = new Date();
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, created, lastModified);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertEquals("department status", Status.Active.ordinal(), dept.getStatus());
   Assert.assertEquals("department created date", created, dept.getCreated());
   Assert.assertEquals("department lastModified date", lastModified, dept.getLastModified());
  }

  /* create, update and test department */
  @Test
  public void testUpdateDepartment() throws Exception {
   String name = "testUpdateDept-" + getUniqueName();
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
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertEquals("department status", Status.Active.ordinal(), dept.getStatus());
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
   
   Assert.assertEquals("department name", name, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertEquals("department code", newCode, dept1.getCode());
   Assert.assertEquals("department description", newDesc, dept1.getDescription());
   Assert.assertEquals("department photoUrl", newPhotoUrl, dept1.getPhotoUrl());
   Assert.assertEquals("department email", newEmail, dept1.getEmail());
   Assert.assertEquals("department created date", created, dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());
  }
  
  /* create and find the department by its name */
  @Test
  public void testFindDepartmentByName() throws Exception {
   String name = "testFindDeptByName-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department lastModified date", dept.getLastModified());

   Department dept1 = dol.findDepartmentByName(name, vs);
   Assert.assertNotNull(dept1);
   Assert.assertEquals("The departments should be equal", dept, dept1);
  }

  /* create and find the department by its code */
  @Test
  public void testFindDepartmentByCode() throws Exception {
   String name = "testFindDeptByCode-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);
   
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department lastModified date", dept.getLastModified());

   Department dept1 = dol.findDepartmentByCode(code, vs);
   Assert.assertNotNull(dept1);
   Assert.assertEquals("The departments should be equal", dept, dept1);
  }

  /* create, delete and update the same */
  @Test
  public void testUpdateDeletedDepartment() throws Exception {
   String name = "testUpdateDeletedDept-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);

   /* create one department */
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department last modified date", dept.getLastModified());

   /* delete the department */
   dol.deleteDepartment(dept, vs);

   String newCode = name + "-nuCode";
   String newDesc = name + "-nuDesc";
   String newPhotoUrl = name + "-nuPhotoUrl";
   String newEmail = name + "-nuEmail";
   Map<String, Object> newParams = buildOptionalParams(newCode, newDesc, newPhotoUrl, newEmail, null, null, null);

   /* update the department with new values */
   Department dept1 = dol.createUpdateDepartment(name, newParams, vs);
   Assert.assertNotNull(dept1);
   
   Assert.assertEquals("department name", name, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertEquals("department code", newCode, dept1.getCode());
   Assert.assertEquals("department description", newDesc, dept1.getDescription());
   Assert.assertEquals("department photoUrl", newPhotoUrl, dept1.getPhotoUrl());
   Assert.assertEquals("department email", newEmail, dept1.getEmail());
   Assert.assertNotNull("department created date", dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());
  }
 
  /* create, delete and test */
  @Test
  public void testDeleteDepartment() throws Exception {
   String name = "testDeleteDept-" + getUniqueName();
   String code = name + "-code";
   String desc = name + "-desc";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params = buildOptionalParams(code, desc, photoUrl, email, null, null, null);

   /* create one department */
   Department dept = dol.createUpdateDepartment(name, params, vs);
   Assert.assertNotNull(dept);
   
   Assert.assertEquals("department name", name, dept.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept.getInstituteId());
   Assert.assertEquals("department code", code, dept.getCode());
   Assert.assertEquals("department description", desc, dept.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept.getPhotoUrl());
   Assert.assertEquals("department email", email, dept.getEmail());
   Assert.assertNotNull("department created date", dept.getCreated());
   Assert.assertNotNull("department last modified date", dept.getLastModified());

   /* delete the department */
   dol.deleteDepartment(dept, vs);

   Department dept1 = dol.findDepartmentByCode(code, vs);
   Assert.assertNull("department should not be found", dept1);

   dept1 = dol.findDepartmentByName(name, vs);
   Assert.assertNull("department should not be found", dept1);
  }
 
  /* create some departments and fetch them */
  @Test
  public void testGetDepartments() throws Exception {
   String name = "testGetDepts-" + getUniqueName();
   String name1 = name + "-name1", name2 = name + "-name2";
   String code1 = name + "-code1", code2 = name + "-code2";
   String desc1 = name + "-desc1", desc2 = name + "-desc2";
   String photoUrl = name + "-url";
   String email = name + "-email";
   Map<String, Object> params1 = buildOptionalParams(code1, desc1, photoUrl, email, null, null, null);

   /* create one department */
   Department dept1 = dol.createUpdateDepartment(name1, params1, vs);
   Assert.assertNotNull(dept1);
   
   Assert.assertEquals("department name", name1, dept1.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept1.getInstituteId());
   Assert.assertEquals("department code", code1, dept1.getCode());
   Assert.assertEquals("department description", desc1, dept1.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept1.getPhotoUrl());
   Assert.assertEquals("department email", email, dept1.getEmail());
   Assert.assertNotNull("department created date", dept1.getCreated());
   Assert.assertNotNull("department last modified date", dept1.getLastModified());

   /* create another department */
   Map<String, Object> params2 = buildOptionalParams(code2, desc2, photoUrl, email, null, null, null);
   Department dept2 = dol.createUpdateDepartment(name2, params2, vs);
   Assert.assertNotNull(dept2);
   
   Assert.assertEquals("department name", name2, dept2.getName());
   Assert.assertEquals("department institute Id", vs.getInstituteId(), dept2.getInstituteId());
   Assert.assertEquals("department code", code2, dept2.getCode());
   Assert.assertEquals("department description", desc2, dept2.getDescription());
   Assert.assertEquals("department photoUrl", photoUrl, dept2.getPhotoUrl());
   Assert.assertEquals("department email", email, dept2.getEmail());
   Assert.assertNotNull("department created date", dept2.getCreated());
   Assert.assertNotNull("department last modified date", dept2.getLastModified());

   /* fetch the departments now */
   List<Department> list = dol.getDepartments(0, 10, vs);
   Assert.assertNotNull(list);
   Assert.assertTrue("list should not be less than 2", list.size() >= 2);
  }

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

}