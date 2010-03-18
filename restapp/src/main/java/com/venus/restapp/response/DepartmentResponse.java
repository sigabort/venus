package com.venus.restapp.response;

import com.venus.restapp.response.dto.DepartmentDTO;
import com.venus.model.Department;

import java.util.List;
import java.util.ArrayList;

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


  public static DepartmentResponse populateDepartments(List departments) {
    DepartmentResponse resp = new DepartmentResponse();
    ArrayList<DepartmentDTO> list = null;
    if (departments != null && departments.size() > 0) {
      list = new ArrayList<DepartmentDTO>(departments.size());
      for (Object dept :  departments) {
	DepartmentDTO dto = DepartmentDTO.getDepartmentDTO((Department) dept);
	if (dto != null) {
	  list.add(dto);
	}
      }
    }
    if (list != null) {
      resp.setEntries(list);
      resp.setItemsPerPage(list.size());
      /* XXX: get the total count and set */
//       respt.setTotalResults(totalCount);
    }
    return resp;
  }

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