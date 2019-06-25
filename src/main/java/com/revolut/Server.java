package com.revolut;

import com.revolut.dto.ResponseMessage;
import com.revolut.service.Model;
import com.revolut.service.impl.ModelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
class Server {
  /**
   * Class logger.
   */
  private final Logger logger = LoggerFactory.getLogger(Server.class);
  /**
   * Response/request type.
   */
  private static final String JSON_TYPE = "application/json";

  /**
   * Starting server and initialise routes and handlers.
   */
  void startServer() {
    Model model = new ModelImpl();
    before("/*", (q, a) ->
            logger.info("Received api call"
                                + q.headers()
                                + "  ;  " + q.toString()));

    post("/users", (request, response) -> {
      response.type(JSON_TYPE);
      ResponseMessage message = model.createUser(request.body());
      response.status(message.getStatus().getCode());
      return message.getJsonMessage();
    });
    get("/users", (request, response) -> {
      response.type(JSON_TYPE);
      ResponseMessage message = model.getUsers(request.body());
      response.status(message.getStatus().getCode());
      return message.getJsonMessage();
    });
    get("/users/:id", (request, response) -> {
      //получить данные клиента
      return null;
    });
    put("/users/:id", (request, response) -> {
      // edit user
      return null;
    });

    path("/account", () -> {
      post("/create", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.createAccount(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      get("/get", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.getAccount(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
    });
    path("/payment", () -> {
      post("/transfer", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.transferMoney(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      post("/deposit", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.deposit(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      post("/withdraw", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.withdraw(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
    });
    path("/user", () -> {
      post("/create", (request, response) -> {
        response.type(JSON_TYPE);
        ResponseMessage message = model.createUser(request.body());
        response.status(message.getStatus().getCode());
        return message.getJsonMessage();
      });
      get("/accounts", (request, response) -> {
        response.type(JSON_TYPE);
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
