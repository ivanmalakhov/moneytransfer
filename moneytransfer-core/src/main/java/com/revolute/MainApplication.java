package com.revolute;

import com.revolute.utils.request_logger.SparkUtils;
import org.apache.log4j.Logger;

import static spark.Spark.get;

public class MainApplication {
  public static void main(String[] args) {
    Logger logger = Logger.getLogger(MainApplication.class);
    SparkUtils.createServerWithRequestLog(logger);

    get("/hello", (request, response) -> "world");
  }
}
