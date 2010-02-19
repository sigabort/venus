package com.venus.model.impl;

import com.venus.model.User;

import java.util.Date;

public class UserImpl implements User {
  private Integer ID;
  private String username;
  private String userId;
  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private String gender;
  private String url;
  private String phone;
  private String address1;
  private String address2;
  private String city;
  private String country;
  private String postalCode;
  private String photoUrl;
  private Date birthDate;
  private Date joinDate;
  private Date created;
  private Date lastModified;

  public Integer getID() {
    return ID;
  }
  public void setID(Integer id) {
    ID = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
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
