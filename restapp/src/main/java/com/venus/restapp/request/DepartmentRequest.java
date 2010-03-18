package com.venus.restapp.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DepartmentRequest {

  @NotNull(message = "Name must be supplied")
  @Size(min=1, max=128, message = "Name size must be between 1 and 128")
  private String name;

  @Size(min=1, max=128, message = "Code size must be between 1 and 128")
  private String code;

  private String description;

  private String photoUrl;

  private String email;

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getDescription() {
    return this.description;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }
  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

}
