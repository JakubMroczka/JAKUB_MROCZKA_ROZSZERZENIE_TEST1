package pl.kurs.fig;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ShapeFactoryTest {
    private final ShapeFactory factory = new ShapeFactory(new MapShapeCache());

    @Test
    public void shouldReturnSameInstanceForEqualSquares() {
        Square s1 = factory.createSquare(10);
        Square s2 = factory.createSquare(10);
        assertSame("Factory should cache by side", s1, s2);
    }

    @Test
    public void shouldReturnSameInstanceForEqualCircles() {
        Circle c1 = factory.createCircle(1.5);
        Circle c2 = factory.createCircle(1.5);
        assertSame("Factory should cache by radius", c1, c2);
    }

    @Test
    public void shouldReturnSameInstanceForEqualRectangles() {
        Rectangle r1 = factory.createRectangle(15, 20);
        Rectangle r2 = factory.createRectangle(15, 20);
        assertSame("Factory should cache by width/height", r1, r2);
    }

    @Test
    public void shouldReturnDifferentInstancesForDifferentParams() {
        assertNotSame(factory.createSquare(10), factory.createSquare(11));
        assertNotSame(factory.createCircle(1.0), factory.createCircle(2.0));
        assertNotSame(factory.createRectangle(10, 20), factory.createRectangle(10, 21));
    }
}