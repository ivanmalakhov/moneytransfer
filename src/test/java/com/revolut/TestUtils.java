package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.entity.User;
import com.revolut.service.Model;

class TestUtils {

  static User createUser(Model model, String json) {
    Gson gson = new Gson();
    ResponseMessage message = model.createUser(json);
    JsonObject object = gson.fromJson(message.getJsonMessage(),
                                      JsonObject.class).getAsJsonObject("Info");
    return gson.fromJson(object.getAsJsonObject("User"), User.class);
  }
}
