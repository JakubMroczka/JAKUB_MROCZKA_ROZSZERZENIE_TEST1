package pl.kurs.fig.model;

import lombok.Getter;
import pl.kurs.fig.interfaces.Shape;

@Getter
public class Rectangle implements Shape {
    private final double width;
    private final double height;

    Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("dims <= 0");
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}
