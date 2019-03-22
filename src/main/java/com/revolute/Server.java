package com.revolute;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.revolute.handler.account.AccountCreateHandler;
import com.revolute.service.AccountService;
import com.revolute.service.impl.AccountServiceImpl;
import com.revolute.utils.request_logger.SparkUtils;
import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Server manipulation.
 */
public class Server {
  private Logger logger = Logger.getLogger(MainApplication.class);

  /**
   * Starting server and initialise routes and handlers.
   */
  public void startServer() {
    SparkUtils.createServerWithRequestLog(logger);

    before("/*", (q, a) -> logger.info("Received api call"));

    path("/account", () -> {
      AccountService accountService = new AccountServiceImpl();
      post("/create", new AccountCreateHandler(accountService));
      get("/balance", null);
    });
    path("/payment", () -> {

      post("/create", null);
      get("/status", null);
    });

    get("/alive", new Route() {
      @Override
      public Object handle(Request request, Response response) throws Exception {
        return "ok";
      }
    });
  }

  public void startServer(int port) {
    port(port);
    startServer();
  }

  public void stop() {
    Spark.stop();
  }
}
