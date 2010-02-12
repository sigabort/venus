package com.venus.dal.impl;

import java.util.Date;

import com.venus.model.User;
import com.venus.model.impl.UserImpl;
import com.venus.util.VenusSession;
import com.venus.dal.UserOperations;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

public class UserOperationsImpl implements UserOperations {
  private String FIND_USER_BY_USERNAME_STR = "findUserByUsername";
  
  public User createUpdateUser(String username, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url, VenusSession session) {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    User user = findUserByUsername(username, session);
    if (user != null) {
      user = updateUser(user, password, firstName, lastName, email, address, gender, birthDate, url, session);
    }
    else {
      user = createUser(username, password, firstName, lastName, email, address, gender, birthDate, url, session);
    }
    return user;
  }

  private User createUser(String username, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url, VenusSession session) {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    User user = new UserImpl();
    if (username != null) {
      user.setUsername(username);
    }

    if (password != null) {
      user.setPassword(password);
    }

    if (firstName != null) {
      user.setFirstName(firstName);
    }

    if (lastName != null) {
      user.setLastName(lastName);
    }

    if (email != null) {
      user.setEmail(email);
    }

    if (address != null) {
      user.setAddress(address);
    }

    if (gender != null) {
      user.setGender(gender);
    }

    if (url != null) {
      user.setUrl(url);
    }

    if (birthDate != null) {
      user.setBirthDate(birthDate);
    }

    Session sess = session.getSession();
    sess.save(user);
    return user;
  }

  private User updateUser(User user, String password, String firstName, String lastName, String email, String address, String gender, Date birthDate, String url, VenusSession session) {
    if (user == null) {
      return null;
    }
    boolean update = false;

    String oldPassword = user.getPassword();
    if (oldPassword == null || !oldPassword.equals(password)) {
      user.setPassword(password);
      update = true;
    }

    String oldFirstName = user.getFirstName();
    if (oldFirstName == null || !oldFirstName.equals(firstName)) {
      user.setFirstName(firstName);
      update = true;
    }

    String oldLastName = user.getLastName();
    if (oldLastName == null || !oldLastName.equals(lastName)) {
      user.setLastName(lastName);
      update = true;
    }

    String oldEmail = user.getEmail();
    if (oldEmail == null || !oldEmail.equals(email)) {
      user.setEmail(email);
      update = true;
    }

    String oldAddress = user.getAddress();
    if (oldAddress == null || !oldAddress.equals(address)) {
      user.setAddress(address);
      update = true;
    }

    String oldUrl = user.getUrl();
    if (oldUrl == null || !oldUrl.equals(url)) {
      user.setUrl(url);
      update = true;
    }

    String oldGender = user.getGender();
    if (oldGender == null || !oldGender.equals(gender)) {
      user.setGender(gender);
      update = true;
    }

    Date oldBirthDate = user.getBirthDate();
    if (oldBirthDate == null || !oldBirthDate.equals(birthDate)) {
      user.setBirthDate(birthDate);
      update = true;
    }

    if (update) {
      Session sess = session.getSession();
      sess.update(user);
    }
    return user;
  }

  public User findUserByUsername(String username, VenusSession vs) {
    if (username == null) {
      return null;
    }
    Session sess = vs.getSession();
    Query query = sess.getNamedQuery(FIND_USER_BY_USERNAME_STR);
    query.setString(0, username);
    return (User)query.uniqueResult();
  }
  
}
