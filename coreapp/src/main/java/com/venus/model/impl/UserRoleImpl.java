package com.venus.model.impl;

import com.venus.model.User;
import com.venus.model.Department;
import com.venus.model.UserRole;

import java.util.Date;

/**
 * Impl class for {@link UserRole}
 * 
 * @author sigabort
 *
 */
public class UserRoleImpl extends BaseModelImpl implements UserRole {
  private Department department;
  private User user;
  private Integer role;
  private Date created;
  private Date lastModified;
  
  public void setDepartment(Department department) {
    this.department = department;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public void setRole(Integer role) {
    this.role = role;
  }
  public void setCreated(Date created) {
    this.created = created;
  }
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
  public Department getDepartment() {
    return department;
  }
  public User getUser() {
    return user;
  }
  public Integer getRole() {
    return role;
  }
  public Date getCreated() {
    return created;
  }
  public Date getLastModified() {
    return lastModified;
  }

}