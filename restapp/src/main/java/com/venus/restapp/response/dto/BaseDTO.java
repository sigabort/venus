package com.venus.restapp.response.dto;


import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.venus.model.BaseModel;

/**
 * Every DTO class should implement this interface
 *
 * @author sigabort
 *
 */
/* make sure we don't send the null values */
@JsonWriteNullProperties(false)
public interface BaseDTO {
  
  /**
   * Get the {@link BaseDTO} object from the {@link BaseModel}
   * model object (object from the DB).
   * @param object      The model object 
   * @return The reponse {@link BaseDTO object} built using the model object
   */
  public abstract BaseDTO getDTO(BaseModel object);
  
}
 
