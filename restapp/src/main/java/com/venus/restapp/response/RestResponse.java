package com.venus.restapp.response;

import java.util.ArrayList;

import com.venus.restapp.response.dto.BaseDTO;

/**
 * The default response object for any request with some action
 * This contains all the details from base {@link BaseResponse response} and details
 * specific to object(s). If the response contains multiple objects, the response
 * object will contain 'entries' attribute with multiple object objects in it.
 * If the response contains single object, 'entry' attribute with single object details
 * will be present in the response object.
 *  
 * @author sigabort
 *
 */
public class RestResponse extends BaseResponse {
  
  private ArrayList<BaseDTO> entries;
  private BaseDTO entry;

  /**
   * Constructor with no arguments
   */
  public RestResponse() {
    super();
  }

  /**
   * set the entries with the details of list of objects
   * @param entries   The list of {@link BaseDTO base} objects
   */
  public void setEntries(ArrayList<BaseDTO> entries) {
    this.entries = entries;
  }

  /**
   * Get the list of {@link BaseDTO object} details
   * @return   List of {@link BaseDTO object} details
   */
  public ArrayList<BaseDTO> getEntries() {
    return this.entries;
  }
  
  /**
   * set the base details in the response 
   * @param entry   The base DTO {@link BaseDTO object} containing the
   *                details about a single object
   */
  public void setEntry(BaseDTO entry) {
    this.entry = entry;
  }
  
  /**
   * Get the base details from the response
   * @return   The {@link BaseDTO object} details
   */
  public BaseDTO getEntry() {
    return this.entry;
  }

}