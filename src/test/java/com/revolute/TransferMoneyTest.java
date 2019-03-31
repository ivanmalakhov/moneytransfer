package com.revolute;

import com.revolute.dto.PaymentRequest;
import com.revolute.handler.Answer;
import com.revolute.handler.payment.DepositMoneyHandler;
import com.revolute.handler.payment.TransferMoneyHandler;
import com.revolute.handler.payment.WithdrawMoneyHandler;
import com.revolute.model.Account;
import com.revolute.model.Currency;
import com.revolute.model.User;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TransferMoneyTest {
  private Model model;
  private Logger logger = Logger.getLogger(TransferMoneyTest.class);
  private TransferMoneyHandler transferMoneyHandler;
  private DepositMoneyHandler depositMoneyHandler;
  private WithdrawMoneyHandler withdrawMoneyHandler;
  private User user;
  private Account account1, account2;
  private static final int DELAY = 10;

  @Before
  public void init() {
    model = new ModelImpl();
    transferMoneyHandler = new TransferMoneyHandler(model);
    depositMoneyHandler = new DepositMoneyHandler(model);
    withdrawMoneyHandler = new WithdrawMoneyHandler(model);
    user = model.createUser(new User("Smith", "John"));
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

  @Test
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
      Answer answer = transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
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
  }


}
