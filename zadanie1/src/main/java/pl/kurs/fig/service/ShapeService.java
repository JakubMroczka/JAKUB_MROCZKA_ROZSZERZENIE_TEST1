package pl.kurs.fig.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.fig.exceptions.ShapeNotFoundException;
import pl.kurs.fig.interfaces.Shape;
import pl.kurs.fig.model.Circle;
import pl.kurs.fig.model.Rectangle;
import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.model.Square;

import java.io.File;
import java.util.*;


public class ShapeService {

    private final ObjectMapper mapper = new ObjectMapper();



    public Shape maxArea(List<Shape> shapes) {
        return (shapes == null ? List.<Shape>of() : shapes)
                .stream()
                .max(Comparator.comparingDouble(Shape::calculateArea))
                .orElseThrow(() -> new ShapeNotFoundException("No figures for comparison (area)"));
    }


    public <T extends Shape> T maxPerimeterOfType(List<Shape> shapes, Class<T> type) {
        return Optional.ofNullable(shapes)
                .orElse(List.of())
                .stream()
                .filter(type::isInstance)
                .map(type::cast)
                .max(Comparator.comparingDouble(Shape::calculatePerimeter))
                .orElseThrow(() -> new ShapeNotFoundException("no figures of type: " + type.getSimpleName()));
    }

    public void exportToJson(List<Shape> shapes, String path) throws Exception {
        List<Map<String, Object>> out = new ArrayList<>();
        if (shapes != null) {
            for (Shape s : shapes) {
                Map<String, Object> m = new LinkedHashMap<>();
                if (s instanceof Circle c) {
                    m.put("type", "circle");
                    m.put("radius", c.getRadius());
                } else if (s instanceof Rectangle r) {
                    m.put("type", "rectangle");
                    m.put("width", r.getWidth());
                    m.put("height", r.getHeight());
                } else if (s instanceof Square sq) {
                    m.put("type", "square");
                    m.put("side", sq.getSide());
                } else {
                    throw new IllegalArgumentException("Unsupported shape class: " + s.getClass());
                }
                out.add(m);
            }
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(path), out);
    }


    public List<Shape> importFromJson(String path, ShapeFactory factory) throws Exception {

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rawList = mapper.readValue(new File(path), List.class);

        List<Shape> result = new ArrayList<>();
        for (Map<String, Object> m : rawList) {
            String type = Objects.toString(m.get("type"), "");
            switch (type) {
                case "circle" -> {
                    double r = ((Number) m.get("radius")).doubleValue();
                    result.add(factory.createCircle(r));
                }
                case "square" -> {
                    double s = ((Number) m.get("side")).doubleValue();
                    result.add(factory.createSquare(s));
                }
                case "rectangle" -> {
                    double w = ((Number) m.get("width")).doubleValue();
                    double h = ((Number) m.get("height")).doubleValue();
                    result.add(factory.createRectangle(w, h));
                }
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            }
        }
        return result;
    }
}
