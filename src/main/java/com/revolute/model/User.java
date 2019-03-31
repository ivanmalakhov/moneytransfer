package com.revolute.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;


public class User {
  private Integer id;
  private String firstName;
  private String secondName;

  public User(Integer id) {
    this.id = id;
  }

  @JsonCreator
  public User(@JsonProperty("firstName") String firstName, @JsonProperty("secondName") String secondName) {
    Random rand = new Random();
    this.id = rand.nextInt(Integer.MAX_VALUE);
    this.firstName = firstName;
    this.secondName = secondName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }
}
