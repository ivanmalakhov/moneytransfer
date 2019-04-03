package com.revolute.handler.account;

import com.revolute.dto.Account;
import com.revolute.dto.User;
import com.revolute.handler.AbstractRequestHandler;
import com.revolute.handler.Answer;
import com.revolute.service.Model;

import java.math.BigDecimal;
import java.util.Map;

public class GetTotalBalanceHandler extends AbstractRequestHandler<User> {

  public GetTotalBalanceHandler(Model model) {
    super(User.class, model);
  }

  @Override
  protected Answer processImpl(User value, Map<String, String> urlParams) {
    try {
      BigDecimal totalBalance = model.getTotalBalanceByUser(value.getId());
      return new Answer(201, dataToJson(totalBalance));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }

  }
}
