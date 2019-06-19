package com.revolut.service.processing;

import com.revolut.dto.AbstractDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.service.UserService;
import com.revolut.service.processing.params.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Check User.
 */
public class CheckUserStage extends ProcessingStage {
  /**
   * Logger.
   */
  private final Logger logger = LoggerFactory.getLogger(CheckUserStage.class);
  /**
   * User service.
   */
  private final UserService userService;

  /**
   * Constructor.
   *
   * @param service user service
   */
  public CheckUserStage(final UserService service) {
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
      logger.error(ResponseStatus.EMPTY_USER_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_USER_ID);
      return responseMessage;
    }
    if (userService.isUserNotExist(paymentDTO.getUserId())) {
      logger.error(ResponseStatus.USER_DOES_NOT_EXIST.getDescription());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    params.setUser(userService.getUser(paymentDTO.getUserId()));

    return performNextOperation(data);
  }
}
