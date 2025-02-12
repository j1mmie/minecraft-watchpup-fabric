package cloud.jimmie.minecraft.fabric.watchpup;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import com.sun.net.httpserver.HttpServer;

import cloud.jimmie.minecraft.fabric.watchpup.requesthandlers.RootRequestHandler;
import net.minecraft.server.PlayerManager;

@NullMarked
public class WebServer {
  private @NonNull HttpServer    server;
  private @NonNull ContextLogger logger;

  public WebServer(
     @NonNull PlayerManager playerManager,
     @NonNull Config        config,
     @NonNull ContextLogger sourceLogger
  ) throws IOException {
    logger = sourceLogger;

    String host = config.getString("host", "0.0.0.0");
    int port = config.getInt("port", 57475);
    boolean hideTrueCount = config.getBoolean("hide_true_count", false);

    InetSocketAddress address = null;

    if (host == null || host.trim().isEmpty()) {
      logger.info("Starting HTTP server on default address, port %d...".formatted(port));
      address = new InetSocketAddress(port);
    } else {
      logger.info("Starting HTTP server on address %s, port %d...".formatted(host, port));
      address = new InetSocketAddress(host, port);
    }

    try {
      server = HttpServer.create(address, 0);
    } catch (IOException e) {
      logger.error("Failed to start HTTP server: %s".formatted(Safe.eMsg(e)));
      throw e;
    }

    var handler = new RootRequestHandler(playerManager, this.logger, hideTrueCount);
    server.createContext("/", handler);

    safeSetServerNullExecutor(server);
    server.start();

    logger.info("HTTP server started on port %d.".formatted(port));
  }

  @SuppressWarnings("nullness")
  private static void safeSetServerNullExecutor(@NonNull HttpServer server) {
    server.setExecutor(null);
  }

  public void shutdown() {
    logger.info("Shutting down HTTP server...");
    server.stop(0);
    logger.info("HTTP server stopped.");
  }
}
