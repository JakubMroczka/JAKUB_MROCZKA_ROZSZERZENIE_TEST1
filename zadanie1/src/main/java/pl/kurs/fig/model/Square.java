package pl.kurs.fig.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.kurs.fig.interfaces.Shape;

@Getter
public class Square implements Shape {
    private final double side;

    Square(double side) {
        if (side <= 0) throw new IllegalArgumentException("side <= 0");
        this.side = side;
    }

    @JsonCreator
    static Square createSquare(@JsonProperty("side") double side) {
        return new Square(side);
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