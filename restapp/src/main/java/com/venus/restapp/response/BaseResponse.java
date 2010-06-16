package com.venus.restapp.response;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import com.venus.restapp.response.dto.ErrorDTO;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;


/**
 * The response object which will be used as base for all
 * responses. This will contain the details about the response sent
 * to the client. The details include: error, error code, error description,
 * the items included in the response, total items, etc.
 * 
 * @author sigabort
 *
 */
/* Make sure we dont send the values which are null */
@JsonWriteNullProperties(false)
public class BaseResponse {
  private Boolean error;
  private Integer httpErrorCode;
  private ArrayList<ErrorDTO> errors;
  private Integer startIndex;
  private Integer itemsPerPage;
  private Integer totalResults;

  public BaseResponse() {
    this.error = false;
    this.httpErrorCode = HttpStatus.OK.value();
    this.itemsPerPage = 0;
    this.totalResults = 0;
  }

  public Boolean getError() {
    return error;
  }

  public void setError(Boolean error) {
    this.error = error;
  }

  public Integer getHttpErrorCode() {
    return httpErrorCode;
  }

  public void setHttpErrorCode(Integer httpErrorCode) {
    this.httpErrorCode = httpErrorCode;
  }

  public Integer getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  public Integer getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(Integer itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  public Integer getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }
  
  public ArrayList<ErrorDTO> getErrors() {
    return errors;
  }

  public void setErrors(ArrayList<ErrorDTO> errors) {
    this.errors = errors;
  }


}