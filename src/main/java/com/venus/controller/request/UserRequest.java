package com.venus.controller.request;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

@XmlRootElement
public class UserRequest extends BaseRequest {
  @NotNull(message = "Username should not be null")
  @NotBlank(message = "Username should not be left blank")
  @FormParam("username")
  private String username;

  @FormParam("userId")
  private String userId;

  @NotBlank
  @FormParam("password")
  private String password;

  @FormParam("firstname")
  private String firstName;

  @FormParam("lastname")
  private String lastName;

  @FormParam("email")
  private String email;

  @FormParam("gender")
  private String gender;

  @FormParam("url")
  private String url;

  @FormParam("phone")
  private String phone;

  @FormParam("address1")
  private String address1;

  @FormParam("address2")
  private String address2;

  @FormParam("city")
  private String city;

  @FormParam("country")
  private String country;

  @FormParam("postalCode")
  private String postalCode;

  @FormParam("photoUrl")
  private String photoUrl;

  @FormParam("birthDate")
  private String birthDate;

  @FormParam("joinDate")
  private String joinDate;

  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getUsername() {
    return this.username;
  }

  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getPassword() {
    return this.password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getFirstName() {
    return this.firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getLastName() {
    return this.lastName;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
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
  public String getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }
  public String getJoinDate() {
    return joinDate;
  }
  public void setJoinDate(String joinDate) {
    this.joinDate = joinDate;
  }
}
