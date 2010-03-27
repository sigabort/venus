package com.venus.restapp;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;

/**
 * Util class to call REST APIs.
 * This provides Calls to call all REST APIs supported by Venus REST Web App.
 * 
 * The APIs provided in this Client will look like:
 *     <code>public HttpResponse getUser(String name, Map params);</code>
 * In the above API:
 *      'get' specifies the HTTP Method : 'GET'
 *      'User' specifies the object on which the call should be made
 *      'name', the mandatory parameter to the REST API
 *      'params', the optional parameters of type: Map&ltString, Object&gt
 *      'returns' the response of the REST call
 *      The above API will result into : GET {baseUrl}/user/{name}?param1=value1&amp HTTP/1.1
 * 
 */
public class VenusRestClient {
  /** Path for login */
  public static String LOGIN_CHECK_PATH = "/j_spring_security_check";
  /** Path for logout */
  public static String LOGOUT_CHECK_PATH = "/j_spring_security_logout";
  /** name of the parameter for username */
  public static String USER_INPUT_FIELD_NAME = "j_username";
  /** name of the parameter for password */
  public static String USER_PASSWD_FIELD_NAME = "j_password";

  /** Default host of the application server */
  public static String DEFAULT_HOST = "localhost";
  /** Default port of the application server */
  public static int DEFAULT_PORT = 8080;
  /** Default path for the REST APIs */
  public static String DEFAULT_PATH = "/restapp";
  /** Default scheme for the REST APIs */
  public static String DEFAULT_SCHEME = HttpHost.DEFAULT_SCHEME_NAME;

  private HttpClient client = null;
  private HttpHost host = null;
  private HttpContext context = null;
  private String basePath = DEFAULT_PATH;
  private static final Logger log = Logger.getLogger(VenusRestClient.class);

  /**
   * Construtor
   * @param host      The host of the app server where REST APIs are available
   * @param port      The port of the app server
   */
  public VenusRestClient(String hostName, int port, String path) {
    if (hostName != null) {
      this.host = new HttpHost(hostName, port, DEFAULT_SCHEME);
    }
    this.basePath = path;
    this.client = new DefaultHttpClient();
    this.context = new SyncBasicHttpContext(new BasicHttpContext());
  }

  /**
   * Construtor
   * @param host   The host object representing server for the REST APIs
   */
  public VenusRestClient(HttpHost host) {
    this.host = host;
    this.client = new DefaultHttpClient();
    this.context = new SyncBasicHttpContext(new BasicHttpContext());
  }

  /**
   * Construtor with no args
   */
  public VenusRestClient() {
    this.host = new HttpHost(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_SCHEME);
    this.client = new DefaultHttpClient();
    this.context = new SyncBasicHttpContext(new BasicHttpContext());
  }

  /**
   * Send the GET request with specified path and params
   * @param path     The path for the GET request
   * @param params   The map of the query parameters
   * @return The response of the GET request
   */
  public VenusRestResponse getRequest(String path, Map params) {
    String url = buildUrl(this.basePath, path, params);
    if (url == null) {
      log.error("Can't build the URL with path: " + path);
      return null;
    }
    return executeRequest(url, HttpGet.METHOD_NAME);
  }

  /**
   * Send the POST request with specified path and params
   * @param path     The path for the POST request
   * @param params   The map of the query parameters
   * @return The response of the POST request
   */
  public VenusRestResponse postRequest(String path, Map params) {
    String url = buildUrl(this.basePath, path, params);
    if (url == null) {
      log.error("Can't build the URL with path: " + path);
      return null;
    }
    return executeRequest(url, HttpPost.METHOD_NAME);
  }

  /**
   * Execute the request
   * @param url      The url for sending the request
   * @param method   The request method
   * @return The response of request
   */
  private VenusRestResponse executeRequest(String url, String method) {
    HttpRequest request = new BasicHttpRequest(method, url);
    RequestLine rl = request.getRequestLine();
    String requestStr = rl.getMethod() + " " + rl.getUri() + " " + rl.getProtocolVersion();
    log.info("Request: " + requestStr);
    HttpResponse resp = null;
    try {
      resp = client.execute(this.host, request, context);
    }
    catch (Exception e) {
      log.error("Error while executing the request: " + requestStr);
      return null;
    }
    /* get the actual response entity */
    HttpEntity entity = resp.getEntity();
    String respStr = null;
    if (entity != null) {
      ByteArrayOutputStream out = null;
      try {
        out = new ByteArrayOutputStream();
        entity.writeTo(out);
      }
      catch (Exception e) {
        log.error("Error writing the response to output: " + requestStr);
        return null;
      }
      /* get the response string */
      respStr = new String(out.toByteArray());
    }
    /* Build custom response to make sure clients get
     * access to both response object and string */
    VenusRestResponse vrr = new VenusRestResponse(resp, respStr, resp.getStatusLine().getStatusCode());
    vrr.setHeaders(resp.getAllHeaders());
    log.info("I got the response code: " + resp.getStatusLine());
    return vrr;
  }

