package com.venus.dal.impl;

import java.util.Map;
import java.util.Date;

import com.venus.model.Status;
import com.venus.model.Department;

import org.apache.log4j.Logger;

/**
 * Utility class for the dal operations impl layer
 * 
 * @author sigabort
 */
public class OperationsUtilImpl {

  /**
   * Get the string value of the parameter from the map
   * @param name      The name of the parameter
   * @param params    The map of the parameters to retrieve the parameter from
   * @param def       The default value, if the param is not found in the map
   * @return          The value from the list for the param, default otherwise
   */
  public static String getStringValue(String name, Map<String, Object> params, String def) {
    if (params != null) {
      String value = (String) params.get(name);
      if (value != null) {
	return value;
      }
    }
    return def;
  }
  
  /**
   * Get the status value from the map
   * @param name      The name of the status parameter
   * @param params    The map of the parameters to retrieve the parameter from
   * @param def       The default value, if the param is not found in the map
   * @return          The value from the list for the param, default otherwise
   */
  public static Status getStatus(String name, Map<String, Object> params, Status def) {
    if (params != null) {
      Status value = (Status) params.get(name);
      if (value != null) {
	return value;
      }
    }
    return def;    
  }

  /**
   * Get the date value from the map
   * @param name      The name of the status parameter
   * @param params    The map of the parameters to retrieve the parameter from
   * @param def       The default value, if the param is not found in the map
   * @return          The value from the list for the param, default otherwise
   */
  public static Date getDate(String name, Map<String, Object> params, Date def) {
    if (params != null) {
      Date value = (Date) params.get(name);
      if (value != null) {
	return value;
      }
    }
    return def;    
  }

  /**
   * Get the boolean value from the map
   * @param name      The name of the status parameter
   * @param params    The map of the parameters to retrieve the parameter from
   * @param def       The default value, if the param is not found in the map
   * @return          The value from the list for the param, default otherwise
   */
  public static Boolean getBoolean(String name, Map<String, Object> params, Boolean def) {
    if (params != null) {
      Boolean value = (Boolean) params.get(name);
      if (value != null) {
	return value;
      }
    }
    return def;    
  }
  
  /**
   * Get the Department value from the map
   * @param name      The name of the department parameter
   * @param params    The map of the parameters to retrieve the parameter from
   * @param def       The default value, if the param is not found in the map
   * @return          The value from the list for the param, default otherwise
   */
  public static Department getDepartment(String name, Map<String, Object> params, Department def) {
    if (params != null) {
      Department value = (Department) params.get(name);
      if (value != null) {
        return value;
      }
    }
    return def;    
  }

}
