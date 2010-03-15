package com.venus.controller.response.error;

import java.util.List;

import net.sf.oval.ConstraintViolation;

import com.venus.controller.response.BaseResponse;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonWriteNullProperties(value=false)
public class ResponseException extends Exception {
  private BaseResponse response;

  public ResponseException(Response.Status status, List<ConstraintViolation> violations, String msg) {
    response = new BaseResponse();
    response.setError(true);
    if (msg != null) {
      response.setErrorDescription(msg);
    }
    response.setHttpErrorCode(status.getStatusCode());
    response.setErrorDescription(status.toString());
    if (violations != null && violations.size() > 0) {
      for (ConstraintViolation cv : violations) {
	//do what?
      }
    }
  }
  
  public BaseResponse getResponse() {
    return this.response;
  }

  public void setResponse(BaseResponse response) {
    this.response = response;
  }

}
