package me.karakelley;

import me.karakelley.http.application.CommandLineArguments;
import me.karakelley.http.application.exit.SystemExit;
import me.karakelley.http.application.requestmatchers.MethodAndExactPathMatcher;
import me.karakelley.http.server.ConnectionHandler;
import me.karakelley.http.server.HttpServer;
import me.karakelley.http.server.RequestReaderFactory;
import me.karakelley.http.server.ServerConfiguration;
import me.karakelley.http.server.handlers.Router;
import me.karakelley.http.server.http.HttpMethod;

import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Map<String, String> argsHash = new CommandLineArguments().parse(args, new SystemExit());
    ServerConfiguration serverConfiguration = new ServerConfiguration();
    serverConfiguration.setPort(argsHash.get("port"));

    Router router = new Router()
            .route(new MethodAndExactPathMatcher(HttpMethod.POST, "/move"), new GameHandler())
            .route(new MethodAndExactPathMatcher(HttpMethod.OPTIONS, "/move"), new OptionsHandler());

    serverConfiguration.setHandler(router);
    HttpServer server = new HttpServer(serverConfiguration, new ConnectionHandler(), new RequestReaderFactory());

    server.start();

  }
}


