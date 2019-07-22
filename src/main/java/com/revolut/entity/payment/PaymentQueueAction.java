package com.revolut.entity.payment;

/**
 * Payment queue actions.
 *
 * @author ivanmalakhov
 */
public interface PaymentQueueAction {
  /**
   * Add payment to queue.
   *
   * @param payment - payment
   * @return - boolean result
   */
  boolean addToQueue(Payment payment);

  /**
   * Remove payment from queue. Search by Payment.
   *
   * @param payment - payment
   * @return - boolean result
   */
  boolean removeTask(Payment payment);

  /**
   * Remove payment from queue. Search by ID.
   *
   * @param id - payment id
   * @return - boolean result
   */
  boolean removeTask(Long id);

  /**
   * Check payment state. Search by Payment.
   *
   * @param payment - Payment.
   * @return - Payment status
   */
  PaymentStatus checkState(Payment payment);

  /**
   * Check payment state. Search by ID.
   *
   * @param id - Payment ID.
   * @return - Payment status
   */
  PaymentStatus checkState(Long id);

  /**
   * Return task.
   *
   * @param payment - Payment
   * @return - Payment
   */
  Payment getTask(Payment payment);

  /**
   * Return task.
   *
   * @param id - Payment ID
   * @return - Payment
   */
  Payment getTask(Long id);

}
