package com.venus.controller.request;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;

@XmlRootElement
public class UserRequest {

  @QueryParam("offset")
  @DefaultValue("0")
  @Min(value = 0, message= "offset must be greater than or equal to 0")
  private Integer offset;

  @QueryParam("maxReturn")
  @DefaultValue("20")
  @Max(value = 200, message = "maxReturn may be no greater than 200")
  @Min(value = 1, message= "maxReturn must be not be less than 1")
  private Integer maxReturn;

  @NotBlank
  @FormParam("username")
  private String username;

  @NotBlank
  @FormParam("password")
  private String password;

  @FormParam("firstname")
  private String firstname;

  @FormParam("lastname")
  private String lastname;

  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getUsername() {
    return this.username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getPassword() {
    return this.password;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  
  public String getFirstname() {
    return this.firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
  
  public String getLastname() {
    return this.lastname;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
  
  public Integer getOffset() {
    return this.offset;
  }

  public void setMaxReturn(Integer maxReturn) {
    this.maxReturn = maxReturn;
  }
  
  public Integer getMaxReturn() {
    return this.maxReturn;
  }

}
