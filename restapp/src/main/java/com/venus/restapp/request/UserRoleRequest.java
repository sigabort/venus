package com.venus.restapp.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

/**
 * Main class representing the user role request parameters. This object contains
 * all parameters needed for creating an user role. The mandatory parameters will
 * be specified in this class. Where as, the optional parameters will be set
 * in {@link BaseUserRoleRequest}
 * 
 * This class will be used against the validator for validation of user role requests.
 * 
 * @author sigabort
 *
 */
public class UserRoleRequest extends BaseUserRoleRequest {

  @NotNull(message = "Username must be supplied")
  @Size(min=1, max=128, message = "Name size must be between 1 and 128")
  private String username;

  @NotNull(message="role(s) must be specified")
  private String[] role;

  public void setUsername(String username) {
    this.username = StringUtils.stripToNull(username);
  }
  
  public String getUsername() {
    return this.username;
  }

  public void setRole(String[] role) {
    this.role = role;
  }
  
  public String[] getRole() {
    return this.role;
  }
  

  public UserRoleRequest(String username, String[] role) {
    this.username = StringUtils.stripToNull(username);
    this.role = role;
  }
  public UserRoleRequest() {}
  
}
