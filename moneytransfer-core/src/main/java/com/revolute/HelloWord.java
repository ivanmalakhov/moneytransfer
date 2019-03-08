package com.revolute;
import static spark.Spark.get;

public class HelloWord {
  public static void main(String[] args) {

    get("/hello", (req, res) -> "Hello World");
  }
}
