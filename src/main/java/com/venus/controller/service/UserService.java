package com.venus.controller.service;

import com.venus.model.User;
import com.venus.dal.UserOperations;
import com.venus.dal.impl.UserOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;


import org.springframework.stereotype.Service;

@Service
public class UserService {
  private UserOperations uo = new UserOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession();

  public User getUser(String username) {
    User user = uo.findUserByUsername(username, vs);
    return user;
  }

}
