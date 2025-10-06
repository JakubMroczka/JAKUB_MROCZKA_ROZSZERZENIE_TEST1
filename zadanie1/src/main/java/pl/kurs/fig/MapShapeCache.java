package pl.kurs.fig;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapShapeCache implements ShapeCache {
    private final Map<String, Shape> map = new ConcurrentHashMap<>();

    @Override
    public Optional<Shape> get(String key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public void put(String key, Shape shape) {
        map.put(key, shape);
    }

    @Override
    public Shape computeIfAbsent(String key, Function<String, Shape> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }
}