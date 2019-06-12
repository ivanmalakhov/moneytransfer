package com.revolut.dto;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
   * .....
   */
  private static final Map<String, Currency> CURRENCIES_BY_CODE =
          Stream.of(values())
                  .collect(Collectors.toMap(Currency::code, e -> e));

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
