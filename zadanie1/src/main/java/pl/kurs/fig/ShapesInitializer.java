package pl.kurs.fig;

import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.service.ShapeService;

public final class ShapesInitializer {
    public static ShapeFactory shapeFactory() { return new ShapeFactory(); }
    public static ShapeService shapeService() { return new ShapeService(); }
}
