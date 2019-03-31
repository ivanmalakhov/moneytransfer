package com.revolute.service.impl;

import com.revolute.dto.User;
import com.revolute.service.UserService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public enum UserServiceImpl implements UserService {
  INSTANCE;

  private Map<Integer, User> users;
  private Logger logger = Logger.getLogger(UserServiceImpl.class);


  UserServiceImpl() {
    users = new HashMap<>();
  }

  @Override
  public User create(User user) {
    users.put(user.getId(), user);
    return user;
  }

  @Override
  public User create(String firstName, String lastName) {
    User user = new User(firstName, lastName);
    users.put(user.getId(), user);
    return user;
  }

  @Override
  public boolean userExist(User user) {
    return user != null && users.containsKey(user.getId());
  }

  @Override
  public boolean userExist(Integer userId) {
    return userId != null && users.containsKey(userId);
  }

  @Override
  public boolean userNotExist(User user) {
    return !(user != null && users.containsKey(user.getId()));
  }

  @Override
  public boolean userNotExist(Integer userId) {
    return !(userId != null && users.containsKey(userId));
  }

  @Override
  public User getUser(Integer id) {
    return users.get(id);
  }
}
