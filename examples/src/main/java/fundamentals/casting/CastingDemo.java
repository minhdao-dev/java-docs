package fundamentals.casting;

public class CastingDemo {

    abstract static class Shape {
        abstract double area();
        String type() { return getClass().getSimpleName(); }
    }

    static class Circle extends Shape {
        double radius;
        Circle(double r)        { this.radius = r; }
        @Override double area() { return Math.PI * radius * radius; }
        void scale(double f)    { radius *= f; }
    }

    static class Rectangle extends Shape {
        double w, h;
        Rectangle(double w, double h) { this.w = w; this.h = h; }
        @Override double area()       { return w * h; }
        void rotate()                 { double t = w; w = h; h = t; }
    }

    static class Triangle extends Shape {
        double base, height;
        Triangle(double b, double h)  { this.base = b; this.height = h; }
        @Override double area()       { return 0.5 * base * height; }
    }

    static void printArea(Shape s) {
        System.out.printf("%s: area = %.2f%n", s.type(), s.area());
    }

    static void describe(Shape s) {
        if (s instanceof Circle c) {
            System.out.printf("Circle — radius=%.1f, scale x2 → ", c.radius);
            c.scale(2);
            System.out.printf("radius=%.1f%n", c.radius);
        } else if (s instanceof Rectangle r) {
            System.out.printf("Rectangle — %.1fx%.1f → rotated: %.1fx%.1f%n",
                r.w, r.h, r.h, r.w);
            r.rotate();
        } else if (s instanceof Triangle t) {
            System.out.printf("Triangle — base=%.1f, h=%.1f%n", t.base, t.height);
        }
    }

    static void safeCheck(Object obj) {
        if (obj instanceof String s && !s.isBlank()) {
            System.out.println("Valid string: " + s);
        } else {
            System.out.println("Not a string or blank: " + obj);
        }
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8)
        };

        System.out.println("=== Areas ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Describe & Mutate ===");
        for (Shape s : shapes) describe(s);

        System.out.println("\n=== Areas after mutation ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Null safety ===");
        safeCheck("Hello");
        safeCheck(null);
        safeCheck("   ");
        safeCheck(42);
    }
}
