package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.revolut.TestJson.USER_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UsersOperationTest {
  private final Logger logger = LoggerFactory.getLogger(UsersOperationTest.class);

  private List<Integer> usersID = new ArrayList<>();
  private Model model = new ModelImpl();
  private Gson gson = new Gson();

  private void createUser(String userJson) {

    ResponseMessage responseMessage = model.createUser(userJson);
    logger.info("New user: {}", responseMessage.getJsonMessage());

    JsonObject jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                                          JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("User");
    User user = gson.fromJson(jsonObject, User.class);
    usersID.add(user.getId());

  }

  @Before
  public void setUp() {
    createUser(USER_JSON);
    createUser(USER_JSON);
    usersID.forEach(System.out::println);
  }

  @Test
  public void getUser() {
    ResponseMessage responseMessage = model.getUser(usersID.get(0).toString(),
                                                    "");
    logger.info("Get User: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

  @Test
  public void getUsers() {
    ResponseMessage responseMessage = model.getUsers("");
    logger.info("Get Users: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

  }

  @Test
  public void updateUser() {
    ResponseMessage responseMessage;
    JsonObject jsonObject;
    responseMessage = model.getUser(usersID.get(0).toString(),
                                    "");

    jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                               JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("User");
    User user1 = gson.fromJson(jsonObject, User.class);

    //Update user
    responseMessage = model.updateUser(usersID.get(0).toString(),
                                       "{\"firstName\":\"Ivan\",\"lastName\":\"Malakhov\"}");
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());


    responseMessage = model.getUser(usersID.get(0).toString(),
                                    "");
    jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                               JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("User");
    User user2 = gson.fromJson(jsonObject, User.class);
    assertNotEquals(user1, user2);
  }

}
