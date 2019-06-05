package com.revolut.handler.user;

import com.revolut.handler.AbstractRequestHandler;
import com.revolut.handler.Answer;
import com.revolut.dto.User;
import com.revolut.service.Model;

import java.util.Map;

public class CreateUserHandler extends AbstractRequestHandler<User> {

  public CreateUserHandler(Model model) {
    super(User.class, model);
  }

  @Override
  protected Answer processImpl(User value, Map<String, String> urlParams) {
    User user = model.createUser(value.getFirstName(), value.getSecondName());
    return new Answer(201, dataToJson(user));
  }

}
