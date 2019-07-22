package com.revolut.entity.payment;

import com.revolut.entity.Account;

import java.math.BigDecimal;

/**
 * Transfer payment between accounts.
 */
public class PaymentTransfer extends Payment {
  /**
   * Constructor.
   *
   * @param paymentAccountFrom - paymentAccountFrom
   * @param paymentAccountTo   - paymentAccountTo
   * @param paymentAmount      - paymentAmount
   */
  public PaymentTransfer(final Account paymentAccountFrom,
                         final Account paymentAccountTo,
                         final BigDecimal paymentAmount) {
    super(paymentAccountFrom, paymentAccountTo, paymentAmount);
  }

  /**
   * Execute transfer payment between accounts.
   *
   * @return payment status
   */
  @Override
  public PaymentStatus execute() {
    Account accountTo = this.getAccountTo();
    Account accountFrom = this.getAccountFrom();

    accountTo.setBalance(accountTo.getBalance().add(this.getAmount()));
    accountFrom.setBalance(accountFrom
                                   .getBalance()
                                   .subtract(this.getAmount()));
    return null;
  }
}
