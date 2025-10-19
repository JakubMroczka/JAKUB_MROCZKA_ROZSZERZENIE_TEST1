package pl.kurs.fig.model;

import lombok.Getter;
import pl.kurs.fig.interfaces.Shape;

@Getter
public class Square implements Shape {
    private final double side;

    Square(double side) {
        if (side <= 0) throw new IllegalArgumentException("side <= 0");
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * side;
    }

    @Override
    public double calculatePerimeter() {
        return 4 * side;
    }
}