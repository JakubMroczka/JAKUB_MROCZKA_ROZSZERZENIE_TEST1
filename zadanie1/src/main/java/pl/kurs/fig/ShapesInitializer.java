package pl.kurs.fig;

public final class ShapesInitializer {
    private ShapesInitializer() {
    }

    public static ShapeFactory shapeFactory() {
        return new ShapeFactory(new MapShapeCache());
    }

    public static ShapeService shapeService() {
        return new ShapeService();
    }
}
