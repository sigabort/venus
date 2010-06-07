package com.venus.restapp;

import java.util.Map;
import java.util.HashMap;

import junit.framework.Assert;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import org.apache.log4j.Logger;

public class VenusRestJSONClient extends VenusRestClient {
  public static String JSON_EXT = ".json";
  private static final Logger log = Logger.getLogger(VenusRestJSONClient.class);
  private static String institute = null;
  private static String REST_REQUEST_CODE_ATTR = "__REST_REQUEST_CODE_ATTR__";  

  public VenusRestJSONClient() {
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
  
  public VenusRestJSONClient(String institute) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("code", institute);
    JSONObject resp = this.createParentInstitute(institute, params);
    Assert.assertFalse("Creating parent institute", resp.getBoolean("error"));
    this.institute = institute;
  }
  
  /**
   * Get the Request URL for the current request.
   * @param path   The path to append to the base path
   * @param params The optional parameters to include in the URL
   * @return The URL with hostname, port, base&appended path, params included
   */
  @Override
  public String getRequestUrl(String path, Map params) {
    params = initParams(params);
    return super.getRequestUrl(path, params);
  }
  
  /**
   * Create an Admin User with specified user name
   *
   * @param username   The username for the admin
   * @param params     Optional parameters to include
   * @return           The JSON Object containing the result
   */
  public JSONObject createAdminUser(String username, Map<String, Object> params) {
    params = initParams(params);
    if (username != null) {
      params.put("username", username);
    }
    VenusRestResponse resp = postRequest("/restricted/users/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Create a parent institute
   * @param name       The institute name
   * @param params     Optional parameters to include
   * @return           The JSON Object containing the result
   */
  public JSONObject createParentInstitute(String name, Map<String, Object> params) {
    params = initParams(params);
    if (name != null) {
      params.put("name", name);
    }
    VenusRestResponse resp = postRequest("/restricted/institutes/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Create Department
   * @param name        The department's name
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject createDepartment(String name, Map<String, Object>params) {
    params = initParams(params);
    if (name != null) {
      params.put("name", name);
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
    params = initParams(params);
    
    VenusRestResponse resp = getRequest("/departments/" + name + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get all of the departments in an institute
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getDepartments(Map<String, Object>params) {
    params = initParams(params);
    VenusRestResponse resp = getRequest("/departments" + JSON_EXT, params);
    return getJSONObject(resp);
  }
  

  /**
   * Create Institute
   * @param name        The institute's name
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject createInstitute(String name, Map<String, Object>params) {
    params = initParams(params);
    if (name != null) {
      params.put("name", name);
    }

    VenusRestResponse resp = postRequest("/institutes/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get Institute
   * @param name        The institute's name
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getInstitute(String name, Map<String, Object>params) {
    params = initParams(params);
    
    VenusRestResponse resp = getRequest("/institutes/" + name + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get all of the institutes in an institute
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getInstitutes(Map<String, Object>params) {
    params = initParams(params);
    VenusRestResponse resp = getRequest("/institutes" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  
  /**
   * Create User
   * @param username        The user's username
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject createUser(String username, Map<String, Object>params) {
    params = initParams(params);
    if (username != null) {
      params.put("username", username);
    }
    VenusRestResponse resp = postRequest("/users/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }
  
  /**
   * Get User
   * @param name        The user's username
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getUser(String username, Map<String, Object>params) {    
    params = initParams(params);
    VenusRestResponse resp = getRequest("/users/" + username + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Create/Add User Roles
   * @param username        The user's username for whom the roles need to be added/created
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject createUserRoles(String username, Map<String, Object>params) {
    params = initParams(params);
    if (username != null) {
      params.put("username", username);
    }
    
    VenusRestResponse resp = postRequest("/userroles/create" + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Get User roles
   * @param name        The user's username
   * @param params      Query parameters to be set
   * @return The response of the request
   */
  public JSONObject getUserRoles(String username, Map<String, Object>params) {    
    params = initParams(params);
    VenusRestResponse resp = getRequest("/userroles/" + username + JSON_EXT, params);
    return getJSONObject(resp);
  }

  /**
   * Logout. Right now, our application redirects to /index when the logout
   * is successful.
   * @return
   */
  public JSONObject logout() {
    VenusRestResponse resp = super.logoutRequest();
    if (resp == null) {
      return logoutFailJSONObject();
    }
    if (validateLocationHeader(resp, "/restapp/login")) {
      return logoutFailJSONObject();
    }
    return logoutSuccessJSONObject();
  }

  
  /**
   * Login using given username and password. This is hack...
   * Once we use VenusRestClient to login, the restapp sends a redirect to
   * the /index right now. In case of login failure, it redirects to 
   * \/restapp\/login again. So, checking the header 'Location' and find
   * if the login succeeded or not. If it succeeded, send JSON reponse with
   * error as false. If not, send JSON response with error as true.
   * @param username
   * @param passwd
   * @param params
   * @return
   */
  public JSONObject login(String username, String passwd, Map<String, Object> params) {
    params = initParams(params);
    log.info("logging in as user: " + username + ", passwd: " + passwd);
    VenusRestResponse resp = super.loginRequest(username, passwd, params);
    if (resp == null) {
      return loginFailJSONObject();
    }
    if (validateLocationHeader(resp, "/restapp/login")) {
      return loginFailJSONObject();
    }
    return loginSuccessJSONObject();
  }

  private boolean validateLocationHeader(VenusRestResponse resp, String toCheck) {
    Header[] headers = resp.getHeaders();
    for (int idx = 0; idx < headers.length; idx++) {
      Header header = headers[idx];
      if ("Location".equals(header.getName())) {
        String value = header.getValue();
        /* the location header contains the given string, return true */
        log.info("[validationLocationHeader] I got the Location header value: " + value);
        if (value.contains(toCheck)) {
          return true;
        }
        /* the location header doesn't contain the given string
         * return false
         */
        else {
          return false;
        }
      }
    }
    /* checked all headers, couldn't find any location header. 
     * retrun false
     */
    return false;
  }
  
  private JSONObject loginFailJSONObject() {
    String str ="{\"error\":true,\"httpErrorCode\":" + HttpStatus.SC_UNAUTHORIZED + "}";
    System.out.println("Sending login failure response: " + str);
    return JSONObject.fromObject(str);
  }
  
  private JSONObject loginSuccessJSONObject() {
    String str ="{\"error\":false,\"httpErrorCode\":" + HttpStatus.SC_OK + "}";
    System.out.println("Sending login success response: " + str);
    return JSONObject.fromObject(str);
  }

  private JSONObject unAuthorizedJSONObject() {
    String str ="{\"error\":true,\"httpErrorCode\":" + HttpStatus.SC_UNAUTHORIZED + "}";
    System.out.println("Sending access denied response: " + str);
    return JSONObject.fromObject(str);
  }

  private JSONObject logoutFailJSONObject() {
    String str ="{\"error\":true,\"httpErrorCode\":" + HttpStatus.SC_UNAUTHORIZED + "}";
    System.out.println("Sending logout failure response: " + str);
    return JSONObject.fromObject(str);
  }

  private JSONObject logoutSuccessJSONObject() {
    String str ="{\"error\":false,\"httpErrorCode\":" + HttpStatus.SC_OK + "}";
    System.out.println("Sending logout success response: " + str);
    return JSONObject.fromObject(str);
  }
  
  public JSONObject buildJSONObjectWithStatus(Integer errorCode) {
    String str ="{\"error\":true,\"httpErrorCode\":" + errorCode + "}";
    System.out.println("Sending custom response: " + str);
    return JSONObject.fromObject(str);    
  }
  
  private boolean validateStatus(VenusRestResponse resp) {
    Integer status = resp.getResponseCode();
    switch(status) {
    /* 200 - is good */
    case HttpStatus.SC_OK:
    /* we check the Location header where it is moved to. 
     * So, we can send true for this too */
    case HttpStatus.SC_MOVED_TEMPORARILY:
      return true;
    
    /* for all other status code, return false */
    default:
      return false;      
    }  
  }
  
  /* get the json object out of the response string */
  private JSONObject getJSONObject(VenusRestResponse resp) {
    if (resp != null) {
      /*
       * See if this action lead to the login page. If so, this action
       * is not authorized. Throw AccessDenied response
       */
      if (validateLocationHeader(resp, "/restapp/login")) {
        return unAuthorizedJSONObject();
      }
      /*
       * See if the response code is proper or not. If not, build error JSONObject
       * with the corresponding response code
       */
      if (!validateStatus(resp)) {
        return buildJSONObjectWithStatus(resp.getResponseCode());
      }
      String str = resp.getResponseString();
      if (str != null) {
        log.info("The response: " + str);
        JSONObject json = JSONObject.fromObject(str);
        /* just send the contents of the response */
        if (json != null) {
          return json.getJSONObject("response");
        }
      }
    }
    return null;
  }

}