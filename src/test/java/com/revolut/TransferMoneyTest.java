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
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class TransferMoneyTest {
  private final Logger logger = Logger.getLogger(TransferMoneyTest.class);

  private User user;
  private Account account1, account2;
  private static final int DELAY = 10;
  private Model model;
  private final Gson gson = new Gson();

  @Before
  public void init() {
    model = new ModelImpl();
    user = createUser(model, USER_JSON);
    logger.info("New User: " + user);
    account1 = model.createAccount(Currency.EUR, user);
    logger.info("Account1: " + account1);
    account2 = model.createAccount(Currency.EUR, user);
    logger.info("Account2: " + account2);
  }

  // deposit money
  private void depositMoney(BigDecimal amount) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(amount);
    paymentDTO.setDstAccount(account1.getNumber());

    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyEmptyUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setDstAccount(account2.getNumber());

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_USER_ID, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyFakeUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(987);
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setDstAccount(account2.getNumber());

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.USER_DOES_NOT_EXIST, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyEmptyAccountSrc() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setDstAccount(account2.getNumber());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_SRC_ACC_ID, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyEmptyAccountDst() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_DEST_ACC_ID, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyFakeAccountSrc() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount("FakeAccount");
    paymentDTO.setDstAccount(account2.getNumber());

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyFakeAccountDst() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setDstAccount("FakeAccount");

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS, responseMessage.getStatus());
  }

  @Test
  public void transferMoneyNotEnoughMoney() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setDstAccount(account2.getNumber());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.NOT_ENOUGH_MONEY_FOR_A_TRANSACTION,
                 responseMessage.getStatus());
  }

  @Test
  public void transferMoneyOK() {
    BigDecimal amount = BigDecimal.ONE;
    depositMoney(amount);
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setDstAccount(account2.getNumber());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage;
    responseMessage = model.transferMoney(gson.toJson(paymentDTO));
    logger.info("TransferMoney: " + responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    PaymentDTO withdrawRequest = new PaymentDTO();
    withdrawRequest.setUserId(user.getId());
    withdrawRequest.setAmount(amount);
    withdrawRequest.setSrcAccount(account2.getNumber());

    responseMessage = model.withdraw(gson.toJson(paymentDTO));
    logger.info("WithdrawPayment: " + responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.NOT_ENOUGH_MONEY_FOR_A_TRANSACTION,
                 responseMessage.getStatus());
  }

/*  @Test
  public void raceConditionTest() {
    int balance = 9999;
    BigDecimal amount = BigDecimal.valueOf(balance);
    depositMoney(amount);

    PaymentDTO paymentRequest = new PaymentDTO();
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

    PaymentDTO withdrawRequest = new PaymentDTO();
    withdrawRequest.setUserId(user.getId());
    withdrawRequest.setAmount(amount);
    withdrawRequest.setSrcAccount(account2.getNumber());
    Answer withdrawAnswer = withdrawMoneyHandler.process(withdrawRequest, Collections.emptyMap());
    logger.info("WithdrawPayment: " + withdrawAnswer);
    assertEquals(201, withdrawAnswer.getCode());
  }*/


}
