package com.venus.model.impl;

import org.scalatest.junit.JUnitSuite;
import org.scalatest.junit.ShouldMatchersForJUnit;

import scala.collection.jcl.HashMap;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.venus.util.VenusSession;

import org.apache.commons.lang.StringUtils;

class AbstractImplTest extends JUnitSuite with ShouldMatchersForJUnit {

  /**
   * Generate 20-char random string
   */
  def getRandomString():String =  BaseImplTest.getRandomString(); 

  /**
   * Generate random number
   */
  def getRandomNumber(): Int = BaseImplTest.getRandomNumber();

  /**
   * Get the venus session
   */
  def getVenusSession(): VenusSession = new BaseImplTest().getVenusSession();

  @Test def dummyTest() {}
 
  /**
   * Verify the object with the given map of key/value pairs
   * The map contains the object's field name and value which should present.
   * 
   * @return nothing if the object contains proper values. Throws assertion
   * exception, otherwise.
   */
  def verifyParamsMap(obj: Object, map: HashMap[String, Object]) = {
    map.foreach(s => {
      for (method <- obj.getClass.getMethods) {
        val name = getParamName(method.getName);
        if (name != null) {
          if (StringUtils.equalsIgnoreCase(s._1, name)) {
            Assert.assertEquals(name + " is not proper", s._2, method.invoke(obj, null));
          }
        }
      }
    });
  }

  /**
   * Verify the object with the given array of key/value pairs
   * The map contains the object's field name and value which should present.
   * 
   * @return nothing if the object contains proper values. Throws assertion
   * exception, otherwise.
   */
  def verifyParamsArray(obj: Object, arr: Array[(String, Object)]) = {
    /* convert to map, and call verifyParamsMap */
    val map: HashMap[String, Object] = new HashMap[String, Object];
    arr.foreach(s => map += s._1 -> s._2);
    verifyParamsMap(obj, map);
  }
  
  def getParamName(name: String): String = {
    var param: String = null;
    if (name.startsWith("get")) {
      param = StringUtils.substring(name, 3);
    }
    param;
  }

}