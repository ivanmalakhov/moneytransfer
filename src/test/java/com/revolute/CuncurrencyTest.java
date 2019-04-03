package com.revolute;

import com.revolute.dto.Account;
import com.revolute.dto.Currency;
import com.revolute.dto.PaymentRequest;
import com.revolute.dto.User;
import com.revolute.handler.Answer;
import com.revolute.handler.account.GetTotalBalanceHandler;
import com.revolute.handler.payment.DepositMoneyHandler;
import com.revolute.handler.payment.TransferMoneyHandler;
import com.revolute.handler.payment.WithdrawMoneyHandler;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CuncurrencyTest {
  private Model model;
  private Logger logger = Logger.getLogger(TransferMoneyTest.class);
  private TransferMoneyHandler transferMoneyHandler;
  private DepositMoneyHandler depositMoneyHandler;
  private GetTotalBalanceHandler getTotalBalanceHandler;
  private User user;
  private static final int NACCOUNTS = 1000;
  private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(10000);
  private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10);
  private static final int DELAY = 10;
  private List<Account> accounts = new ArrayList<>();

  @Before
  public void init() {
    model = new ModelImpl();
    transferMoneyHandler = new TransferMoneyHandler(model);
    depositMoneyHandler = new DepositMoneyHandler(model);
    getTotalBalanceHandler = new GetTotalBalanceHandler(model);
    user = model.createUser("Smith", "John");
    logger.info("New User: " + user);

  }

  // deposit money
  private void createAccountAndDepositMoney(int countAccount, BigDecimal amount) {
    for (int i = 0; i < countAccount; i++) {
      depostMoney(model.createAccount(Currency.EUR, user), amount);
    }
    accounts.addAll(model.getAccountListByUser(user.getId()));
  }

  private void depostMoney(Account account, BigDecimal amount) {
    PaymentRequest paymentRequest = new PaymentRequest();
    paymentRequest.setUserId(user.getId());
    paymentRequest.setAmount(amount);
    paymentRequest.setDstAccount(account.getNumber());
    Answer answer = depositMoneyHandler.process(paymentRequest, Collections.emptyMap());
  }

  @Test
  public void raceConditionTest() {
    createAccountAndDepositMoney(NACCOUNTS, INITIAL_BALANCE);
    Answer answer = getTotalBalanceHandler.process(user, Collections.emptyMap());
    BigDecimal totalInitialBalance = new BigDecimal(answer.getBody());
    logger.info("Initial balance:" + totalInitialBalance);
    Thread[] threads = new Thread[NACCOUNTS];
    final AssertionError[] exc = new AssertionError[1];
    for (int i = 0; i < NACCOUNTS; i++) {
      Account fromAccount = accounts.get(i);
      Runnable r = () -> {
        try {
          Account toAccount = accounts.get((int) (accounts.size() * Math.random()));
          BigDecimal amount = MAX_AMOUNT.multiply(BigDecimal.valueOf(Math.random()));
          PaymentRequest paymentRequest = new PaymentRequest();
          paymentRequest.setUserId(user.getId());
          paymentRequest.setSrcAccount(fromAccount.getNumber());
          paymentRequest.setDstAccount(toAccount.getNumber());
          paymentRequest.setAmount(amount);
          transferMoneyHandler.process(paymentRequest, Collections.emptyMap());
          BigDecimal balansAfterPayment = new BigDecimal(getTotalBalanceHandler.process(user, Collections.emptyMap()).getBody());
//            logger.info(Thread.currentThread() + "; TotalBalance" + balansAfterPayment);
          assertTrue("Total balance was changed", totalInitialBalance.compareTo(balansAfterPayment) == 0);
          sleep((int) (DELAY * Math.random()));
        } catch (InterruptedException e) {
          logger.error(e);
        } catch (AssertionError e) {
          exc[0] = e;
        }

      };
      threads[i] = new Thread(r);
      threads[i].start();
    }
    for (int i = 0; i < NACCOUNTS; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (exc[0] != null)
      throw exc[0];
  }

  @After
  public void after() {
    logger.info("Итоговый баланс:" + getTotalBalanceHandler.process(user, Collections.emptyMap()).getBody());
    Set<Account> accounts = model.getAccountListByUser(user.getId());
    BigDecimal sum = BigDecimal.ZERO;
    for (Account account : accounts) {
      sum = sum.add(account.getBalance());
    }
    logger.info("Итоговый баланс(проверка):" + sum);

  }

}
