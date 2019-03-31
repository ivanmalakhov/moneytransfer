package com.revolute.handler.user;

import com.revolute.handler.AbstractRequestHandler;
import com.revolute.handler.Answer;
import com.revolute.model.User;
import com.revolute.service.Model;

import java.util.Map;

public class CreateUserHandler extends AbstractRequestHandler<User> {

  public CreateUserHandler(Model model) {
    super(User.class, model);
  }

  @Override
  protected Answer processImpl(User value, Map<String, String> urlParams) {
    User user = model.createUser(value);
    return new Answer(201, dataToJson(user));
  }

}
