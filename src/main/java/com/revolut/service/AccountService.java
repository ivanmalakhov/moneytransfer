package com.revolut.service;

import com.revolut.data.Account;
import com.revolut.data.User;
import com.revolut.dto.Currency;

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
   * @param userId - user id
   * @return accounts set
   */
  Set<Account> getAccountListByUser(Integer userId);

  /**
   * Get account by number.
   *
   * @param userId        - user id
   * @param accountNumber - account number
   * @return Account
   */
  Account getAccountById(Integer userId, String accountNumber);
}
