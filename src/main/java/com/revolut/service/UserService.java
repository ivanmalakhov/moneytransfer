package com.revolut.service;

import com.revolut.data.User;

public interface UserService {

  User create(String firstName, String lastName);

  boolean userExist(User user);

  boolean userExist(Integer userId);

  boolean userNotExist(User user);

  boolean userNotExist(Integer userId);

  User getUser(Integer id);

}
