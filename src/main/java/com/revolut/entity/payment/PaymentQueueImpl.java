package com.revolut.entity.payment;

/**
 * Payment Queue implementation.
 *
 * @author ivanmalakhov
 */
public class PaymentQueueImpl implements PaymentQueueAction {

  @Override
  public boolean addToQueue(final Payment payment) {
    return false;
  }

  @Override
  public boolean removeTask(final Payment payment) {
    return false;
  }

  @Override
  public boolean removeTask(final Long id) {
    return false;
  }

  @Override
  public PaymentStatus checkState(final Payment payment) {
    return null;
  }

  @Override
  public PaymentStatus checkState(final Long id) {
    return null;
  }

  @Override
  public Payment getTask(final Payment payment) {
    return null;
  }

  @Override
  public Payment getTask(final Long id) {
    return null;
  }
}
