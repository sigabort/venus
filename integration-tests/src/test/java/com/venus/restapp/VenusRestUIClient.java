package com.venus.restapp;

import java.util.Map;
import java.util.HashMap;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import org.apache.log4j.Logger;

public class VenusRestUIClient extends VenusRestClient {
  private static final Logger log = Logger.getLogger(VenusRestUIClient.class);
  private static String institute = null;
  private static String REST_REQUEST_CODE_ATTR = "__REST_REQUEST_CODE_ATTR__";  

  public VenusRestUIClient() {
    //empty constructor
  }

  private Map<String, Object> initParams(Map<String, Object> params) {
    if (params == null) {
      params = new HashMap<String, Object>(1);
    }
    if (this.institute != null) {
      params.put(REST_REQUEST_CODE_ATTR, this.institute);
    }
    return params;
  }
  
  public VenusRestUIClient(String institute) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", institute);
    VenusRestResponse resp = this.createParentInstitute(institute, params);
    Assert.assertEquals("Creating parent institute", (Integer)HttpStatus.SC_OK, (Integer)resp.getResponseCode());
    this.institute = institute;
  }

  /**
   * Create a parent institute
   * @param name       The institute name
   * @param params     Optional parameters to include
   * @return           The JSON Object containing the result
   */
  public VenusRestResponse createParentInstitute(String name, Map<String, Object> params) {
    params = initParams(params);
    if (name != null) {
      params.put("name", name);
    }
    return postRequest("/restricted/institutes/create", params);
  }
  
  public VenusRestResponse getHomePage(Map<String, Object> params) {
    return getRequest(null, params);
  }
  
}