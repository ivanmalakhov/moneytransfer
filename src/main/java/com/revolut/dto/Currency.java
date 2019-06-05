package com.revolut.dto;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Currency {
  RUR(810),
  EUR(978),
  USD(840);

  private final int currencyCode;

  private static final Map<String, Currency> CURRENCIES_BY_CODE = Stream.of(values())
          .collect(Collectors.toMap(Currency::code, e -> e));

  Currency(int currencyCode) {
    this.currencyCode = currencyCode;
  }
  public String code() {
    return name();
  }

  @Override
  public String toString() {
    return code() + "(" + currencyCode + ")";
  }
}
