package com.venus.controller.auth;

/**
 * The different roles of a user
 */
public enum Role {
    SuperUser,
    Admin,
    Moderator,
    Member,
    Guest,
    Anonymous;
    
    public static String getRoleWithPrefix(String role) {
      if ("SuperUser".equals(role) || "Admin".equals(role) || "Moderator".equals(role))
	return "ROLE_ADMIN";
      else if ("Member".equals(role)) {
	return "ROLE_USER";
      }
      else {
	return "ROLE_ANONYMOUS";
      }
    }
}
