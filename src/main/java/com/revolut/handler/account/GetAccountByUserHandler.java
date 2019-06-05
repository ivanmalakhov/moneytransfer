package com.revolut.handler.account;

import com.revolut.handler.AbstractRequestHandler;
import com.revolut.handler.Answer;
import com.revolut.dto.Account;
import com.revolut.dto.User;
import com.revolut.service.Model;

import java.util.Map;
import java.util.Set;

public class GetAccountByUserHandler extends AbstractRequestHandler<User> {

  public GetAccountByUserHandler(Model model) {
    super(User.class, model);
  }

  @Override
  protected Answer processImpl(User value, Map<String, String> urlParams) {
    try {
      Set<Account> accounts = model.getAccountListByUser(value.getId());
      return new Answer(201, dataToJson(accounts));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }

  }
}
