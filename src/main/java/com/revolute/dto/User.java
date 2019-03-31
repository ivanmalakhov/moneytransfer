package com.revolute.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Random;

@Data
public class User {
  private Integer id;
  private String firstName;
  private String secondName;

  @JsonCreator
  public User(@JsonProperty("firstName") String firstName, @JsonProperty("secondName") String secondName) {
    Random rand = new Random();
    this.id = rand.nextInt(Integer.MAX_VALUE);
    this.firstName = firstName;
    this.secondName = secondName;
  }
}
