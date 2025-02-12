package cloud.jimmie.minecraft.fabric.watchpup;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import net.fabricmc.loader.api.FabricLoader;

@NullMarked
public class Config {
  private static final @NonNull String WATCHPUP_CONFIG_FILE_NAME = "watchpup.yml";

  private final @NonNull ContextLogger logger;
  private final @NonNull Path path;
  private @NonNull HashMap<@NonNull String, @NonNull Object> data;

  public Config(
    @NonNull ContextLogger loggerSource
  ) {
    this.logger = new ContextLogger(loggerSource, "Config");

    var configDir = FabricLoader.getInstance().getConfigDir();
    path = configDir.resolve(WATCHPUP_CONFIG_FILE_NAME);

    if (path.toFile().exists()) {
      logger.info("Config not found on disk. Creating empty config in memory...");
      data = new HashMap<>();
      return;
    }

    Yaml yaml = new Yaml();
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      logger.info("Loading config from disk...");
      data = yaml.load(inputStream);
    } catch (IOException e) {
      logger.error("Config appears to exist, but failed to load it from disk or parse it. Creating empty config in memory...");
      data = new HashMap<>();
    }
  }

  public void createDefault(@NonNull String key, @NonNull String value) {
    if (data.containsKey(key)) return;
    data.put(key, value);
  }

  public void createDefault(@NonNull String key, int value) {
    if (data.containsKey(key)) return;
    data.put(key, value);
  }

  public void createDefault(@NonNull String key, boolean value) {
    if (data.containsKey(key)) return;
    data.put(key, value);
  }

  public void createYamlFileFromDefaultsIfNonExistent() throws ConfigWriteException {
    if (path.toFile().exists()) {
      logger.info("Config file already exists. Skipping creation.");
      return;
    }

    DumperOptions options = new DumperOptions();
    options.setIndent(2);
    options.setPrettyFlow(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    Yaml yaml = new Yaml(options);

    try (FileWriter writer = new FileWriter(path.toFile())) {
      yaml.dump(data, writer);
    } catch (IOException e) {
      logger.error("Failed to write default config to disk.");
      throw new ConfigWriteException("Failed to write default config", e);
    }
  }


  public boolean getBoolean(@NonNull String key, boolean defaultValue) {
    var value = data.getOrDefault(key, defaultValue);
    if (value instanceof Boolean bool) {
      return bool;
    }

    return defaultValue;
  }

  public int getInt(@NonNull String key, int defaultValue) {
    var value = data.getOrDefault(key, defaultValue);
    if (value instanceof Integer integer) {
      return integer;
    }

    return defaultValue;
  }

  public @Nullable String getString(@NonNull String key, @Nullable String defaultValue) {
    if (data.containsKey(key)) {
      return data.get(key).toString();
    } else {
      return defaultValue;
    }
  }
}
