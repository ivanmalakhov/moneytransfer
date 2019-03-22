package com.revolute.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Payment {
  private Integer id;
  private Account accountFrom;
  private Account accountTo;
  private Date dt;
  private BigDecimal amount;
}
