package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.data.User;
import com.revolut.dto.ResponseMessage;
import com.revolut.service.Model;

public class TestUtils {

  public static User createUser(Model model, String json) {
    Gson gson = new Gson();
    ResponseMessage message = model.createUser(json);
    JsonObject object = gson.fromJson(message.getJsonMessage(),
                                      JsonObject.class).getAsJsonObject("Info");
    User createdUser = gson.fromJson(object.getAsJsonObject("User"), User.class);
    return createdUser;
  }
}
