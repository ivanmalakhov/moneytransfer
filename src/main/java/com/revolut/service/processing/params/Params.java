package com.revolut.service.processing.params;

import com.revolut.entity.User;
import lombok.Data;

/**
 * Base params.
 */
@Data
public class Params {
  /**
   * User.
   */
  private User user;
}
