package com.revolute.handler.payment;

import com.revolute.dto.PaymentRequest;
import com.revolute.handler.AbstractRequestHandler;
import com.revolute.handler.Answer;
import com.revolute.model.Payment;
import com.revolute.service.Model;

import java.util.Map;

public class DepositMoneyHandler extends AbstractRequestHandler<PaymentRequest> {

  public DepositMoneyHandler(Model model) {
    super(PaymentRequest.class, model);
  }

  @Override
  protected Answer processImpl(PaymentRequest value, Map<String, String> urlParams) {
    try {
      Payment payment = model.deposit(value);
      return new Answer(201, dataToJson(payment));

    } catch (IllegalArgumentException e) {
      return new Answer(404, e.getMessage());
    }
  }
}