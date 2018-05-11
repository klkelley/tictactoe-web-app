package me.karakelley;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.json.JSONException;
import org.json.JSONObject;

public class TicTacToeParser {

  private final IFn require = Clojure.var("clojure.core", "require");
  private final IFn vector = Clojure.var("clojure.core", "vec");
  private final IFn computerMove = Clojure.var("tictactoe.random-ai", "get-computer-move");
  private final IFn board = Clojure.var("tictactoe.board", "place-move");
  private final IFn winnerExists = Clojure.var("tictactoe.board", "winner?");
  private final IFn tie = Clojure.var("tictactoe.board", "tie?");

  public Object placeMoves(String body) throws JSONException {
    Object gameBoard = parseBoard(body);
    Object spot = parseChosenSpot(body);

    Object vectorBoard = getVector(gameBoard);
    Object boardWithNewMoves = placeMove(spot, vectorBoard);

    if (!winner(boardWithNewMoves) && !tie(boardWithNewMoves)) {
      return getComputerMove(boardWithNewMoves);
    }
    return boardWithNewMoves;
  }

  private Object getVector(Object gameBoard) {
    return vector.invoke(Clojure.read(String.valueOf(gameBoard)));
  }

  private Object placeMove(Object spot, Object vectorBoard) {
    require.invoke(Clojure.read("tictactoe.board"));
    return board.invoke(spot, "X", vectorBoard);
  }

  private Object getComputerMove(Object newBoard) {
    require.invoke(Clojure.read("tictactoe.random-ai"));
    Object updatedBoard = getVector(newBoard);
    Object move = computerMove.invoke(newBoard, "O", "X");
    return board.invoke(move, "O", updatedBoard);
  }

  public Boolean winner(Object board) {
    return (Boolean) winnerExists.invoke(board);
  }

  public Boolean tie(Object board) {
    return (Boolean) tie.invoke(board);
  }

  private Object parseBoard(String data) throws JSONException {
    return new JSONObject(data).get("board");
  }

  private Object parseChosenSpot(String data) throws JSONException {
    return new JSONObject(data).get("spot");
  }
}
