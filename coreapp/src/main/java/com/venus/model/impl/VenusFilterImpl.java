package com.venus.model.impl;


public class VenusFilterImpl {
  private String filterBy;
  private Object filterValue;
  private String filterOp;

  public VenusFilterImpl(Object filterValue, String filterBy) {
    this.filterValue = filterValue;
    this.filterBy = filterBy;
    this.filterOp = "contains";
  }
  public VenusFilterImpl(String filterOp, Object filterValue, String filterBy) {
    this.filterOp = filterOp;
    this.filterValue = filterValue;
    this.filterBy = filterBy;
  }

  public String getFilterBy() {
    return filterBy;
  }
  public void setFilterBy(String filterBy) {
    this.filterBy = filterBy;
  }
  public Object getFilterValue() {
    return filterValue;
  }
  public void setFilterValue(Object filterValue) {
    this.filterValue = filterValue;
  }
  public String getFilterOp() {
    return filterOp;
  }
  public void setFilterOp(String filterOp) {
    this.filterOp = filterOp;
  }

  
  
}