package com.revolut.service.processing.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.dto.UserDTO;
import com.revolut.entity.User;
import com.revolut.service.UserService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import lombok.extern.slf4j.Slf4j;

/**
 * Create user.
 */
@Slf4j
public class CreateUserStage extends ProcessingStage {
  /**
   * User service.
   */
  private UserService userService;

  /**
   * Constructor.
   *
   * @param service - user service
   */
  public CreateUserStage(final UserService service) {
    this.userService = service;
  }

  /**
   * Create user.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();
    UserDTO userDTO = (UserDTO) data.getDto();

    User user = userService.create(userDTO.getFirstName(),
                                   userDTO.getLastName());
    if (user == null) {
      log.warn("Error creating new client.");
      responseMessage.setStatus(ResponseStatus.USER_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(User.class.getSimpleName(),
                   gson.toJsonTree(user, User.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Create new client. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;

  }
}
