package com.venus.restapp.request;

import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;


/**
 * Main class representing the department request parameters. This object contains
 * all parameters needed for creating a department. The mandatory parameters will
 * be specified in this class. Where as, the optional parameters will be set
 * in {@link BaseDepartmentRequest}
 * 
 * This class will be used against the validator for validation of department requests.
 * 
 * @author sigabort
 *
 */
public class DepartmentRequest extends BaseDepartmentRequest {

  @NotNull(message = "Name must be supplied")
  @Size(min=1, max=128, message = "Name size must be between 1 and 255")
  private String name;

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }

  public DepartmentRequest() {}
  
  public DepartmentRequest(String name) {
    this.name = name;
  }

}
