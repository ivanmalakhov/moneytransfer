package com.revolute.service;

import com.revolute.dto.PaymentRequest;
import com.revolute.dto.Account;
import com.revolute.dto.Currency;
import com.revolute.dto.Payment;
import com.revolute.dto.User;

import java.math.BigDecimal;
import java.util.Set;

public interface Model {
  /**
   * Create new account
   *
   * @param currency currency
   * @param user     use
   * @return Account
   */
  Account createAccount(Currency currency, User user);

  /**
   * Creae new User
   *
   * @param firstName firstName
   * @param lastName lastName
   * @return User
   */
  User createUser(String firstName, String lastName);

  /**
   * Get All user Account
   *
   * @param userId userid
   * @return Set<Account>
   */
  Set<Account> getAccountListByUser(Integer userId);

  /**
   * Transfer money from one account to another
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment transferMoney(PaymentRequest paymentRequest);

  /**
   * Withdraw money from account
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment withdraw(PaymentRequest paymentRequest);

  /**
   * Deposit money to account
   *
   * @param paymentRequest paymentRequest
   * @return Payment
   */
  Payment deposit(PaymentRequest paymentRequest);

  /**
   * Get total balance for all user account
   *
   * @param userId user id
   * @return total balance
   */
  BigDecimal getTotalBalanceByUser(Integer userId);
}
