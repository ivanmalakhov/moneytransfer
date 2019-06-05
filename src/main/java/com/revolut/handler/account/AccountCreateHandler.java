package com.revolut.handler.account;

import com.revolut.dto.Account;
import com.revolut.dto.AccountRequest;
import com.revolut.handler.AbstractRequestHandler;
import com.revolut.handler.Answer;
import com.revolut.service.Model;

import java.util.Map;

public class AccountCreateHandler extends AbstractRequestHandler<AccountRequest> {

  public AccountCreateHandler(final Model model) {
    super(AccountRequest.class, model);
  }

  @Override
  protected Answer processImpl(final AccountRequest value,
                               final Map<String, String> urlParams) {
    try {
      Account account = model.createAccount(value.getCurrency(),
              value.getUser());
      return new Answer(201, dataToJson(account));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }
  }

}
