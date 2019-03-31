package com.revolute.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
  private Integer userId;
  private String srcAccount;
  private String dstAccount;
  private BigDecimal amount;
}
