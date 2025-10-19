package pl.kurs.fig;

import pl.kurs.fig.interfaces.Shape;
import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.service.ShapeService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ShapeFactory factory = ShapesInitializer.shapeFactory();
        ShapeService service = ShapesInitializer.shapeService();

        List<Shape> shapes = List.of(
                factory.createCircle(1.5),
                factory.createRectangle(15, 20),
                factory.createSquare(7)
        );

        Path tmp = Files.createTempFile("shapes-demo-", ".json");
        String path = tmp.toString();

        service.exportToJson(shapes, path);
        List<Shape> back = service.importFromJson(path, factory);

        System.out.println("Saved to: " + path);
        System.out.println("Loaded " + back.size() + " figures. First: " + back.get(0));
    }
}
