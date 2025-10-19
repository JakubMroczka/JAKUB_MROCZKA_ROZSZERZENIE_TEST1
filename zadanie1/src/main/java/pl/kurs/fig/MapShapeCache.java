package pl.kurs.fig;

import pl.kurs.fig.interfaces.Shape;
import pl.kurs.fig.interfaces.ShapeCache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapShapeCache implements ShapeCache {
    private final Map<String, Shape> map = new ConcurrentHashMap<>();


    @Override
    public Shape computeIfAbsent(String key, Function<String, Shape> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }
}