package com.revolute.service.impl;

import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.User;
import com.revolute.service.AccountService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public enum AccountServiceImpl implements AccountService {
  INSTANCE;

  private Map<Integer, Set<Account>> userAccounts;
  private Logger logger = Logger.getLogger(AccountServiceImpl.class);
  Random rand = new Random();

  AccountServiceImpl() {
    userAccounts = new HashMap<>();
  }

  public Account create(Currency currency, User user) {
    Account account = new Account(UUID.randomUUID(),
            "40702" + currency.code() + rand.nextInt(Integer.MAX_VALUE),
            BigDecimal.ZERO,
            user,
            currency);
    if (!userAccounts.containsKey(user.getId())) {
      userAccounts.put(user.getId(), new HashSet<>());
    }
    userAccounts.get(user.getId()).add(account);

    return account;
  }

  @Override
  public Set<Account> getAccountListByUser(Integer userId) {
    return userAccounts.get(userId);
  }

  @Override
  public Account getAccountById(Integer userId, String accountNumber) {
    final Account[] account = {null};
    userAccounts.get(userId)
            .stream()
            .filter(a -> a.getNumber().equals(accountNumber))
            .findFirst()
            .ifPresent(a -> account[0] = a);
    return account[0];
  }

}
