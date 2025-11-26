package pl.kurs.fig.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.fig.exceptions.ShapeNotFoundException;
import pl.kurs.fig.interfaces.Shape;


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
        mapper.writerFor(new TypeReference<List<Shape>>() {
                })
                .withDefaultPrettyPrinter()
                .writeValue(new File(path), shapes == null ? List.of() : shapes);
    }


    public List<Shape> importFromJson(String path) throws Exception {
        return mapper.readValue(new File(path), new TypeReference<List<Shape>>() {
        });
    }
}
