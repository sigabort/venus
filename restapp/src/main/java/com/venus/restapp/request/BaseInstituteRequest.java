package com.venus.restapp.request;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Size;

/**
 * Basic request object for Institute. This object contains all the optional
 * parameters can be set for an institute. The mandatory params will be set
 * in {@link InstituteRequest} object which extends this class. 
 *  
 * @author sigabort
 *
 */
public class BaseInstituteRequest extends BaseRequest {

  @Size(min=1, max=128, message = "Code size must be between 1 and 128")
  private String code = null;

  @Size(max=1024, message = "Display Name should not exceed 1024 characters")
  private String displayName = null;
  
  @Size(max=255, message = "Parent Institute Name should not exceed 255 characters")
  private String parent = null;

  @Size(max=4096, message = "Description should not exceed 4096 characters")
  private String description = null;

  @Size(max=2048, message = "photo url should not exceed 2048 characters")
  private String photoUrl = null;

  @Email
  @Size(max=2048, message = "email should not exceed 2048 characters")
  private String email = null;

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
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
