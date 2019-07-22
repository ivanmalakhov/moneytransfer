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
 * Update User information.
 */
@Slf4j
public class UpdateUserStage extends ProcessingStage {
  /**
   * User service.
   */
  private final UserService userService;

  /**
   * Constructor.
   *
   * @param service - user service.
   */
  public UpdateUserStage(final UserService service) {
    this.userService = service;
  }

  /**
   * Update user.
   *
   * @param data - request
   * @return - updated user
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();
    UserDTO userDTO = (UserDTO) data.getDto();

    User user = userService.update(data.getParams().getUser().getId(),
                                   userDTO);
    if (user == null) {
      log.error("Error update user.");
      responseMessage.setStatus(ResponseStatus.USER_UPDATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(User.class.getSimpleName(),
                   gson.toJsonTree(user, User.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Update user. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;
  }
}
