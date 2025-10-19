package pl.kurs.fig;

import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.service.ShapeService;

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
