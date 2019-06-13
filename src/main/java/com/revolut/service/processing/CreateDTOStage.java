package com.revolut.service.processing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create DTO.
 */
public class CreateDTOStage extends ProcessingStage {
  /**
   * Logger.
   */
  private final Logger logger = LoggerFactory.getLogger(CreateDTOStage.class);

  /**
   * Create DTO from string request.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();
    try {
      JsonObject jsonObject = gson.fromJson(data.getRequestData(),
                                            JsonObject.class);
      data.setDto(gson.fromJson(jsonObject, data.getDto().getClass()));
    } catch (Exception e) {
      logger.error("JSON parsing error. Input string: {} ;",
                   data.getRequestData(),
                   e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    return performNextOperation(data);
  }
}
