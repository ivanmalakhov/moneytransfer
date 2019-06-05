package com.revolut.handler.payment;

import com.revolut.dto.PaymentRequest;
import com.revolut.handler.AbstractRequestHandler;
import com.revolut.handler.Answer;
import com.revolut.dto.Payment;
import com.revolut.service.Model;

import java.util.Map;

public class TransferMoneyHandler extends AbstractRequestHandler<PaymentRequest> {

  public TransferMoneyHandler(Model model) {
    super(PaymentRequest.class, model);
  }

  @Override
  protected Answer processImpl(PaymentRequest value, Map<String, String> urlParams) {
    try {
      Payment payment = model.transferMoney(value);
      return new Answer(201, dataToJson(payment));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }
  }
}
