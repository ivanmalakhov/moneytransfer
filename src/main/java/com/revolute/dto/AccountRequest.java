package com.revolute.dto;

import com.revolute.model.Currency;
import com.revolute.model.User;
import lombok.Data;

@Data
public class AccountRequest {
  private Currency currency;
  private User user;

}
