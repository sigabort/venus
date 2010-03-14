package com.venus.rest;

import org.wiztools.restclient.Request;
import org.wiztools.restclient.Response;
import org.wiztools.restclient.RequestBean;
import org.wiztools.restclient.HTTPMethod;
import org.wiztools.restclient.Implementation;
import org.wiztools.restclient.RequestExecuter;

import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Util class to call REST APIs.
 */
public class VenusRestClient {

  private String prefixUrl;
  private RequestBean requestBean = new RequestBean();
  public static String DEFAULT_LOC = "http://localhost:8080/venus/edu/";
  private static final Logger log = Logger.getLogger(VenusRestClient.class);

  public VenusRestClient(String prefixUrl) {
    this.prefixUrl = prefixUrl;
  }

  public VenusRestClient() {
    this.prefixUrl = DEFAULT_LOC;
  }

  public Response getRequest(String url, Map params) throws Exception {
    url = buildUrl(this.prefixUrl, url);
    requestBean.setUrl(new java.net.URL(url));
    requestBean.setMethod(HTTPMethod.GET);
    VenusRestClientView view = VenusRestClientView.getView();
    RequestExecuter executer = Implementation.of(RequestExecuter.class);
    executer.execute(requestBean, view);
    return view.getResponse();
  }
  
  private static String buildUrl(String prefixUrl, String path) {
    String url = null;
    if (prefixUrl != null) {
      if (path != null && !"".equals(path)) {
	if (prefixUrl.endsWith("/") && path.startsWith("/")) {
	  prefixUrl = prefixUrl.substring(0, prefixUrl.length() - 1);
	}
	url = prefixUrl + path;
      }
      else {
	url = prefixUrl;
      }
    }
    log.info("buildUrl: " + url);
    return url;
  }

}