package com.revolute.service;

import com.revolute.dto.account.AccountResponse;
import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.User;

import java.util.List;
import java.util.UUID;

public interface AccountService extends Model{
  // TODO убрать после создания механизма инициализации данных
  AccountResponse create(Currency currency, User user);
  List<Account> getAccountListByUser(UUID userId);
  Account getAccountById(UUID id);
  void updateAccount(Account account);
}
