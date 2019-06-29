package com.revolut.service.processing.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.User;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import lombok.extern.slf4j.Slf4j;

/**
 * Get user.
 */
@Slf4j
public class GetUserStage extends ProcessingStage {
  /**
   * Get user.
   *
   * @param data - request
   * @return ResponseMessage with user
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();

    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(User.class.getSimpleName(),
                   gson.toJsonTree(data.getParams().getUser(),
                                   User.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("User data. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;

  }
}
