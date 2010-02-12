package com.venus.model;

import java.util.Date;

public interface User {

  public abstract void setID(Integer id);

  public abstract Integer getID();
  
  public abstract void setFirstName(String name);

  public abstract String getFirstName();
  
  public abstract void setLastName(String name);

  public abstract String getLastName();

  public abstract void setUsername(String uname);

  public abstract String getUsername();

  public abstract void setPassword(String password);

  public abstract String getPassword();

  public abstract void setEmail(String email);

  public abstract String getEmail();

  public abstract void setGender(String gender);

  public abstract String getGender();

  public abstract void setAddress(String address);

  public abstract String getAddress();
  
  public abstract void setUrl(String url);

  public abstract String getUrl();

  public abstract void setBirthDate(Date birthDate);

  public abstract Date getBirthDate();

}
