package com.revolute.service;

import com.revolute.dto.PaymentRequest;
import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.Payment;
import com.revolute.model.User;

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
   * @param user user
   * @return User
   */
  User createUser(User user);

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

}
