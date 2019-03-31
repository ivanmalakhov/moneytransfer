package com.revolute.service;

import com.revolute.model.Account;
import com.revolute.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
  Payment transferMoney(Account srcAccount, Account dstAccount, BigDecimal amount);

  Payment deposit(Account account, BigDecimal amount);

  Payment withdraw(Account account, BigDecimal amount);
}
