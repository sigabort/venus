package com.venus.controller.service;

import java.util.List;
import java.util.ArrayList;

import com.venus.model.User;
import com.venus.model.impl.UserImpl;
import com.venus.dal.UserOperations;
import com.venus.dal.impl.UserOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import com.venus.controller.request.UserRequest;

import org.springframework.stereotype.Service;


@Service
public class UserService {
  private UserOperations uo = new UserOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession();

  public User getUser(String username) {
    User user = uo.findUserByUsername(username, vs);
    return user;
  }
  
  public User createUpdateUser(UserRequest req) {
    String username = req.getUsername();
    String firstname = req.getFirstname();
    String lastname = req.getLastname();
    String password = req.getPassword();
    vs.getSession().beginTransaction();
    User user = uo.createUpdateUser(username, password, firstname, lastname, null, null, null, null, null, vs);
    if (user == null) {
      throw new RuntimeException("Unable to create/update user");
    }
    vs.getSession().getTransaction().commit();
    return user;
  }

  public List<User> getUsers(int offset, int maxRet) {
    List<User> users = uo.getUsers(offset, maxRet, vs);
    return users;
  }


}
