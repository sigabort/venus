package com.venus.restapp.request;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Size;

/**
 * Basic request object for Department. This object contains all the optional
 * parameters can be set for an department. The mandatory params will be set
 * in {@link DepartmentRequest} object which extends this class. 
 *  
 * @author sigabort
 *
 */
public class BaseDepartmentRequest extends BaseRequest {

  @Size(min=1, max=255, message = "Code size must be between 1 and 255")
  private String code = null;

  @Size(max=4096, message="Description's size should not exceed 4096")
  private String description = null;

  @Size(max=2048, message="Photo Url's size should not exceed 2048")
  private String photoUrl = null;

  @Email (message="Email is not valid")
  @Size (max=2048, message="Email's should not exceed 2048")
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