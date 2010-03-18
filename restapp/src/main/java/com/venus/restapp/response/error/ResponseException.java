package com.venus.restapp.response.error;

import java.util.List;

import com.venus.controller.response.BaseResponse;

import org.springframework.http.HttpStatus;

public class ResponseException extends Exception {
  private BaseResponse response;

  public ResponseException(HttpStatus status, String msg) {
    response = new BaseResponse();
    response.setError(true);
    if (msg != null) {
      response.setErrorDescription(msg);
    }
    response.setHttpErrorCode(status.value());
    response.setErrorDescription(status.toString());
  }
  
  public BaseResponse getResponse() {
    return this.response;
  }

  public void setResponse(BaseResponse response) {
    this.response = response;
  }

}
