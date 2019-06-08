package com.revolut.dto;

import lombok.Data;

/**
 * Data transfer object with new account information.
 */
@Data
public class AccountDTO {
  /**
   * Account currency.
   */
  private Currency currency;
  /**
   * Owner id.
   */
  private Integer userId;


}
