package com.venus.model.impl;

import com.venus.model.Department;
import com.venus.model.Program;

import java.util.Date;

public class ProgramImpl extends BaseModelImpl implements Program {
  private String name;
  private Department department;
  private String description;
  private String code;
  private String prerequisites;
  private Integer duration;
  private Date created;
  private Date lastModified;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Department getDepartment() {
    return department;
  }
  public void setDepartment(Department department) {
    this.department = department;
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
  public String getPrerequisites() {
    return prerequisites;
  }
  public void setPrerequisites(String prerequisites) {
    this.prerequisites = prerequisites;
  }
  public Integer getDuration() {
    return duration;
  }
  public void setDuration(Integer duration) {
    this.duration = duration;
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
