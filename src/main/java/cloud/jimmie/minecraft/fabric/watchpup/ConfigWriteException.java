package cloud.jimmie.minecraft.fabric.watchpup;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ConfigWriteException extends Exception {
  public ConfigWriteException(
    @NonNull String message,
    @NonNull Throwable cause
  ) {
    super(message, cause);
  }
}
