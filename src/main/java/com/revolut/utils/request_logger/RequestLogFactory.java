package com.revolut.utils.request_logger;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.AbstractNCSARequestLog;

public class RequestLogFactory {

  private Logger logger;

  public RequestLogFactory(Logger logger) {
    this.logger = logger;
  }

  AbstractNCSARequestLog create() {
    return new AbstractNCSARequestLog() {
      @Override
      protected boolean isEnabled() {
        return true;
      }

      @Override
      public void write(String s) {
        logger.info(s);
      }
    };
  }
}
