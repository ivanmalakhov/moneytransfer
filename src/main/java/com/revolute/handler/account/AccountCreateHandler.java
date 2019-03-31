package com.revolute.handler.account;

import com.revolute.handler.AbstractRequestHandler;
import com.revolute.dto.AccountRequest;
import com.revolute.handler.Answer;
import com.revolute.model.Account;
import com.revolute.service.Model;

import java.util.Map;

public class AccountCreateHandler extends AbstractRequestHandler<AccountRequest> {

  public AccountCreateHandler(Model model) {
    super(AccountRequest.class, model);
  }

  @Override
  protected Answer processImpl(AccountRequest value, Map<String, String> urlParams) {
    try {
      Account account = model.createAccount(value.getCurrency(), value.getUser());
      return new Answer(201, dataToJson(account));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }
  }

}
