package com.venus.restapp.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

/**
 * Main class representing the institute request parameters. This object contains
 * all parameters needed for creating an institute. The mandatory parameters will
 * be specified in this class. Where as, the optional parameters will be set
 * in {@link BaseInstituteRequest}
 * 
 * This class will be used against the validator for validation of institute requests.
 * 
 * @author sigabort
 *
 */
public class InstituteRequest extends BaseInstituteRequest {

  @NotNull(message = "Name must be supplied")
  @Size(min=1, max=128, message = "Name size must be between 1 and 128")
  private String name;

  public void setName(String name) {
    this.name = StringUtils.stripToNull(name);
  }
  
  public String getName() {
    return this.name;
  }

  public InstituteRequest() {}
  
  public InstituteRequest(String name) {
    this.name = name;
  }

}
