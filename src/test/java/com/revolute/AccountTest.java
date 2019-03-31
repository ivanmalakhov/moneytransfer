package com.revolute;

import com.revolute.dto.AccountRequest;
import com.revolute.handler.Answer;
import com.revolute.handler.account.AccountCreateHandler;
import com.revolute.handler.account.GetAccountByUserHandler;
import com.revolute.dto.Currency;
import com.revolute.dto.User;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AccountTest {
  private Model model;
  private AccountCreateHandler accountCreateHandler;
  private GetAccountByUserHandler getAccountByUserHandler;
  private Logger logger = Logger.getLogger(AccountTest.class);
  private User user;

  @Before
  public void init() {
    model = new ModelImpl();
    accountCreateHandler = new AccountCreateHandler(model);
    getAccountByUserHandler = new GetAccountByUserHandler(model);
    user = model.createUser(new User("Smith", "John"));
  }

  @Test
  public void fakeUserTest() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCurrency(Currency.EUR);
    accountRequest.setUser(new User("Smith", "John"));
    Answer answer = accountCreateHandler.process(accountRequest, Collections.emptyMap());
    assertEquals(404, answer.getCode());
  }

  @Test
  public void CreateAccountTestOK() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCurrency(Currency.EUR);
    accountRequest.setUser(user);
    Answer answer = accountCreateHandler.process(accountRequest, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }

  @Test
  public void NoAccountsTest() {
    Answer answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(404, answer.getCode());
  }

  @Test
  public void GetAllAccountTest() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCurrency(Currency.EUR);
    accountRequest.setUser(user);
    Answer answer = accountCreateHandler.process(accountRequest, Collections.emptyMap());
    assertEquals(201, answer.getCode());

    answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }
}
