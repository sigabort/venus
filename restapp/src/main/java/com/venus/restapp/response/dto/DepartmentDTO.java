package com.venus.restapp.response.dto;

import com.venus.model.Department;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class DepartmentDTO {
  private String name;
  private String code;
  private String description;
  private String photoUrl;
  private String email;

  public DepartmentDTO()  {}

  public DepartmentDTO(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getPhotoUrl() {
    return photoUrl;
  }
  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  
  public static DepartmentDTO getDepartmentDTO(Department department) {
    if (department != null) {
      DepartmentDTO dto = new DepartmentDTO(department.getName(), department.getCode());
      dto.setDescription(department.getDescription());
      dto.setPhotoUrl(department.getPhotoUrl());
      dto.setEmail(department.getEmail());
      return dto;
    }
    return null;
  }
}
 