package me.karakelley;

import me.karakelley.http.server.Handler;
import me.karakelley.http.server.http.HttpMethod;
import me.karakelley.http.server.http.Request;
import me.karakelley.http.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionsHandlerTest {

  @Test
  void testOptionsRequest() {
    Request request = new Request.Builder().setMethod(HttpMethod.OPTIONS).setPath("/move").setProtocol("HTTP/1.1").setPort(0).build();
    Handler optionsHandler = new OptionsHandler();
    HashMap<String, String> headers = new HashMap<>();
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    headers.put("Access-Control-Allow-Methods", "POST, OPTIONS");
    Response response = optionsHandler.respond(request);
    assertEquals(response.getStatus(), "200 OK");
    assertEquals(response.getHeaders(), headers);
  }
}
