package com.revolut.service.impl;

import com.revolut.dto.AccountDTO;
import com.revolut.dto.PaymentDTO;
import com.revolut.dto.ResponseMessage;
import com.revolut.dto.UserDTO;
import com.revolut.service.AccountService;
import com.revolut.service.Model;
import com.revolut.service.PaymentService;
import com.revolut.service.UserService;
import com.revolut.service.processing.CheckRequestStage;
import com.revolut.service.processing.CheckUserStage;
import com.revolut.service.processing.CreateDTOStage;
import com.revolut.service.processing.ProcessingStage;
import com.revolut.service.processing.StageData;
import com.revolut.service.processing.account.CreateAccountStage;
import com.revolut.service.processing.account.GetAccountStage;
import com.revolut.service.processing.params.AccountParams;
import com.revolut.service.processing.params.Params;
import com.revolut.service.processing.params.PaymentParams;
import com.revolut.service.processing.payment.CheckAmountStage;
import com.revolut.service.processing.payment.CheckDstAccountStage;
import com.revolut.service.processing.payment.CheckSrcAccountStage;
import com.revolut.service.processing.payment.DepositMoneyStage;
import com.revolut.service.processing.payment.TransferMoneyStage;
import com.revolut.service.processing.payment.WithdrawMoneyStage;
import com.revolut.service.processing.user.CreateUserStage;
import com.revolut.service.processing.user.GetUserAccountsStage;
import com.revolut.service.processing.user.GetUserStage;
import com.revolut.service.processing.user.GetUsersStage;
import com.revolut.service.processing.user.UpdateUserStage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation model.
 */
@Slf4j
public final class ModelImpl implements Model {
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
  public ResponseMessage createAccount(final String user,
                                       final String data) {

    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new CreateAccountStage(accountService));
    StageData stageData = new StageData(data,
                                        new AccountDTO(),
                                        new AccountParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage getAccount(final String user,
                                    final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new GetAccountStage(accountService));
    StageData stageData = new StageData(data,
                                        new AccountDTO(),
                                        new AccountParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage createUser(final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CreateUserStage(userService));
    StageData stageData = new StageData(data,
                                        new UserDTO(),
                                        new AccountParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage updateUser(final String user,
                                    final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new UpdateUserStage(userService));
    StageData stageData = new StageData(data,
                                        new UserDTO(),
                                        new Params());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage getUser(final String user,
                                 final String data) {
    ProcessingStage processingStage = new CheckUserStage(user, userService);
    processingStage.linkWith(new GetUserStage());
    StageData stageData = new StageData(data,
                                        new UserDTO(),
                                        new Params());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage getUsers(final String data) {
    ProcessingStage processingStage = new GetUsersStage(userService);
    StageData stageData = new StageData(data,
                                        new UserDTO(),
                                        new AccountParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public ResponseMessage getAccountsByUser(final String user,
                                           final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new GetUserAccountsStage(accountService));
    StageData stageData = new StageData(data,
                                        new UserDTO(),
                                        new Params());
    return processingStage.performOperation(stageData);
  }

  @Override
  public synchronized ResponseMessage transferMoney(final String user,
                                                    final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
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
  public synchronized ResponseMessage deposit(final String user,
                                              final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new CheckDstAccountStage(accountService))
            .linkWith(new CheckAmountStage())
            .linkWith(new DepositMoneyStage(paymentService));
    StageData stageData = new StageData(data,
                                        new PaymentDTO(),
                                        new PaymentParams());
    return processingStage.performOperation(stageData);
  }

  @Override
  public synchronized ResponseMessage withdraw(final String user,
                                               final String data) {
    ProcessingStage processingStage = new CheckRequestStage();
    processingStage.linkWith(new CreateDTOStage())
            .linkWith(new CheckUserStage(user, userService))
            .linkWith(new CheckSrcAccountStage(accountService))
            .linkWith(new CheckAmountStage())
            .linkWith(new WithdrawMoneyStage(paymentService));
    StageData stageData = new StageData(data,
                                        new PaymentDTO(),
                                        new PaymentParams());
    return processingStage.performOperation(stageData);
  }
}
