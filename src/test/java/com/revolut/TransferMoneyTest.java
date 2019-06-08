package com.revolut;

import com.google.gson.Gson;
import com.revolut.data.Account;
import com.revolut.data.User;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentRequest;
import com.revolut.handler.Answer;
import com.revolut.handler.payment.DepositMoneyHandler;
import com.revolut.handler.payment.TransferMoneyHandler;
import com.revolut.handler.payment.WithdrawMoneyHandler;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class TransferMoneyTest {
  private Logger logger = Logger.getLogger(TransferMoneyTest.class);
  private TransferMoneyHandler transferMoneyHandler;
  private DepositMoneyHandler depositMoneyHandler;
  private WithdrawMoneyHandler withdrawMoneyHandler;

  private User user;
  private Account account1, account2;
  private static final int DELAY = 10;
  Model model;
  Gson gson = new Gson();

  @Before
  public void init() {
    model = new ModelImpl();
    transferMoneyHandler = new TransferMoneyHandler(model);
    depositMoneyHandler = new DepositMoneyHandler(model);
    withdrawMoneyHandler = new WithdrawMoneyHandler(model);
    user = createUser(model, USER_JSON);
    logger.info("New User: " + user);
    account1 = model.createAccount(Currency.EUR, user);
    logger.info("Account1: " + account1);
    account2 = model.createAccount(Currency.EUR, user);
    logger.info("Account2: " + account2);
  }

  // deposit money
  private void depositMoney(BigDecimal amount) {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(amount);
    paymentRequest.setDstAccount(account1.getNumber());
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    logger.info("Deposit money payment: " + answer.getBody());
  }

  // deposit money
  public void withdrawMoney(BigDecimal amount) {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(amount);
    paymentRequest.setSrcAccount(account2.getNumber());
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    logger.info("Deposit money payment: " + answer.getBody());
  }

  @Test
  public void transferMoneyEmptyUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount(account2.getNumber());
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty user ID", answer.getBody());
  }

  @Test
  public void transferMoneyFakeUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(987);
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount(account2.getNumber());
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("User not found", answer.getBody());

  }

  @Test
  public void transferMoneyEmptyAccountSrc() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setDstAccount(account2.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty source account ID", answer.getBody());
  }

  @Test
  public void transferMoneyEmptyAccountDst() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty destination account ID", answer.getBody());
  }

  @Test
  public void transferMoneyFakeAccountSrc() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount("FakeAccount");
    paymentRequest.setDstAccount(account2.getNumber());
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Source account doesn't exists", answer.getBody());
  }

  @Test
  public void transferMoneyFakeAccountDst() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount("FakeAccount");
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Destination account doesn't exists", answer.getBody());
  }

  @Test
  public void transferMoneyNotEnoughMoney() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount(account2.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Not enough money for a transaction", answer.getBody());
  }

  @Test
  public void transferMoneyOK() {
    BigDecimal amount = BigDecimal.ONE;
    depositMoney(amount);
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount(account2.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
    logger.info("TransferMoney: " + answer);
    assertEquals(201, answer.getCode());

    PaymentRequest withdrawRequest = new PaymentRequest();
    withdrawRequest.setUserId(user.getId());
    withdrawRequest.setAmount(amount);
    withdrawRequest.setSrcAccount(account2.getNumber());
    Answer withdrawAnswer = withdrawMoneyHandler.process(withdrawRequest, Collections.emptyMap());
    logger.info("WithdrawPayment: " + withdrawAnswer);
    assertEquals(201, withdrawAnswer.getCode());
  }

/*  @Test
  public void raceConditionTest() {
    int balance = 9999;
    BigDecimal amount = BigDecimal.valueOf(balance);
    depositMoney(amount);

    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setDstAccount(account2.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);

    Thread[] threads = new Thread[balance];
    Runnable r = () -> {
      try {
        Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
        logger.info(Thread.currentThread() + "; TotalBalance" + getTotalBalanceHandler.process(user, Collections.emptyMap()).getBody());
        sleep((int) (DELAY * Math.random()));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    };
    for (int i = 0; i < balance; i++) {
      threads[i] = new Thread(r);
      threads[i].start();
    }
    for (int i = 0; i < balance; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    logger.info("Итог: " + getTotalBalanceHandler.process(user, Collections.emptyMap()).getBody());

    Set<Account> accounts = model.getAccountListByUser(user.getId());
    BigDecimal sum = BigDecimal.ZERO;
    for (Account account : accounts) {
      sum = sum.add(account.getBalance());
    }
    assertEquals(amount, sum);

    PaymentRequest withdrawRequest = new PaymentRequest();
    withdrawRequest.setUserId(user.getId());
    withdrawRequest.setAmount(amount);
    withdrawRequest.setSrcAccount(account2.getNumber());
    Answer withdrawAnswer = withdrawMoneyHandler.process(withdrawRequest, Collections.emptyMap());
    logger.info("WithdrawPayment: " + withdrawAnswer);
    assertEquals(201, withdrawAnswer.getCode());
  }*/


}
