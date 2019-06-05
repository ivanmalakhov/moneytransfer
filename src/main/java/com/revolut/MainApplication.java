package com.revolut;

import org.apache.log4j.Logger;

public class MainApplication {
  public static void main(String[] args) {
    Logger logger = Logger.getLogger(MainApplication.class);
    Server server = new Server();
    try {
      server.startServer();
    } catch (Exception e) {
      logger.error("Couldn't start server", e);
      System.exit(-2);
    }

  }
}
