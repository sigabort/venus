package com.venus.dal.impl;

import java.util.Date;
import java.util.List;

import com.venus.model.User;
import com.venus.model.Status;
import com.venus.model.impl.UserImpl;
import com.venus.util.VenusSession;
import com.venus.dal.UserOperations;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

public class UserOperationsImpl implements UserOperations {
  private String FIND_USER_BY_USERNAME_STR = "findUserByUsername";
  
  public User createUpdateUser(String username, String userId, String password, String firstName, String lastName, String email, String gender, String url, String phone, String address1, String address2, String city, String country, String postalCode, String photoUrl, Date birthDate, Date joinDate, Date created, Date lastModified, VenusSession session) {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    User user = findUserByUsername(username, session);
    if (user != null) {
      user = updateUser(user, userId, password, firstName, lastName, email, gender, url, phone, address1, address2, city, country, postalCode, photoUrl, birthDate, joinDate, Status.Active, session);
    }
    else {
      user = createUser(username, userId, password, firstName, lastName, email, gender, url, phone, address1, address2, city, country, postalCode, photoUrl, birthDate, joinDate, created, lastModified, session);
    }
    return user;
  }

  private User createUser(String username, String userId, String password, String firstName, String lastName, String email, String gender, String url, String phone, String address1, String address2, String city, String country, String postalCode, String photoUrl, Date birthDate, Date joinDate, Date created, Date lastModified, VenusSession session) {
    if (username == null) {
      throw new IllegalArgumentException("Username must be supplied");
    }
    User user = new UserImpl();
    if (username != null) {
      user.setUsername(username);
    }

    if (userId != null) {
      user.setUserId(userId);
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

    if (gender != null) {
      user.setGender(gender);
    }

    if (url != null) {
      user.setUrl(url);
    }

    if (phone != null) {
      user.setPhone(phone);
    }

    if (address1 != null) {
      user.setAddress1(address1);
    }

    if (address2 != null) {
      user.setAddress2(address2);
    }

    if (city != null) {
      user.setCity(city);
    }

    if (country != null) {
      user.setCountry(country);
    }

    if (postalCode != null) {
      user.setPostalCode(postalCode);
    }

    if (photoUrl != null) {
      user.setPhotoUrl(photoUrl);
    }

    if (birthDate != null) {
      user.setBirthDate(birthDate);
    }

    if (joinDate != null) {
      user.setJoinDate(joinDate);
    }

    user.setStatus(Status.Active.ordinal());
    user.setCreated((created != null)? created : new Date());
    user.setLastModified((lastModified != null)? lastModified : new Date());

    Session sess = session.getSession();
    sess.save(user);
    return user;
  }

  private User updateUser(User user, String userId, String password, String firstName, String lastName, String email, String gender, String url, String phone, String address1, String address2, String city, String country, String postalCode, String photoUrl, Date birthDate, Date joinDate, Status status, VenusSession session) {
    if (user == null) {
      return null;
    }
    boolean update = false;

    String oldUserId = user.getUserId();
    if (oldUserId == null || !oldUserId.equals(userId)) {
      user.setUserId(userId);
      update = true;
    }

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

    String oldGender = user.getGender();
    if (oldGender == null || !oldGender.equals(gender)) {
      user.setGender(gender);
      update = true;
    }

    String oldUrl = user.getUrl();
    if (oldUrl == null || !oldUrl.equals(url)) {
      user.setUrl(url);
      update = true;
    }

    String oldPhone = user.getPhone();
    if (oldPhone == null || !oldPhone.equals(phone)) {
      user.setPhone(phone);
      update = true;
    }

    String oldAddress1 = user.getAddress1();
    if (oldAddress1 == null || !oldAddress1.equals(address1)) {
      user.setAddress1(address1);
      update = true;
    }

    String oldAddress2 = user.getAddress2();
    if (oldAddress2 == null || !oldAddress2.equals(address2)) {
      user.setAddress2(address2);
      update = true;
    }

    String oldCity = user.getCity();
    if (oldCity == null || !oldCity.equals(city)) {
      user.setCity(city);
      update = true;
    }

    String oldCountry = user.getCountry();
    if (oldCountry == null || !oldCountry.equals(country)) {
      user.setCountry(country);
      update = true;
    }

    String oldPostalCode = user.getPostalCode();
    if (oldPostalCode == null || !oldPostalCode.equals(postalCode)) {
      user.setPostalCode(postalCode);
      update = true;
    }

    String oldPhotoUrl = user.getPhotoUrl();
    if (oldPhotoUrl == null || !oldPhotoUrl.equals(photoUrl)) {
      user.setPhotoUrl(photoUrl);
      update = true;
    }

    Date oldBirthDate = user.getBirthDate();
    if (oldBirthDate == null || !oldBirthDate.equals(birthDate)) {
      user.setBirthDate(birthDate);
      update = true;
    }

    Date oldJoinDate = user.getJoinDate();
    if (oldJoinDate == null || !oldJoinDate.equals(joinDate)) {
      user.setJoinDate(joinDate);
      update = true;
    }

    Integer oldStatus = user.getStatus();
    if (oldStatus == null || !oldStatus.equals(status.ordinal())) {
      user.setStatus(status.ordinal());
      update = true;
    }

    if (update) {
      user.setLastModified(new Date());
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

  public List<User> getUsers(int offset, int maxRet, VenusSession vs) {
    Session sess = vs.getSession();
    Query query = sess.createQuery("from UserImpl");
    query.setFirstResult(offset);
    query.setMaxResults(maxRet);
    return query.list();
  }
  

}
