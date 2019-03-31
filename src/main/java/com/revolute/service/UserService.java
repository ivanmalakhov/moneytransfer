package com.revolute.service;

import com.revolute.model.User;

public interface UserService {

  User create(User user);

  User create(String firstName, String lastName);

  boolean userExist(User user);

  boolean userExist(Integer userId);

  boolean userNotExist(User user);

  boolean userNotExist(Integer userId);

  User getUser(Integer id);

}
