package com.venus.model;

import java.util.Date;
import java.util.List;

/**
 * Institute bean representing the an institute
 * @author sigabort
 */
public interface Institute extends BaseModel {

  public abstract String getName();

  public abstract void setName(String name);
  
  public abstract String getCode();

  public abstract void setCode(String code);  

  public abstract String getDisplayName();

  public abstract void setDisplayName(String displayName);

  public abstract Institute getParent();

  public abstract void setParent(Institute parent);

  public abstract String getDescription();

  public abstract void setDescription(String description);

  public abstract String getPhotoUrl();

  public abstract void setPhotoUrl(String photoUrl);

  public abstract String getEmail();

  public abstract void setEmail(String email);
  
  public abstract Date getCreated();

  public abstract void setCreated(Date created);

  public abstract Date getLastModified();

  public abstract void setLastModified(Date lastModified);
}
