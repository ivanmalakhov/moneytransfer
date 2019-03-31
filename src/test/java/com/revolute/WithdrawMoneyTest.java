package com.revolute;

import com.revolute.dto.PaymentRequest;
import com.revolute.handler.Answer;
import com.revolute.handler.payment.DepositMoneyHandler;
import com.revolute.handler.payment.WithdrawMoneyHandler;
import com.revolute.dto.Account;
import com.revolute.dto.Currency;
import com.revolute.dto.User;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WithdrawMoneyTest {
  private static final String EMPTY_SOURCE_ACCOUNT_ID = "Empty source account ID";
  private static final String NOT_ENOUGH_MONEY_FOR_A_TRANSACTION = "Not enough money for a transaction";
  private Model model;
  private Logger logger = Logger.getLogger(WithdrawMoneyTest.class);
  private DepositMoneyHandler depositMoneyHandler;
  private WithdrawMoneyHandler withdrawMoneyHandler;
  private User user;
  private Account account1;

  @Before
  public void init() {
    model = new ModelImpl();
    depositMoneyHandler = new DepositMoneyHandler(model);
    withdrawMoneyHandler = new WithdrawMoneyHandler(model);
    user = model.createUser(new User("Smith", "John"));
    logger.info("New User: " + user);
    account1 = model.createAccount(Currency.EUR, user);
    logger.info("Account1: " + account1);
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

  @Test
  public void withdrawMoneyEmptyUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount(account1.getNumber());
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty user ID", answer.getBody());
  }

  @Test
  public void withdrawMoneyFakeUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(987);
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount(account1.getNumber());
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("User not found", answer.getBody());

  }

  @Test
  public void withdrawMoneyEmptyAccountSrc() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals(EMPTY_SOURCE_ACCOUNT_ID, answer.getBody());
  }

  @Test
  public void withdrawMoneyFakeAccountSrc() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setSrcAccount("FakeAccount");
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Source account doesn't exists", answer.getBody());
  }

  @Test
  public void withdrawMoneyNotEnoughMoney() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setSrcAccount(account1.getNumber());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = withdrawMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals(NOT_ENOUGH_MONEY_FOR_A_TRANSACTION, answer.getBody());
  }

  @Test
  public void withdrawMoneyOK() {
    BigDecimal amount = BigDecimal.TEN;
    depositMoney(amount);

    PaymentRequest withdrawRequest = new PaymentRequest();
    withdrawRequest.setUserId(user.getId());
    withdrawRequest.setAmount(amount);
    withdrawRequest.setSrcAccount(account1.getNumber());
    Answer withdrawAnswer = withdrawMoneyHandler.process(withdrawRequest, Collections.emptyMap());
    logger.info("WithdrawPayment: " + withdrawAnswer);
    assertEquals(201, withdrawAnswer.getCode());
  }

}
