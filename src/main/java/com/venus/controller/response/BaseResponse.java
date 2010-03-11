package com.venus.controller.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.venus.controller.error.HttpStatusCode;

@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonWriteNullProperties(value=false)
public class BaseResponse {
  private Boolean error;
  private Integer errorCode;
  private Integer httpErrorCode;
  private String errorDescription;

  public BaseResponse() {
    this.error = false;
    this.errorCode = 0;
    this.httpErrorCode = HttpStatusCode.OK.getBit();
  }

  public Boolean getError() {
    return error;
  }

  public void setError(Boolean error) {
    this.error = error;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

  public Integer getHttpErrorCode() {
    return httpErrorCode;
  }

  public void setHttpErrorCode(Integer httpErrorCode) {
    this.httpErrorCode = httpErrorCode;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

}