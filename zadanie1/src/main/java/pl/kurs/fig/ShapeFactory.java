package pl.kurs.fig;

public class ShapeFactory {
    private final ShapeCache cache;

    public ShapeFactory(ShapeCache cache) {
        this.cache = cache;
    }

    public Square createSquare(double side) {
        String key = "square:" + side;
        return (Square) cache.computeIfAbsent(key, k -> new Square(side));
    }

    public Circle createCircle(double radius) {
        String key = "circle:" + radius;
        return (Circle) cache.computeIfAbsent(key, k -> new Circle(radius));
    }

    public Rectangle createRectangle(double width, double height) {
        String key = "rectangle:" + width + "x" + height;
        return (Rectangle) cache.computeIfAbsent(key, k -> new Rectangle(width, height));
    }

    private Shape putAndReturn(String key, Shape s) {
        cache.put(key, s);
        return s;
    }
}
