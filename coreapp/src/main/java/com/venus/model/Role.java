/**
 * @file Contains the roles
 */
package com.venus.model;

/**
 * The roles which will be used by {@link UserRole}
 * 
 * @author sigabort
 */
public enum Role {
  /**
   * Admin of the Department/Institute
   */
  ADMIN, 
  /**
   * Principal of the Institute
   */
  PRINCIPAL, 
  /**
   * Head of Department for a specific(one or more) departments
   */
  HEADOFDEPARTMENT, 
  /**
   * Instructor in (one ore more) departments
   */
  INSTRUCTOR, 
  /**
   * Staff for institute (or specific to department)
   */
  STAFF, 
  /**
   * Student of institute, specific to a department
   */
  STUDENT;


  /**
   * Check for the specific role, department is needed or not
   * @return true if the department is needed for the given role, false otherwise
   */
  public Boolean isDepartmentRequired() {
    switch(this) {
    case ADMIN:
      return Boolean.FALSE;
    case PRINCIPAL:
      return Boolean.FALSE;
    case HEADOFDEPARTMENT:
      return Boolean.TRUE;
    case INSTRUCTOR:
      return Boolean.TRUE;
    case STAFF:
      return Boolean.FALSE;
    case STUDENT:
      return Boolean.TRUE;
    }
    return Boolean.TRUE;
  }
}
