package com.revolute.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonSyntaxException;
import com.revolute.service.Model;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractRequestHandler<V> implements RequestHandler<V>, Route {

  private Class<V> valueClass;
  protected Model model;

  public AbstractRequestHandler(Class<V> valueClass, Model model){
    this.valueClass = valueClass;
    this.model = model;
  }

  protected static String dataToJson(Object data) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper.writeValueAsString(data);
    } catch (IOException e){
      return "";
    }
  }

  public final Answer process(V value, Map<String, String> urlParams) {
    return processImpl(value, urlParams);
  }

  protected abstract Answer processImpl(V value, Map<String, String> urlParams);


  @Override
  public Object handle(Request request, Response response) throws Exception {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      V value = objectMapper.readValue(request.body(), valueClass);

      Map<String, String> urlParams = request.params();
      Answer answer = process(value, urlParams);
      response.status(answer.getCode());
      response.body(answer.getBody());
      return answer.getBody();
    } catch (JsonSyntaxException e) {
      response.status(400);
      response.body(e.getMessage());
      return e.getMessage();
    }
  }

}
