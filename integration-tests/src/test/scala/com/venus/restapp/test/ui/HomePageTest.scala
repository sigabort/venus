package com.venus.restapp.test.ui;

import org.scalatest.junit.JUnitRunner;
import org.junit.runner.RunWith;
import org.scalatest.FlatSpec;
import org.scalatest.matchers.ShouldMatchers;

import com.gargoylesoftware.htmlunit.html;

import com.venus.restapp.VenusRestClient;
import com.venus.restapp.test.AbstractTest;

import com.venus.restapp.ui.WebToolKit;

@RunWith(classOf[JUnitRunner])
class HomePageTest extends FlatSpec  with ShouldMatchers {
  val DEFAULT_HOME_PAGE_TITLE = "Home Page";
  
  val DEFAULT_CONTAINER_DIV_ID = "container";
  val DEFAULT_SUBCONTAINER_DIV_ID = "sub-container";
  val DEFAULT_HEADER_DIV_ID = "header";
  val DEFAULT_NAVIGATION_DIV_ID = "horiz-nav";
  val DEFAULT_FOOTER_DIV_ID = "footer";
  
  /* Div elements for the home page */
  val divs = List(DEFAULT_CONTAINER_DIV_ID, DEFAULT_SUBCONTAINER_DIV_ID, 
                  DEFAULT_HEADER_DIV_ID, DEFAULT_NAVIGATION_DIV_ID,
                  DEFAULT_FOOTER_DIV_ID);


  val DEFAULT_NAVIGATION_UL_ID = "horiz-nav-links";
  /* Total unordered lists in the home page */
  val uls = List(DEFAULT_NAVIGATION_UL_ID);
  
  val lis = List("Home", "Departments", "Programs", "Staff");
  
  /* Create one separate institute for these tests */
  val name = "HPUIT-" + AbstractTest.getRandomString();
  /* get home page */
  val url: String = new VenusRestJSONClient(name).getRequestUrl(null, null);
  
  "Home Page" should "contain title - 'Home Page'" in {
    /* Get the home page title */
    val wtk:WebToolKit = new WebToolKit(url);
    println("The url: " + url);
    assert(DEFAULT_HOME_PAGE_TITLE === wtk.getTitle);
  }

  "Home page" should "contain all div elements" in {
    val wtk = new WebToolKit(url);
    val list = wtk.getDescendentDivElements(DEFAULT_CONTAINER_DIV_ID);
    assert (list != null);
    assert (list.length >= (divs.length - 1), "Expected at least: " + (divs.length - 1) + " elements. Got: " + list.length);
    /* check each and every div now */
    divs.map(divId => assert(wtk.getDivElement(divId) != null));
  }

  "Home page" should "contain all UL elements" in {
    val wtk = new WebToolKit(url);
    val list = wtk.getDescendentULElements(DEFAULT_CONTAINER_DIV_ID, "div");
    assert (list != null);
    assert (list.length >= uls.length, "Expected at least: " + uls.length + " elements. Got: " + list.length);
    /* check each and every div now */
    uls.map(ulId => assert(wtk.getULElement(ulId) != null));
  }

  "Home page" should "contain all LI elements" in {
    val wtk = new WebToolKit(url);
    val list = wtk.getDescendentElements(DEFAULT_NAVIGATION_UL_ID, "ul", null, "li");
    assert (list != null);
    assert (list.length >= lis.length, "Expected at least: " + lis.length + " elements. Got: " + list.length);
  }

  
  "Home page" should "contain proper horizontal navigation bar" in {
    val wtk:WebToolKit = new WebToolKit(url);
    val navDiv = wtk.getDivElement(DEFAULT_NAVIGATION_DIV_ID);
    assert(navDiv != null);
    val ul = wtk.getULElement(DEFAULT_NAVIGATION_UL_ID);
    assert(ul != null);
  }
  
  "Home page" should "contain proper header div" in {
    val wtk:WebToolKit = new WebToolKit(url);
    val hdrDiv = wtk.getDivElement(DEFAULT_HEADER_DIV_ID);
    assert(hdrDiv != null);
  }
  
  "Home page" should "contain proper footer div" in {
    val wtk:WebToolKit = new WebToolKit(url);
    val footerDiv = wtk.getDivElement(DEFAULT_FOOTER_DIV_ID);
    assert(footerDiv != null);
  }
  
  "Home page" should "contain proper subcontainer div" in {
    val wtk:WebToolKit = new WebToolKit(url);
    val scDiv = wtk.getDivElement(DEFAULT_SUBCONTAINER_DIV_ID);
    assert(scDiv != null);
  }
  
  "Home page" should "get all the div elements" in {
    val wtk:WebToolKit = new WebToolKit(url);
    val divs = wtk.getDescendentDivElements(DEFAULT_CONTAINER_DIV_ID);
    assert(divs != null);
  }
  
}

