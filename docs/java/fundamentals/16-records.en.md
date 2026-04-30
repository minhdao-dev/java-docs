# Records

## 1. What is a Record

**Record** (Java 16+) is a special class designed to be a **data carrier** — an object whose sole purpose is to hold data, with no complex behavior. A record automatically generates all the boilerplate code developers would otherwise write by hand.

```java
// Record declaration — one line
public record Point(int x, int y) { }
```

The compiler automatically generates:
- A **constructor** that accepts all fields
- An **accessor method** for each field (same name as the field, no `get` prefix)
- **`equals()`** — compares field by field
- **`hashCode()`** — computed from all fields
- **`toString()`** — prints the class name and all fields

```java
Point p = new Point(3, 4);

System.out.println(p.x());          // 3   ← accessor
System.out.println(p.y());          // 4
System.out.println(p);              // Point[x=3, y=4]  ← toString()
System.out.println(p.equals(new Point(3, 4))); // true  ← equals()
```

---

## 2. Why It Matters

Before records, creating a simple data class required a lot of code:

```java
// ❌ Traditional class — 40+ lines just to hold 2 fields
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

```java
// ✅ Record — 1 line, 100% equivalent
public record Point(int x, int y) { }
```

Records solve:

- **Boilerplate** — no more hand-writing constructors, getters, equals, hashCode, toString
- **Immutability by default** — all fields are `final`, cannot change after creation
- **Clear intent** — anyone reading the code immediately understands this is a data class with no side effects
- **Fewer bugs** — equals/hashCode are generated correctly, no forgotten fields

---

## 3. Basic Record

### Declaration

```java
public record Person(String name, int age) { }

public record Point(double x, double y) { }

public record Range(int min, int max) { }
```

### Creating and using records

```java
Person p = new Person("Alice", 30);

// Accessors — same name as field, no "get" prefix
System.out.println(p.name()); // Alice
System.out.println(p.age());  // 30

// Automatic toString()
System.out.println(p); // Person[name=Alice, age=30]

// Automatic equals() — compares by value
Person p2 = new Person("Alice", 30);
System.out.println(p.equals(p2)); // true

// hashCode() consistent with equals()
System.out.println(p.hashCode() == p2.hashCode()); // true
```

### Immutability — fields are always final

```java
Person p = new Person("Alice", 30);
// p.name = "Bob"; // ❌ won't compile — field is final
// p.age  = 31;    // ❌ won't compile
```

!!! tip "Records are immutable by design"
    Immutable objects are safer in multi-threaded environments, easier to test, and easier to reason about since state never changes after creation. Records enforce this without any extra code.

---

## 4. Compact Constructor — Validating Input

Use a **compact constructor** to validate input before it's stored in the fields. The compact constructor doesn't declare parameters (they're inherited from the record header) and doesn't need `this.field = value` assignments (the compiler adds them after the constructor body).

```java
public record Range(int min, int max) {

    // Compact constructor — only write validation
    Range {
        if (min > max) {
            throw new IllegalArgumentException(
                "min (%d) must be <= max (%d)".formatted(min, max));
        }
    }
}
```

```java
Range r1 = new Range(1, 10);  // ✅
Range r2 = new Range(10, 1);  // ❌ IllegalArgumentException: min (10) must be <= max (1)
```

```java
// Another example — normalize a String
public record Email(String address) {

    Email {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + address);
        }
        address = address.toLowerCase().trim(); // (1)
    }
}
```

1. Inside a compact constructor, you can reassign a component — the compiler uses the new value for the field assignment. This is the only way to transform input inside a record.

```java
Email e = new Email("  Alice@Example.COM  ");
System.out.println(e.address()); // alice@example.com — normalized
```

---

## 5. Custom Methods in Records

Records can have regular instance methods and static methods:

```java
public record Point(double x, double y) {

    // Instance method
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Static factory method — common pattern
    public static Point origin() {
        return new Point(0, 0);
    }
}
```

```java
Point a = new Point(0, 0);
Point b = new Point(3, 4);

System.out.println(a.distanceTo(b)); // 5.0
System.out.println(b.magnitude());  // 5.0
System.out.println(Point.origin()); // Point[x=0.0, y=0.0]
```

---

## 6. Record implements Interface

Records can implement interfaces but **cannot extend a class** (they already implicitly extend `java.lang.Record`).

```java
public interface Describable {
    String describe();
}

public record Product(String name, double price) implements Describable {

