package com.revolut.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Response structure.
 *
 * @author ivanmalakhov
 */
public final class ResponseMessage {
  /**
   * Response status.
   */
  private ResponseStatus status;
  /**
   * JSON object with response.
   */
  private JsonObject jsonMessage;
  /**
   * Gson object.
   */
  private Gson gson = new Gson();

  /**
   * Return information about status.
   *
   * @return ResponseStatus
   */
  public ResponseStatus getStatus() {
    return this.status;
  }

  /**
   * Set status.
   *
   * @param responseStatus ResponseStatus
   */
  public void setStatus(final ResponseStatus responseStatus) {
    this.status = responseStatus;
  }

  /**
   * Return completed message for response.
   *
   * @return String with response
   */
  public String getJsonMessage() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("Code", status.getCode());
    jsonObject.addProperty("Status", status.getDescription());
    jsonObject.add("Info", jsonMessage);
    return gson.toJson(jsonObject);
  }

  /**
   * Set JsonObject for response.
   *
   * @param jsonObject JsonObject
   */
  public void setJsonMessage(final JsonObject jsonObject) {
    this.jsonMessage = jsonObject;
  }
}
