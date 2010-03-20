package com.venus.restapp.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BaseDepartmentRequest {

  @Size(min=1, max=128, message = "Code size must be between 1 and 128")
  private String code = null;

  private String description = null;

  private String photoUrl = null;

  private String email = null;

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