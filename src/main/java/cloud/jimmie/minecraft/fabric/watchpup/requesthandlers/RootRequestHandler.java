package cloud.jimmie.minecraft.fabric.watchpup.requesthandlers;

import java.io.IOException;

import org.jspecify.annotations.NonNull;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cloud.jimmie.minecraft.fabric.watchpup.ContextLogger;
import net.minecraft.server.PlayerManager;

public class RootRequestHandler implements HttpHandler {
  public final @NonNull PlayerManager playerManager;
  public final @NonNull ContextLogger logger;

  public final boolean hideTrueCount;

  public RootRequestHandler(
     @NonNull PlayerManager playerManager,
     @NonNull ContextLogger sourceLogger,
     boolean hideTrueCount
  ) {
    this.playerManager = playerManager;
    this.logger        = new ContextLogger(sourceLogger, "Request");
    this.hideTrueCount = hideTrueCount;
  }

  private String getResponse() {
    int playerCount = playerManager.getPlayerList().size();

    if (this.hideTrueCount) {
      return playerCount > 0 ? "1" : "0";
    }

    return Integer.toString(playerCount);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    var response = this.getResponse();

    exchange.sendResponseHeaders(200, response.length());

    try (var os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }
}
