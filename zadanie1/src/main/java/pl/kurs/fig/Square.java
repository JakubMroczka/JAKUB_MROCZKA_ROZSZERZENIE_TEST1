package pl.kurs.fig;

public final class Square implements Shape {
    private final double side;

    Square(double side) {
        if (side <= 0) throw new IllegalArgumentException("side <= 0");
        this.side = side;
    }

    double getSide() {
        return side;
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }

    @Override
    public String type() {
        return "square";
    }
}