package com.venus.restapp.response;

import com.venus.restapp.response.dto.InstituteDTO;
import com.venus.model.Institute;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * The default response object for the institute requests
 * This contains all the details from base {@link BaseResponse response} and details
 * specific to institute. If the response contains multiple objects, the response
 * object will contain 'entries' attribute with multiple institute objects in it.
 * If the response contains single object, 'entry' attribute with single institute details
 * will be present in the response object.
 *  
 * @author sigabort
 *
 */
/* XXX: we need to make sure 'entries' will be renamed to 'entry' */
/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class InstituteResponse extends BaseResponse {
  
  private ArrayList<InstituteDTO> entries;
  private InstituteDTO entry;

  /**
   * Constructor with no arguments
   */
  public InstituteResponse() {
    super();
  }

  /**
   * set the entries with the details of list of institutes
   * @param entries   The list of {@link InstituteDTO institute} objects
   */
  public void setEntries(ArrayList<InstituteDTO> entries) {
    this.entries = entries;
  }

  /**
   * Get the list of {@link InstituteDTO institute} details
   * @return   List of {@link InstituteDTO institute} details
   */
  public ArrayList<InstituteDTO> getEntries() {
    return this.entries;
  }
  
  /**
   * set the institute details in the response 
   * @param entry   The institute DTO {@link InstituteDTO object} containing the
   *                details about a single institute
   */
  public void setEntry(InstituteDTO entry) {
    this.entry = entry;
  }
  
  /**
   * Get the institute details from the response
   * @return   The {@link InstituteDTO institute} details
   */
  public InstituteDTO getEntry() {
    return this.entry;
  }


  /**
   * Populate the Institute response, given the list of institutes of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for institute
   * and, then setting that DTOs to the response.
   * @param institutes     The List of Institute model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of institutes in the institute
   * @return The {@link InstituteResponse} containing the institutes details
   */
  public static InstituteResponse populateInstitutes(List institutes, Integer totalCount) {
    InstituteResponse resp = new InstituteResponse();
    ArrayList<InstituteDTO> list = null;
    /* populate only if the list is not empty */
    if (institutes != null && institutes.size() > 0) {
      list = new ArrayList<InstituteDTO>(institutes.size());
      for (Object institute :  institutes) {
        InstituteDTO dto = InstituteDTO.getInstituteDTO((Institute) institute);
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
   * Populate the Institute response, given the institute model object(DB Mapped object)
   * This involves converting the model object into the equivalent DTO for institute
   * and, then setting that DTO to the response.
   * @param institute     The Institute model object (Hibernate Mapped object)
   * @return The InstituteResponse containing the institute details
   */
  public static InstituteResponse populateInstitute(Object institute) {
    InstituteResponse resp = new InstituteResponse();
    InstituteDTO dto = InstituteDTO.getInstituteDTO((Institute) institute);
    if (dto != null) {
      resp.setEntry(dto);
      resp.setTotalResults(1);
      resp.setItemsPerPage(1);
    }
    return resp;
  }
  
}
