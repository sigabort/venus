package com.venus.controller.request;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;

import java.util.List;

@XmlRootElement
public class BaseRequest {

  @QueryParam("startIndex")
  @DefaultValue("0")
  @Min(value = 0, message= "startIndex must be greater than or equal to 0")
  private Integer startIndex;

  @QueryParam("itemsPerPage")
  @DefaultValue("20")
  @Max(value = 200, message = "itemsPerPage may be no greater than 200")
  @Min(value = 1, message= "itemsPerPage must be not be less than 1")
  private Integer itemsPerPage;

  @QueryParam("format")
  @DefaultValue("json")
  private String format;

  @QueryParam("sortBy")
  private List<String> sortFields;

  @QueryParam("filterBy")
  private String filterField;

  @QueryParam("filterValue")
  private String filterValue;

  @QueryParam("filterOp")
  private String filterOp;

  @QueryParam("sortOrder")
  @DefaultValue("ascending")
  private String sortOrder;
    
  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }
  
  public Integer getStartIndex() {
    return this.startIndex;
  }

  public void setItemsPerPage(Integer itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }
  
  public Integer getItemsPerPage() {
    return this.itemsPerPage;
  }

  public void setFormat(String format) {
    this.format = format;
  }
  
  public String getFormat() {
    return this.format;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  public String getSortOrder() {
    return this.sortOrder;
  }

  public void setSortFields(List<String> sortFields) {
    this.sortFields = sortFields;
  }
  
  public List<String> getSortFields() {
    return this.sortFields;
  }

  public void setFilterField(String filterField) {
    this.filterField = filterField;
  }
  
  public String getFilterField() {
    return this.filterField;
  }

  public void setFilterValue(String filterValue) {
    this.filterValue = filterValue;
  }
  
  public String getFilterValue() {
    return this.filterValue;
  }

  public void setFilterOp(String filterOp) {
    this.filterOp = filterOp;
  }
  
  public String getFilterOp() {
    return this.filterOp;
  }

}
