package com.revolut.service.processing.account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.revolut.dto.AccountDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.ResponseStatus;
import com.revolut.entity.Account;
import com.revolut.service.AccountService;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.params.AccountParams;
import lombok.extern.slf4j.Slf4j;

/**
 * Create account stage.
 */
@Slf4j
public class CreateAccountStage extends ProcessingStage {
  /**
   * Account service.
   */
  private AccountService accountService;

  /**
   * Constructor.
   *
   * @param service - account service.
   */
  public CreateAccountStage(final AccountService service) {
    this.accountService = service;
  }

  /**
   * Create account.
   *
   * @param data - request
   * @return ResponseMessage
   */
  @Override
  public ResponseMessage performOperation(final StageData data) {
    Gson gson = new Gson();
    ResponseMessage responseMessage = new ResponseMessage();
    AccountDTO accountDTO = (AccountDTO) data.getDto();
    AccountParams accountParams = (AccountParams) data.getParams();

    Account account = accountService.create(accountDTO.getCurrency(),
                                            accountParams.getUser());
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
}
