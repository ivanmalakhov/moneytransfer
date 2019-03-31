package com.revolute;

import com.revolute.handler.Answer;
import com.revolute.handler.user.CreateUserHandler;
import com.revolute.dto.User;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
  private Model model;
  private CreateUserHandler createUserHandler;
  private Logger logger = Logger.getLogger(CreateUserTest.class);

  @Before
  public void init() {
    model = new ModelImpl();
    createUserHandler = new CreateUserHandler(model);
  }

  @Test
  public void userExists() {
    User user = new User("John", "Smith");
    Answer answer = createUserHandler.process(user, Collections.emptyMap());
    logger.info("New user: " + answer);
    assertEquals(201, answer.getCode());
  }
}
