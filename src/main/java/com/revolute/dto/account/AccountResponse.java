package com.revolute.dto.account;

import lombok.Data;

@Data
public class AccountResponse {
  private String accountId;

  public AccountResponse setAccountId(String accountId) {
    this.accountId = accountId;
    return this;
  }
}
