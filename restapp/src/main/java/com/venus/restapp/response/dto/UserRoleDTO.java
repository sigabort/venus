package com.venus.restapp.response.dto;

import java.util.Date;

import com.venus.model.UserRole;
import com.venus.model.Role;
import com.venus.model.BaseModel;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * This class represents the object which is sent to the client as part of
 * the userRole response. This object is built using the model object from the
 * DB: {@link UserRole}. 
 *
 * @author sigabort
 *
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public class UserRoleDTO implements BaseDTO {
  private String username = null;
  private String role = null;
  private String department = null;
  private Date created = null;
  private Date lastModified = null;

  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }

  public String getRole() {
    return this.role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }

  public String getDepartment() {
    return this.department;
  }
  
  public void setDepartment(String department) {
    this.department = department;
  }

  
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }
  public Date getLastModified() {
    return lastModified;
  }
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public UserRoleDTO()  {}

  public UserRoleDTO(String role, String username) {
    this.role = role;
    this.username = username;
  }

  
  /**
   * Get the {@link UserRoleDTO} object from the {@link UserRole}
   * model object (object from the DB).
   * @param userRole      The model object 
   * @return The DTO {@link UserRoleDTO object} built using the model object
   */
  public UserRoleDTO getDTO(BaseModel ur) {
    UserRole userRole = (UserRole) ur;
    if (userRole != null) {
      String role = Role.values()[userRole.getRole()].toString();
      String username = userRole.getUser().getUsername();
      UserRoleDTO dto = new UserRoleDTO(role, username);
      if (userRole.getDepartment() != null) {
        dto.setDepartment(userRole.getDepartment().getName());
      }
      dto.setCreated(userRole.getCreated());
      dto.setLastModified(userRole.getLastModified());

      return dto;
    }
    return null;
  }
}
 