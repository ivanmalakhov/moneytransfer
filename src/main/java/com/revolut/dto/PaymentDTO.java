package com.revolut.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object with payment information.
 */
@Data
public class PaymentDTO implements AbstractDTO {
  /**
   * UserId.
   */
  private Integer userId;
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
