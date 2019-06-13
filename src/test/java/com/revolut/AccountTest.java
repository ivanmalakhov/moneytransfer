package com.revolut;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.dto.UserDTO;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class AccountTest {
  private final Logger logger = LoggerFactory.getLogger(AccountTest.class);
  private User user;
  private final Gson gson = new Gson();
  private Model model;

  @Before
  public void init() {
    model = new ModelImpl();
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
    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());
    ResponseMessage responseMessage = model.getAccountsByUser(gson.toJson(userDTO));
    logger.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.ACCOUNT_DOES_NOT_EXIST, responseMessage.getStatus());
  }

  @Test
  public void getAccount() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(user.getId());

    ResponseMessage responseMessage;
    responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonObject jsonObject = gson.fromJson(responseMessage.getJsonMessage(),
                                          JsonObject.class)
            .getAsJsonObject("Info").getAsJsonObject("Account");
    Account account = gson.fromJson(jsonObject, Account.class);

    AccountDTO getAccountDto = new AccountDTO();
    getAccountDto.setNumber(account.getNumber());
    getAccountDto.setUserId(user.getId());
    responseMessage = model.getAccount(gson.toJson(getAccountDto));
    logger.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

  }

  @Test
  public void getAccountFail() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(user.getId());

    ResponseMessage responseMessage;
    responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    AccountDTO getAccountDto = new AccountDTO();
    getAccountDto.setNumber("qqe");
    getAccountDto.setUserId(user.getId());
    responseMessage = model.getAccount(gson.toJson(getAccountDto));
    logger.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.ACCOUNT_DOES_NOT_EXIST, responseMessage.getStatus());

  }

  @Test
  public void getAllAccountTest() {
    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setCurrency(Currency.EUR);
    accountDTO.setUserId(user.getId());

    ResponseMessage responseMessage;
    responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    responseMessage = model.createAccount(gson.toJson(accountDTO));
    logger.info("New account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());

    responseMessage = model.getAccountsByUser(gson.toJson(userDTO));
    logger.info("Account: {}", responseMessage.getJsonMessage());
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

    JsonArray jsonArray = gson.fromJson(responseMessage.getJsonMessage(),
                                        JsonObject.class)
            .getAsJsonObject("Info")
            .getAsJsonArray("Accounts");
    Set<Account> accounts = new HashSet<>();
    for (JsonElement jsonElement : jsonArray) {
      accounts.add(gson.fromJson(jsonElement, Account.class));
    }
    assertEquals(2, accounts.size());

  }
}
