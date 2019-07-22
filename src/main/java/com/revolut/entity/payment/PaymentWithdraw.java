package com.revolut.entity.payment;

import com.revolut.entity.Account;

import java.math.BigDecimal;

/**
 * Withdraw money from account.
 */
public class PaymentWithdraw extends Payment {
  /**
   * Constructor.
   *
   * @param paymentAccountFrom - paymentAccountFrom
   * @param paymentAmount      - paymentAmount
   */
  public PaymentWithdraw(final Account paymentAccountFrom,
                         final BigDecimal paymentAmount) {
    super(paymentAccountFrom, null, paymentAmount);
  }

  /**
   * Execute withdraw payment.
   *
   * @return payment status
   */
  @Override
  public PaymentStatus execute() {
    Account account = this.getAccountFrom();
    account.setBalance(account.getBalance().subtract(this.getAmount()));
    return PaymentStatus.COMPLETED;
  }
}
