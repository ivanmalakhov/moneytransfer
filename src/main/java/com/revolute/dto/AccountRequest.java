package com.revolute.dto;

import lombok.Data;

@Data
public class AccountRequest {
  private Currency currency;
  private User user;

}
