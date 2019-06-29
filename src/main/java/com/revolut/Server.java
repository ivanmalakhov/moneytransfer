package com.revolut;

import com.revolut.dto.ResponseMessage;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import lombok.extern.slf4j.Slf4j;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.stop;


/**
 * Server manipulation.
 */
@Slf4j
class Server {
  /**
   * Starting server and initialise routes and handlers.
   */
  void startServer() {
    Model model = new ModelImpl();
    before((request, response) -> response.type("application/json"));
    before("/*", (q, a) ->
            log.info("Received api call"
                             + q.headers()
                             + "  ;  " + q.toString()));

    post("/users", (request, response) -> {
      ResponseMessage message = model.createUser(request.body());
      response.status(message.getStatus().getCode());
      return message.getJsonMessage();
    });
    get("/users", (request, response) -> {
      ResponseMessage message = model.getUsers(request.body());
      response.status(message.getStatus().getCode());
      return message.getJsonMessage();
    });
    get("/users/:id", (request, response) -> {
      ResponseMessage message = model.getUser(request.params(":id"),
                                              request.body());
      response.status(message.getStatus().getCode());
      return message.getJsonMessage();
    });
    put("/users/:id", (request, response) -> {
      // edit user
      return null;
    });
/*
Старый код. Зарефакторить API
*/
    path("/account", () -> {
      post("/create", (request, response) -> {
        ResponseMessage message = model.createAccount(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      get("/get", (request, response) -> {
        ResponseMessage message = model.getAccount(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
    });
    path("/payment", () -> {
      post("/transfer", (request, response) -> {
        ResponseMessage message = model.transferMoney(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      post("/deposit", (request, response) -> {
        ResponseMessage message = model.deposit(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      post("/withdraw", (request, response) -> {
        ResponseMessage message = model.withdraw(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
    });
    path("/user", () -> {
      post("/create", (request, response) -> {
        ResponseMessage message = model.createUser(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      get("/accounts", (request, response) -> {
        ResponseMessage message = model.getAccountsByUser(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      post("/update", null);
    });

    get("/alive", (request, response) -> "ok");
  }

  /**
   * Starting server.
   *
   * @param port port number
   */
  void startServer(final int port) {
    port(port);
    startServer();
  }

  /**
   * Server shutdown.
   */
  void stopServer() {
    stop();
  }
}
