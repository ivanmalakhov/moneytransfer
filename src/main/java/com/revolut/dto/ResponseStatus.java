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
  USER_CREATE_ERROR(530, "New user cannot be created"),
  /**
   * New account cannot be created.
   */
  ACCOUNT_CREATE_ERROR(531, "New user cannot be created"),
  /**
   * User doesn't exist.
   */
  USER_DOES_NOT_EXIST(532, "User does not exist"),
  /**
   * User does not have accounts.
   */
  ACCOUNT_DOES_NOT_EXIST(533, "User does not have accounts"),
  /**
   * Empty destination account ID.
   */
  EMPTY_DEST_ACC_ID(534, "Empty destination account ID"),
  /**
   * Empty source account ID.
   */
  EMPTY_SRC_ACC_ID(535, "Empty source account ID"),
  /**
   * Destination account doesn't exists.
   */
  DEST_ACC_DOES_NOT_EXISTS(537, "Destination account doesn't exists"),
  /**
   * Source account doesn't exists.
   */
  SRC_ACC_DOES_NOT_EXISTS(538, "Source account doesn't exists"),
  /**
   * Not enough money for a transaction.
   */
  NOT_ENOUGH_MONEY_FOR_A_TRANSACTION(539, "Not enough money for a transaction"),
  /**
   * New client cannot be created.
   */
  USER_UPDATE_ERROR(540, "User cannot be updated");

  /**
   * Int Code.
   */
  private final int code;
  /**
   * Status description.
   */
  private final String description;

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
