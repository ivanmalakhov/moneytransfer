package com.revolut.service;

import com.revolut.entity.User;

import java.util.List;

/**
 * Service for working with user.
 */
public interface UserService {
  /**
   * Create new user.
   *
   * @param firstName - first name
   * @param lastName  - last name
   * @return User
   */
  User create(String firstName, String lastName);

  /**
   * Check user.
   *
   * @param user - user
   * @return boolean
   */
  boolean isUserNotExist(User user);

  /**
   * Check user by user id.
   *
   * @param userId - user id
   * @return boolean
   */
  boolean isUserNotExist(Integer userId);

  /**
   * Get user by id.
   *
   * @param id - user id
   * @return User
   */
  User getUser(Integer id);

  /**
   * Get all users.
   *
   * @return - list with users
   */
  List<User> getUsers();

}
