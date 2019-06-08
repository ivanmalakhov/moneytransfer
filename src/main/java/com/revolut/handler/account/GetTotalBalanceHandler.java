package com.revolut.handler.account;

import com.revolut.data.User;
import com.revolut.handler.AbstractRequestHandler;
import com.revolut.handler.Answer;
import com.revolut.service.Model;

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
