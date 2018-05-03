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


public class GameHandlerTest {

  @Test
  void test200ResponseAtRootPath() {
    ClientHelper client = new ClientHelper();
    HttpServer server = setServerWithRoutes();
    startOnNewThread(server);
    connectClient(server, client);

    String request = "GET / HTTP/1.1\r\n\r\n";
    List<String> response = client.sendMessage(request);
    assertTrue(response.contains("HTTP/1.1 sdf OK"));
  }

  private void startOnNewThread(HttpServer httpServer) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(httpServer::start);
  }

  private HttpServer setServerWithRoutes() {
    Router router = new Router()
            .route(new MethodAndExactPathMatcher(HttpMethod.GET, "/"), new GameHandler());

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
