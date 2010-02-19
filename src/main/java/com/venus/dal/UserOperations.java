package com.venus.dal;

import java.util.Date;
import java.util.List;

import com.venus.model.User;
import com.venus.util.VenusSession;

public interface UserOperations {
  
  public abstract User createUpdateUser(String username, String userId, 
					String password, String firstName, String lastName, 
					String email, String gender, String url, 
					String phone, String address1, String address2,
					String city, String country, String postalCode, 
					String photoUrl, Date birthDate, Date joinDate,
					Date created, Date lastModified, VenusSession session);
  
  public abstract User findUserByUsername(String username, VenusSession session);

//   public abstract User findUserByUserId(String userId, VenusSession session);

//   public abstract User findUserByEmail(String email, VenusSession session);

  public abstract List<User> getUsers(int offset, int maxRet, VenusSession vs);
  
}
