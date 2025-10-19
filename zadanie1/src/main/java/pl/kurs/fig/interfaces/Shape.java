package pl.kurs.fig.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.kurs.fig.model.Circle;
import pl.kurs.fig.model.Rectangle;
import pl.kurs.fig.model.Square;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name = "circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle"),
        @JsonSubTypes.Type(value = Square.class, name = "square")
})
public interface Shape {
    double calculateArea();
    double calculatePerimeter();
}