    @Override
    public String describe() {
        return "%s — %.2f USD".formatted(name, price);
    }
}
```

```java
Product p = new Product("Laptop", 999.99);
System.out.println(p.describe()); // Laptop — 999.99 USD
```

---

## 7. Records as DTOs — the Most Common Use Case

**DTO (Data Transfer Object)** is an extremely common pattern in Spring Boot — used to receive requests from clients or send responses back without directly exposing entities. Records are a perfect fit:

```java
// Request DTO — receive data from client
public record CreateUserRequest(
    String username,
    String email,
    String password
) {
    CreateUserRequest {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be blank");
        if (!email.contains("@"))
            throw new IllegalArgumentException("Invalid email");
    }
}

// Response DTO — send back to client (password excluded)
public record UserResponse(
    Long   id,
    String username,
    String email
) { }
```

```java
// You'll learn to use these with Spring Boot in Phase 05
// CreateUserRequest req = new CreateUserRequest("alice", "alice@mail.com", "secret");
// UserResponse res = userService.create(req);
// return ResponseEntity.ok(res);
```

---

## 8. Generic Records

Records support type parameters:

```java
public record Pair<A, B>(A first, B second) {

    public Pair<B, A> swap() {
        return new Pair<>(second, first);
    }
}
```

```java
Pair<String, Integer> p = new Pair<>("hello", 42);
System.out.println(p.first());  // hello
System.out.println(p.second()); // 42

Pair<Integer, String> swapped = p.swap();
System.out.println(swapped); // Pair[first=42, second=hello]
```

---

## 9. Record vs Class vs Lombok

| | Traditional | Lombok `@Data` | Record (Java 16+) |
|---|---|---|---|
| Boilerplate | High | Low (annotation) | None |
| Immutable | Optional (`final`) | No (setters generated) | Always |
| Inheritance | Yes | Yes | Cannot extend classes |
| External dependency | No | Lombok required | No |
| Java version | Any | Any (with Lombok) | Java 16+ |
| Best for | Any object type | Any object type | Data-only, DTO, value object |

!!! tip "When to use Record vs a regular class?"
    **Use Record** when the object only holds data, needs no mutation after creation, and needs no inheritance — DTOs, response objects, value objects, config tuples.
    **Use a regular class** when you need mutable state, inheritance, complex logic, or framework requirements (JPA entities require a no-arg constructor and setters — records can't provide these).

---

## 10. Record Limitations

```java
// ❌ Cannot extend a class
public record Point(int x, int y) extends Shape { } // compile error

// ❌ Cannot have instance fields outside record components
public record Point(int x, int y) {
    private int z; // ❌ compile error — only static fields are allowed
    private static int count = 0; // ✅ static field is fine
}

// ❌ Cannot be abstract
public abstract record Shape(double area) { } // compile error

// ❌ JPA Entity cannot be a Record
// @Entity
// public record Student(...) { } // JPA needs a no-arg constructor and setters
```

---

## 11. Full Example

!!! info "Verified"
    Full compilable source: [`RecordDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/records/RecordDemo.java)

```java linenums="1"
public class RecordDemo {

    // Basic record with validation
    record Point(double x, double y) {
        Point {
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("Coordinates cannot be NaN");
        }

        double distanceTo(Point other) {
            double dx = x - other.x, dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        static Point origin() { return new Point(0, 0); }
    }

    // Generic record
    record Pair<A, B>(A first, B second) {
        Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    // Record as DTO with normalization
    record StudentDTO(String name, double gpa) {
        StudentDTO {
            if (gpa < 0 || gpa > 10)
                throw new IllegalArgumentException("GPA must be in [0, 10]");
            name = name.strip();
        }

        String grade() { // (1)
            if (gpa >= 9.0) return "Distinction";
            if (gpa >= 8.0) return "Merit";
            if (gpa >= 6.5) return "Credit";
            if (gpa >= 5.0) return "Pass";
            return "Fail";
        }
    }

    public static void main(String[] args) {
        // Point
        Point a = Point.origin();
        Point b = new Point(3, 4);
        System.out.println(b);               // Point[x=3.0, y=4.0]
        System.out.println(a.distanceTo(b)); // 5.0

        // Value-based equals
        System.out.println(b.equals(new Point(3, 4))); // true

        // Pair
        Pair<String, Integer> pair = new Pair<>("score", 95);
        System.out.println(pair);        // Pair[first=score, second=95]
        System.out.println(pair.swap()); // Pair[first=95, second=score]

        // StudentDTO
        StudentDTO s = new StudentDTO("  Alice Smith  ", 8.5);
        System.out.println(s.name());  // Alice Smith — stripped
        System.out.println(s.grade()); // Merit
        System.out.println(s);         // StudentDTO[name=Alice Smith, gpa=8.5]

        // Immutability — record cannot be mutated after creation
        // s.name = "other"; // ❌ won't compile
    }
}
```

