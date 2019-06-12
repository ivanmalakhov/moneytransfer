package com.revolut.service.impl;

import com.revolut.data.User;
import com.revolut.service.UserService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for working with user.
 */
public enum UserServiceImpl implements UserService {
  /**
   * Singleton Instance.
   */
  INSTANCE;
  /**
   * All users storage.
   */
  private Map<Integer, User> users;
  /**
   * Logger.
   */
  private Logger logger = Logger.getLogger(UserServiceImpl.class);

  /**
   * Constructor.
   */
  UserServiceImpl() {
    users = new HashMap<>();
  }

  @Override
  public User create(final String firstName,
                     final String lastName) {
    User user = new User(firstName, lastName);
    users.put(user.getId(), user);
    return user;
  }

  @Override
  public boolean isUserNotExist(final User user) {
    return !(user != null && users.containsKey(user.getId()));
  }

  @Override
  public boolean isUserNotExist(final Integer userId) {
    return !(userId != null && users.containsKey(userId));
  }

  @Override
  public User getUser(final Integer id) {
    return users.get(id);
  }
}
