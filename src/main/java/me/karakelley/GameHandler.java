package me.karakelley;

import me.karakelley.http.server.Handler;
import me.karakelley.http.server.http.Request;
import me.karakelley.http.server.http.Response;
import me.karakelley.http.server.http.responses.Ok;

public class GameHandler implements Handler {
  @Override
  public Response respond(Request request) {
    return new Ok();
  }
}
