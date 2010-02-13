package com.venus.dal;

import java.util.Date;
import java.util.List;

import com.venus.model.User;
import com.venus.util.VenusSession;

public interface UserOperations {
  
  public abstract User createUpdateUser(String username, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url, VenusSession session);
  
  public abstract User findUserByUsername(String username, VenusSession session);

  public abstract List<User> getUsers(int offset, int maxRet, VenusSession vs);
  
}
