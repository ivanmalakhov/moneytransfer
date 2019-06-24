package com.revolut.service.impl;

import com.revolut.entity.Payment;
import com.revolut.service.PaymentService;
import com.revolut.service.processing.params.PaymentParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for working with payments.
 */
public enum PaymentServiceImpl implements PaymentService {
  /**
   * Singleton Instance.
   */
  INSTANCE;
  /**
   * All payments storage.
   */
  private final List<Payment> payments;

  /**
   * Constructor.
   */
  PaymentServiceImpl() {
    this.payments = new ArrayList<>();
  }

  @Override
  public Payment transferMoney(final PaymentParams paymentParams) {
    Payment payment = new Payment(paymentParams.getSrcAccount(),
                                  paymentParams.getDstAccount(),
                                  paymentParams.getAmount());
    payments.add(payment);
    payment.execute();
    return payment;

  }

  @Override
  public Payment deposit(final PaymentParams paymentParams) {
    Payment payment = new Payment(null,
                                  paymentParams.getDstAccount(),
                                  paymentParams.getAmount());
    payments.add(payment);
    payment.execute();
    return payment;
  }

  @Override
  public Payment withdraw(final PaymentParams paymentParams) {
    Payment payment = new Payment(paymentParams.getSrcAccount(),
                                  null,
                                  paymentParams.getAmount());
    payments.add(payment);
    payment.execute();
    return payment;
  }
}
