package com.revolute.service;

import com.revolute.dto.Account;
import com.revolute.dto.Currency;
import com.revolute.dto.User;

import java.util.Set;

public interface AccountService {
  Account create(Currency currency, User user);

  Set<Account> getAccountListByUser(Integer userId);

  Account getAccountById(Integer userId, String accountNumber);
}
