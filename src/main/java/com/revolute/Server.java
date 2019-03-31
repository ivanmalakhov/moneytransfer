package com.revolute;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

import com.revolute.handler.account.AccountCreateHandler;
import com.revolute.handler.payment.DepositMoneyHandler;
import com.revolute.handler.payment.TransferMoneyHandler;
import com.revolute.handler.user.CreateUserHandler;
import com.revolute.handler.account.GetAccountByUserHandler;
import com.revolute.handler.payment.WithdrawMoneyHandler;
import com.revolute.service.Model;
import com.revolute.service.impl.ModelImpl;
import com.revolute.utils.request_logger.SparkUtils;
import org.apache.log4j.Logger;

import spark.Spark;

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

  void stop() {
    Spark.stop();
  }
}
