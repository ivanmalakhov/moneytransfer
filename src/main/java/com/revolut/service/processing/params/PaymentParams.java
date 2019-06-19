package com.revolut.service.processing.params;

import com.revolut.entity.Account;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Payment parameters.
 */
@Data
public class PaymentParams extends Params {
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
