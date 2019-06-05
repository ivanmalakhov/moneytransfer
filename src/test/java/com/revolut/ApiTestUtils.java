package com.revolut;

import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.fail;

class ApiTestUtils {
  static TestResponse request(String method, int port, String path, String requestBody) {


    try {
      URL url = new URL("http://localhost:" + port + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);
      if (null != requestBody) {
        OutputStream outStream = connection.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
        outStreamWriter.write(requestBody);
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();
      }
      connection.connect();
      String body = IOUtils.toString(connection.getInputStream());
      return new TestResponse(connection.getResponseCode(), body);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Sending request failed: " + e.getMessage());
      return null;
    }
  }

  static class TestResponse {

    final String body;
    final int status;

    TestResponse(int status, String body) {
      this.status = status;
      this.body = body;
    }
  }
}