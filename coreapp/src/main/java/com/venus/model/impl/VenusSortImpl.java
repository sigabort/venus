package com.venus.model.impl;


public class VenusSortImpl {
  private String sortBy;
  private Boolean isAscending;
 
  public String getSortBy() {
    return sortBy;
  }
  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }
  public Boolean getIsAscending() {
    return isAscending;
  }
  public void setIsAscending(Boolean isAscending) {
    this.isAscending = isAscending;
  }
  
  public VenusSortImpl(String sortBy) {
    this.sortBy = sortBy;
    this.isAscending = Boolean.TRUE;
  }
  
  public VenusSortImpl(String sortBy, Boolean isAscending) {
    this.sortBy = sortBy;
    this.isAscending = isAscending;
  }
  
}