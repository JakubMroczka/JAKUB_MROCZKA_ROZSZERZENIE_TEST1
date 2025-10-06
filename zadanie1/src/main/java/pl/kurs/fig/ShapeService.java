package pl.kurs.fig;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;


public class ShapeService {
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<Shape> maxArea(List<Shape> shapes) {
        return Optional.ofNullable(shapes).orElse(List.of()).stream()
                .max(Comparator.comparingDouble(Shape::area));
    }

    public <T extends Shape> Optional<T> maxPerimeterOfType(List<Shape> shapes, Class<T> type) {
        return Optional.ofNullable(shapes).orElse(List.of()).stream()
                .filter(s -> type.isAssignableFrom(s.getClass()))
                .map(type::cast)
                .max(Comparator.comparingDouble(Shape::perimeter));
    }


    public void exportToJson(List<Shape> shapes, String path) throws Exception {
        List<Map<String, Object>> list = Optional.ofNullable(shapes).orElse(List.of()).stream()
                .map(this::toJsonMap)
                .toList();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
    }

    public List<Shape> importFromJson(String path, ShapeFactory factory) throws Exception {
        List<?> list = mapper.readValue(new File(path), List.class);
        return list.stream()
                .map(o -> (Map<String, Object>) o)
                .map(m -> fromJsonMap(m, factory))
                .toList();
    }

    private Map<String, Object> toJsonMap(Shape s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("type", s.type());
        switch (s.type()) {
            case "circle" -> m.put("radius", ((Circle) s).getRadius());
            case "square" -> m.put("side", ((Square) s).getSide());
            case "rectangle" -> {
                m.put("width", ((Rectangle) s).getWidth());
                m.put("height", ((Rectangle) s).getHeight());
            }
            default -> throw new IllegalArgumentException("Unknown type: " + s.type());
        }
        return m;
    }

    private Shape fromJsonMap(Map<String, Object> m, ShapeFactory f) {
        String type = Objects.toString(m.get("type"), "");
        return switch (type) {
            case "circle" -> f.createCircle(((Number) m.get("radius")).doubleValue());
            case "square" -> f.createSquare(((Number) m.get("side")).doubleValue());
            case "rectangle" -> f.createRectangle(
                    ((Number) m.get("width")).doubleValue(),
                    ((Number) m.get("height")).doubleValue()
            );
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}