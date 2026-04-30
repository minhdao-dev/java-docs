package fundamentals.controlflow;

/**
 * Demonstrates control-flow patterns: early return, switch expressions
 * with arrow syntax and {@code yield}, and pattern matching switch with
 * guarded patterns (Java 21+).
 *
 * <p>Lesson 04 — Control Flow
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/04-control-flow.en.md">docs/java/fundamentals/04-control-flow.en.md</a>)
 */
public class ControlFlowDemo {

    // Early return — phân loại điểm
    static String gradeOf(int score) {
        if (score < 0 || score > 100) return "Invalid";
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    // switch expression — tên tháng
    static String monthName(int month) {
        return switch (month) {
            case 1  -> "January";
            case 2  -> "February";
            case 3  -> "March";
            case 4  -> "April";
            case 5  -> "May";
            case 6  -> "June";
            case 7  -> "July";
            case 8  -> "August";
            case 9  -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

    // switch expression với yield
    static int httpStatus(String code) {
        return switch (code) {
            case "OK"        -> 200;
            case "NOT_FOUND" -> 404;
            case "ERROR" -> {
                System.out.println("Logging server error...");
                yield 500;
            }
            default -> throw new IllegalArgumentException("Unknown code: " + code);
        };
    }

    // Pattern matching (Java 21+)
    static String describe(Object obj) {
        return switch (obj) {
            case null                      -> "null";
            case Integer i when i < 0      -> "Negative: " + i;
            case Integer i                 -> "Integer: " + i;
            case String s when s.isEmpty() -> "Empty string";
            case String s                  -> "String: \"" + s + "\"";
            default -> "Type: " + obj.getClass().getSimpleName();
        };
    }

    public static void main(String[] args) {
        System.out.println(gradeOf(85));             // B
        System.out.println(monthName(7));            // July
        System.out.println(httpStatus("NOT_FOUND")); // 404
        System.out.println(describe(-42));           // Negative: -42
        System.out.println(describe("Hello"));       // String: "Hello"
        System.out.println(describe(""));            // Empty string
        System.out.println(describe(null));          // null
    }
}
