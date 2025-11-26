package pl.kurs.fig.model;

import pl.kurs.fig.interfaces.Shape;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShapeFactory {

    private final Map<String, Shape> cache = new ConcurrentHashMap<>();

    public Square createSquare(double side) {
        String key = "square:" + side;
        return (Square) cache.computeIfAbsent(key, k -> Square.createSquare(side));
    }

    public Circle createCircle(double radius) {
        String key = "circle:" + radius;
        return (Circle) cache.computeIfAbsent(key, k -> Circle.createCircle(radius));
    }

    public Rectangle createRectangle(double width, double height) {
        String key = "rectangle:" + width + "x" + height;
        return (Rectangle) cache.computeIfAbsent(key, k -> Rectangle.createRectangle(width, height));
    }
}

