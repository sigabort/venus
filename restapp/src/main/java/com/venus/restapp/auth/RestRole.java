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
    STUDENT;
    
    public static String getRoleWithPrefix(Role role) {
      return "ROLE_" + role.toString();
    }
}
