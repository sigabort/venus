package com.venus.dal.impl;

import org.scalatest.junit.JUnitSuite;
import org.scalatest.junit.ShouldMatchersForJUnit;

import scala.collection.mutable.ListBuffer;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.venus.model.BaseModel;
import com.venus.model.Institute;
import com.venus.model.Department;
import com.venus.model.impl.InstituteImpl;
import com.venus.model.Status;
import com.venus.util.VenusSession;
import com.venus.dal.DataAccessException;

import com.venus.model.impl.AbstractImplTest;
import com.venus.model.impl.InstituteImplTest;
import scala.collection.jcl.HashMap;

import scala.collection.mutable.Map;

import java.util.{Date};

class AbstractDalImplTest extends AbstractImplTest {
  val aol = new AbstractDalImpl();
  var sess: Session = _;
  var vs: VenusSession = _;
  val columns = Array[String]("code", "displayName", "description" , "photoUrl", "email", "name", "status", "created", "lastModified");
  
  @Before def setUp() {
    vs = getVenusSession();
    sess = vs.getHibernateSession();
    val name = "AbstractOpsImplTest-" + getRandomString();
    val institute: Institute = InstituteImplTest.createTestInstitute(name, vs);
    sess.beginTransaction();
    sess.save(institute);
    vs.setInstitute(institute);
  }

  @Test def testCreateInstitute() {
    val name = "AOIT_CreateInst-" + getRandomString();
    val map:HashMap[String, Object] = buildOptionalParams(name);
    val niMap = new HashMap[String, Object];
    niMap += "name" -> name;
    
    val inst = aol.createUpdate("com.venus.model.impl.InstituteImpl", niMap.underlying, null, map.underlying, sess);
    Assert.assertNotNull("Institute", inst);

    /* verify the NI params */
    verifyParamsMap(inst, niMap);
    /* verify other params */
    verifyParamsMap(inst, map);
  }
    
  def buildOptionalParams(name: String) = {
    val list = List("code", "displayName", "description" , "photoUrl", "email");
    val map = new HashMap[String, Object];
    list.foreach(s => map += s -> (name + "-" + s));
    map += "status" -> Status.Active.asInstanceOf[Object];
    map += "created" -> new Date();
    map += "lastModified" -> new Date();
    map
  }
  
}
