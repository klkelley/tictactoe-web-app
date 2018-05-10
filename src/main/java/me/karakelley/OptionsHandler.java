package me.karakelley;

import me.karakelley.http.server.Handler;
import me.karakelley.http.server.http.Request;
import me.karakelley.http.server.http.Response;
import me.karakelley.http.server.http.responses.Ok;

public class OptionsHandler implements Handler {
  @Override
  public Response respond(Request request) {
    Response response = new Ok();
    response.setHeaders("Access-Control-Allow-Origin", "*");
    response.setHeaders("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    response.setHeaders("Access-Control-Allow-Methods", "POST, OPTIONS");
    return response;
  }
}
