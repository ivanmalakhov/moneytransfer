package com.revolut.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object with payment information.
 */
@Data
public class PaymentDTO extends AbstractDTO {
  /**
   * SrcAccount.
   */
  private String srcAccount;
  /**
   * DstAccount.
   */
  private String dstAccount;
  /**
   * Amount.
   */
  private BigDecimal amount;
}
