package com.venus.restapp.request;


import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

/**
 * Main class representing the course request parameters. This object contains
 * all parameters needed for creating a course. The mandatory parameters will
 * be specified in this class. Where as, the optional parameters will be set
 * in {@link BaseCourseRequest}
 * 
 * This class will be used against the validator for validation of course requests.
 * 
 * @author sigabort
 *
 */
public class CourseRequest extends BaseCourseRequest {
  @NotNull
  @Length(min=1, max=255, message="Size of Code should be in between 1 and 255 characters")
  //Check for Special Characters
  private String code;
  
  @NotNull
  @Length(min=1, max=255, message="Size of Name should be in between 1 and 255 characters")
  private String name;
  
  @NotNull
  @Length(min=1, max=255, message="Size of department should be in between 1 and 255 characters")
  private String department;
  
  @NotNull
  @Length(min=1, max=255, message="Size of instructor should be in between 1 and 255 characters")
  private String instructor;
  
  @NotNull
  @Length(min=1, max=255, message="Size of admin should be in between 1 and 255 characters")
  private String admin;

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getName() {
    return this.name;
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
}