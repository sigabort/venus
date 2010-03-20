package com.venus.restapp.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DepartmentRequest extends BaseDepartmentRequest {

  @NotNull(message = "Name must be supplied")
  @Size(min=1, max=128, message = "Name size must be between 1 and 128")
  private String name;

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }


}
