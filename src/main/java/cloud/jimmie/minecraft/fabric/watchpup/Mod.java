package cloud.jimmie.minecraft.fabric.watchpup;

import java.io.IOException;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

@NullMarked
public class Mod implements ModInitializer {
	public static final @NonNull String MOD_ID = "watchpup-fabric";

	private @NonNull ContextLogger logger;
	private @Nullable WebServer webServer = null;

	public Mod() {
		var rootLogger = LoggerFactory.getLogger(MOD_ID);
		logger = new ContextLogger(rootLogger, "Loader");
	}

	@Override
	public void onInitialize() {
		logger.info("Waiting for SERVER_STARTED event. Process ID: %s".formatted(ProcessHandle.current().pid()));

		ServerLifecycleEvents.SERVER_STARTED.register(this::handleServerStarted);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::handleServerStopping);
	}

	private void handleServerStarted(@NonNull MinecraftServer server) {
		logger.info("SERVER_STARTED event received.");

		var config = new Config(logger);
		config.createDefault("host", "0.0.0.0");
		config.createDefault("port", 57475);
		config.createDefault("hide_true_count", false);

		try {
			config.createYamlFileFromDefaultsIfNonExistent();
		} catch (ConfigWriteException e) {
			logger.error("Failed to write config file: %s".formatted(Safe.eMsg(e)));
			return;
		}

		var playerManager = server.getPlayerManager();
		if (playerManager == null) {
			logger.error("Player manager is null. Unable to start Watchpup.");
			return;
		}

		logger.info("Initializing WebServerManager.");

		try {
			webServer = new WebServer(playerManager, config, logger);
		} catch (IOException e) {
			logger.error("Failed to start WebServer: %s".formatted(Safe.eMsg(e)));
		}
	}

	private void handleServerStopping(@NonNull MinecraftServer server) {
		logger.info("SERVER_STOPPING event received. isRunning: %s".formatted(server.isRunning()));

		if (webServer != null) {
			shutdownWebServer(logger, webServer);
		} else {
			logger.info("WebServer is null. Nothing to shut down.");
		}
	}

	private static void shutdownWebServer(
		@NonNull ContextLogger tempLogger,
		@NonNull WebServer tempWebServer
	) {
		tempLogger.info("Shutting down WebServer...");
		tempWebServer.shutdown();
	}
}
