package com.venus.controller.response.dto;

import com.venus.model.Program;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonWriteNullProperties(value=false)
public class ProgramDTO {
  private String name;
  private String department;
  private String description;
  private String code;
  private String prerequisites;
  private Integer duration;

  public ProgramDTO()  {}

  public ProgramDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return department;
  }
  public void setDepartment(String department) {
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
  
  public static ProgramDTO getProgramDTO(Program program) {
    ProgramDTO dto = new ProgramDTO(program.getName());
    dto.setDescription(program.getDescription());
    if (program.getDepartment() != null) {
      dto.setDepartment(program.getDepartment().getName());
    }
    dto.setCode(program.getCode());
    dto.setPrerequisites(program.getPrerequisites());
    dto.setDuration(program.getDuration());
    return dto;
  }
  
}
 