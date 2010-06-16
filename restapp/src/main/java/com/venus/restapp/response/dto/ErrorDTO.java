package com.venus.restapp.response.dto;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.venus.model.BaseModel;

/**
 * This class represents the Errors sent to the response
 * @author sigabort
 *
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public class ErrorDTO implements BaseDTO {
  
  private String field;
  private String errorCode;
  private String message;
  
  public ErrorDTO(String field, String errorCode, String message) {
    this.field = field;
    this.errorCode = errorCode;
    this.message = message;
  }
  
  public BaseDTO getDTO(BaseModel object) {
    return null;
  }
  
}
 
