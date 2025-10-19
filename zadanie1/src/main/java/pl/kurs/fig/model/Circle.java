package pl.kurs.fig.model;

import lombok.Getter;
import pl.kurs.fig.interfaces.Shape;

@Getter
public class Circle implements Shape {
    private final double radius;

    Circle(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("radius <= 0");
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}
