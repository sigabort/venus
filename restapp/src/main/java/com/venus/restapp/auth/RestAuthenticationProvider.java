package com.venus.restapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.venus.restapp.service.UserService;
import com.venus.restapp.service.UserRoleService;
import com.venus.model.UserRole;
import com.venus.model.Role;
import com.venus.model.User;

import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class RestAuthenticationProvider implements AuthenticationProvider, InitializingBean {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRoleService userRoleService;
  

  public RestAuthenticationProvider() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Not sure about this handler
   * 
   * @param authn    The authn object
   * @return
   */
  public boolean supports(Class authn) {
    return true;
  }

  private static final Logger log = Logger.getLogger(RestAuthenticationProvider.class);

  /**
   * Authenticate the user.
   * This function calls service layer to get the user details to validate. Once the user
   * is validated, then the roles are fetched and added to the authorities list
   * 
   * @param token    The authn object 
   * @return The authentication object with more details
   * @throws AuthenticationException thrown if the user is not authenticated
   */
  public Authentication authenticate(Authentication token) throws AuthenticationException {
    
    UsernamePasswordAuthenticationToken upToken;
    String username = (String)token.getPrincipal();
    String password = (String)token.getCredentials();

    log.info("Got username: " + username + ", password: " + password);

    /* See if the username is passed or not */
    if ((username == null) || ("".equals(username)) || ("".equals(username.trim()))) {
      throw new BadCredentialsException("UserName is not supplied or is empty");
    }
    User user = null;
    try {
      user = userService.getUser(username, null);
    }
    catch (Exception e) {
      log.error("Internal error", e);
      return null;
    }
    if (user == null) {
      log.info("username/password not valid");
      throw new BadCredentialsException("User with username: " + username + " not found");
    }
    if (!password.equals(user.getPassword())) {
      throw new BadCredentialsException("Username/password not correct");      
    }
    
    List<GrantedAuthority> gaList = getAuthorities(user);
    /*
     * The gaList can be empty/null because: the user exists in the Database. 
     * But, he doesn't have any roles.
     * Right now, lets add a default role : ROLE_USER. This allows user to
     * access some information. We have to think about this later.
     */
    gaList = addDefaultRoleToUser(gaList);

    /* Add the roles to the token */
    upToken = new UsernamePasswordAuthenticationToken(username, token.getCredentials(), (GrantedAuthority[]) gaList.toArray(new GrantedAuthority[gaList.size()]));
    upToken.setDetails(token.getDetails());

    log.info("RestAuthenticationProvider: returning token for: " + username);
    
    return upToken;
  }

  
  /**
   * Get the User's roles from DB and use them to map to the authorities
   * 
   * @param user    The user model object for finding the roles
   * @return        The list of granted authorities
   */
  private List<GrantedAuthority> getAuthorities(User user) {
    List<UserRole> roles = null;
    List<GrantedAuthority> gaList = null;
    
    try {
      roles = userRoleService.getUserRoles(user, null);
    }
    catch (Exception e) {
      log.error("Internal error", e);
      return null;      
    }
    
    /* see if the user has any roles yet or not */
    if (roles != null && roles.size() > 0) {
      gaList = new ArrayList<GrantedAuthority>();
    }
    else {
      log.error("User: " + user.getUsername() + " doesn't have any roles yet!");
      return null;
    }
    /* add up all the roles to the list */
    for (UserRole userRole: roles) {
      Role role = Role.values()[userRole.getRole()];
      GrantedAuthority ga = new GrantedAuthorityImpl(RestRole.getRoleWithPrefix(role));
      gaList.add(ga);
    }
    return gaList;
  }

  /**
   * Add default role ('ROLE_USER') to the existing user
   * 
   * @param gaList    The existing list of granted authorities 
   * @return          The list of granted authorities with added default role(s)
   */
  private List<GrantedAuthority> addDefaultRoleToUser(List<GrantedAuthority> existing) {
    if (existing == null) {
      existing = new ArrayList<GrantedAuthority>(1);
    }

    /* add default role to all logged in users */
    GrantedAuthority ga = new GrantedAuthorityImpl(RestRole.getRoleWithPrefix(RestRole.USER));
    existing.add(ga);

    return existing;
  }

  /**
   * This API will be called after all of the spring security properties are set
   * @throws Exception thrown if there is any error
   */
  public final void afterPropertiesSet() throws Exception {}
  
}
  