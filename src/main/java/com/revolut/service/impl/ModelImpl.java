package com.revolut.service.impl;

import com.revolut.dto.PaymentRequest;
import com.revolut.dto.Account;
import com.revolut.dto.Currency;
import com.revolut.dto.Payment;
import com.revolut.dto.User;
import com.revolut.service.AccountService;
import com.revolut.service.Model;
import com.revolut.service.PaymentService;
import com.revolut.service.UserService;
import org.apache.log4j.Logger;
import spark.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Implementation model.
 */
public final class ModelImpl implements Model {
  /**
   * Constant "Empty destination account ID".
   */
  private static final String
          EMPTY_DESTINATION_ACCOUNT_ID = "Empty destination account ID";
  /**
   * Constant "Empty source account ID".
   */
  private static final String
          EMPTY_SOURCE_ACCOUNT_ID = "Empty source account ID";
  /**
   * Constant "Empty user ID".
   */
  private static final String
          EMPTY_USER_ID = "Empty user ID";
  /**
   * Constant "Source account doesn't exists".
   */
  private static final String
          SOURCE_ACCOUNT_DOES_NOT_EXISTS = "Source account doesn't exists";
  /**
   * Constant "Destination account doesn't exists".
   */
  private static final String
          DESTINATION_ACCOUNT_DOES_NOT_EXISTS =
          "Destination account doesn't exists";
  /**
   * Constant "Not enough money for a transaction".
   */
  private static final String
          NOT_ENOUGH_MONEY_FOR_A_TRANSACTION =
          "Not enough money for a transaction";
  /**
   * Constant "User not found".
   */
  private static final String
          USER_NOT_FOUND = "User not found";

  /**
   * Service for working with Payments.
   */
  private final PaymentService paymentService;
  /**
   * Service for working with Accounts.
   */
  private final AccountService accountService;
  /**
   * Service for working with Users.
   */
  private final UserService userService;
  /**
   * Logger.
   */
  private final Logger logger = Logger.getLogger(ModelImpl.class);

  /**
   * Main constructor.
   */
  public ModelImpl() {
    this.accountService = AccountServiceImpl.INSTANCE;
    this.paymentService = PaymentServiceImpl.INSTANCE;
    this.userService = UserServiceImpl.INSTANCE;
  }

  @Override
  public synchronized Account createAccount(final Currency currency,
                                            final User user) {
    if (userService.userNotExist(user)) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    return accountService.create(currency, user);
  }

  @Override
  public synchronized Set<Account> getAccountListByUser(final Integer userId) {
    if (userService.userNotExist(userId)) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }

    Set<Account> accounts = accountService.getAccountListByUser(userId);
    if (null == accounts) {
      logger.warn("User doesn't have account");
      throw new IllegalArgumentException("User doesn't have account");

    }
    return accounts;
  }

  @Override
  public synchronized Payment transferMoney(
          final PaymentRequest paymentRequest) {
    if (StringUtils.isBlank(paymentRequest.getDstAccount())) {
      logger.error(EMPTY_DESTINATION_ACCOUNT_ID);
      throw new IllegalArgumentException(EMPTY_DESTINATION_ACCOUNT_ID);
    }
    if (StringUtils.isBlank(paymentRequest.getSrcAccount())) {
      logger.error(EMPTY_SOURCE_ACCOUNT_ID);
      throw new IllegalArgumentException(EMPTY_SOURCE_ACCOUNT_ID);
    }
    if (paymentRequest.getUserId() == null) {
      logger.error(EMPTY_USER_ID);
      throw new IllegalArgumentException(EMPTY_USER_ID);
    }
    if (userService.userNotExist(paymentRequest.getUserId())) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    Account srcAccount = accountService.getAccountById(
            paymentRequest.getUserId(),
            paymentRequest.getSrcAccount());
    if (null == srcAccount) {
      logger.error(SOURCE_ACCOUNT_DOES_NOT_EXISTS);
      throw new IllegalArgumentException(SOURCE_ACCOUNT_DOES_NOT_EXISTS);
    }
    Account dstAccount = accountService.getAccountById(
            paymentRequest.getUserId(),
            paymentRequest.getDstAccount());
    if (null == dstAccount) {
      logger.error(DESTINATION_ACCOUNT_DOES_NOT_EXISTS);
      throw new IllegalArgumentException(DESTINATION_ACCOUNT_DOES_NOT_EXISTS);
    }
    if (srcAccount.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
      logger.error(NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
      throw new IllegalArgumentException(NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
    }

    return paymentService.transferMoney(srcAccount,
            dstAccount,
            paymentRequest.getAmount());
  }

  @Override
  public synchronized Payment deposit(final PaymentRequest paymentRequest) {
    if (StringUtils.isBlank(paymentRequest.getDstAccount())) {
      logger.error(EMPTY_DESTINATION_ACCOUNT_ID);
      throw new IllegalArgumentException(EMPTY_DESTINATION_ACCOUNT_ID);
    }
    if (paymentRequest.getUserId() == null) {
      logger.error(EMPTY_USER_ID);
      throw new IllegalArgumentException(EMPTY_USER_ID);
    }
    if (userService.userNotExist(paymentRequest.getUserId())) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    Account dstAccount = accountService.getAccountById(
            paymentRequest.getUserId(),
            paymentRequest.getDstAccount());
    if (null == dstAccount) {
      logger.error(DESTINATION_ACCOUNT_DOES_NOT_EXISTS);
      throw new IllegalArgumentException(DESTINATION_ACCOUNT_DOES_NOT_EXISTS);
    }
    return paymentService.deposit(dstAccount, paymentRequest.getAmount());
  }

  @Override
  public synchronized Payment withdraw(final PaymentRequest paymentRequest) {
    if (paymentRequest.getUserId() == null) {
      logger.error(EMPTY_USER_ID);
      throw new IllegalArgumentException(EMPTY_USER_ID);
    }
    if (userService.userNotExist(paymentRequest.getUserId())) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    if (StringUtils.isBlank(paymentRequest.getSrcAccount())) {
      logger.error(EMPTY_SOURCE_ACCOUNT_ID);
      throw new IllegalArgumentException(EMPTY_SOURCE_ACCOUNT_ID);
    }
    Account srcAccount = accountService.getAccountById(
            paymentRequest.getUserId(),
            paymentRequest.getSrcAccount());
    if (null == srcAccount) {
      logger.error(SOURCE_ACCOUNT_DOES_NOT_EXISTS);
      throw new IllegalArgumentException(SOURCE_ACCOUNT_DOES_NOT_EXISTS);
    }
    if (srcAccount.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
      logger.error(NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
      throw new IllegalArgumentException(NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
    }

    return paymentService.withdraw(srcAccount, paymentRequest.getAmount());
  }


  @Override
  public synchronized User createUser(final String firstName,
                                      final String lastName) {
    return userService.create(firstName, lastName);
  }

  @Override
  public synchronized BigDecimal getTotalBalanceByUser(final Integer userId) {
    return accountService.getTotalBalanceByUser(userId);
  }
}