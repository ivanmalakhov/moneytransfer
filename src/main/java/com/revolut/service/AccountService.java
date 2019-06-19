package com.revolut.service;

import com.revolut.dto.Currency;
import com.revolut.entity.Account;
import com.revolut.entity.User;

import java.util.Set;

/**
 * Service to work with accounts.
 */
public interface AccountService {
  /**
   * Create account.
   *
   * @param currency - currency
   * @param user     - user
   * @return Account
   */
  Account create(Currency currency, User user);

  /**
   * Get all user accounts.
   *
   * @param user User
   * @return accounts set
   */
  Set<Account> getAccountListByUser(User user);

  /**
   * Get account by number.
   *
   * @param accountNumber - account number
   * @param user          - User
   * @return Account
   */
  Account getAccountById(String accountNumber, User user);
}
