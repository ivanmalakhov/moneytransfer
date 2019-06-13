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
 * Checking destination account.
 */
@Slf4j
public class CheckDstAccountStage extends ProcessingStage {
  /**
   * Account service.
   */
  private AccountService accountService;

  /**
   * Constructor.
   *
   * @param service - account service
   */
  public CheckDstAccountStage(final AccountService service) {
    this.accountService = service;
  }

  /**
   * Check destination account.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    PaymentDTO paymentDTO = (PaymentDTO) data.getDto();
    ResponseMessage responseMessage = new ResponseMessage();

    if (StringUtils.isBlank(paymentDTO.getDstAccount())) {
      log.error(ResponseStatus.EMPTY_DEST_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_DEST_ACC_ID);
      return responseMessage;
    }
    Account account = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getDstAccount());
    if (null == account) {
      log.error(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }
    data.getPaymentParams().setDstAccount(account);
    return performNextOperation(data);
  }
}
