package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

@Slf4j
public class WithdrawMoneyTest {
  private final Gson gson = new Gson();
  private User user;
  private Account account1;
  private Model model;

  @Before
  public void init() {
    model = new ModelImpl();
    user = createUser(model, USER_JSON);
    log.info("New User: " + user);
    account1 = createAccount(Currency.EUR, user);
    log.info("Account1: " + account1);
  }

  private Account createAccount(Currency currency, User user) {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(currency);
    accountDTO.setUserId(user.getId());
    ResponseMessage responseMessage = model.createAccount(gson.toJson(accountDTO));
    log.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonObject jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                                          JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("Account");
    return gson.fromJson(jsonObject, Account.class);
  }

  // deposit money
  private void depositMoney(BigDecimal amount) {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(amount);
    paymentDTO.setDstAccount(account1.getNumber());
    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    log.info("Deposit money payment: " + responseMessage.getJsonMessage());
  }

  @Test
  public void withdrawMoneyEmptyUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount(account1.getNumber());

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_USER_ID, responseMessage.getStatus());
  }

  @Test
  public void withdrawMoneyFakeUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(987);
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount(account1.getNumber());

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.USER_DOES_NOT_EXIST, responseMessage.getStatus());
  }

  @Test
  public void withdrawMoneyEmptyAccountSrc() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_SRC_ACC_ID, responseMessage.getStatus());
  }

  @Test
  public void withdrawMoneyFakeAccountSrc() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setSrcAccount("FakeAccount");

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS, responseMessage.getStatus());
  }

  @Test
  public void withdrawMoneyNotEnoughMoney() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setSrcAccount(account1.getNumber());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.NOT_ENOUGH_MONEY_FOR_A_TRANSACTION,
                 responseMessage.getStatus());
  }

  @Test
  public void withdrawMoneyOK() {
    BigDecimal amount = BigDecimal.TEN;
    depositMoney(amount);

    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(amount);
    paymentDTO.setSrcAccount(account1.getNumber());

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

}
