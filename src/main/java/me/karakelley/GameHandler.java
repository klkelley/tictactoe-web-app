package me.karakelley;

import me.karakelley.http.server.Handler;
import me.karakelley.http.server.http.Request;
import me.karakelley.http.server.http.Response;
import me.karakelley.http.server.http.responses.Ok;
import org.json.JSONException;
import org.json.JSONObject;


public class GameHandler implements Handler {

  private final TicTacToeParser parser  = new TicTacToeParser();

  @Override
  public Response respond(Request request) {
    Response response =  new Ok();
    Object board;
    try {
      board = parser.placeMoves(new String(request.getBody()));
    } catch (JSONException e) {
      return new UnproccesableEntity();
    }

    JSONObject responseData = constructResponseData(board);
    response.setHeaders("Access-Control-Allow-Origin", "*");
    response.setHeaders("Content-Type", "application/json");
    response.setHeaders("Content-Length", String.valueOf(responseData.length()));
    response.setBody(String.valueOf(responseData));
    return response;
  }

  private JSONObject constructResponseData(Object board) {
    JSONObject jsonData = new JSONObject();
    try {
      jsonData.put("board", board);
      jsonData.put("winner", parser.winner(board));
      jsonData.put("tie", parser.tie(board));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    return jsonData;
  }
}
