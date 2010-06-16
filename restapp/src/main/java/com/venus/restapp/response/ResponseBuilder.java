package com.venus.restapp.response;

import com.venus.restapp.response.dto.BaseDTO;
import com.venus.restapp.response.dto.ErrorDTO;

import com.venus.model.BaseModel;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


public class ResponseBuilder {

  /**
   * Populate the response, given the list of objects of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for object
   * and, then setting that DTOs to the response.
   * @param objects     The List of model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of objects in the object
   * @return The {@link RestResponse} containing the objects details
   */
  public static RestResponse createResponse(List objects, Integer totalCount, BaseDTO dtoObj) {
    RestResponse resp = new RestResponse();
    ArrayList<BaseDTO> list = null;
    /* populate only if the list is not empty */
    if (objects != null && objects.size() > 0) {
      list = new ArrayList<BaseDTO>(objects.size());
      for (Object object :  objects) {
        BaseDTO dto = dtoObj.getDTO((BaseModel) object);
        if (dto != null) {
          list.add(dto);
        }
      }
    }
    /* if the list contains some elements, then set the response */
    if (list != null && list.size() > 0) {
      resp.setEntries(list);
      resp.setItemsPerPage(list.size());
      resp.setTotalResults(totalCount);
    }
    return resp;
  }

  /**
   * Populate the response, given the object model object(DB Mapped object)
   * This involves converting the model object into the equivalent DTO for object
   * and, then setting that DTO to the response.
   * @param object     The model object (Hibernate Mapped object)
   * @return The RestResponse containing the object details
   */
  public static RestResponse createResponse(BaseModel object, BaseDTO dtoObj) {
    RestResponse resp = new RestResponse();
    BaseDTO dto = dtoObj.getDTO((BaseModel)object);
    if (dto != null) {
      resp.setEntry(dto);
      resp.setTotalResults(1);
      resp.setItemsPerPage(1);
    }
    return resp;
  }
  
  public static BaseResponse createResponse(HttpStatus status, BindingResult result) {
    BaseResponse response = new BaseResponse();
    if (result.hasErrors()) {
      response.setError(true);
      response.setHttpErrorCode(status.value());
      /* create an array of total error count */
      ArrayList<ErrorDTO> errors = new ArrayList<ErrorDTO>(result.getErrorCount());
      /* append all field errors */
      for (FieldError err: result.getFieldErrors()) {
        System.out.println("I got field error for: " + err.getField() + ", message: " + err.getDefaultMessage() + ", code: " + err.getCode());
        ErrorDTO dto = new ErrorDTO(err.getField(), err.getCode(), err.getDefaultMessage());
        errors.add(dto);
      }
      /* append global errors now */
      for (ObjectError err: result.getGlobalErrors()) {
        System.out.println("I got global error for: " + err.getObjectName() + ", message: " + err.getDefaultMessage() + ", code: " + err.getCode());
        ErrorDTO dto = new ErrorDTO(err.getObjectName(), err.getCode(), err.getDefaultMessage());
        errors.add(dto);
      }
      response.setErrors(errors);
    }
    return response;
  }
  
}
