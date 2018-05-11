package me.karakelley;

import me.karakelley.http.server.http.Response;

public class UnproccesableEntity extends Response {

  public String getStatus() {
    return "422 Unprocessable Entity";
  }
}
