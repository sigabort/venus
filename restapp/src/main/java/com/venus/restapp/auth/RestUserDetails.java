package com.venus.restapp.auth;

import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom {@link User} object to store information about the logged in user and the
 * institute he belongs to. 
 * @author sigabort
 *
 */
public class RestUserDetails extends User {
  private String institute;
  
  public RestUserDetails(String username, String password, String institute, boolean enabled, Collection<GrantedAuthority> authorities) {
    /* right now, use 'enabled' for all other account expiry related values */
    super(username, password, enabled, enabled, enabled, enabled, authorities);
    /* set the institute Id to which this user belongs to */
    this.institute = institute;
  }
  
  public String getInstitute() {
    return this.institute;
  }
  
  /**
   * Build the {@link RestUserDetails} object from {@link com.venus.model.User} object
   * 
   * @param user             The {@link com.venus.model.User} object
   * @param enabled          Is the account enabled or not
   * @param authorities      The list of granted authorities for this user
   */
  public static RestUserDetails getRestUserDetails(com.venus.model.User user, boolean enabled, Collection<GrantedAuthority> authorities) {
    return new RestUserDetails(user.getUsername(), user.getPassword(), user.getInstitute().getName(), enabled, authorities);
  }
  
}