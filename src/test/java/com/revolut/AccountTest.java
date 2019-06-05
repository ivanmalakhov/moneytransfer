package com.revolut;

import com.revolut.dto.AccountRequest;
import com.revolut.handler.Answer;
import com.revolut.handler.account.AccountCreateHandler;
import com.revolut.handler.account.GetAccountByUserHandler;
import com.revolut.dto.Currency;
import com.revolut.dto.User;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AccountTest {
  private AccountCreateHandler accountCreateHandler;
  private GetAccountByUserHandler getAccountByUserHandler;
  private Logger logger = Logger.getLogger(AccountTest.class);
  private User user;

  @Before
  public void init() {
    Model model = new ModelImpl();
    accountCreateHandler = new AccountCreateHandler(model);
    getAccountByUserHandler = new GetAccountByUserHandler(model);
    user = model.createUser("Smith", "John");
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
  public void createAccountTestOK() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCurrency(Currency.EUR);
    accountRequest.setUser(user);
    Answer answer = accountCreateHandler.process(accountRequest, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }

  @Test
  public void noAccountsTest() {
    Answer answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(404, answer.getCode());
  }

  @Test
  public void getAllAccountTest() {
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setCurrency(Currency.EUR);
    accountRequest.setUser(user);
    Answer answer = accountCreateHandler.process(accountRequest, Collections.emptyMap());
    assertEquals(201, answer.getCode());

    answer = getAccountByUserHandler.process(user, Collections.emptyMap());
    assertEquals(201, answer.getCode());
  }
}
