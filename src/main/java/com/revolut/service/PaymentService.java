package com.revolut.service;

import com.revolut.data.Account;
import com.revolut.data.Payment;

import java.math.BigDecimal;

/**
 * Service for working with payment.
 */
public interface PaymentService {
  /**
   * Transfer money from between tow accounts.
   *
   * @param srcAccount - source account
   * @param dstAccount - destination account
   * @param amount     - amount
   * @return Payment
   */
  Payment transferMoney(Account srcAccount,
                        Account dstAccount,
                        BigDecimal amount);

  /**
   * Deposit money to account.
   *
   * @param account - account
   * @param amount  - amount
   * @return Payment
   */
  Payment deposit(Account account, BigDecimal amount);

  /**
   * Withdraw money from account.
   *
   * @param account - account
   * @param amount  - amount
   * @return Payment
   */
  Payment withdraw(Account account, BigDecimal amount);
}
