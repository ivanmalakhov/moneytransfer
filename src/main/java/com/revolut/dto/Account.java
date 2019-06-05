package com.revolut.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Account class.
 */
@Data
public class Account {
  /**
   * Unique id.
   */
  private UUID id;
  /**
   * Account number.
   */
  private String number;
  /**
   * Account Balance.
   */
  private BigDecimal balance;
  /**
   * Account user.
   */
  private User user;
  /**
   * Account currency.
   */
  private Currency currency;

  /**
   * Constructor.
   *
   * @param accountId       - id
   * @param accountNumber   - number
   * @param accountBalance  - balance
   * @param accountUser     - user
   * @param accountCurrency - currency
   */
  public Account(final UUID accountId,
                 final String accountNumber,
                 final BigDecimal accountBalance,
                 final User accountUser,
                 final Currency accountCurrency) {
    this.id = accountId;
    this.number = accountNumber;
    this.balance = accountBalance;
    this.user = accountUser;
    this.currency = accountCurrency;
  }

}
