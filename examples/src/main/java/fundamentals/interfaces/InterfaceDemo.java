package fundamentals.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterfaceDemo {

    interface Shape {
        double area();
        double perimeter();

        default String describe() {
            return "%s | Area: %.2f | Perimeter: %.2f"
                .formatted(getClass().getSimpleName(), area(), perimeter());
        }
    }

    interface Resizable {
        void resize(double factor);
    }

    static class Circle implements Shape, Resizable {
        private double radius;

        Circle(double radius) { this.radius = radius; }

        @Override public double area()      { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
        @Override public void resize(double factor) { radius *= factor; }
    }

    static class Rectangle implements Shape, Comparable<Rectangle> {
        private double w, h;

        Rectangle(double w, double h) { this.w = w; this.h = h; }

        @Override public double area()      { return w * h; }
        @Override public double perimeter() { return 2 * (w + h); }

        @Override
        public int compareTo(Rectangle other) {
            return Double.compare(this.area(), other.area());
        }
    }

    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5));
        shapes.add(new Rectangle(4, 6));
        shapes.add(new Circle(3));

        for (Shape s : shapes) {
            System.out.println(s.describe());
        }

        System.out.println();

        Circle c = new Circle(5);
        System.out.println("Before: " + c.describe());
        c.resize(2.0);
        System.out.println("After:  " + c.describe());

        System.out.println();

        List<Rectangle> rects = List.of(
            new Rectangle(3, 4),
            new Rectangle(1, 2),
            new Rectangle(5, 6)
        );
        List<Rectangle> sorted = new ArrayList<>(rects);
        Collections.sort(sorted);
        sorted.forEach(r -> System.out.printf("%.0fx%.0f = %.0f%n", r.w, r.h, r.area()));
    }
}
