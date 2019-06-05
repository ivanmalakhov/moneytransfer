package com.revolut;

import com.revolut.handler.Answer;
import com.revolut.handler.user.CreateUserHandler;
import com.revolut.dto.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
  private CreateUserHandler createUserHandler;
  private Logger logger = Logger.getLogger(CreateUserTest.class);

  @Before
  public void init() {
    Model model = new ModelImpl();
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