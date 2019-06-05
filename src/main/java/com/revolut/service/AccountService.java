package com.revolut.service;

import com.revolut.dto.Account;
import com.revolut.dto.Currency;
import com.revolut.dto.User;

import java.math.BigDecimal;
import java.util.Set;

public interface AccountService {
  Account create(Currency currency, User user);

  Set<Account> getAccountListByUser(Integer userId);

  Account getAccountById(Integer userId, String accountNumber);

  BigDecimal getTotalBalanceByUser(Integer userId);
}