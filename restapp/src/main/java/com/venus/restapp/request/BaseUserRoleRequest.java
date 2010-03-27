package com.venus.restapp.request;

/**
 * Basic request object for UserRole. This object contains all the optional
 * parameters can be set for an user role. The mandatory params will be set
 * in {@link UserRoleRequest} object which extends this class. 
 *  
 * @author sigabort
 *
 */
public class BaseUserRoleRequest extends BaseRequest {
  private String username = null;
  private String[] role = null;
  private String departmentName = null;

  public void setUsername(String username) {
    this.username = username;
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
  
  public String getDepartmentName() {
    return departmentName;
  }
  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }
  
}