package com.venus.restapp.response;

import com.venus.restapp.response.dto.UserDTO;
import com.venus.model.User;

import java.util.List;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

/**
 * The default response object for the user requests
 * This contains all the details from base {@link BaseResponse response} and details
 * specific to user. If the response contains multiple objects, the response
 * object will contain 'entries' attribute with multiple user objects in it.
 * If the response contains single object, 'entry' attribute with single user details
 * will be present in the response object.
 *  
 * @author sigabort
 *
 */
/* XXX: we need to make sure 'entries' will be renamed to 'entry' */
/* JsonWriteNullProperties: annotation for sending attributes with null values */
@JsonWriteNullProperties(false)
public class UserResponse extends BaseResponse {
  
  private ArrayList<UserDTO> entries;
  private UserDTO entry;

  /**
   * Constructor with no arguments
   */
  public UserResponse() {
    super();
  }

  /**
   * set the entries with the details of list of users
   * @param entries   The list of {@link UserDTO user} objects
   */
  public void setEntries(ArrayList<UserDTO> entries) {
    this.entries = entries;
  }

  /**
   * Get the list of {@link UserDTO user} details
   * @return   List of {@link UserDTO user} details
   */
  public ArrayList<UserDTO> getEntries() {
    return this.entries;
  }
  
  /**
   * set the user details in the response 
   * @param entry   The user DTO {@link UserDTO object} containing the
   *                details about a single user
   */
  public void setEntry(UserDTO entry) {
    this.entry = entry;
  }
  
  /**
   * Get the user details from the response
   * @return   The {@link UserDTO user} details
   */
  public UserDTO getEntry() {
    return this.entry;
  }


  /**
   * Populate the User response, given the list of users of Model objects(Hibernate Mapped object)
   * This involves converting the model objects into the equivalent DTOs for user
   * and, then setting that DTOs to the response.
   * @param users     The List of User model objects (Hibernate Mapped objects)
   * @param totalCount      The total count of users in the institute
   * @return The {@link UserResponse} containing the users details
   */
  public static UserResponse populateUsers(List users, Integer totalCount) {
    UserResponse resp = new UserResponse();
    ArrayList<UserDTO> list = null;
    /* populate only if the list is not empty */
    if (users != null && users.size() > 0) {
      list = new ArrayList<UserDTO>(users.size());
      for (Object user :  users) {
        UserDTO dto = UserDTO.getUserDTO((User) user);
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
   * Populate the User response, given the user model object(DB Mapped object)
   * This involves converting the model object into the equivalent DTO for user
   * and, then setting that DTO to the response.
   * @param user     The User model object (Hibernate Mapped object)
   * @return The UserResponse containing the user details
   */
  public static UserResponse populateUser(Object user) {
    UserResponse resp = new UserResponse();
    UserDTO dto = UserDTO.getUserDTO((User) user);
    if (dto != null) {
      resp.setEntry(dto);
      resp.setTotalResults(1);
      resp.setItemsPerPage(1);
    }
    return resp;
  }
  
}