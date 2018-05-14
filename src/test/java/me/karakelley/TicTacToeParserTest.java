package me.karakelley;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicTacToeParserTest {

  TicTacToeParser parser;

  @BeforeEach
  void setUp() {
    parser = new TicTacToeParser();
  }

  @Test
  void testParserPlacesHumanMoveAndComputerMove() throws JSONException {
    assertTrue(parser.placeMoves("{\"board\": [null, null, null, null, null, null, null, null, null], \"spot\": 0}").toString().startsWith("[X"));
    assertTrue(parser.placeMoves("{\"board\": [null, null, null, null, null, null, null, null, null], \"spot\": 0}").toString().contains("O"));
  }

  @Test
  void testComputerDoesNotGetOptionToPickMoveWithOneSpotLeft() throws JSONException {
    String json = "{\"board\": [X, O, X, O, X, O, X, O, null ], \"spot\": 8}";
    String board = "[X O X O X O X O X]";
    assertEquals(board, parser.placeMoves(json).toString());
  }

  @Test
  void testExceptionIsCaughtAndRethrownForInvalidJson() {
    assertThrows(JSONException.class, () -> {
      String json = "\"board\": [X, O, X, O, X, O, X, O, 8 ], \"spot\": 8";
      parser.placeMoves(json);
    });
  }

  @Test
  void testJSONExceptionCaughtAndRethrownForDataWithoutSpot() {
    assertThrows(JSONException.class, () -> {
      String json = "{\"board\": [X, O, X, O, X, O, X, O, 8 ]}";
      parser.placeMoves(json);
    });
  }

  @Test
  void testJSONExceptionCaughtAndRethrownForDataWithoutBoard() {
    assertThrows(JSONException.class, () -> {
      String json = "{\"spot\": 8}";
      parser.placeMoves(json);
    });
  }
}
