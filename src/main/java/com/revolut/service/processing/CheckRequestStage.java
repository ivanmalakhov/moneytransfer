package com.revolut.service.processing;

import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;

/**
 * Check request message.
 */
public class CheckRequestStage extends ProcessingStage {

  /**
   * Check empty request.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    ResponseMessage responseMessage = new ResponseMessage();

    if (data.getRequestData().isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    return performNextOperation(data);
  }
}
