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
  
  public static ProgramDTO getProgramDTO(Program program) {
    ProgramDTO dto = new ProgramDTO(program.getName());
    return dto;
  }
  
}
 