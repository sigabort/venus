package com.venus.model.impl;

import com.venus.model.User;

import java.util.Date;

public class UserImpl implements User {
  private Integer id;
  private String lastName;
  private String firstName;
  private String username;
  private String password;
  private String email;
  private String address;
  private String gender;
  private String url;
  private Date birthDate;
  
  public void setID(Integer id) {
    this.id = id;
  }

  public Integer getID() {
    return this.id;
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

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return this.address;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getGender() {
    return this.gender;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return this.url;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Date getBirthDate() {
    return this.birthDate;
  }

}
