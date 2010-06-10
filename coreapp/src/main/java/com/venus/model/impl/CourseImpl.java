package com.venus.model.impl;

import java.util.Date;

/**
 * CourseImpl
 */
public class CourseImpl extends BaseModelImpl implements java.io.Serializable {

  private InstituteImpl  institute;
  private String         code;
  private DepartmentImpl department;
  private ProgramImpl    program;
  private String         name;
  private String         description;
  private String         photoUrl;
  private String         content;
  private UserImpl       admin;
  private UserImpl       instructor;
  private String         prerequisites;
  private Integer        duration;
  private Integer        status;

  public InstituteImpl getInstitute() {
    return this.institute;
  }

  public void setInstitute(InstituteImpl institute) {
    this.institute = institute;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public DepartmentImpl getDepartment() {
    return this.department;
  }

  public void setDepartment(DepartmentImpl department) {
    this.department = department;
  }

  public ProgramImpl getProgram() {
    return this.program;
  }

  public void setProgram(ProgramImpl program) {
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

  public UserImpl getAdmin() {
    return this.admin;
  }

  public void setAdmin(UserImpl admin) {
    this.admin = admin;
  }

  public UserImpl getInstructor() {
    return this.instructor;
  }

  public void setInstructor(UserImpl instructor) {
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

  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof CourseImpl))
      return false;
    CourseImpl castOther = (CourseImpl) other;

    return ((this.getInstitute() == castOther.getInstitute()) || (this
        .getInstitute() != null
        && castOther.getInstitute() != null && this.getInstitute().equals(
        castOther.getInstitute())))
        && ((this.getCode() == castOther.getCode()) || (this.getCode() != null
            && castOther.getCode() != null && this.getCode().equals(
            castOther.getCode())));
  }

  public int hashCode() {
    int result = 17;

    return result;
  }

}
