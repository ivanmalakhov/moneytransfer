package com.revolut.dto;

import com.revolut.data.User;
import lombok.Data;

@Data
public class AccountRequest {
  private Currency currency;
  private User user;

}
