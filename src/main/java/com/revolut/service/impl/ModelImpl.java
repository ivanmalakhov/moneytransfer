package com.revolut.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.data.Account;
import com.revolut.data.Payment;
import com.revolut.data.User;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.Currency;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.dto.UserDTO;
import com.revolut.service.AccountService;
import com.revolut.service.Model;
import com.revolut.service.PaymentService;
import com.revolut.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.StringUtils;

import java.util.Set;

/**
 * Implementation model.
 */
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
   * Logger.
   */
  private final Logger logger = LoggerFactory.getLogger(ModelImpl.class);

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
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }
    return accountService.create(currency, user);
  }

  @Override
  public ResponseMessage createAccount(final String data) {
    Gson gson = new Gson();
    logger.info("Create new account Request: {}", data);
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
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    User user = userService.getUser(accountDTO.getUserId());
    if (user == null) {
      logger.error(USER_DOES_NOT_EXIST, accountDTO.getUserId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }

    Account account = accountService.create(accountDTO.getCurrency(), user);
    if (account == null) {
      logger.warn("Error creating new account.");
      responseMessage.setStatus(ResponseStatus.ACCOUNT_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Account", gson.toJsonTree(account, Account.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Create new account. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;

  }

  @Override
  public synchronized Set<Account> getAccountsByUser(final Integer userId) {
    if (userService.isUserNotExist(userId)) {
      logger.error(USER_NOT_FOUND);
      throw new IllegalArgumentException(USER_NOT_FOUND);
    }

    Set<Account> accounts = accountService.getAccountListByUser(userId);
    if (null == accounts) {
      logger.warn("User doesn't have account");
      throw new IllegalArgumentException("User doesn't have account");

    }
    return accounts;
  }

  @Override
  public ResponseMessage getAccountsByUser(final String data) {
    Gson gson = new Gson();
    logger.info("Get user accounts Request: {}", data);
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
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    User user = userService.getUser(userDTO.getId());
    if (user == null) {
      logger.error(USER_DOES_NOT_EXIST, userDTO.getId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    Set<Account> accounts = accountService.getAccountListByUser(user.getId());
    if (accounts == null) {
      logger.warn("User does not have accounts.");
      responseMessage.setStatus(ResponseStatus.ACCOUNT_DOES_NOT_EXIST);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Accounts", gson.toJsonTree(accounts));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Create new account. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;

  }

  @Override
  public synchronized ResponseMessage transferMoney(final String data) {
    Gson gson = new Gson();
    logger.info("Transfer money. Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    PaymentDTO paymentDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      paymentDTO = gson.fromJson(jsonObject, PaymentDTO.class);
    } catch (Exception e) {
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    if (StringUtils.isBlank(paymentDTO.getDstAccount())) {
      logger.error(ResponseStatus.EMPTY_DEST_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_DEST_ACC_ID);
      return responseMessage;
    }

    if (StringUtils.isBlank(paymentDTO.getSrcAccount())) {
      logger.error(ResponseStatus.EMPTY_SRC_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_SRC_ACC_ID);
      return responseMessage;
    }

    if (paymentDTO.getUserId() == null) {
      logger.error(ResponseStatus.EMPTY_USER_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_USER_ID);
      return responseMessage;
    }
    if (userService.isUserNotExist(paymentDTO.getUserId())) {
      logger.error(ResponseStatus.USER_DOES_NOT_EXIST.getDescription());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    Account srcAccount = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getSrcAccount());
    if (null == srcAccount) {
      logger.error(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }
    Account dstAccount = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getDstAccount());
    if (null == dstAccount) {
      logger.error(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }
    if (srcAccount.getBalance().compareTo(paymentDTO.getAmount()) < 0) {
      logger.error(ResponseStatus
                           .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION
                           .getDescription());
      responseMessage.setStatus(ResponseStatus
                                        .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
      return responseMessage;
    }

    Payment payment = paymentService.deposit(dstAccount,
                                             paymentDTO.getAmount());
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Payment", gson.toJsonTree(payment, Payment.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Transfer Money . Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;
  }

  @Override
  public synchronized ResponseMessage deposit(final String data) {
    Gson gson = new Gson();
    logger.info("Deposit account. Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    PaymentDTO paymentDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      paymentDTO = gson.fromJson(jsonObject, PaymentDTO.class);
    } catch (Exception e) {
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    if (StringUtils.isBlank(paymentDTO.getDstAccount())) {
      logger.error(ResponseStatus.EMPTY_DEST_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_DEST_ACC_ID);
      return responseMessage;

    }
    if (paymentDTO.getUserId() == null) {
      logger.error(ResponseStatus.EMPTY_USER_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_USER_ID);
      return responseMessage;
    }
    if (userService.isUserNotExist(paymentDTO.getUserId())) {
      logger.error(ResponseStatus.USER_DOES_NOT_EXIST.getDescription());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    Account dstAccount = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getDstAccount());
    if (null == dstAccount) {
      logger.error(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.DEST_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }

    Payment payment = paymentService.deposit(dstAccount,
                                             paymentDTO.getAmount());
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Payment", gson.toJsonTree(payment, Payment.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Deposit Account. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;
  }

  @Override
  public synchronized ResponseMessage withdraw(final String data) {
    Gson gson = new Gson();
    logger.info("Transfer money. Request: {}", data);
    ResponseMessage responseMessage = new ResponseMessage();
    if (data.isEmpty()) {
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    PaymentDTO paymentDTO;
    try {
      JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
      paymentDTO = gson.fromJson(jsonObject, PaymentDTO.class);
    } catch (Exception e) {
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    if (paymentDTO.getUserId() == null) {
      logger.error(ResponseStatus.EMPTY_USER_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_USER_ID);
      return responseMessage;
    }
    if (userService.isUserNotExist(paymentDTO.getUserId())) {
      logger.error(ResponseStatus.USER_DOES_NOT_EXIST.getDescription());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }
    if (StringUtils.isBlank(paymentDTO.getSrcAccount())) {
      logger.error(ResponseStatus.EMPTY_SRC_ACC_ID.getDescription());
      responseMessage.setStatus(ResponseStatus.EMPTY_SRC_ACC_ID);
      return responseMessage;
    }
    Account srcAccount = accountService.getAccountById(
            paymentDTO.getUserId(),
            paymentDTO.getSrcAccount());
    if (null == srcAccount) {
      logger.error(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS.getDescription());
      responseMessage.setStatus(ResponseStatus.SRC_ACC_DOES_NOT_EXISTS);
      return responseMessage;
    }
    if (srcAccount.getBalance().compareTo(paymentDTO.getAmount()) < 0) {
      logger.error(ResponseStatus
                           .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION
                           .getDescription());
      responseMessage.setStatus(ResponseStatus
                                        .NOT_ENOUGH_MONEY_FOR_A_TRANSACTION);
      return responseMessage;
    }

    Payment payment = paymentService.withdraw(srcAccount,
                                              paymentDTO.getAmount());
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Payment", gson.toJsonTree(payment, Payment.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Withdraw. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;
  }

  @Override
  public ResponseMessage createUser(final String data) {
    Gson gson = new Gson();
    logger.info("Create new client. Request: {}", data);
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
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }
    User user = userService.create(userDTO.getFirstName(),
                                   userDTO.getLastName());
    if (user == null) {
      logger.warn("Error creating new client.");
      responseMessage.setStatus(ResponseStatus.USER_CREATE_ERROR);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("User", gson.toJsonTree(user, User.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Create new client. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;
  }

  @Override
  public ResponseMessage getAccount(final String data) {
    Gson gson = new Gson();
    logger.info("Get account balance. Request: {}", data);
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
      logger.error(JSON_PARSING_ERROR, data, e);
      responseMessage.setStatus(ResponseStatus.BAD_REQUEST);
      return responseMessage;
    }

    User user = userService.getUser(accountDTO.getUserId());
    if (user == null) {
      logger.error(USER_DOES_NOT_EXIST, accountDTO.getUserId());
      responseMessage.setStatus(ResponseStatus.USER_DOES_NOT_EXIST);
      return responseMessage;
    }

    Account account = accountService.getAccountById(user.getId(),
                                                    accountDTO.getNumber());
    if (account == null) {
      logger.warn("Account with number {} does not exist.",
                  accountDTO.getNumber());
      responseMessage.setStatus(ResponseStatus.ACCOUNT_DOES_NOT_EXIST);
      return responseMessage;
    }
    responseMessage.setStatus(ResponseStatus.SUCCESS);
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("Account", gson.toJsonTree(account, Account.class));
    responseMessage.setJsonMessage(jsonObject);
    logger.info("Account information. Response: {}",
                responseMessage.getJsonMessage());
    return responseMessage;
  }
}
