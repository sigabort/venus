package com.venus.model.impl;

import com.venus.model.Department;
import com.venus.model.Program;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class DepartmentImpl extends BaseModelImpl implements Department {
  private String name;
  private String description;
  private String code;
  private String photoUrl;
  private String email;
  private List<Program> programs = new ArrayList<Program>();
  private Date created;
  private Date lastModified;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
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
  public List<Program> getPrograms() {
    return programs;
  }
  public void setPrograms(List<Program> programs) {
    this.programs = programs;
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
