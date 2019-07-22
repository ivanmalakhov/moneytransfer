package com.revolut.entity.payment;

import com.revolut.entity.Account;
import lombok.Data;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Abstract Payment class.
 */
@Data
public abstract class Payment implements PaymentAction {
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
   * Payment status.
   */
  private PaymentStatus status;

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
    this.status = PaymentStatus.NEW;
  }
}
