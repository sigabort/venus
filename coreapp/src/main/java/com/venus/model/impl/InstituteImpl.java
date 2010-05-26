package com.venus.model.impl;

import com.venus.model.Department;
import com.venus.model.Institute;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation class for Institute bean
 * @author sigabort
 */
public class InstituteImpl extends BaseModelImpl implements Institute {
  private String name;
  private String displayName;
  private Institute parent;
  private String description;
  private String code;
  private String photoUrl;
  private String email;
  private List<Department> departments = new ArrayList<Department>();
  private Date created;
  private Date lastModified;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public Institute getParent() {
    return parent;
  }
  public void setParent(Institute parent) {
    this.parent = parent;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
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
  public List<Department> getDepartments() {
    return departments;
  }
  public void setDepartments(List<Department> departments) {
    this.departments = departments;
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
  
}
