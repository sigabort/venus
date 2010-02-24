package com.venus.model;

import java.util.Date;

public interface Program extends BaseModel {

  public abstract String getName();

  public abstract void setName(String name);

  public abstract Department getDepartment();

  public abstract void setDepartment(Department department);

  public abstract String getDescription();

  public abstract void setDescription(String description);

  public abstract String getCode();

  public abstract void setCode(String code);

  public abstract String getPrerequisites();

  public abstract void setPrerequisites(String prerequisites);

  public abstract Integer getDuration();

  public abstract void setDuration(Integer duration);

  public abstract Date getCreated();

  public abstract void setCreated(Date created);

  public abstract Date getLastModified();

  public abstract void setLastModified(Date lastModified);
}
