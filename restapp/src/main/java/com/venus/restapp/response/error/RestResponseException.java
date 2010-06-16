package com.venus.restapp.response.error;

import org.springframework.http.HttpStatus;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * The Exception containing details which will be returned to the client
 * This is thrown when there is any exception in the request processing.
 * The details of the exception will be converted to the BaseResponse object
 * and that is sent to the client when any exception happens
 */
/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class RestResponseException extends Exception {
  private String message;
  private String field;
  private HttpStatus errorCode;

  public RestResponseException(String field, HttpStatus errorCode, String msg) {
    this.field = field;
    this.message = msg;
    this.errorCode = errorCode;
  }
  
  public HttpStatus getErrorCode() {
    return this.errorCode;
  }
  
  public String getField() {
    return this.field;
  }  
  
  public String getMessage() {
    return this.message;
  }
}
