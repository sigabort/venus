package com.venus.model.impl;

import com.venus.model.BaseModel;

public class BaseModelImpl implements BaseModel {
  private Integer ID;
  private Integer status;

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
}