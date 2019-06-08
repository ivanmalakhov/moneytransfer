package com.revolut.service.impl;

import com.revolut.data.Account;
import com.revolut.dto.Payment;
import com.revolut.service.PaymentService;

import java.math.BigDecimal;
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
   * All payments.
   */
  private List<Payment> payments;

  /**
   * Constructor.
   */
  PaymentServiceImpl() {
    this.payments = new ArrayList<>();
  }

  @Override
  public Payment transferMoney(final Account srcAccount,
                               final Account dstAccount,
                               final BigDecimal amount) {
    Payment payment = new Payment(srcAccount, dstAccount, amount);
    payments.add(payment);
    payment.execute();
    return payment;

  }

  @Override
  public Payment deposit(final Account account, final BigDecimal amount) {
    Payment payment = new Payment(null, account, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }

  @Override
  public Payment withdraw(final Account account, final BigDecimal amount) {
    Payment payment = new Payment(account, null, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }
}
