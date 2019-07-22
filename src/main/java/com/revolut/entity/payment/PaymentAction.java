package com.revolut.entity.payment;

/**
 * Action with payment.
 */
public interface PaymentAction {
  /**
   * Execute payment.
   *
   * @return payment status.
   */
  PaymentStatus execute();

}
