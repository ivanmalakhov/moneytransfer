package com.revolute.service.impl;

import com.revolute.dto.Account;
import com.revolute.dto.Payment;
import com.revolute.service.PaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public enum PaymentServiceImpl implements PaymentService {
  INSTANCE;
  private List<Payment> payments;

  PaymentServiceImpl() {
    this.payments = new ArrayList<>();
  }

  private Lock lock = new ReentrantLock();

  @Override
  public Payment transferMoney(Account srcAccount, Account dstAccount, BigDecimal amount) {
    Payment payment = new Payment(srcAccount, dstAccount, amount);
    payments.add(payment);
    payment.execute();
    return payment;

  }

  @Override
  public Payment deposit(Account account, BigDecimal amount) {
    Payment payment = new Payment(null, account, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }

  @Override
  public Payment withdraw(Account account, BigDecimal amount) {
    Payment payment = new Payment(account, null, amount);
    payments.add(payment);
    payment.execute();
    return payment;
  }
}
