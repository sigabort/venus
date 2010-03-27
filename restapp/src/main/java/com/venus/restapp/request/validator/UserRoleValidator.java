package com.venus.restapp.request.validator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.venus.restapp.request.UserRoleRequest;
import com.venus.model.Role;

public class UserRoleValidator implements Validator {

  public boolean supports(Class clazz) {
    return UserRoleRequest.class.isAssignableFrom(clazz);
  }
  
  public void validate(Object object, Errors errors) {
    UserRoleRequest urr = (UserRoleRequest) object;
    /* see if the role is provided */
    String username = urr.getUsername();
    if (StringUtils.isEmpty(username)) {
      errors.rejectValue("username", "field.min.length", new Object[] {new Integer(1)}, "Username must be between 1 and 128 chars");
    }
    String[] role = urr.getRole();
    if (ArrayUtils.isEmpty(role)) {
//      System.out.println("[validator]--------Role is not found......");
      errors.rejectValue("role", "400", "No role is supplied");
    }
    for (int idx = 0; idx < role.length; idx++) {
//      System.out.println("[validator]I got user role: " + role[idx]);
      if (StringUtils.isEmpty(role[idx]) || role[idx].length() <= 0 || role[idx].length() > 20) {
//        System.out.println("--------Role length is wrong......");
        errors.rejectValue("role", "400", "role must be between 1 and 20 chars");
      }
      Role r = null;
      try {
        r = Role.valueOf(role[idx].toUpperCase());
      }
      catch(Exception e) {
//        System.out.println("[validator]--------Wrong role provided......");
        errors.rejectValue("role", "400", "Invalid Role value: " + role[idx]);
      }
    }
  }
}