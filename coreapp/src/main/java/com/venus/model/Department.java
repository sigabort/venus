package com.venus.model;

import java.util.Date;
import java.util.List;

/**
 * Department bean representing the Department in an institute
 * @author sigabort
 */
public interface Department extends BaseModel {

  public abstract String getName();

  public abstract void setName(String name);

  public abstract Integer getInstituteId();

  public abstract void setInstituteId(Integer instituteId);

  public abstract String getDescription();

  public abstract void setDescription(String description);

  public abstract String getCode();

  public abstract void setCode(String code);

  public abstract String getPhotoUrl();

  public abstract void setPhotoUrl(String photoUrl);

  public abstract String getEmail();

  public abstract void setEmail(String email);
  
  public abstract void setPrograms(List<Program> programs);

  public abstract List<Program> getPrograms();

  public abstract Date getCreated();

  public abstract void setCreated(Date created);

  public abstract Date getLastModified();

  public abstract void setLastModified(Date lastModified);
}
