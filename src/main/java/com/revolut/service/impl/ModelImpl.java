package com.revolut.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.dto.UserDTO;
import com.revolut.entity.Account;
import com.revolut.entity.User;
import com.revolut.service.AccountService;
import com.revolut.service.Model;
import com.revolut.service.PaymentParams;
import com.revolut.service.PaymentService;
import com.revolut.service.UserService;
import com.revolut.service.processing.CheckRequestStage;
import com.revolut.service.processing.CheckUserStage;
import com.revolut.service.processing.CreateDTOStage;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.payment.CheckAmountStage;
import com.revolut.service.processing.payment.CheckDstAccountStage;
import com.revolut.service.processing.payment.CheckSrcAccountStage;
import com.revolut.service.processing.payment.DepositMoneyStage;
import com.revolut.service.processing.payment.TransferMoneyStage;
import com.revolut.service.processing.payment.WithdrawMoneyStage;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Implementation model.
 */
@Slf4j
public final class ModelImpl implements Model {
  /**
   * Constant "User not found".
   */
  private static final String
          USER_NOT_FOUND = "User not found";
  /**
   * Constant "JSON parsing error".
   */
  private static final String JSON_PARSING_ERROR =
          "JSON parsing error. Input string: {} ;";
  /**
   * Constant "User doesn't exist".
   */
  private static final String USER_DOES_NOT_EXIST =
          "User with id={} doesn't exist";

  /**
   * Service for working with Payments.
   */
  private final PaymentService paymentService;
  /**
   * Service for working with Accounts.
   */
  private final AccountService accountService;
  /**
   * Service for working with Users.
   */
  private final UserService userService;

  /**
   * Main constructor.
   */
  public ModelImpl() {
    this.accountService = AccountServiceImpl.INSTANCE;
    this.paymentService = PaymentServiceImpl.INSTANCE;
    this.userService = UserServiceImpl.INSTANCE;
  }

  @Override
  public synchronized Account createAccount(final Currency currency,
                                            final User user) {
    if (userService.isUserNotExist(user)) {
      log.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    return accountService.create(currency, user);
  }

  @Override
  public ResponseMessage createAccount(final String data) {
    Gson gson = new Gson();
    log.info("Create new account Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    AccountDTO accountDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      accountDTO = gson.fromJson(jsonObject, AccountDTO.class);
    } catch (Exception e) {
      log.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    User user = userService.getUser(accountDTO.getUserId());
    if (user == null) {
      log.error(USER_DOES_NOT_EXIST, accountDTO.getUserId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }

    Account account = accountService.create(accountDTO.getCurrency(), user);
    if (account == null) {
      log.warn("Error creating new account.");
      responseMessage.setStatus(ResponseStatus.ACCOUNT_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(Account.class.getSimpleName(),
                   gson.toJsonTree(account, Account.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Create new account. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;

  }

  @Override
  public synchronized Set<Account> getAccountsByUser(final Integer userId) {
    if (userService.isUserNotExist(userId)) {
      log.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }

    Set<Account> accounts = accountService.getAccountListByUser(userId);
    if (null == accounts) {
      log.warn("User doesn't have account");
      throw new IllegalArgumentException("User doesn't have account");

    }
    return accounts;
  }

  @Override
  public ResponseMessage getAccountsByUser(final String data) {
    Gson gson = new Gson();
    log.info("Get user accounts Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    UserDTO userDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      userDTO = gson.fromJson(jsonObject, UserDTO.class);
    } catch (Exception e) {
      log.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    User user = userService.getUser(userDTO.getId());
    if (user == null) {
      log.error(USER_DOES_NOT_EXIST, userDTO.getId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    Set<Account> accounts = accountService.getAccountListByUser(user.getId());
    if (accounts == null) {
      log.warn("User does not have accounts.");
      responseMessage.setStatus(ResponseStatus.ACCOUNT_DOES_NOT_EXIST);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Accounts", gson.toJsonTree(accounts));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Create new account. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;

  }

  @Override
  public synchronized ResponseMessage transferMoney(final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(userService))
            .linkWith(new CheckDstAccountStage(accountService))
            .linkWith(new CheckSrcAccountStage(accountService))
            .linkWith(new CheckAmountStage())
            .linkWith(new TransferMoneyStage(paymentService));
    StageData stageData = new StageData(data,
                                        new PaymentDTO(),
                                        new PaymentParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public synchronized ResponseMessage deposit(final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(userService))
            .linkWith(new CheckDstAccountStage(accountService))
            .linkWith(new CheckAmountStage())
            .linkWith(new DepositMoneyStage(paymentService));
    StageData stageData = new StageData(data,
                                        new PaymentDTO(),
                                        new PaymentParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public synchronized ResponseMessage withdraw(final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(userService))
            .linkWith(new CheckSrcAccountStage(accountService))
            .linkWith(new CheckAmountStage())
            .linkWith(new WithdrawMoneyStage(paymentService));
    StageData stageData = new StageData(data,
                                        new PaymentDTO(),
                                        new PaymentParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage createUser(final String data) {
    Gson gson = new Gson();
    log.info("Create new client. Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    UserDTO userDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      userDTO = gson.fromJson(jsonObject, UserDTO.class);
    } catch (Exception e) {
      log.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    User user = userService.create(userDTO.getFirstName(),
                                   userDTO.getLastName());
    if (user == null) {
      log.warn("Error creating new client.");
      responseMessage.setStatus(ResponseStatus.USER_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(User.class.getSimpleName(),
                   gson.toJsonTree(user, User.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Create new client. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;
  }

  @Override
  public ResponseMessage getAccount(final String data) {
    Gson gson = new Gson();
    log.info("Get account balance. Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    AccountDTO accountDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      accountDTO = gson.fromJson(jsonObject, AccountDTO.class);
    } catch (Exception e) {
      log.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    User user = userService.getUser(accountDTO.getUserId());
    if (user == null) {
      log.error(USER_DOES_NOT_EXIST, accountDTO.getUserId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }

    Account account = accountService.getAccountById(user.getId(),
                                                    accountDTO.getNumber());
    if (account == null) {
      log.warn("Account with number {} does not exist.",
               accountDTO.getNumber());
      responseMessage.setStatus(ResponseStatus.ACCOUNT_DOES_NOT_EXIST);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add(Account.class.getSimpleName(),
                   gson.toJsonTree(account, Account.class));
    responseMessage.setJsonMessage(jsonObject);
    log.info("Account information. Response: {}",
             responseMessage.getJsonMessage());
    return responseMessage;
  }
}
