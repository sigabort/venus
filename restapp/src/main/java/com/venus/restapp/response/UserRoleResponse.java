package com.venus.restapp.response;

import com.venus.restapp.response.dto.UserRoleDTO;
import com.venus.model.UserRole;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * The default response object for the userRole requests
 * This contains all the details from base {@link BaseResponse response} and details
 * specific to userRole. If the response contains multiple objects, the response
 * object will contain 'entries' attribute with multiple userRole objects in it.
 * If the response contains single object, 'entry' attribute with single userRole details
 * will be present in the response object.
 *  
 * @author sigabort
 *
 */
/* XXX: we need to make sure 'entries' will be renamed to 'entry' */
/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class UserRoleResponse extends BaseResponse {
  
  @XmlElement(name="entry")
  private ArrayList<UserRoleDTO> entries;
  @XmlElement(name="entry")
  private UserRoleDTO entry;

  /**
   * Constructor with no arguments
   */
  public UserRoleResponse() {
    super();
  }

  /**
   * set the entries with the details of list of userRoles
   * @param entries   The list of {@link UserRoleDTO userRole} objects
   */
  public void setEntries(ArrayList<UserRoleDTO> entries) {
    this.entries = entries;
  }

  /**
   * Get the list of {@link UserRoleDTO userRole} details
   * @return   List of {@link UserRoleDTO userRole} details
   */
  public ArrayList<UserRoleDTO> getEntries() {
    return this.entries;
  }
  
  /**
   * set the userRole details in the response 
   * @param entry   The userRole DTO {@link UserRoleDTO object} containing the
   *                details about a single userRole
   */
  public void setEntry(UserRoleDTO entry) {
    this.entry = entry;
  }
  
  /**
   * Get the userRole details from the response
   * @return   The {@link UserRoleDTO userRole} details
   */
  public UserRoleDTO getEntry() {
    return this.entry;
  }


  /**
   * Populate the UserRole response, given the list of userRoles of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for userRole
   * and, then setting that DTOs to the response.
   * @param userRoles     The List of UserRole model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of userRoles for this user in the institue
   * @return The {@link UserRoleResponse} containing the userRoles details
   */
  public static UserRoleResponse populateUserRoles(List userRoles, Integer totalCount) {
    UserRoleResponse resp = new UserRoleResponse();
    ArrayList<UserRoleDTO> list = null;
    /* populate only if the list is not empty */
    if (userRoles != null && userRoles.size() > 0) {
      list = new ArrayList<UserRoleDTO>(userRoles.size());
      for (Object userRole :  userRoles) {
        UserRoleDTO dto = UserRoleDTO.getUserRoleDTO((UserRole) userRole);
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
   * Populate the UserRole response, given the userRole model object(DB Mapped object)
   * This involves converting the model object into the equivalent DTO for userRole
   * and, then setting that DTO to the response.
   * @param userRole     The UserRole model object (Hibernate Mapped object)
   * @return The UserRoleResponse containing the userRole details
   */
  public static UserRoleResponse populateUserRole(Object userRole) {
    UserRoleResponse resp = new UserRoleResponse();
    UserRoleDTO dto = UserRoleDTO.getUserRoleDTO((UserRole) userRole);
    if (dto != null) {
      resp.setEntry(dto);
      resp.setTotalResults(1);
      resp.setItemsPerPage(1);
    }
    return resp;
  }
  
}