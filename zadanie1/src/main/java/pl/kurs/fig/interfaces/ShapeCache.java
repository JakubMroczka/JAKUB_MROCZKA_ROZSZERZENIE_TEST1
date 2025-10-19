package pl.kurs.fig.interfaces;

import java.util.function.Function;

public interface ShapeCache {
    Shape computeIfAbsent(String key, Function<String, Shape> mappingFunction);
}