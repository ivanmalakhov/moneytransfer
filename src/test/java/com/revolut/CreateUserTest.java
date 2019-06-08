package com.revolut;

import com.revolut.data.User;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {
  private Logger logger = LoggerFactory.getLogger(CreateUserTest.class);

  @Test
  public void userExists() {
    Model model = new ModelImpl();

    User user = new User("John", "Smith");
    String userJson = "{\"firstName\":\"John\",\"lastName\":\"Smith\"}";
    ResponseMessage responseMessage = model.createUser(userJson);
    logger.info("New user: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }
}
