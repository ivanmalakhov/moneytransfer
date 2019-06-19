package com.revolut.service.processing.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Payment;
import com.revolut.service.PaymentService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.params.PaymentParams;
import lombok.extern.slf4j.Slf4j;

/**
 * Transfer money stage.
 */
@Slf4j
public class TransferMoneyStage extends ProcessingStage {
  /**
   * Payment service.
   */
  private final PaymentService paymentService;

  /**
   * Constructor.
   *
   * @param service - payment service.
   */
  public TransferMoneyStage(final PaymentService service) {
    this.paymentService = service;
  }

  /**
   * Transfer money.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();

    Payment payment = paymentService.transferMoney(
            (PaymentParams) data.getParams());
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(Payment.class.getSimpleName(),
                   gson.toJsonTree(payment, Payment.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Transfer money. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;
  }
}
