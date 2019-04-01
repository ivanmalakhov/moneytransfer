package com.revolute.handler.user;

import com.revolute.handler.AbstractRequestHandler;
import com.revolute.handler.Answer;
import com.revolute.dto.User;
import com.revolute.service.Model;

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
