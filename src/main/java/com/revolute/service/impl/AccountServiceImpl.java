package com.revolute.service.impl;

import com.revolute.dto.account.AccountResponse;
import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.User;
import com.revolute.service.AccountService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

  public AccountResponse create(Currency currency, User user) {
    Account account = new Account();
    account.setCurrency(currency);
    account.setUser(user);
    account.setId(UUID.randomUUID());
    Random rand = new Random();
    account.setNumber("40702"+currency.code()+rand.nextInt());
    return new AccountResponse().setAccountId(account.getId().toString());
  }

  @Override
  public List<Account> getAccountListByUser(UUID userId) {
    return null;
  }

  @Override
  public Account getAccountById(UUID id) {
    return null;
  }

  @Override
  public void updateAccount(Account account) {

  }
}
