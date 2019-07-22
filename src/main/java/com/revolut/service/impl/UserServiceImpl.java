package com.revolut.service.impl;

import com.revolut.dto.UserDTO;
import com.revolut.entity.User;
import com.revolut.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private final Map<Integer, User> users;

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
  public User update(final Integer userId, final UserDTO userDTO) {
    User userForUpdate = users.get(userId);
    userForUpdate.setFirstName(userDTO.getFirstName());
    userForUpdate.setLastName(userDTO.getLastName());
    return userForUpdate;
  }

  @Override
  public boolean isUserNotExist(final Integer userId) {
    return !(userId != null && users.containsKey(userId));
  }

  @Override
  public User getUser(final Integer id) {
    return users.get(id);
  }

  @Override
  public List<User> getUsers() {
    return new ArrayList<>(users.values());
  }

}
