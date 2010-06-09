package com.venus.model.impl;

import org.scalatest.junit.JUnitSuite;
import org.scalatest.junit.ShouldMatchersForJUnit;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import com.venus.util.VenusSession;

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
  
}