1. Records can have business methods — but if the logic becomes complex, consider using a regular class instead of stuffing everything into a record.

---

## 12. Common Mistakes

### Mistake 1 — Calling getter with the `get` prefix

```java
record Person(String name, int age) { }

Person p = new Person("Alice", 30);

// ❌ NoSuchMethodError — records do NOT generate getName()
p.getName();

// ✅ Record accessors have no "get" prefix
p.name();
p.age();
```

### Mistake 2 — Using Record for a JPA Entity

```java
// ❌ JPA needs a no-arg constructor and setters — record can't provide them
@Entity
public record Student(Long id, String name) { }

// ✅ JPA Entities must be regular classes
@Entity
public class Student {
    @Id Long id;
    String name;
    // getters, setters, no-arg constructor...
}
```

### Mistake 3 — Confusing compact constructor with canonical constructor

```java
record Range(int min, int max) {

    // ❌ This is a canonical constructor — redeclaring it is redundant
    //    and easy to get wrong (must match the signature exactly)
    Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // ✅ Compact constructor — no parameter list, no manual assignments
    Range {
        if (min > max) throw new IllegalArgumentException("min > max");
    }
}
```

### Mistake 4 — Adding instance fields to a record

```java
// ❌ Instance fields outside components are not allowed
record Point(int x, int y) {
    private String label; // compile error
}

// ✅ Add the field as a component if you need it
record Point(int x, int y, String label) { }

// ✅ Or derive it from existing components
record Point(int x, int y) {
    double magnitude() { return Math.sqrt(x * x + y * y); }
}
```

---

## 13. Interview Questions

**Q1: What is a Java Record and when should you use it?**

> A Record (Java 16+) is a special class for representing **immutable data carriers**. The compiler auto-generates the canonical constructor, accessor methods, `equals()`, `hashCode()`, and `toString()`. Use records when an object only holds data and needs no mutation after creation — DTOs, value objects, response payloads, config tuples. Avoid them for JPA entities (require mutable state) or classes that need inheritance.

**Q2: How does a Record differ from a regular class?**

> Key differences: (1) all components are `private final` — always immutable; (2) cannot extend any class (implicitly extends `java.lang.Record`); (3) cannot have instance fields beyond the declared components; (4) accessor methods have no `get` prefix; (5) `equals()`/`hashCode()`/`toString()` are auto-generated based on all components. Regular classes are more flexible but require significant boilerplate.

**Q3: What is a compact constructor and when do you use it?**

> A compact constructor uses a special syntax — no parameter list (inherited from the record header), no `this.field = value` assignments (the compiler appends them after the body). Use it to validate input or normalize data (e.g., trimming Strings, lowercasing). To transform a component, simply reassign it inside the compact constructor — the compiler uses the new value for the field assignment.

**Q4: Can a Record implement an interface?**

> Yes. A record can implement any interface and must provide implementations for all abstract methods. However, a record cannot extend a class because it already implicitly extends `java.lang.Record`. This makes interface implementation the only way to add shared behavior to records — for example, implementing `Comparable`, `Serializable`, or a custom interface.

**Q5: Why can't a JPA Entity be a Record?**

> JPA (Hibernate) requires entities to have a **no-argument constructor** (so Hibernate can instantiate objects via reflection when loading from the database) and **setter methods** (so Hibernate can populate fields). Records don't provide either — the canonical constructor always requires all components, and all fields are `final` so no setters can exist. This is a fundamental design constraint when combining records with frameworks that require mutable objects.

---

## 14. References

| Resource | Content |
|----------|---------|
| [JEP 395 — Records](https://openjdk.org/jeps/395) | Official design proposal |
| [Oracle Docs — Record Classes](https://docs.oracle.com/en/java/docs/api/java.base/java/lang/Record.html) | Javadoc |
| [Oracle Tutorial — Record Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/records.html) | Official guide |
| [Baeldung — Java Records](https://www.baeldung.com/java-record-keyword) | Practical walkthrough |
