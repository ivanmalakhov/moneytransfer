package com.revolute.service.impl;

import com.revolute.model.Account;
import com.revolute.model.Payment;
import com.revolute.service.PaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public enum PaymentServiceImpl implements PaymentService {
  INSTANCE;
  private List<Payment> payments;

  PaymentServiceImpl() {
    this.payments = new ArrayList<>();
  }


  @Override
  public synchronized Payment transferMoney(Account srcAccount, Account dstAccount, BigDecimal amount) {
    Payment payment = new Payment(srcAccount, dstAccount, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }

  @Override
  public synchronized Payment deposit(Account account, BigDecimal amount) {
    Payment payment = new Payment(null, account, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }

  @Override
  public synchronized Payment withdraw(Account account, BigDecimal amount) {
    Payment payment = new Payment(account, null, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }
}
