package com.revolut.service.processing.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.service.AccountService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.params.Params;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Get user accounts.
 */
@Slf4j
public class GetUserAccountsStage extends ProcessingStage {
  /**
   * User service.
   */
  private final AccountService accountService;

  /**
   * Constructor.
   *
   * @param service - user service
   */
  public GetUserAccountsStage(final AccountService service) {
    this.accountService = service;
  }

  /**
   * Get user account.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();

    Params userParams = data.getParams();

    Set<Account> accounts = accountService.getAccountListByUser(
            userParams.getUser());
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
}
