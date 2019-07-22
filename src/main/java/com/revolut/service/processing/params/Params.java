package com.revolut.service.processing.params;

import com.revolut.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Base params.
 */
@Getter
@Setter
public class Params {
  /**
   * User.
   */
  private User user;
}
