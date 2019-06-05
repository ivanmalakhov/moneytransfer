package com.revolut;

import com.revolut.handler.account.AccountCreateHandler;
import com.revolut.handler.account.GetAccountByUserHandler;
import com.revolut.handler.account.GetTotalBalanceHandler;
import com.revolut.handler.payment.DepositMoneyHandler;
import com.revolut.handler.payment.TransferMoneyHandler;
import com.revolut.handler.payment.WithdrawMoneyHandler;
import com.revolut.handler.user.CreateUserHandler;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import com.revolut.utils.request_logger.SparkUtils;
import org.apache.log4j.Logger;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;


/**
 * Server manipulation.
 */
class Server {
  private Logger logger = Logger.getLogger(Server.class);

  /**
   * Starting server and initialise routes and handlers.
   */
  void startServer() {
    SparkUtils.createServerWithRequestLog(logger);
    Model model = new ModelImpl();
    before("/*", (q, a) -> logger.info("Received api call" + q.headers() + "  ;  " + q.toString()));

    path("/account", () -> {
      post("/create", new AccountCreateHandler(model));
      get("/getall", new GetAccountByUserHandler(model));
      get("/totalbalance", new GetTotalBalanceHandler(model));
    });
    path("/payment", () -> {
      post("/transfer", new TransferMoneyHandler(model));
      post("/deposit", new DepositMoneyHandler(model));
      post("/withdraw", new WithdrawMoneyHandler(model));
    });
    path("/user", () -> {
      post("/create", new CreateUserHandler(model));
      post("/update", null);
    });

    get("/alive", (request, response) -> "ok");
  }

  void startServer(int port) {
    port(port);
    startServer();
  }

  void stopServer() {
    stop();
  }
}
