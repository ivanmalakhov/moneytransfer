package com.revolut.service;

import com.revolut.dto.PaymentRequest;
import com.revolut.dto.Account;
import com.revolut.dto.Currency;
import com.revolut.dto.Payment;
import com.revolut.dto.User;

import java.math.BigDecimal;
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
   * Create new User.
   *
   * @param firstName firstName
   * @param lastName lastName
   * @return User
   */
  User createUser(String firstName, String lastName);

  /**
   * Get All user Account.
   *
   * @param userId userId
   * @return Set<Account>
   */
  Set<Account> getAccountListByUser(Integer userId);

  /**
   * Transfer money from one account to another.
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment transferMoney(PaymentRequest paymentRequest);

  /**
   * Withdraw money from account.
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment withdraw(PaymentRequest paymentRequest);

  /**
   * Deposit money to account.
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment deposit(PaymentRequest paymentRequest);

  /**
   * Get total balance for all user account.
   *
   * @param userId user id
   * @return total balance
   */
  BigDecimal getTotalBalanceByUser(Integer userId);
}
