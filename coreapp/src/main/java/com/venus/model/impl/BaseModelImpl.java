package com.venus.model.impl;

import java.util.Date;

import com.venus.model.BaseModel;

public class BaseModelImpl implements BaseModel {
  private Integer ID;
  private Integer status;
  private Date    created;
  private Date    lastModified;

  public void setID(Integer ID) {
    this.ID = ID;
  }

  public Integer getID() {
    return this.ID;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getStatus() {
    return this.status;
  }

  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getLastModified() {
    return this.lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

}