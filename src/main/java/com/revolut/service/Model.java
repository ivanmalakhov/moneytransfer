package com.revolut.service;

import com.revolut.dto.ResponseMessage;

/**
 * Main model.
 */
public interface Model {
  /**
   * Create new account.
   *
   * @param user user
   * @param data Json with request entity
   * @return Account
   */
  ResponseMessage createAccount(String user, String data);

  /**
   * Create new User without handler.
   *
   * @param data Json with entity about new user
   * @return ResponseMessage
   */
  ResponseMessage createUser(String data);

  /**
   * Update user information.
   *
   * @param user - id user
   * @param data Json with entity about new user
   * @return ResponseMessage
   */
  ResponseMessage updateUser(String user, String data);

  /**
   * Get User.
   *
   * @param user - id user
   * @param data - request
   * @return - ResponseMessage
   */
  ResponseMessage getUser(String user, String data);

  /**
   * Get all users.
   *
   * @param data Json with entity about new user
   * @return ResponseMessage
   */
  ResponseMessage getUsers(String data);

  /**
   * Get All user Account.
   *
   * @param user - user id
   * @param data - Json request
   * @return ResponseMessage
   */
  ResponseMessage getAccountsByUser(String user, String data);

  /**
   * Transfer money from one account to another.
   *
   * @param user user id
   * @param data json request
   * @return ResponseMessage
   */
  ResponseMessage transferMoney(String user, String data);

  /**
   * Withdraw money from account.
   *
   * @param user user id
   * @param data json request
   * @return ResponseMessage
   */
  ResponseMessage withdraw(String user, String data);

  /**
   * Deposit money to account.
   *
   * @param user user id
   * @param data - json request
   * @return ResponseMessage
   */
  ResponseMessage deposit(String user, String data);

  /**
   * Get information about account.
   *
   * @param user user id
   * @param data - Json request
   * @return ResponseMessage account information
   */
  ResponseMessage getAccount(String user, String data);
}
