package com.revolut;

import com.google.gson.Gson;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class ConcurrencyTest {
  private Model model;
  private final Logger logger = LoggerFactory.getLogger(ConcurrencyTest.class);
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
    logger.info("New User: " + user);

  }

  @Test
  public void test1() {
    createAccountAndDepositMoney(BigDecimal.TEN);
  }

  private void createAccountAndDepositMoney(BigDecimal amount) {
    for (int i = 0; i < ConcurrencyTest.N_ACCOUNTS; i++) {
      depositMoney(model.createAccount(Currency.EUR, user), amount);
    }
    accounts.addAll(model.getAccountsByUser(user.getId()));
  }

  private void depositMoney(Account account, BigDecimal amount) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(amount);
    paymentDTO.setDstAccount(account.getNumber());

    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    logger.info("Deposit money {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

/*
  @Test
  public void raceConditionTest() {
    createAccountAndDepositMoney(INITIAL_BALANCE);
    Answer answer = getTotalBalanceHandler.process(user, Collections.emptyMap());
    BigDecimal totalInitialBalance = new BigDecimal(answer.getBody());
    logger.info("Initial balance:" + totalInitialBalance);
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
          logger.error(e);
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
    Set<Account> accounts = model.getAccountsByUser(user.getId());
    BigDecimal sum = BigDecimal.ZERO;
    for (Account account : accounts) {
      sum = sum.add(account.getBalance());
    }
    logger.info("Итоговый баланс(проверка):" + sum);

  }

}
