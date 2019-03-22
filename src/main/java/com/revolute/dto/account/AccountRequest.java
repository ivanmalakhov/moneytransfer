package com.revolute.dto.account;

import com.revolute.model.Currency;
import com.revolute.model.User;
import com.revolute.dto.Validable;
import lombok.Data;

@Data
public class AccountRequest implements Validable {
  private Currency currency;
  private User user;

  @Override
  public boolean isValid() {
    return true;
  }
}
