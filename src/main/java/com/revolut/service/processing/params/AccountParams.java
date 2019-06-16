package com.revolut.service.processing.params;


import com.revolut.entity.Account;
import lombok.Data;

/**
 * Account params.
 */
@Data
public class AccountParams extends Params {
  /**
   * Account.
   */
  private Account account;
}
