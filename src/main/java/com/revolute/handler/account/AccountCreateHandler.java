package com.revolute.handler.account;

import com.revolute.dto.account.AccountResponse;
import com.revolute.handler.AbstractRequestHandler;
import com.revolute.dto.account.AccountRequest;
import com.revolute.handler.Answer;
import com.revolute.service.AccountService;

import java.util.Map;

public class AccountCreateHandler extends AbstractRequestHandler<AccountRequest> {

  AccountService accountService;

  public AccountCreateHandler(AccountService accountService) {
    super(AccountRequest.class, accountService);
    this.accountService = accountService;

  }

  @Override
  protected Answer processImpl(AccountRequest value, Map<String, String> urlParams, boolean shouldReturnHtml) {
    AccountResponse accountResponse = accountService.create(value.getCurrency(),value.getUser());
    return new Answer(201, dataToJson(accountResponse));
  }

}
