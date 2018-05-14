package me.karakelley;

import me.karakelley.http.application.requestmatchers.MethodAndExactPathMatcher;
import me.karakelley.http.server.ConnectionHandler;
import me.karakelley.http.server.HttpServer;
import me.karakelley.http.server.RequestReaderFactory;
import me.karakelley.http.server.ServerConfiguration;
import me.karakelley.http.server.handlers.Router;
import me.karakelley.http.server.http.HttpMethod;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ApiTest {

  @Test
  void test200ResponseWhenPostingToMoveWithBlankBoard() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[0,1,2,3,4,5,6,7,8],\"spot\":1}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 38\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.contains("HTTP/1.1 200 OK"));
  }

  @Test
  void testSendsGameStatusInResponse() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[X,1,2,3,O,5,6,7,8],\"spot\":1}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 38\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.get(5).contains("{\"gameResults\":\"Player null Wins!\",\"gameState\":false"));
  }

  @Test
  void testIfGameIsWonByPlayerXStatusIsUpdated() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[X,X,X,3,O,5,6,7,8],\"spot\":1}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 38\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.get(5).contains("{\"gameResults\":\"Player X Wins!\",\"gameState\":true"));
  }

  @Test
  void testIfGameIsWonByPlayerOStatusIsUpdated() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[O,O,O,3,O,5,6,7,8],\"spot\":8}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 38\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.get(5).contains("{\"gameResults\":\"Player O Wins!\",\"gameState\":true"));
  }

  @Test
  void testIfGameIsTiedStatusIsUpdated() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[X,O,X,X,O,X,O,7,O],\"spot\":7}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 38\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.get(5).contains("{\"gameResults\":\"Its a tie!\",\"gameState\":true"));
  }

  @Test
  void test422ReturnedForSyntacticallyInvalidData() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "\"board\":[X,O,X,X,O,X,O,7,O],\"spot\":7";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 36\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.contains("HTTP/1.1 422 Unprocessable Entity"));
  }

  @Test
  void test422ReturnedForDataWithoutBoard() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"board\":[X,O,X,X,O,X,O,7,O]}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 29\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.contains("HTTP/1.1 422 Unprocessable Entity"));
  }

  @Test
  void test422ReturnedForDataWithoutChosenSpot() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String moves = "{\"spot\":7}";
    String request = "POST /move HTTP/1.1\r\nContent-Length: 10\r\nContent-Type: application/json\r\n\r\n" + moves;
    List<String> response = client.sendMessage(request);
    assertTrue(response.contains("HTTP/1.1 422 Unprocessable Entity"));
  }

  private void startOnNewThread(HttpServer httpServer) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(httpServer::start);
  }

  private HttpServer setServerWithRoutes() {
    Router router = new Router()
            .route(new MethodAndExactPathMatcher(HttpMethod.POST, "/move"), new GameHandler());

    ServerConfiguration serverConfiguration = new ServerConfiguration();
    serverConfiguration.setHandler(router);
    serverConfiguration.setPort("0");
    return new HttpServer(serverConfiguration, new ConnectionHandler(), new RequestReaderFactory());
  }


  private void connectClient(HttpServer httpServer, ClientHelper client) {
    try {
      client.connectWithTry("127.0.0.1", httpServer);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
