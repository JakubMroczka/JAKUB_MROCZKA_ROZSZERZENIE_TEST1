package pl.kurs.fig;


import org.junit.Before;
import org.junit.Test;
import pl.kurs.fig.model.Circle;
import pl.kurs.fig.model.Rectangle;
import pl.kurs.fig.model.ShapeFactory;
import pl.kurs.fig.model.Square;

import static org.junit.Assert.*;

public class ShapeFactoryTest {

    private ShapeFactory factory;

    @Before
    public void setUp() {
        factory = new ShapeFactory();
    }

    @Test
    public void shouldReturnSameInstanceForEqualSquares() {
        Square s1 = factory.createSquare(10);
        Square s2 = factory.createSquare(10);
        assertSame(s1, s2);
    }

    @Test
    public void shouldReturnSameInstanceForEqualCircles() {
        Circle c1 = factory.createCircle(1.5);
        Circle c2 = factory.createCircle(1.5);
        assertSame(c1, c2);
    }

    @Test
    public void shouldReturnSameInstanceForEqualRectangles() {
        Rectangle r1 = factory.createRectangle(15, 20);
        Rectangle r2 = factory.createRectangle(15, 20);
        assertSame(r1, r2);
    }

    @Test
    public void shouldReturnDifferentInstancesForDifferentParams() {
        assertNotSame(factory.createSquare(10), factory.createSquare(11));
        assertNotSame(factory.createCircle(1.0), factory.createCircle(2.0));
        assertNotSame(factory.createRectangle(10, 20), factory.createRectangle(10, 21));
    }
}