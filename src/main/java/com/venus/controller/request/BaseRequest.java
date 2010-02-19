package com.venus.controller.request;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;

@XmlRootElement
public class BaseRequest {

  @QueryParam("offset")
  @DefaultValue("0")
  @Min(value = 0, message= "offset must be greater than or equal to 0")
  private Integer offset;

  @QueryParam("maxReturn")
  @DefaultValue("20")
  @Max(value = 200, message = "maxReturn may be no greater than 200")
  @Min(value = 1, message= "maxReturn must be not be less than 1")
  private Integer maxReturn;

  public void setOffset(Integer offset) {
    this.offset = offset;
  }
  
  public Integer getOffset() {
    return this.offset;
  }

  public void setMaxReturn(Integer maxReturn) {
    this.maxReturn = maxReturn;
  }
  
  public Integer getMaxReturn() {
    return this.maxReturn;
  }

}
