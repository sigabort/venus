package com.venus.model;

import java.util.Date;

/**
 * This class represents the role of a user in the institute/department
 * 
 * @author sigabort
 *
 */
public interface UserRole extends BaseModel {

  public abstract Department getDepartment();

  public abstract void setDepartment(Department department);

  public abstract User getUser();

  public abstract void setUser(User user);
  
  public abstract Integer getRole();
  
  public abstract void setRole(Integer role);

  public abstract Date getCreated();

  public abstract void setCreated(Date created);

  public abstract Date getLastModified();

  public abstract void setLastModified(Date lastModified);
}
