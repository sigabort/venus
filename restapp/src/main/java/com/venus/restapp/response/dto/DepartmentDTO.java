package com.venus.restapp.response.dto;

import com.venus.model.Department;
import com.venus.model.BaseModel;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * This class represents the object which is sent to the client as part of
 * the department response. This object is built using the model object from the
 * DB: {@link Department}. 
 *
 * @author sigabort
 *
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public class DepartmentDTO implements BaseDTO {
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
  
  /**
   * Get the {@link DepartmentDTO} object from the {@link Department}
   * model object (object from the DB).
   * @param department      The model object 
   * @return The reponse {@link DepartmentDTO object} built using the model object
   */
  public DepartmentDTO getDTO(BaseModel dept) {
    Department department = (Department) dept;
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
 