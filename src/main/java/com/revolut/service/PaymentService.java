package com.revolut.service;

import com.revolut.data.Account;
import com.revolut.dto.Payment;

import java.math.BigDecimal;

public interface PaymentService {
  Payment transferMoney(Account srcAccount, Account dstAccount, BigDecimal amount);

  Payment deposit(Account account, BigDecimal amount);

  Payment withdraw(Account account, BigDecimal amount);
}
