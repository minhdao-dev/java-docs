# 01. Variables, Data Types, Operators

Practice exercises for:
[Variables & Data Types](../../java/fundamentals/02-variables-datatypes.md) ·
[Operators](../../java/fundamentals/03-operators.md)

---

## Exercise 1 — Predict the Output: String Pool · 🟢 Easy

Given the code below, predict the output of each `println` **without running the program**:

```java
String a = "hello";
String b = "hello";
String c = new String("hello");

System.out.println(a == b);       // (1)
System.out.println(a == c);       // (2)
System.out.println(a.equals(c));  // (3)
System.out.println(c.equals(b));  // (4)
```

??? tip "Hint"
    - `==` compares **addresses** on the Stack, not content
    - String literals use the **String Pool** — same content → same object
    - `new String(...)` creates a new object on the Heap, bypassing the Pool
    - `.equals()` compares **character content**

??? success "Solution"
    | Line | Output | Explanation |
    |------|--------|-------------|
    | (1) | `true` | `a` and `b` point to the same object in the String Pool |
    | (2) | `false` | `c` is a separate Heap object — different address from `a` |
    | (3) | `true` | `.equals()` compares content — both are `"hello"` |
    | (4) | `true` | `.equals()` compares content — both are `"hello"` |

---

## Exercise 2 — Fix the Bug · 🟢 Easy

The code below has **3 compile errors**. Find and fix all of them:

```java
public class Main {
    public static void main(String[] args) {
        var total;                       // (A)
        total = 100.5;

        long population = 8_000_000_000; // (B)

        float price = 19.99;             // (C)

        System.out.println(total + " / " + population + " / " + price);
    }
}
```

??? tip "Hint"
    - What does `var` require on the right-hand side?
    - What suffix does an integer literal larger than `int` need?
    - What is the default type of a floating-point literal in Java?

??? success "Solution"
    | Location | Error | Fix |
    |----------|-------|-----|
    | (A) | `var` cannot be declared without an initializer | Replace with `var total = 100.5;` or `double total;` |
    | (B) | `8_000_000_000` exceeds `int` range | Add suffix `L`: `8_000_000_000L` |
    | (C) | `19.99` is a `double` literal — assigning to `float` loses precision | Add suffix `f`: `19.99f`, or change type to `double` |

    ```java
    public class Main {
        public static void main(String[] args) {
            var total = 100.5;
            long population = 8_000_000_000L;
            float price = 19.99f;
            System.out.println(total + " / " + population + " / " + price);
        }
    }
    ```

---

## Exercise 3 — Write Code: Student Card · 🟡 Medium

Write a class `StudentCard` with a `main` method that:

1. Declares variables: full name (`String`), age (`int`), GPA (`double`), enrolled (`boolean`), student ID (`long`)
2. Assigns sample data.
3. Prints in this exact format:

    ```
    ╔══════════════════════════╗
    │      STUDENT CARD        │
    ╠══════════════════════════╣
    │ Name   : Nguyen Van An   │
    │ Age    : 20              │
    │ GPA    : 3.75            │
    │ Status : Enrolled        │
    │ ID     : 20240001        │
    ╚══════════════════════════╝
    ```

4. Uses `var` for at least 2 variables.

??? tip "Hint"
    - `System.out.printf("%-16s", value)` — left-align, pad to 16 characters
    - Ternary for boolean: `isEnrolled ? "Enrolled" : "Not Enrolled"`

??? success "Solution"
    ```java
    public class StudentCard {
        public static void main(String[] args) {
            var name           = "Nguyen Van An";
            int age            = 20;
            var gpa            = 3.75;
            boolean isEnrolled = true;
            long studentId     = 20240001L;

            String status = isEnrolled ? "Enrolled" : "Not Enrolled";

            System.out.println("╔══════════════════════════╗");
            System.out.println("│      STUDENT CARD        │");
            System.out.println("╠══════════════════════════╣");
            System.out.printf( "│ Name   : %-16s│%n", name);
            System.out.printf( "│ Age    : %-16d│%n", age);
            System.out.printf( "│ GPA    : %-16.2f│%n", gpa);
            System.out.printf( "│ Status : %-16s│%n", status);
            System.out.printf( "│ ID     : %-16d│%n", studentId);
            System.out.println("╚══════════════════════════╝");
        }
    }
    ```
