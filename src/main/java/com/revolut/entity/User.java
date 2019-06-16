package com.revolut.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.security.SecureRandom;

/**
 * Class with information about user.
 */
@Data
public class User {
  /**
   * User ID.
   */
  private Integer id;
  /**
   * User first Name.
   */
  private String firstName;
  /**
   * User second Name.
   */
  private String lastName;

  /**
   * Custom constructor.
   *
   * @param userFirstName - user first name
   * @param userLastName  - user last name
   */
  @JsonCreator
  public User(final @JsonProperty("firstName") String userFirstName,
              final @JsonProperty("lastName") String userLastName) {
    SecureRandom rand = new SecureRandom();
    this.id = rand.nextInt(Integer.MAX_VALUE);
    this.firstName = userFirstName;
    this.lastName = userLastName;
  }
}
