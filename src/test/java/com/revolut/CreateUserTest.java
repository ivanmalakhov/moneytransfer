package com.revolut;

import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.revolut.TestJson.USER_JSON;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {
  private final Logger logger = LoggerFactory.getLogger(CreateUserTest.class);

  @Test
  public void userExists() {
    Model model = new ModelImpl();

    ResponseMessage responseMessage = model.createUser(USER_JSON);
    logger.info("New user: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }
}
