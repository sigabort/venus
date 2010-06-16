package com.venus.restapp.response.dto;

import java.util.Date;

import com.venus.model.impl.CourseImpl;
import com.venus.model.BaseModel;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * This class represents the object which is sent to the client as part of the
 * course response. This object is built using the model object from the DB:
 * {@link CourseImpl}.
 * 
 * @author sigabort
 * 
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public class CourseDTO implements BaseDTO {
  private String  department;
  private String  admin;
  private String  instructor;
  private String  program;
  private String  code;
  private String  name;
  private String  description;
  private String  photoUrl;
  private String  content;
  private String  prerequisites;
  private Integer duration;
  private String  status;
  private Date    created;
  private Date    lastModified;

  public CourseDTO() {
  }

  public CourseDTO(String code, String name, String department,
      String instructor, String admin) {
    this.name = name;
    this.code = code;
    this.department = department;
    this.instructor = instructor;
    this.admin = admin;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDepartment() {
    return this.department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getProgram() {
    return this.program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhotoUrl() {
    return this.photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAdmin() {
    return this.admin;
  }

  public void setAdmin(String admin) {
    this.admin = admin;
  }

  public String getInstructor() {
    return this.instructor;
  }

  public void setInstructor(String instructor) {
    this.instructor = instructor;
  }

  public String getPrerequisites() {
    return this.prerequisites;
  }

  public void setPrerequisites(String prerequisites) {
    this.prerequisites = prerequisites;
  }

  public Integer getDuration() {
    return this.duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getLastModified() {
    return this.lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Get the {@link CourseDTO} object from the {@link Course} model object
   * (object from the DB).
   * 
   * @param course
   *          The model object
   * @return The reponse {@link CourseDTO object} built using the model object
   */
  public CourseDTO getDTO(BaseModel course) {
    CourseImpl c = (CourseImpl) course;
    if (course != null) {
      CourseDTO dto = new CourseDTO(c.getCode(), c.getName(), c.getDepartment()
          .getName(), c.getInstructor().getUsername(), c.getAdmin()
          .getUsername());
      dto.setDescription(c.getDescription());
      dto.setPhotoUrl(c.getPhotoUrl());
      dto.setContent(c.getContent());
      dto.setCreated(c.getCreated());
      dto.setDuration(c.getDuration());
      dto.setLastModified(c.getLastModified());
      dto.setPrerequisites(c.getPrerequisites());
      dto.setStatus(c.getStatus().toString());
      if (c.getProgram() != null) {
        dto.setProgram(c.getProgram().getName());
      }
      return dto;
    }
    return null;
  }
}
