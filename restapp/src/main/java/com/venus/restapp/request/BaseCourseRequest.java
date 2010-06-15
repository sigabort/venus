package com.venus.restapp.request;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.exclusion.Nullable;

/**
 * Basic request object for Course. This object contains all the optional
 * parameters can be set for an course. The mandatory params will be set in
 * {@link CourseRequest} object which extends this class.
 * 
 * @author sigabort
 * 
 */
public class BaseCourseRequest extends BaseRequest {
  @Length(max=255)
  private String  department;

  @Length(max=255)
  private String  program;

  @Length(max=255)
  private String  name;
  
  @Length(max=4096)
  private String  description;
  
  @Length(max=2048)
  private String  photoUrl;
  
  @Length(max=8192)
  private String  content;
  
  @Length(max=255)
  private String  admin;
  
  @Length(max=255)
  private String  instructor;
  
  @Length(max=2048)
  private String  prerequisites;
  
  private Integer duration;
  
  private Integer status;

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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
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

}

