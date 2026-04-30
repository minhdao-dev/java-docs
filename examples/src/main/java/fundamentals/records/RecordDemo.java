package fundamentals.records;

public class RecordDemo {

    record Point(double x, double y) {
        Point {
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("Coordinates must not be NaN");
        }

        double distanceTo(Point other) {
            double dx = x - other.x, dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        static Point origin() { return new Point(0, 0); }
    }

    record Pair<A, B>(A first, B second) {
        Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    record StudentDTO(String name, double gpa) {
        StudentDTO {
            if (gpa < 0 || gpa > 10)
                throw new IllegalArgumentException("GPA must be in [0, 10]");
            name = name.strip();
        }

        String grade() {
            if (gpa >= 9.0) return "Excellent";
            if (gpa >= 8.0) return "Good";
            if (gpa >= 6.5) return "Above average";
            if (gpa >= 5.0) return "Average";
            return "Below average";
        }
    }

    public static void main(String[] args) {
        // Point
        Point a = Point.origin();
        Point b = new Point(3, 4);
        System.out.println(b);                  // Point[x=3.0, y=4.0]
        System.out.println(a.distanceTo(b));    // 5.0

        // Value-based equality
        System.out.println(b.equals(new Point(3, 4))); // true

        // Pair
        Pair<String, Integer> pair = new Pair<>("score", 95);
        System.out.println(pair);               // Pair[first=score, second=95]
        System.out.println(pair.swap());        // Pair[first=95, second=score]

        // StudentDTO
        StudentDTO s = new StudentDTO("  Nguyen Van A  ", 8.5);
        System.out.println(s.name());   // Nguyen Van A — stripped
        System.out.println(s.grade());  // Good
        System.out.println(s);          // StudentDTO[name=Nguyen Van A, gpa=8.5]
    }
}
