package com.revolute.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Data
public class Payment {
  private Integer id;
  private Account accountFrom;
  private Account accountTo;
  private Date dt;
  private BigDecimal amount;


  public Payment(Account accountFrom, Account accountTo, BigDecimal amount) {
    Random rand = new Random();
    this.id = rand.nextInt(Integer.MAX_VALUE);
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
    this.amount = amount;
    this.dt = new Date();
  }

  public void execute() {
    if (null != accountFrom) {
      accountFrom.setBalance(accountFrom.getBalance().subtract(this.getAmount()));
    }
    if (null != accountTo) {
      accountTo.setBalance(accountTo.getBalance().add(this.getAmount()));
    }

  }
}
