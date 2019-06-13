package com.revolut.service.processing.payment;

import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.service.AccountService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import lombok.extern.slf4j.Slf4j;
import spark.utils.StringUtils;

/**
 * Checking source account.
 */
@Slf4j
public class CheckSrcAccountStage extends ProcessingStage {
  /**
   * Account service.
   */
  private AccountService accountService;

  /**
   * Constructor.
   *
   * @param service - account service
   */
  public CheckSrcAccountStage(final AccountService service) {
    this.accountService = service;
  }

  /**
   * Check source account.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    PaymentDTO paymentDTO = (PaymentDTO) data.getDto();
    ResponseMessage responseMessage = new ResponseMessage();

    if (StringUtils.isBlank(paymentDTO.getSrcAccount())) {
      log.error(ResponseStatus.EMPTY_SRC_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_SRC_ACC_ID);
      return responseMessage;
    }
    Account account = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getSrcAccount());
    if (null == account) {
      log.error(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }
    data.getPaymentParams().setSrcAccount(account);
    return performNextOperation(data);
  }
}
