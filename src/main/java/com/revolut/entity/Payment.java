package com.revolut.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Class with information about payment.
 */
@Data
public final class Payment {
  /**
   * Payment Id.
   */
  private Integer id;
  /**
   * Payment accountFrom.
   */
  private Account accountFrom;
  /**
   * Payment accountTo.
   */
  private Account accountTo;
  /**
   * Payment Date.
   */
  private Date dt;
  /**
   * Payment amount.
   */
  private BigDecimal amount;

  /**
   * Constructor.
   *
   * @param paymentAccountFrom - paymentAccountFrom
   * @param paymentAccountTo   - paymentAccountTo
   * @param paymentAmount      - paymentAmount
   */
  public Payment(final Account paymentAccountFrom,
                 final Account paymentAccountTo,
                 final BigDecimal paymentAmount) {
    SecureRandom rand = new SecureRandom();
    this.id = rand.nextInt(Integer.MAX_VALUE);
    this.accountFrom = paymentAccountFrom;
    this.accountTo = paymentAccountTo;
    this.amount = paymentAmount;
    this.dt = new Date();
  }

  /**
   * Execute transfer from one account to another.
   */
  public void execute() {
    if (null != accountFrom) {
      accountFrom.setBalance(accountFrom
                                     .getBalance()
                                     .subtract(this.getAmount()));
    }
    if (null != accountTo) {
      accountTo.setBalance(accountTo.getBalance().add(this.getAmount()));
    }

  }
}
