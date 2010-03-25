package com.venus.restapp.response;

import com.venus.restapp.response.dto.DepartmentDTO;
import com.venus.model.Department;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * The default response object for the department requests
 * This contains all the details from base {@link BaseResponse response} and details
 * specific to department. If the response contains multiple objects, the response
 * object will contain 'entries' attribute with multiple department objects in it.
 * If the response contains single object, 'entry' attribute with single department details
 * will be present in the response object.
 *  
 * @author sigabort
 *
 */
/* XXX: we need to make sure 'entries' will be renamed to 'entry' */
/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class DepartmentResponse extends BaseResponse {
  
  private ArrayList<DepartmentDTO> entries;
  private DepartmentDTO entry;

  /**
   * Constructor with no arguments
   */
  public DepartmentResponse() {
    super();
  }

  /**
   * set the entries with the details of list of departments
   * @param entries   The list of {@link DepartmentDTO department} objects
   */
  public void setEntries(ArrayList<DepartmentDTO> entries) {
    this.entries = entries;
  }

  /**
   * Get the list of {@link DepartmentDTO department} details
   * @return   List of {@link DepartmentDTO department} details
   */
  public ArrayList<DepartmentDTO> getEntries() {
    return this.entries;
  }
  
  /**
   * set the department details in the response 
   * @param entry   The department DTO {@link DepartmentDTO object} containing the
   *                details about a single department
   */
  public void setEntry(DepartmentDTO entry) {
    this.entry = entry;
  }
  
  /**
   * Get the department details from the response
   * @return   The {@link DepartmentDTO department} details
   */
  public DepartmentDTO getEntry() {
    return this.entry;
  }


  /**
   * Populate the Department response, given the list of departments of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for department
   * and, then setting that DTOs to the response.
   * @param departments     The List of Department model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of departments in the institute
   * @return The {@link DepartmentResponse} containing the departments details
   */
  public static DepartmentResponse populateDepartments(List departments, Integer totalCount) {
    DepartmentResponse resp = new DepartmentResponse();
    ArrayList<DepartmentDTO> list = null;
    /* populate only if the list is not empty */
    if (departments != null && departments.size() > 0) {
      list = new ArrayList<DepartmentDTO>(departments.size());
      for (Object dept :  departments) {
	DepartmentDTO dto = DepartmentDTO.getDepartmentDTO((Department) dept);
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
   * Populate the Department response, given the department model object(DB Mapped object)
   * This involves converting the model object into the equivalent DTO for department
   * and, then setting that DTO to the response.
   * @param department     The Department model object (Hibernate Mapped object)
   * @return The DepartmentResponse containing the department details
   */
  public static DepartmentResponse populateDepartment(Object department) {
    DepartmentResponse resp = new DepartmentResponse();
    DepartmentDTO dto = DepartmentDTO.getDepartmentDTO((Department) department);
    if (dto != null) {
      resp.setEntry(dto);
      resp.setTotalResults(1);
      resp.setItemsPerPage(1);
    }
    return resp;
  }
  
}