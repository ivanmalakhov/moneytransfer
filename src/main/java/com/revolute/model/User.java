package com.revolute.model;

import lombok.Data;

@Data
public class User {
  private String id;

  public User(String id) {
    this.id = id;
  }
  //  private String firstName;
//  private String secondName;
}
