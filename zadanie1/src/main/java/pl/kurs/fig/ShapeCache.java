package pl.kurs.fig;

import java.util.Optional;
import java.util.function.Function;

public interface ShapeCache {
    Optional<Shape> get(String key);

    void put(String key, Shape shape);

    Shape computeIfAbsent(String key, Function<String, Shape> mappingFunction);
}