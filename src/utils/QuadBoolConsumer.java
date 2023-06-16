package utils;

import java.util.Objects;

@FunctionalInterface
public interface QuadBoolConsumer<X, Y, Z, W> {
    boolean accept(X x, Y y, Z z, W w);

    default QuadBoolConsumer<X, Y, Z, W> andThen(QuadBoolConsumer<? super X, ? super Y, ? super Z, ? super W> after) {
        Objects.requireNonNull(after);

        return (x, y, z, w) -> {
            accept(x, y, z, w);
            return after.accept(x, y, z, w);
        };
    }
}
