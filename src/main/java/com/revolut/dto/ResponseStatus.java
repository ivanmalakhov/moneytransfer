package com.revolut.dto;

/**
 * Enumeration with Status code and description.
 *
 * @author ivanmalakhov
 */
public enum ResponseStatus {
  /**
   * Success status.
   */
  SUCCESS(201, "Success"),
  /**
   * Invalid request.
   */
  BAD_REQUEST(400, "Invalid request value"),
  /**
   * New client cannot be created.
   */
  USER_CREATE_ERROR(530, "New client cannot be created");

  /**
   * Int Code.
   */
  private int code;
  /**
   * Status description.
   */
  private String description;

  /**
   * Constructor.
   *
   * @param statusCode        int code
   * @param statusDescription Status description
   */
  ResponseStatus(final int statusCode,
                 final String statusDescription) {
    this.code = statusCode;
    this.description = statusDescription;
  }

  /**
   * Getter for code.
   *
   * @return int value with code
   */
  public int getCode() {
    return this.code;
  }

  /**
   * Getter for description.
   *
   * @return String description
   */
  public String getDescription() {
    return this.description;
  }
}
