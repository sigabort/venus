package com.venus.model;

import java.util.Date;

public interface User extends BaseModel {

  public abstract String getUsername();

  public abstract void setUsername(String username);

  public abstract Institute getInstitute();

  public abstract void setInstitute(Institute institute);

  public abstract String getUserId();

  public abstract void setUserId(String userId);

  public abstract String getPassword();

  public abstract void setPassword(String password);

  public abstract String getFirstName();

  public abstract void setFirstName(String firstName);

  public abstract String getLastName();

  public abstract void setLastName(String lastName);

  public abstract String getEmail();

  public abstract void setEmail(String email);

  public abstract String getGender();

  public abstract void setGender(String gender);

  public abstract String getUrl();

  public abstract void setUrl(String url);

  public abstract String getPhone();

  public abstract void setPhone(String phone);

  public abstract String getAddress1();

  public abstract void setAddress1(String address1);

  public abstract String getAddress2();

  public abstract void setAddress2(String address2);

  public abstract String getCity();

  public abstract void setCity(String city);

  public abstract String getCountry();

  public abstract void setCountry(String country);

  public abstract String getPostalCode();

  public abstract void setPostalCode(String postalCode);

  public abstract String getPhotoUrl();

  public abstract void setPhotoUrl(String photoUrl);

  public abstract Date getBirthDate();

  public abstract void setBirthDate(Date birthDate);

  public abstract Date getJoinDate();

  public abstract void setJoinDate(Date joinDate);

  public abstract Date getCreated();

  public abstract void setCreated(Date created);

  public abstract Date getLastModified();

  public abstract void setLastModified(Date lastModified);
}
