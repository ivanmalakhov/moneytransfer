package com.revolut.entity.payment;

/**
 * Payment statuses.
 */
public enum PaymentStatus {
  NEW,
  WAIT,
  IN_PROGRESS,
  COMPLETED,
  FAILED,
  REJECTED;
}
