package com.revolut;

import com.google.gson.Gson;
import com.revolut.data.User;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.handler.Answer;
import com.revolut.handler.account.GetAccountByUserHandler;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class AccountTest {
  private GetAccountByUserHandler getAccountByUserHandler;
  private Logger logger = LoggerFactory.getLogger(AccountTest.class);
  private User user;
  private Gson gson = new Gson();
  Model model;

  @Before
  public void init() {
    model = new ModelImpl();
    getAccountByUserHandler = new GetAccountByUserHandler(model);
    user = createUser(model, USER_JSON);

  }

  @Test
  public void fakeUserTest() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(123);

    ResponseMessage responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.USER_DOES_NOT_EXIST, responseMessage.getStatus());

  }

  @Test
  public void createAccountTestOK() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(user.getId());
    ResponseMessage responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());
  }

  @Test
  public void noAccountsTest() {
    Answer answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(404, answer.getCode());
  }

  @Test
  public void getAllAccountTest() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(user.getId());

    ResponseMessage responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    Answer answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }
}
