package pl.kurs.fig;


import org.junit.Test;
import pl.kurs.fig.exceptions.ShapeNotFoundException;
import pl.kurs.fig.interfaces.Shape;
import pl.kurs.fig.model.Circle;
import pl.kurs.fig.model.Rectangle;
import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.model.Square;
import pl.kurs.fig.service.ShapeService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class ShapeServiceTest {

    private final ShapeService service = new ShapeService();

    @Test
    public void shouldFindShapeWithMaxArea2() {
        ShapeFactory factory = new ShapeFactory();
        Square s = factory.createSquare(2);
        Circle c = factory.createCircle(1);
        Rectangle r = factory.createRectangle(3, 4);

        Shape max = service.maxArea(List.of(s, c, r));
        assertSame(r, max);
    }

    @Test(expected = ShapeNotFoundException.class)
    public void shouldThrowWhenListEmpty_ForMaxArea() {
        service.maxArea(List.of());
    }

    @Test
    public void shouldFindMaxPerimeterAmongCircles() {
        ShapeFactory factory = new ShapeFactory();
        Circle c1 = factory.createCircle(1);
        Circle c2 = factory.createCircle(2);
        Square s = factory.createSquare(5);

        Circle result = service.maxPerimeterOfType(List.of(c1, c2, s), Circle.class);

        assertSame(c2, result);
    }


    @Test(expected = ShapeNotFoundException.class)
    public void shouldThrowWhenTypeNotPresent_ForMaxPerimeterOfType() {
        ShapeFactory factory = new ShapeFactory();
        List<Shape> shapes = List.of(
                factory.createSquare(3),
                factory.createRectangle(2, 5)
        );

        service.maxPerimeterOfType(shapes, Circle.class);
    }


    @Test
    public void import_preservesShapesCorrectly() throws Exception {
        ShapeFactory factory = new ShapeFactory();
        ShapeService service = new ShapeService();

        List<Shape> input = List.of(
                factory.createCircle(1.5),
                factory.createRectangle(15, 20),
                factory.createSquare(7)
        );

        Path tmp = Files.createTempFile("shapes-test-", ".json");
        tmp.toFile().deleteOnExit();


        service.exportToJson(input, tmp.toString());

        List<Shape> back = service.importFromJson(tmp.toString());

        assertEquals(input.size(), back.size());


        for (int i = 0; i < input.size(); i++) {
            assertEquals(input.get(i).calculateArea(), back.get(i).calculateArea(), 1e-9);
            assertEquals(input.get(i).calculatePerimeter(), back.get(i).calculatePerimeter(), 1e-9);
        }
    }
}
