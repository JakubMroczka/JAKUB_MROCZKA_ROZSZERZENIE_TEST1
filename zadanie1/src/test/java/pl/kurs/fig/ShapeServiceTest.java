package pl.kurs.fig;


import org.junit.Test;
import pl.kurs.fig.exceptions.ShapeNotFoundException;
import pl.kurs.fig.interfaces.Shape;
import pl.kurs.fig.model.Circle;
import pl.kurs.fig.model.Rectangle;
import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.model.Square;
import pl.kurs.fig.service.ShapeService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShapeServiceTest {

    private final ShapeService service = new ShapeService();

    @Test
    public void shouldFindShapeWithMaxArea2() {
        ShapeFactory factory = new ShapeFactory(new MapShapeCache());

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
        ShapeFactory factory = new ShapeFactory(new MapShapeCache());
        Circle c1 = factory.createCircle(1);
        Circle c2 = factory.createCircle(2);
        Square s = factory.createSquare(5);

        Circle result = service.maxPerimeterOfType(List.of(c1, c2, s), Circle.class);

        assertSame(c2, result);
    }


    @Test(expected = ShapeNotFoundException.class)
    public void shouldThrowWhenTypeNotPresent_ForMaxPerimeterOfType() {
        ShapeFactory factory = new ShapeFactory(new MapShapeCache());
        List<Shape> shapes = List.of(
                factory.createSquare(3),
                factory.createRectangle(2, 5)
        );

        service.maxPerimeterOfType(shapes, Circle.class);
    }

    @Test
    public void exportImport_usesCache() throws Exception {
        ShapeFactory factory = new ShapeFactory(new MapShapeCache());
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

        assertEquals(input, back);
    }

    @Test
    public void import_usesFactoryMethods() throws Exception {
        ShapeFactory factory = mock(ShapeFactory.class);

        Circle circleMock = mock(Circle.class);
        Rectangle rectMock = mock(Rectangle.class);
        Square squareMock = mock(Square.class);

        when(factory.createCircle(1.5)).thenReturn(circleMock);
        when(factory.createRectangle(15, 20)).thenReturn(rectMock);
        when(factory.createSquare(7)).thenReturn(squareMock);

        ShapeFactory realFactory = new ShapeFactory(new MapShapeCache());
        List<Shape> toExport = List.of(
                realFactory.createCircle(1.5),
                realFactory.createRectangle(15, 20),
                realFactory.createSquare(7)
        );

        Path tmp = Files.createTempFile("shapes-test-", ".json");
        File tmpFile = tmp.toFile();
        tmpFile.deleteOnExit();

        service.exportToJson(toExport, tmpFile.getAbsolutePath());
        List<Shape> imported = service.importFromJson(tmpFile.getAbsolutePath(), factory);

        assertEquals(3, imported.size());
        verify(factory, times(1)).createCircle(1.5);
        verify(factory, times(1)).createRectangle(15, 20);
        verify(factory, times(1)).createSquare(7);
        verifyNoMoreInteractions(factory);
    }
}