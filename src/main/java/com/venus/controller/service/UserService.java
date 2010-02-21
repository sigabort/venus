package com.venus.controller.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.venus.model.User;
import com.venus.model.impl.UserImpl;
import com.venus.dal.UserOperations;
import com.venus.dal.DataAccessException;
import com.venus.dal.impl.UserOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.controller.request.UserRequest;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

@Service
public class UserService {
  private UserOperations uo = new UserOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession();
  private Logger log = Logger.getLogger(UserService.class);

  public User getUser(String username) {
    User user = null;
    try {
      user = uo.findUserByUsername(username, vs);
    }
    catch (DataAccessException dae) {
      log.error("Internal error while getting user: " + username, dae);
      throw new RuntimeException("Internal error while getting user: " + username, dae);
    }
    return user;
  }
  
  public User createUpdateUser(UserRequest req) {
    String username = req.getUsername();
    String userId = req.getUserId();
    String password = req.getPassword();
    String firstname = req.getFirstName();
    String lastname = req.getLastName();
    String email = req.getEmail();
    String gender = req.getGender();
    String url = req.getUrl();
    String phone = req.getPhone();
    String address1 = req.getAddress1();
    String address2 = req.getAddress2();
    String city = req.getCity();
    String country = req.getCountry();
    String postalCode = req.getPostalCode();
    String photoUrl = req.getPhotoUrl();
    String birthDate = req.getBirthDate();
    String joinDate = req.getJoinDate();

    User user = null;

    try {
      user = uo.createUpdateUser(username, userId, password, firstname, 
				      lastname, email, gender, url, phone, address1, 
				      address2, city, country, postalCode, photoUrl, 
				      null, null, null, null, vs);
    }
    catch (DataAccessException dae) {
      log.error("Internal error while creating/updating user: " + username, dae);
      throw new RuntimeException("Internal error while creating/updating user: " + username, dae);
    }
    if (user == null) {
      throw new RuntimeException("Unable to create/update user");
    }
    return user;
  }

  public List<User> getUsers(int offset, int maxRet) {
    List<User> users = null;
    try {
      users = uo.getUsers(offset, maxRet, vs);
    }
    catch (DataAccessException dae) {
      log.error("Internal error while getting users", dae);
      throw new RuntimeException("Internal error while getting users", dae);
    }
    
    return users;
  }


}
