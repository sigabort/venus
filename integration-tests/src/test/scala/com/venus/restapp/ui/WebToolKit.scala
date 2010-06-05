package com.venus.restapp.ui;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import scala.collection.jcl.ArrayList;

class WebToolKit(url:String) extends WebClient {
  val client:WebClient = new WebClient();
  val page:HtmlPage = client.getPage(url);

  /* return the page title */
  def getTitle:String = page.getTitleText();
  
  /** return the div element with the given ID */
  def getDivElement(divId: String): HtmlDivision = page.getByXPath("//div[@id='" + divId + "']").get(0).asInstanceOf[HtmlDivision];

  /** return the div elements within the given div element */
  def getDescendentDivElements(element: String): List[HtmlDivision] = {
    val array = page.getByXPath("//div[@id='" + element + "']//div").toArray;
    var list: List[HtmlDivision] = Nil;
    for (x <- array) {
      list = x.asInstanceOf[HtmlDivision] :: list
    }
    list
  }
    
}