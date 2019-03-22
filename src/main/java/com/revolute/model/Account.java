package com.revolute.model;

import lombok.Data;
import java.util.UUID;


import java.math.BigDecimal;

@Data
public class Account {
  private UUID id;
  private String number;
  private BigDecimal balance;
  private User user;
  private Currency currency;
}
