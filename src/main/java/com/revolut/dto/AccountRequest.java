package com.revolut.dto;

import lombok.Data;

@Data
public class AccountRequest {
  private Currency currency;
  private User user;

}
