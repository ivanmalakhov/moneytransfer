package com.revolut.service.impl;

import com.revolut.dto.Currency;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.AccountService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Account service implementation.
 */
public enum AccountServiceImpl implements AccountService {
  /**
   * Singleton.
   */
  INSTANCE;
  /**
   * Account storage.
   */
  private final Map<Integer, Set<Account>> userAccounts;

  /**
   * Constructor. Create account storage.
   */
  AccountServiceImpl() {
    userAccounts = new HashMap<>();
  }

  @Override
  public Account create(final Currency currency, final User user) {
    Account account = new Account(UUID.randomUUID(),
                                  "40702"
                                          + currency.code()
                                          + new Random().
                                          nextInt(Integer.MAX_VALUE),
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
  public Set<Account> getAccountListByUser(final User user) {
    return userAccounts.get(user.getId());
  }

  @Override
  public Account getAccountById(final String accountNumber,
                                final User user) {
    final Account[] account = {null};
    userAccounts.get(user.getId())
            .stream()
            .filter(a -> a.getNumber().equals(accountNumber))
            .findFirst()
            .ifPresent(a -> account[0] = a);
    return account[0];
  }
}
