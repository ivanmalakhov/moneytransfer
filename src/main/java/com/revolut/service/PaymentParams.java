package com.revolut.service;

import com.revolut.entity.Account;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Payment parameters.
 */
@Data
public class PaymentParams {
  /**
   * Source account.
   */
  private Account srcAccount;
  /**
   * Destination account.
   */
  private Account dstAccount;
  /**
   * Amount.
   */
  private BigDecimal amount;
}
