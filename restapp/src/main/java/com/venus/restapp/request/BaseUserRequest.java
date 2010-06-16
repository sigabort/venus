package com.venus.restapp.request;

import java.util.Date;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

/**
 * Basic request object for User. This object contains all the optional
 * parameters can be set for an user. The mandatory params will be set
 * in {@link UserRequest} object which extends this class. 
 *  
 * @author sigabort
 *
 */
public class BaseUserRequest extends BaseUserRoleRequest {
  @Email
  @Size(max=2048, message="email size should not exceed 2048")
  private String email;
  @Size(max=255, message="userId size should not exceed 255")
  private String userId;
  @Size(max=255, message="password size should not exceed 255")
  private String password;
  @Size(max=255, message="firstName size should not exceed 255")
  private String firstName;
  @Size(max=255, message="lastName size should not exceed 255")
  private String lastName;
  @Size(max=10, message="gender size should not exceed 10")
  private String gender;
  @Size(max=2048, message="url size should not exceed 2048")
  private String url;
  @Size(max=20, message="phone size should not exceed 20")
  private String phone;
  @Size(max=255, message="address1 size should not exceed 255")
  private String address1;
  @Size(max=255, message="address2 size should not exceed 255")
  private String address2;
  @Size(max=50, message="city size should not exceed 20")
  private String city;
  @Size(max=50, message="country size should not exceed 50")
  private String country;
  @Size(max=10, message="postalCode size should not exceed 10")
  private String postalCode;
  @Size(max=2048, message="photoUrl size should not exceed 2048")
  private String photoUrl;

  @DateTimeFormat(style="S-")
  private Date birthDate;
  
  @DateTimeFormat(style="S-")
  private Date joinDate;
  
  @DateTimeFormat(style="S-")
  private Date created;
  
  @DateTimeFormat(style="S-")
  private Date lastModified;
  
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getAddress1() {
    return address1;
  }
  public void setAddress1(String address1) {
    this.address1 = address1;
  }
  public String getAddress2() {
    return address2;
  }
  public void setAddress2(String address2) {
    this.address2 = address2;
  }
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public String getCountry() {
    return country;
  }
  public void setCountry(String country) {
    this.country = country;
  }
  public String getPostalCode() {
    return postalCode;
  }
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  public String getPhotoUrl() {
    return photoUrl;
  }
  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
  public Date getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
  public Date getJoinDate() {
    return joinDate;
  }
  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }
  public Date getCreated() {
    return created;
  }
  public void setCreated(Date created) {
    this.created = created;
  }
  public Date getLastModified() {
    return lastModified;
  }
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

}