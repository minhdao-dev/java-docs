package fundamentals.variables;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Demonstrates Java primitive and reference types: {@code int}, {@code double},
 * {@code boolean}, {@code char}, arrays, {@code var} inference (Java 10+),
 * {@code BigDecimal} for monetary values, {@code ==} vs {@code equals},
 * and the Integer cache boundary.
 *
 * <p>Lesson 02 — Variables &amp; Data Types
 * (<a href="https://github.com/minhdao-dev/java-docs/blob/main/docs/java/fundamentals/02-variables-datatypes.en.md">docs/java/fundamentals/02-variables-datatypes.en.md</a>)
 */
public class DataTypesDemo {

    public static void main(String[] args) {
        // ── Primitive ───────────────────────────────────────────
        int age = 28;
        double salary = 25_000.50;
        boolean isEmployed = true;
        char grade = 'A';

        // ── Reference ───────────────────────────────────────────
        String name = "Nguyen Van A";      // literal → String Pool
        int[] scores = {85, 90, 78};       // array object trên Heap

        // ── var (Java 10+) ──────────────────────────────────────
        var city = "Ho Chi Minh";          // String — compiler tự suy
        var list = new ArrayList<String>(); // ArrayList<String>

        // ── Tiền — luôn dùng BigDecimal ─────────────────────────
        BigDecimal price = new BigDecimal("199.99");
        BigDecimal tax   = new BigDecimal("0.10");
        BigDecimal total = price.multiply(BigDecimal.ONE.add(tax));
        System.out.println("Total: " + total); // 219.989 — chính xác

        // ── == vs equals ────────────────────────────────────────
        String s1 = new String("Java");
        String s2 = new String("Java");
        System.out.println(s1 == s2);      // false — khác địa chỉ
        System.out.println(s1.equals(s2)); // true  — cùng nội dung

        // ── Integer cache boundary ──────────────────────────────
        Integer a = 100; Integer b = 100;
        System.out.println(a == b);  // true  (cached)

        Integer c = 200; Integer d = 200;
        System.out.println(c == d);  // false (ngoài cache)
    }
}
