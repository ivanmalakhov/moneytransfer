package com.revolute.service;

import com.revolute.model.Payment;
import com.revolute.model.User;

import java.util.List;

public interface PaymentService extends Model {
  List<Payment> getPaymentsList();
  void savePayment(Payment payment);
  List<Payment> getTransferListByUser(User user);
}
