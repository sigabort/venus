package com.venus.restapp.response.error;

import java.util.List;
import java.util.Map;

import com.venus.restapp.response.BaseResponse;

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
public class ResponseException extends Exception {
  private BaseResponse response = null;
  private String message = null;

  /**
   * Constructor. This populates the proper BaseResponse object depends on the
   * parameters. When the exception happens, the BaseResponse Object corresponding
   * to this exception will be returned to the client.
   * @param status     HttpStatus object specifying the status code
   * @param msg       The error description
   * @param e         The base exception
   * @param options   The optional parameters to include in the response exception
   */
  public ResponseException(HttpStatus status, String msg, Exception e, Map<String, Object> options) {
    response = new BaseResponse();
    response.setError(true);
    this.message = msg;
    if (msg != null) {
      response.setErrorDescription(msg);
    }
    response.setHttpErrorCode(status.value());
  }
  
  public BaseResponse getResponse() {
    return this.response;
  }

  public void setResponse(BaseResponse response) {
    this.response = response;
  }
  
  public String getMessage() {
    return this.message;
  }

}
