package pl.kurs.fig;

public final class Circle implements Shape {
    private final double radius;

    Circle(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("radius <= 0");
        this.radius = radius;
    }

    double getRadius() {
        return radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String type() {
        return "circle";
    }
}
