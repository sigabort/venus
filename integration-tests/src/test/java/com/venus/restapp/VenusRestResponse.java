package com.venus.restapp;

import org.apache.http.HttpResponse;
import org.apache.http.Header;

public class VenusRestResponse {
  private HttpResponse httpResponse;
  private String responseStr;
  private Integer responseCode;
  private Header[] headers;
  
  public VenusRestResponse(HttpResponse resp, String str, Integer code) {
    this.httpResponse = resp;
    this.responseStr = str;
    this.responseCode = code;
  }
  
  public void setHttpResponse(HttpResponse resp) {
    this.httpResponse = resp;
  }
  
  public HttpResponse getHttpResponse() {
    return this.httpResponse;
  }
  
  public void setResponseString(String str) {
    this.responseStr = str;
  }
  
  public String getResponseString() {
    return this.responseStr;
  }

  public void setResponseCode(Integer code) {
    this.responseCode = code;
  }
  
  public Integer getResponseCode() {
    return this.responseCode;
  }
  
  public void setHeaders(Header[] headers) {
    this.headers = headers;
  }
  
  public Header[] getHeaders() {
    return this.headers;
  }
  
}