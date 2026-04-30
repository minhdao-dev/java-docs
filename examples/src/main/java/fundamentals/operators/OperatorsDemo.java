package fundamentals.operators;

/**
 * Demonstrates Java operators: arithmetic, prefix/postfix increment,
 * short-circuit evaluation, bitwise flags, ternary, instanceof pattern
 * matching (Java 16+), and silent integer overflow.
 *
 * <p>Lesson 03 — Operators
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/03-operators.en.md">docs/java/fundamentals/03-operators.en.md</a>)
 */
public class OperatorsDemo {

    public static void main(String[] args) {

        // ── Số học ─────────────────────────────────────────────
        int a = 10, b = 3;
        System.out.println(a / b);          // 3  — chia nguyên, cắt bỏ phần thập phân
        System.out.println((double) a / b); // 3.3333...
        System.out.println(a % b);          // 1

        // ── Prefix vs Postfix ──────────────────────────────────
        int x = 5;
        System.out.println(x++); // 5 — in trước, tăng sau
        System.out.println(x);   // 6
        System.out.println(++x); // 7 — tăng trước, in sau

        // ── Short-circuit ──────────────────────────────────────
        String str = null;
        boolean safe = (str != null && str.length() > 0); // không NPE
        System.out.println(safe); // false

        // ── Toán tử bit ───────────────────────────────────────
        int READ    = 1, WRITE = 2, EXECUTE = 4;
        int perm    = READ | WRITE;               // 3 = 0b011
        System.out.println((perm & READ)    != 0); // true
        System.out.println((perm & EXECUTE) != 0); // false

        // ── Tam phân ──────────────────────────────────────────
        int score = 85;
        String grade = (score >= 90) ? "A" : (score >= 70) ? "B" : "C";
        System.out.println(grade); // "B"

        // ── instanceof pattern matching (Java 16+) ─────────────
        Object obj = "Hello";
        if (obj instanceof String s) {
            System.out.println(s.toUpperCase()); // HELLO — không cần cast
        }

        // ── Overflow ngầm ─────────────────────────────────────
        int max = Integer.MAX_VALUE;
        System.out.println(max + 1); // -2147483648 — overflow, không báo lỗi
    }
}
