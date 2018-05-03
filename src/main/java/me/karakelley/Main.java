package me.karakelley;

import me.karakelley.http.application.CommandLineArguments;
import me.karakelley.http.application.exit.SystemExit;
import me.karakelley.http.application.requestmatchers.MethodAndExactPathMatcher;
import me.karakelley.http.server.*;
import me.karakelley.http.server.handlers.Router;
import me.karakelley.http.server.http.HttpMethod;

import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Map<String, String> argsHash = new CommandLineArguments().parse(args, new SystemExit());
    ServerConfiguration serverConfiguration = new ServerConfiguration();
    serverConfiguration.setPort(argsHash.get("port"));

    Router router = new Router()
            .route(new MethodAndExactPathMatcher(HttpMethod.GET, "/"), new GameHandler());

    serverConfiguration.setHandler(router);
    HttpServer server = new HttpServer(serverConfiguration, new ConnectionHandler(), new RequestReaderFactory());

    server.start();
  }
}


