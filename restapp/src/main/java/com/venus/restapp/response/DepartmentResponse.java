package com.venus.restapp.response;

import com.venus.restapp.response.dto.DepartmentDTO;
import com.venus.model.Department;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class DepartmentResponse extends BaseResponse {
  
  private ArrayList<DepartmentDTO> entries;
  private DepartmentDTO entry;

  public DepartmentResponse() {
    super();
  }
  
  public void setEntries(ArrayList<DepartmentDTO> entries) {
    this.entries = entries;
  }
  
  public ArrayList<DepartmentDTO> getEntries() {
    return this.entries;
  }
  
  public void setEntry(DepartmentDTO entry) {
    this.entry = entry;
  }
  
  public DepartmentDTO getEntry() {
    return this.entry;
  }


  /**
   * Populate the Department response, given the list of departments of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for department
   * and, then setting that DTOs to the response.
   * @param departments     The List of Department model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of departments in the institute
   * @return The DepartmentResponse containing the departments details
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