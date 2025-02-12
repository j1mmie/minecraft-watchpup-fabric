package cloud.jimmie.minecraft.fabric.watchpup;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
public class ContextLogger {
  public final @NonNull Logger parentLogger;
  public final @NonNull String context;

  public ContextLogger(
    @NonNull Logger parentLogger,
    @NonNull String context) {
    this.parentLogger = parentLogger;
    this.context = context;
  }

  public ContextLogger(
    @NonNull ContextLogger siblingLogger,
    @NonNull String        context) {
    this(siblingLogger.parentLogger, context);
  }

  public void info(String message) {
    parentLogger.info("%s: %s".formatted(context, message));
  }

  public void error(String message) {
    parentLogger.error("%s: %s".formatted(context, message));
  }
}
