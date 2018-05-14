package me.karakelley;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeParser {

  private final IFn require = Clojure.var("clojure.core", "require");
  private final IFn vector = Clojure.var("clojure.core", "vec");
  private final IFn toSymbol = Clojure.var("clojure.core", "symbol");
  private final IFn computerMove = Clojure.var("tictactoe.ai", "get-computer-move");
  private final IFn board = Clojure.var("tictactoe.board", "place-move");
  private final IFn winnerExists = Clojure.var("tictactoe.board", "winner?");
  private final IFn tie = Clojure.var("tictactoe.board", "tie?");
  private final IFn winningMarker = Clojure.var("tictactoe.board", "winner");
  private final IFn gameResults = Clojure.var("tictactoe.board", "game-results");

  public Object placeMoves(String body) throws JSONException {
    Object gameBoard = parseBoard(body);
    Object spot = parseChosenSpot(body);

    Object vectorBoard = getVector(gameBoard);
    Object boardWithNewMoves = placeMove(spot, vectorBoard);

    if (!gameOver(boardWithNewMoves)) {
      return getComputerMove(boardWithNewMoves);
    }
    return boardWithNewMoves;
  }

  public String winningMessage(Object board) {
    require.invoke(Clojure.read("tictactoe.board"));
    if (tie(board)) {
      return (String) gameResults.invoke(board);
    }
    return String.valueOf(gameResults.invoke(board));
  }

  public Boolean gameOver(Object board) {
    return winner(board) || tie(board);
  }

  private Object getVector(Object gameBoard) {
    return vector.invoke(Clojure.read(String.valueOf(gameBoard)));
  }

  private Object placeMove(Object spot, Object vectorBoard) {
    require.invoke(Clojure.read("tictactoe.board"));
    return board.invoke(spot, toSymbol("X"), vectorBoard);
  }

  private Object getComputerMove(Object newBoard) {
    require.invoke(Clojure.read("tictactoe.ai"));
    Object updatedBoard = getVector(newBoard);
    Object move = computerMove.invoke(newBoard, toSymbol("O"), toSymbol("X"));
    return board.invoke(move, toSymbol("O"), updatedBoard);
  }

  private Boolean winner(Object board) {
    return (Boolean) winnerExists.invoke(board);
  }

  private Boolean tie(Object board) {
    return (Boolean) tie.invoke(board);
  }

  private List<String> parseBoard(String data) throws JSONException {
    return constructGameBoard((JSONArray) new JSONObject(data).get("board"));
  }

  private Object toSymbol(String marker) {
    return toSymbol.invoke(Clojure.read(marker));
  }

  private Object parseChosenSpot(String data) throws JSONException {
    return new JSONObject(data).get("spot");
  }

  private List<String> constructGameBoard(JSONArray board) throws JSONException {
    List<String> gameBoard = new ArrayList<>();

    for(int i = 0; i < board.length(); i++) {
      if (board.get(i).toString().equals("null")) {
        gameBoard.add(i, String.valueOf(i));
      } else {
        gameBoard.add(i, String.valueOf(board.get(i)));
      }
    }

    return gameBoard;
  }
}
