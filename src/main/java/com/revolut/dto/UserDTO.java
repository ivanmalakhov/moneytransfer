package com.revolut.dto;

import lombok.Data;

/**
 * Data transfer object with user information.
 */
@Data
public class UserDTO {
  /**
   * User id.
   */
  private Integer id;
  /**
   * User first name.
   */
  private String firstName;
  /**
   * User last Name.
   */
  private String lastName;
}
