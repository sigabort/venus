package com.venus.restapp.auth;

import com.venus.model.Role;

/**
 * The different roles of a user
 */
public enum RestRole {
    ADMIN,
    PRINCIPAL,
    HEADOFDEPARTMENT,
    INSTRUCTOR,
    STAFF,
    STUDENT,
    /**
     * This is added separately for REST Layer. This will be used as default
     * role for all users who login succesfully.
     */
    USER;
    
    public static String getRoleWithPrefix(Role role) {
      return "ROLE_" + role.toString();
    }

    public static String getRoleWithPrefix(RestRole role) {
      return "ROLE_" + role.toString();
    }
}
