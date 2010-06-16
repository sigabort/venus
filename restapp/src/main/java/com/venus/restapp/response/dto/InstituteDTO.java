package com.venus.restapp.response.dto;

import com.venus.model.Institute;
import com.venus.model.BaseModel;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * This class represents the object which is sent to the client as part of
 * the institute response. This object is built using the model object from the
 * DB: {@link Institute}. 
 *
 * @author sigabort
 *
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public class InstituteDTO implements BaseDTO {
  private String name;
  private String displayName;
  private String parent;
  private String code;
  private String description;
  private String photoUrl;
  private String email;

  public InstituteDTO()  {}

  public InstituteDTO(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
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
   * Get the {@link InstituteDTO} object from the {@link Institute}
   * model object (object from the DB).
   * @param institute      The model object 
   * @return The reponse {@link InstituteDTO object} built using the model object
   */
  public InstituteDTO getDTO(BaseModel inst) {
    Institute institute = (Institute) inst;
    if (institute != null) {
      InstituteDTO dto = new InstituteDTO(institute.getName(), institute.getCode());
      dto.setDisplayName(institute.getDisplayName());
      Institute parent = institute.getParent();
      dto.setParent(parent != null? parent.getName() : null);
      dto.setDescription(institute.getDescription());
      dto.setPhotoUrl(institute.getPhotoUrl());
      dto.setEmail(institute.getEmail());
      return dto;
    }
    return null;
  }
}
 
