package cloud.jimmie.minecraft.fabric.watchpup;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Safe {
  private Safe() {

  }

  public static @NonNull String val(
    @Nullable String value,
    @NonNull String fallback
  ) {
    return value == null ? fallback : value;
  }

  public static @NonNull String eMsg(
    @NonNull Exception ex
  ) {
    return val(ex.getMessage(), "Unknown Exception");
  }
}
