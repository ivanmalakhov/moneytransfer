package com.revolut.dto;


/**
 * Enum with currency.
 */
public enum Currency {
  /**
   * Russian ruble.
   */
  RUR(810),
  /**
   * Euro.
   */
  EUR(978),
  /**
   * US Dollar.
   */
  USD(840);

  /**
   * Currency code.
   */
  private final int currencyCode;

  /**
   * Constructor.
   *
   * @param code - код валюты
   */
  Currency(final int code) {
    this.currencyCode = code;
  }

  /**
   * Get enum name.
   *
   * @return String
   */
  public String code() {
    return name();
  }

  @Override
  public String toString() {
    return code() + "(" + currencyCode + ")";
  }
}
