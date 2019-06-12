package com.revolut.service.impl;

import com.revolut.data.Account;
import com.revolut.data.User;
import com.revolut.dto.Currency;
import com.revolut.service.AccountService;
import org.apache.log4j.Logger;

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
  private Map<Integer, Set<Account>> userAccounts;
  /**
   * Logger.
   */
  private Logger logger = Logger.getLogger(AccountServiceImpl.class);

  /**
   * Constructor. Create account storage.
   */
  AccountServiceImpl() {
    userAccounts = new HashMap<>();
  }

  @Override
  public Account create(final Currency currency, final User user) {
    Random rand = new Random();
    Account account = new Account(UUID.randomUUID(),
                                  "40702"
                                          + currency.code()
                                          + rand.nextInt(Integer.MAX_VALUE),
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
  public Set<Account> getAccountListByUser(final Integer userId) {
    return userAccounts.get(userId);
  }

  @Override
  public Account getAccountById(final Integer userId,
                                final String accountNumber) {
    final Account[] account = {null};
    userAccounts.get(userId)
            .stream()
            .filter(a -> a.getNumber().equals(accountNumber))
            .findFirst()
            .ifPresent(a -> account[0] = a);
    return account[0];
  }
}
