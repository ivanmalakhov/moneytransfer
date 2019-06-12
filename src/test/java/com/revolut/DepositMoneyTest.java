package com.revolut;

import com.google.gson.Gson;
import com.revolut.data.Account;
import com.revolut.data.User;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.TestJson.USER_JSON;
import static com.revolut.TestUtils.createUser;
import static org.junit.Assert.assertEquals;

public class DepositMoneyTest {
  private Logger logger = Logger.getLogger(TransferMoneyTest.class);
  private Gson gson = new Gson();
  private User user;
  private Account account1;
  private Model model;

  @Before
  public void init() {
    model = new ModelImpl();
    user = createUser(model, USER_JSON);
    logger.info("New User: " + user);
    account1 = model.createAccount(Currency.EUR, user);
    logger.info("Account1: " + account1);
  }

  // deposit money test. in the end we have 10000 eur on source account
  @Test
  public void depositMoneyEmptyUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setDstAccount(account1.getNumber());

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_USER_ID, responseMessage.getStatus());
  }

  @Test
  public void depositMoneyFakeUser() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(987);
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setDstAccount(account1.getNumber());

    ResponseMessage responseMessage = model.withdraw(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.USER_DOES_NOT_EXIST, responseMessage.getStatus());
  }

  @Test
  public void depositMoneyEmptyAccountDst() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);

    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.EMPTY_DEST_ACC_ID, responseMessage.getStatus());
  }

  @Test
  public void depositMoneyFakeAccountDst() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.ONE);
    paymentDTO.setDstAccount("FakeAccount");

    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS, responseMessage.getStatus());
  }

  @Test
  public void depositMoneyOK() {
    PaymentDTO paymentDTO = new PaymentDTO();
    paymentDTO.setUserId(user.getId());
    paymentDTO.setAmount(BigDecimal.valueOf(9999));
    paymentDTO.setDstAccount(account1.getNumber());

    ResponseMessage responseMessage = model.deposit(gson.toJson(paymentDTO));
    assertEquals(ResponseStatus.SUCCESS, responseMessage.getStatus());

  }
}
