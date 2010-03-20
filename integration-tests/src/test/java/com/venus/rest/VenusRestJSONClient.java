package com.venus.rest;

import java.util.Map;
import java.util.HashMap;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class VenusRestJSONClient extends VenusRestClient { 
  public static String JSON_EXT = ".json";
  private static final Logger log = Logger.getLogger(VenusRestJSONClient.class);
  
  /**
   * Create Department
   * @param name        The department's name
   * @param code        The department's code
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject createDepartment(String name, String code, Map<String, Object>params) {
    if (params == null) {
      params = new HashMap<String, Object>(2);
    }
    if (name != null) {
      params.put("name", name);
    }
    if (code != null) {
      params.put("code", code);
    }
    
    VenusRestResponse resp = postRequest("/departments/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get Department
   * @param name        The department's name
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getDepartment(String name, Map<String, Object>params) {
    if (params == null) {
      params = new HashMap<String, Object>(1);
    }
    
    VenusRestResponse resp = getRequest("/departments/" + name + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get all of the departments in an institute
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getDepartments(Map<String, Object>params) {
    VenusRestResponse resp = getRequest("/departments" + JSON_EXT, params);
    return getJSONObject(resp);
  }
  

  /* get the json object out of the response string */
  private JSONObject getJSONObject(VenusRestResponse resp) {
    if (resp != null) {
      String str = resp.getResponseString();
      if (str != null) {
	log.info("The response: " + str);
	return JSONObject.fromObject(str);
      }
    }
    return null;
  }

}