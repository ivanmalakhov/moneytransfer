package com.revolut.service;

import com.revolut.entity.Payment;
import com.revolut.service.processing.params.PaymentParams;

/**
 * Service for working with payment.
 */
public interface PaymentService {
  /**
   * Transfer money from between tow accounts.
   *
   * @param paymentParams - payment params
   * @return Payment
   */
  Payment transferMoney(PaymentParams paymentParams);

  /**
   * Deposit money to account.
   *
   * @param paymentParams - payment params
   * @return Payment
   */
  Payment deposit(PaymentParams paymentParams);

  /**
   * Withdraw money from account.
   *
   * @param paymentParams - payment params
   * @return Payment
   */
  Payment withdraw(PaymentParams paymentParams);
}
