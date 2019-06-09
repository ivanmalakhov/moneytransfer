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
   * User FirstName.
   */
  private String firstName;
  /**
   * User LastName.
   */
  private String lastName;
}
