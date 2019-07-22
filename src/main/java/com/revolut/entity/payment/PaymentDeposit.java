package com.revolut.entity.payment;

import com.revolut.entity.Account;

import java.math.BigDecimal;

/**
 * Deposit money.
 */
public class PaymentDeposit extends Payment {
  /**
   * Constructor.
   *
   * @param paymentAccountTo - paymentAccountTo
   * @param paymentAmount    - paymentAmount
   */
  public PaymentDeposit(final Account paymentAccountTo,
                        final BigDecimal paymentAmount) {
    super(null, paymentAccountTo, paymentAmount);
  }

  /**
   * Execute deposit money transaction.
   *
   * @return payment status
   */
  @Override
  public PaymentStatus execute() {
    Account account = this.getAccountTo();
    account.setBalance(account.getBalance().add(this.getAmount()));

    return PaymentStatus.COMPLETED;
  }
}
