package com.venus.rest.test;

import com.venus.rest.VenusRestClient;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

public class SampleTest {

  /* test getting default page - with out any parameters */
  @Test
  public void testGetDefaultPage() throws Exception {
    VenusRestClient client = new VenusRestClient();
    HttpResponse resp = client.getRequest(null, null);
    Assert.assertEquals("Response code", 200, resp.getStatusLine().getStatusCode());
  }

  /**** Test the code in VenusRestClient  *****/

  /** Test buildUrl() with passing new Path for appending */
  @Test
  public void testBuildUrl() throws Exception {
    String url = "/restapp";
    /* Append the path and check */
    String newPath = "/newPath";
    String newUrl = VenusRestClient.buildUrl(url, newPath, null);
    Assert.assertNotNull("New URL", newUrl);
    Assert.assertEquals("New URL is not proper", newUrl, url + newPath);
    
    /* dont pass any path and check */
    newUrl = VenusRestClient.buildUrl(url, null, null);
    Assert.assertNotNull("New URL", newUrl);
    Assert.assertEquals("New URL is not proper", newUrl, url);
    
    /* dont pass any path and check */
    newPath = "newPath";
    newUrl = VenusRestClient.buildUrl(url, newPath, null);
    Assert.assertNotNull("New URL", newUrl);
    Assert.assertEquals("New URL is not proper", newUrl, url + "/" + newPath);
  }

  /** Create URL, and check passing parameters */
  @Test
  public void testBuildUrlWithSimpleQueryParams() throws Exception {
    String url = "/restapp";

    /* Now, pass the parameters */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("test1", "value1");
    params.put("test2", "value2");
    /* pass only 2 parameters with corresponding values */
    String newUrl = VenusRestClient.buildUrl(url, null, params);
    Assert.assertNotNull("URL build failed", newUrl);
    Assert.assertTrue("URL build failed", newUrl.startsWith(url));

    /* check the query string */
    Assert.assertTrue("Query String is not proper", newUrl.contains("test1=value1"));
    Assert.assertTrue("Query String is not proper", newUrl.contains("test2=value2"));
  }


  /** Create URL, and check passing multiple values for same parameter */
  @Test
  public void testBuildUrlWithMultiValuedQueryParam() throws Exception {
    String url = "/restapp";

    /* Append the path and check */
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("test1", "value1");
    params.put("test2", "value2");
    List list = new ArrayList<String>(3);
    list.add("value3");
    list.add("value4");
    list.add("value5");
    params.put("multiParam", list);
    params.put("test6", "value6");
    params.put("test7", "value3");
    List list1 = new ArrayList<String>(4);
    list1.add("value3");
    list1.add("value4");
    list1.add("value5");
    list1.add("value6");
    params.put("multiParam1", list1);

    /* set the parameters now */
    String newPath = "newPath/nextPath";
    String newUrl = VenusRestClient.buildUrl(url, newPath, params);
    Assert.assertNotNull("URL build failed", url);
    Assert.assertTrue("URL build failed", newUrl.startsWith(url + "/" + newPath));


    /* check the query string */
    /* we can't guarantee the order of the query parameters, so we need to check each and every parameter */
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("test1=value1"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("test2=value2"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam=value3"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam=value4"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam=value5"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("test6=value6"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("test7=value3"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam1=value3"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam1=value4"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam1=value5"));
    Assert.assertTrue("NewUrl String is not proper", newUrl.contains("multiParam1=value6"));
  }

  

}
