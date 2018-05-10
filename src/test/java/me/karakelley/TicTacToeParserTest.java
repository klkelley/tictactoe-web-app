package me.karakelley;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeParserTest {

  TicTacToeParser parser;

  @BeforeEach
  void setUp() {
    parser = new TicTacToeParser();
  }

  @Test
  void testParserPlacesHumanMoveAndComputerMove() throws JSONException {
    assertTrue(parser.placeMoves("{\"board\": [0, 1, 2, 3, 4, 5, 6, 7, 8], \"spot\": 0}").toString().startsWith("[\"X\""));
    assertTrue(parser.placeMoves("{\"board\": [0, 1, 2, 3, 4, 5, 6, 7, 8], \"spot\": 0}").toString().contains("O"));
  }

  @Test
  void testComputerDoesNotGetOptionToPickMoveWithOneSpotLeft() throws JSONException {
    String json = "{\"board\": [X, O, X, O, X, O, X, O, 8 ], \"spot\": 8}";
    Object board = new ArrayList<>(Arrays.asList("X", "O", "X", "O", "X", "O", "X", "O", "X"));
    assertEquals(board, parser.placeMoves(json));
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
