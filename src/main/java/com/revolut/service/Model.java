package com.revolut.service;

import com.revolut.dto.Currency;
import com.revolut.dto.ResponseMessage;
import com.revolut.entity.Account;
import com.revolut.entity.User;

import java.util.Set;

/**
 * Main model.
 */
public interface Model {
  /**
   * Create new account.
   *
   * @param currency currency
   * @param user     use
   * @return Account
   */
  Account createAccount(Currency currency, User user);

  /**
   * Create new account.
   *
   * @param data Json with request entity
   * @return Account
   */
  ResponseMessage createAccount(String data);

  /**
   * Create new User without handler.
   *
   * @param data Json with entity about new user
   * @return ResponseMessage
   */
  ResponseMessage createUser(String data);

  /**
   * Get All user Account.
   *
   * @param userId userId
   * @return Set<Account>
   */
  Set<Account> getAccountsByUser(Integer userId);

  /**
   * Get All user Account.
   *
   * @param data - Json request
   * @return ResponseMessage
   */
  ResponseMessage getAccountsByUser(String data);

  /**
   * Transfer money from one account to another.
   *
   * @param data json request
   * @return ResponseMessage
   */
  ResponseMessage transferMoney(String data);

  /**
   * Withdraw money from account.
   *
   * @param data json request
   * @return ResponseMessage
   */
  ResponseMessage withdraw(String data);

  /**
   * Deposit money to account.
   *
   * @param data - json request
   * @return ResponseMessage
   */
  ResponseMessage deposit(String data);

  /**
   * Get information about account.
   *
   * @param data - Json request
   * @return ResponseMessage account information
   */
  ResponseMessage getAccount(String data);
}
