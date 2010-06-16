package com.venus.restapp.controller;


import org.scalatest.junit.JUnitSuite;

import org.scalatest.junit.ShouldMatchersForJUnit;

import scala.collection.jcl.HashMap;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.sun.net.ssl.internal.www.protocol.https.Handler;
import com.venus.util.VenusSession;

import org.apache.commons.lang.StringUtils;

import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.HttpMethod;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.test.web.ModelAndViewAssert._;
import com.venus.restapp.response.BaseResponse;

/**
 * Helper class for Scala Controller tests.This mostly uses the base class
 * for the controller tests : AbstractControllerTest
 * @author sigabort
 *
 */
class ScalaHelperTest extends JUnitSuite with ShouldMatchersForJUnit {

  def handlerAdapter: HandlerAdapter = AbstractControllerTest.handlerAdapter;
  def appContext: ClassPathXmlApplicationContext = AbstractControllerTest.appContext;
  def fakeAdmin:Unit = AbstractControllerTest.fakeAdmin;
  
  /**
   * Generate 20-char random string
   */
  def getRandomString:String =  AbstractControllerTest.getRandomString; 
  def getRandomString(count:Int):String =  AbstractControllerTest.getRandomString(count); 

  /**
   * Generate random number
   */
  def getRandomNumber(): Int = AbstractControllerTest.getRandomNumber;

  @Test def dummyTest() {}

  @Before def createSetup() {
   new AbstractControllerTest().setUp();
  }
  
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

  
  /******* Helper methods ***********/
  
  def setReqMethodAndURI(request:MockHttpServletRequest, method: HttpMethod, action: String) = {
    request.setMethod(method.toString());
    request.setRequestURI(action);
  }
  
  def setReqParams(req: MockHttpServletRequest, params: (String, String)*) {
    params.foreach(s => req.setParameter(s._1, s._2));
  }
  

  /**
   * Check for the error in the ModelAndView object.
   * @return
   */
  def checkForError(mav: ModelAndView, view: String, errorCode: Int) {
    assertViewName(mav, view);
    val resp: BaseResponse = assertAndReturnModelAttributeOfType(mav, "response", classOf[BaseResponse]);
    Assert.assertNotNull("Didn't get the response", resp);
    Assert.assertTrue("The error", resp.getError.toString.toBoolean);
    val errors = resp.getErrors();
    Assert.assertNotNull("errors", errors);
    errors.size should (be > 0);
    resp.getHttpErrorCode should be (errorCode);
  }
  
}