  /**
   * Login with the given username/passwd
   * @param username     The username to be logged in
   * @param passwd       The passwd used for the login
   * @return The response of the request
   */
  public VenusRestResponse login(String username, String passwd) {
    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put(USER_INPUT_FIELD_NAME, username);
    params.put(USER_PASSWD_FIELD_NAME, passwd);      
    return postRequest(LOGIN_CHECK_PATH, params);
  }

  /**
   * Logout - to clear the context
   * @return The response of the request
   */
  public VenusRestResponse logout() {
    return postRequest(LOGOUT_CHECK_PATH, null);
  }


  /**
   * Build URL, given the path and parameters
   * @param url      The base url for which we need to append the path/params
   * @param path     The optional path parameter to be appended to the existing path
   * @param parmas   The query parameters to be appended to the URL
   * @param appendPath   Whether to append the path to the existing path or replace it
   * @return         the new url string with new path(if any), and new parameters (if any).
   */
  public static String buildUrl(String url, String path, Map<String, Object> params) {
    if (url == null || "".equals(url)) {
      url = path;
    }
    else {
      /* if we need to append the path, append it */
      if (path != null) {
	/* Set the path properly. Normally the path should start with "/", but check anyway */
	if (url.endsWith("/")) {
	  url = url.substring(0, url.length() - 1);
	}
	if (!path.startsWith("/")) {
	  path = "/" + path;
	}
	/* path should start with "/" */
	url = url + path;
      }
      String queryString = "";

      /* ok, set the query parameters now */
      if (params != null) {
	String[] names = new String[params.size()];
	String[] values = new String[params.size()];
	int idx = 0;

	for (Object param : params.keySet()) {
	  Object value = params.get((String)param);
	  if (value instanceof String) {
	    names[idx] = (String) param;
	    values[idx++] = (String) value;
	    if (log.isDebugEnabled()) {
	      log.debug("Adding param: " + (String)param + ", value: " + (String) value + " to names list");
	    }
	  } //if value instanceof
	  /* if the value is not a string, 
	   * it should be List of values to be set to the same parameter */
	  else {
	    Object[] values1 = ((ArrayList)value).toArray();
	    String[] names1 = new String[values1.length];
	    Arrays.fill(names1, param);
	    /* ok, copy both arrays to a new big array */
	    int totalLen = names1.length + idx;
	    /* the total array length should be : 
	     * the original names (till now) + this list length - 1 
	     * ( -1 because we are replacing one object by multiple values)  */
	    String[] totalNamesArray = new String[totalLen + (names.length - idx) - 1];
	    
	    System.arraycopy(names, 0, totalNamesArray, 0, idx);
	    System.arraycopy(names1, 0, totalNamesArray, idx, totalLen - idx);
	    
	    String[] totalValuesArray = new String[totalLen + (values.length - idx) - 1];
	    System.arraycopy(values, 0, totalValuesArray, 0, idx);
	    System.arraycopy(values1, 0, totalValuesArray, idx, totalLen - idx);
	    
	    if (log.isDebugEnabled()) {
	      log.debug("Copying the values from the object, for param:" + (String) param);
	    }
	    
	    /* now, set the new arrays back to the original arrays */
	    names = totalNamesArray;
	    values = totalValuesArray;
	    /* set the index */
	    idx = totalLen;
	  } //else
	} //for object param..
	queryString = buildQueryString(names, values);
	url += queryString;
      } // if params != null
      log.info("The url: " + url);
      return url;
    }
    return null;
  }

  public static String buildQueryString(String[] names, String[] values) {
    if (names == null || names.length == 0 || values == null || values.length == 0) {
      return "";
    }
    String query = "";
    for (int idx = 0; idx < names.length; idx++) {
      query += ((idx > 0)? "&":"?") + (names[idx] + "=" + values[idx]);
    }
    return query;
  }
}