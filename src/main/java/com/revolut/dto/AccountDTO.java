package com.revolut.dto;

import lombok.Data;

import java.util.UUID;

/**
 * Data transfer object with new account information.
 */
@Data
public class AccountDTO implements AbstractDTO {
  /**
   * Unique id.
   */
  private UUID id;
  /**
   * Account number.
   */
  private String number;
  /**
   * Account currency.
   */
  private Currency currency;
  /**
   * Owner id.
   */
  private Integer userId;


}
