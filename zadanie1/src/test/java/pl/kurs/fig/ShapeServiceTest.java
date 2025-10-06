package pl.kurs.fig;


import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ShapeServiceTest {


    private final ShapeService service = new ShapeService();
    private final ShapeFactory factory = new ShapeFactory(new MapShapeCache());

    @Test
    public void shouldFindShapeWithMaxArea() {

        List<Shape> shapes = List.of(
                factory.createSquare(2),         //area = 4
                factory.createCircle(1),         //area ~ 3.14
                factory.createRectangle(3, 4)    //area = 12 -> max
        );


        Optional<Shape> max = service.maxArea(shapes);

        assertTrue("Expected a max shape", max.isPresent());
        assertEquals("rectangle", max.get().type());
    }

    @Test
    public void shouldReturnEmptyForMaxAreaWhenListEmpty() {

        List<Shape> empty = List.of();


        Optional<Shape> max = service.maxArea(empty);

        assertFalse("Expected empty optional for empty list", max.isPresent());
    }

    @Test
    public void shouldFindMaxPerimeterAmongCircles() {

        List<Shape> shapes = List.of(
                factory.createCircle(1),
                factory.createCircle(2),
                factory.createSquare(5)
        );


        Optional<Circle> maxCirc = service.maxPerimeterOfType(shapes, Circle.class);


        assertTrue(maxCirc.isPresent());
        assertEquals(2.0, maxCirc.get().getRadius(), 1e-9);
    }

    @Test
    public void shouldExportThenImportShapesJson() throws Exception {

        List<Shape> input = List.of(
                factory.createCircle(1.5),
                factory.createRectangle(15, 20),
                factory.createSquare(7)
        );

        Path tmp = Files.createTempFile("shapes-test-", ".json");
        File tmpFile = tmp.toFile();
        tmpFile.deleteOnExit();

        service.exportToJson(input, tmpFile.getAbsolutePath());
        List<Shape> back = service.importFromJson(tmpFile.getAbsolutePath(), factory);


        assertEquals(3, back.size());
        assertEquals("circle", back.get(0).type());
        assertEquals("rectangle", back.get(1).type());
        assertEquals("square", back.get(2).type());


        assertSame(factory.createSquare(7), back.get(2));
    }

    @Test
    public void shouldReturnEmptyForMaxPerimeterOfTypeWhenTypeNotPresent() {
        List<Shape> shapes = List.of(
                factory.createSquare(3),
                factory.createRectangle(2, 5)
        );
        Optional<Circle> result = service.maxPerimeterOfType(shapes, Circle.class);
        assertFalse(result.isPresent());
    }
}