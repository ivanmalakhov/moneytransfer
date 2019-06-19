package com.revolut.service.processing.payment;

import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.params.PaymentParams;
import lombok.extern.slf4j.Slf4j;

/**
 * Check money on source account.
 */
@Slf4j
public class CheckAmountStage extends ProcessingStage {

  /**
   * Check money on source account.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    ResponseMessage responseMessage = new ResponseMessage();
    PaymentParams paymentParams = (PaymentParams) data.getParams();
    Account srcAccount = paymentParams.getSrcAccount();
    PaymentDTO paymentDTO = (PaymentDTO) data.getDto();

    if (srcAccount != null
            && srcAccount.getBalance().compareTo(paymentDTO.getAmount()) < 0) {
      log.error(ResponseStatus
                        .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION
                        .getDescription());
      responseMessage.setStatus(ResponseStatus
                                        .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
      return responseMessage;
    }
    paymentParams.setAmount(paymentDTO.getAmount());
    data.setParams(paymentParams);
    return performNextOperation(data);
  }
}
