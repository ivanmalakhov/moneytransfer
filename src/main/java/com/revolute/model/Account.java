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

  public Account(UUID id, String number, BigDecimal balance, User user, Currency currency) {
    this.id = id;
    this.number = number;
    this.balance = balance;
    this.user = user;
    this.currency = currency;
  }

  public Account() {
  }
}
