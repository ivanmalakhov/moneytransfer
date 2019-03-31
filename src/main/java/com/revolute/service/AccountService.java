package com.revolute.service;

import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.User;

import java.util.Set;

public interface AccountService {
  Account create(Currency currency, User user);

  Set<Account> getAccountListByUser(Integer userId);

  Account getAccountById(Integer userId, String accountNumber);
}
