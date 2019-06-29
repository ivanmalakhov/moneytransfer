package com.revolut.service.processing;

import com.revolut.dto.AbstractDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.service.UserService;
import com.revolut.service.processing.params.Params;
import lombok.extern.slf4j.Slf4j;

/**
 * Check User.
 */
@Slf4j
public class CheckUserStageNew extends ProcessingStage {
  /**
   * User service.
   */
  private final UserService userService;

  /**
   * Constructor.
   *
   * @param service user service
   */
  public CheckUserStageNew(final UserService service) {
    this.userService = service;
  }

  /**
   * Check user.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    AbstractDTO paymentDTO = data.getDto();
    ResponseMessage responseMessage = new ResponseMessage();
    Params params = data.getParams();
    if (paymentDTO.getUserId() == null) {
      log.error(ResponseStatus.EMPTY_USER_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_USER_ID);
      return responseMessage;
    }
    if (userService.isUserNotExist(paymentDTO.getUserId())) {
      log.error(ResponseStatus.USER_DOES_NOT_EXIST.getDescription());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    params.setUser(userService.getUser(paymentDTO.getUserId()));

    return performNextOperation(data);
  }
}
