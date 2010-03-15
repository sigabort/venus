package com.venus.controller.request;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@XmlRootElement
public class DepartmentRequest extends BaseRequest {

  @NotNull(message = "Name must be supplied")
  @NotBlank(message = "Name must not be blank")
  @FormParam("name")
  private String name;

  @NotNull(message = "Code must be supplied")
  @NotBlank(message = "Code must not be blank")
  @FormParam("code")
  private String code;

  @FormParam("description")
  private String description;

  @FormParam("photoUrl")
  private String photoUrl;

  @FormParam("email")
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
