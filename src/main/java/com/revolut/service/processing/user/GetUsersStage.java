package com.revolut.service.processing.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.User;
import com.revolut.service.UserService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Get all users.
 */
@Slf4j
public class GetUsersStage extends ProcessingStage {
  /**
   * User service.
   */
  private UserService userService;

  /**
   * Constructor.
   *
   * @param service - user service.
   */
  public GetUsersStage(final UserService service) {
    this.userService = service;
  }

  /**
   * Get all users.
   *
   * @param data - request
   * @return ResponseMessage with users collection
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();

    List<User> users = userService.getUsers();
    if (users == null) {
      log.warn("Error creating new client.");
      responseMessage.setStatus(ResponseStatus.USER_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Users", gson.toJsonTree(users));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Create new client. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;

  }
}
