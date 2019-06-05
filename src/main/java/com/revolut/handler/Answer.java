package com.revolut.handler;

import java.util.Objects;

public class Answer {

  public Answer(int code) {
    this.code = code;
    this.body = "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Answer answer = (Answer) o;
    return code == answer.code &&
            Objects.equals(body, answer.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, body);
  }

  @Override
  public String toString() {
    return "Answer(code=" + code + ", body=" + body + ")";
  }

  public String getBody() {
    return body;
  }

  public int getCode() {
    return code;
  }

  private int code;
  private String body;

  public Answer(int code, String body) {
    this.code = code;
    this.body = body;
  }

  public static Answer ok(String body) {
    return new Answer(200, body);
  }

}