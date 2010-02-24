package com.venus.controller.request;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@XmlRootElement
public class ProgramRequest extends BaseRequest {

  @NotNull(message = "Name must be supplied")
  @NotBlank(message = "Name must not be blank")
  @FormParam("name")
  private String name;

  @NotNull(message = "Department Name must be supplied")
  @NotBlank(message = "Department Name must not be blank")
  @FormParam("departmentName")
  private String departmentName;  

  @FormParam("code")
  private String code;

  @FormParam("description")
  private String description;

  @FormParam("prerequisites")
  private String prerequisites;

  @FormParam("duration")
  private Integer duration;

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }
  
  public String getDepartmentName() {
    return this.departmentName;
  }

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getDescription() {
    return this.description;
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

}
