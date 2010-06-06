package com.venus.restapp.ui;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;

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
    array.foreach(x => list = x.asInstanceOf[HtmlDivision] :: list)
    list
  }

  /** return the UL element with the given ID */
  def getULElement(ulId: String): HtmlUnorderedList = page.getByXPath("//ul[@id='" + ulId + "']").get(0).asInstanceOf[HtmlUnorderedList];

  /** return the UL elements within the given div element */
  def getDescendentULElements(parent: String, parentType:String): List[HtmlUnorderedList] = {
    val array = page.getByXPath("//" + parentType + "[@id='" + parent + "']//ul").toArray;
    var list: List[HtmlUnorderedList] = Nil;
    array.foreach(x => list = x.asInstanceOf[HtmlUnorderedList] :: list)
    list
  }
  
  /** return the Html element with the given ID and type of element */
  def getElement(id: String, elementType:String): HtmlElement = page.getByXPath("//" + elementType +"[@id='" + id + "']").get(0).asInstanceOf[HtmlElement];

  /** return the Html elements within the given element */
  def getDescendentElements(parent: String, parentType:String, child: String, childType:String): List[HtmlElement] = {
    var query = "//" + parentType + "[@id='" + parent + "']//" + childType;
    if (child != null) 
      query += "[@id='" + child + "']";
    
    val array = page.getByXPath(query).toArray;
    var list: List[HtmlElement] = Nil;
    array.foreach(x => list = x.asInstanceOf[HtmlElement] :: list)
    list
  }
  
  
}