package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.dto.UserDTO;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

@Slf4j
public class ConcurrencyTest {
  private Model model;
  private User user;
  private static final int N_ACCOUNTS = 1000;
  private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(10000);
  private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10);
  private static final int DELAY = 10;
  private final List<Account> accounts = new ArrayList<>();
  private final Gson gson = new Gson();

  @Before
  public void init() {
    model = new ModelImpl();
    user = createUser(model, USER_JSON);
    log.info("New User: " + user);

  }

  @Test
  public void test1() {
    createAccountAndDepositMoney(BigDecimal.TEN);
  }

  private void createAccountAndDepositMoney(BigDecimal amount) {
    for (int i = 0; i < ConcurrencyTest.N_ACCOUNTS; i++) {
      Account account = createAccount(Currency.EUR, user);
      depositMoney(account, amount);
    }
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(user.getId());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());

    ResponseMessage responseMessage = model.getAccountsByUser(user.getId().toString(),
                                                              gson.toJson(userDTO));
    log.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonArray jsonArray = gson.fromJson(responseMessage.getJsonMessage(),
                                        JsonObject.class)
            .getAsJsonObject("Info")
            .getAsJsonArray("Accounts");
    Set<Account> accounts = new HashSet<>();
    for (JsonElement jsonElement : jsonArray) {
      accounts.add(gson.fromJson(jsonElement, Account.class));
    }
  }

  private Account createAccount(Currency currency, User user) {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(currency);
    accountDTO.setUserId(user.getId());
    ResponseMessage responseMessage = model.createAccount(user.getId().toString(),
                                                          gson.toJson(accountDTO));
    log.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonObject jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                                          JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("Account");
    return gson.fromJson(jsonObject, Account.class);
  }

  private void depositMoney(Account account, BigDecimal amount) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(amount);
    paymentDTO.setDstAccount(account.getNumber());

    ResponseMessage responseMessage = model.deposit(user.getId().toString(),
                                                    gson.toJson(paymentDTO));
    log.info("Deposit money {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

/*
  @Test
  public void raceConditionTest() {
    createAccountAndDepositMoney(INITIAL_BALANCE);
    Answer answer = getTotalBalanceHandler.process(user, Collections.emptyMap());
    BigDecimal totalInitialBalance = new BigDecimal(answer.getBody());
    log.info("Initial balance:" + totalInitialBalance);
    Thread[] threads = new Thread[N_ACCOUNTS];
    final AssertionError[] exc = new AssertionError[1];
    for (int i = 0; i < N_ACCOUNTS; i++) {
      Account fromAccount = accounts.get(i);
      Runnable r = () -> {
        try {
          Account toAccount = accounts.get((int) (accounts.size() * Math.random()));
          BigDecimal amount = MAX_AMOUNT.multiply(BigDecimal.valueOf(Math.random()));
          PaymentDTO paymentRequest = new PaymentDTO();
          paymentRequest.setUserId(user.getId());
          paymentRequest.setSrcAccount(fromAccount.getNumber());
          paymentRequest.setDstAccount(toAccount.getNumber());
          paymentRequest.setAmount(amount);
          transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
          BigDecimal balanceAfterPayment = new BigDecimal(getTotalBalanceHandler.process(user, Collections.emptyMap()).getBody());
          assertEquals("Total balance was changed", 0, totalInitialBalance.compareTo(balanceAfterPayment));
          sleep((int) (DELAY * Math.random()));
        } catch (InterruptedException e) {
          log.error(e);
        } catch (AssertionError e) {
          exc[0] = e;
        }

      };
      threads[i] = new Thread(r);
      threads[i].start();
    }
    for (int i = 0; i < N_ACCOUNTS; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (exc[0] != null) {
      throw exc[0];
    }
  }
*/

  @After
  public void after() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(user.getId());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());

    ResponseMessage responseMessage = model.getAccountsByUser(user.getId().toString(),
                                                              gson.toJson(userDTO));
    log.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonArray jsonArray = gson.fromJson(responseMessage.getJsonMessage(),
                                        JsonObject.class)
            .getAsJsonObject("Info")
            .getAsJsonArray("Accounts");
    Set<Account> accounts = new HashSet<>();
    BigDecimal sum = BigDecimal.ZERO;

    for (JsonElement jsonElement : jsonArray) {
      accounts.add(gson.fromJson(jsonElement, Account.class));
    }

    log.info("Итоговый баланс(проверка):" + sum);

  }

}
