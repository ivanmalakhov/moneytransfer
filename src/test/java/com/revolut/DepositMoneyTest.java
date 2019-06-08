package com.revolut;

import com.revolut.dto.PaymentRequest;
import com.revolut.handler.Answer;
import com.revolut.handler.payment.DepositMoneyHandler;
import com.revolut.dto.Account;
import com.revolut.dto.Currency;
import com.revolut.data.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class DepositMoneyTest {
  private Logger logger = Logger.getLogger(TransferMoneyTest.class);
  private DepositMoneyHandler depositMoneyHandler;
  private User user;
  private Account account1;

  @Before
  public void init() {
    Model model = new ModelImpl();
    depositMoneyHandler = new DepositMoneyHandler(model);
    user = model.createUser("Smith", "John");
    logger.info("New User: " + user);
    account1 = model.createAccount(Currency.EUR, user);
    logger.info("Account1: " + account1);
  }

  // deposit money test. in the end we have 10000 eur on source account
  @Test
  public void depositMoneyEmptyUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setDstAccount(account1.getNumber());
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty user ID", answer.getBody());
  }

  @Test
  public void depositMoneyFakeUser() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(987);
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setDstAccount(account1.getNumber());
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("User not found", answer.getBody());

  }

  @Test
  public void depositMoneyEmptyAccountDst() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Empty destination account ID", answer.getBody());
  }

  @Test
  public void depositMoneyFakeAccountDst() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.ONE);
    paymentRequest.setDstAccount("FakeAccount");
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
    assertEquals("Destination account doesn't exists", answer.getBody());
  }

  @Test
  public void depositMoneyOK() {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(BigDecimal.valueOf(9999));
    paymentRequest.setDstAccount(account1.getNumber());
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }
}
