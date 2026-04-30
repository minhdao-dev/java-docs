# Enum

## 1. What is an Enum

**Enum** (enumeration) is a special type in Java used to represent a **fixed set of named constants with clear meaning**.

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

Each value in an enum (`MONDAY`, `TUESDAY`, ...) is called an **enum constant**. These are objects, not plain integers or strings.

```java
Day today = Day.WEDNESDAY;
System.out.println(today); // WEDNESDAY
```

---

## 2. Why It Matters

Before enums, developers used `int` or `String` constants to represent fixed sets:

```java
// ❌ Old way — int constants
public static final int STATUS_PENDING  = 0;
public static final int STATUS_ACTIVE   = 1;
public static final int STATUS_INACTIVE = 2;

void setStatus(int status) { ... } // accepts any int — not type-safe
setStatus(99); // compiler won't complain, but 99 is invalid
```

```java
// ❌ Old way — String constants
public static final String ROLE_ADMIN = "ADMIN";
public static final String ROLE_USER  = "USER";

void setRole(String role) { ... } // typos go undetected
setRole("admin"); // differs from "ADMIN" — logic bug, compiler won't catch it
```

**Enum solves all of these problems:**

```java
// ✅ Enum — type-safe, expressive, impossible to pass invalid values
public enum OrderStatus { PENDING, ACTIVE, INACTIVE }
public enum Role { ADMIN, USER }

void setStatus(OrderStatus status) { ... }
setStatus(OrderStatus.PENDING); // ✅
setStatus(99);                  // ❌ compile error immediately
```

**Concrete benefits:**

- **Type-safe** — compiler catches invalid values at compile time
- **Readable** — code is self-documenting, no comments needed
- **Maintainable** — add/remove constants in one place, IDE highlights all usages
- **Switchable** — switch and pattern matching natively support enums
- **Singleton guarantee** — each constant exists as exactly one instance in the JVM

---

## 3. Basic Enum

### Declaration

```java
public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER
}
```

Convention: enum name in **PascalCase**, constants in **SCREAMING_SNAKE_CASE**.

### Usage

```java
Season current = Season.SUMMER;

// Compare with ==, not .equals()
if (current == Season.SUMMER) {
    System.out.println("It's summer!");
}
```

### name() and ordinal()

Every enum constant has two built-in properties:

```java
Season s = Season.AUTUMN;

System.out.println(s.name());    // "AUTUMN" — the constant's name as a String
System.out.println(s.ordinal()); // 2 — position in declaration (zero-based)
```

!!! warning "Don't rely on ordinal()"
    `ordinal()` returns the declaration position. If you insert a new constant in the middle later, every ordinal after it shifts — breaking any logic that depended on the old values.

### values() and valueOf()

```java
// values() — returns an array of all constants in declaration order
for (Season s : Season.values()) {
    System.out.println(s); // SPRING, SUMMER, AUTUMN, WINTER
}

// valueOf() — converts a String to an enum constant (case-sensitive)
Season s = Season.valueOf("WINTER"); // Season.WINTER
Season x = Season.valueOf("winter"); // ❌ IllegalArgumentException
```

---

## 4. Enum with Fields and Constructor

Enum is a special class — it can have **fields**, a **constructor**, and **methods**.

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS  (4.869e+24, 6.0518e6),
    EARTH  (5.976e+24, 6.37814e6),
    MARS   (6.421e+23, 3.3972e6);

    private final double mass;   // kg
    private final double radius; // m

    Planet(double mass, double radius) { // constructor is always private
        this.mass   = mass;
        this.radius = radius;
    }

    static final double G = 6.67300E-11;

    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}
```

```java
double earthWeight = 75.0;
double mass = earthWeight / Planet.EARTH.surfaceGravity();

for (Planet p : Planet.values()) {
    System.out.printf("Weight on %s: %.2f%n", p, p.surfaceWeight(mass));
}
// Weight on MERCURY: 28.33
// Weight on EARTH: 75.00
// ...
```

!!! tip "Enum constructors are always private"
    Even without an explicit `private` keyword, enum constructors are always private — no one can create enum instances from outside. This is how Java guarantees the singleton property of each constant.

---

## 5. Real-World Example — OrderStatus

This pattern appears in almost every backend application:

```java
public enum OrderStatus {
    PENDING("Awaiting processing"),
    CONFIRMED("Confirmed"),
    SHIPPING("In transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }
}
```

```java
OrderStatus status = OrderStatus.SHIPPING;
System.out.println(status.getDisplayName()); // In transit
System.out.println(status.isFinal());        // false
```

---

## 6. Enum in switch

Enums pair naturally with switch — the compiler checks all cases when using enhanced switch.

### Traditional switch

```java
Day day = Day.SATURDAY;

switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println("Weekday");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println("Weekend");
        break;
}
```

### Enhanced switch (Java 14+ — preferred)

```java
String type = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
    case SATURDAY, SUNDAY                              -> "Weekend";
};
System.out.println(type); // Weekend
```

!!! tip "Enhanced switch with enum — exhaustiveness check"
    If the switch expression doesn't cover all enum constants, the compiler raises an error. This is one of the most powerful reasons to use enum + switch for fixed-set logic.

---

## 7. Enum with Abstract Methods

Each constant can override an abstract method — giving each constant its own behavior.

```java
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) { this.symbol = symbol; }

    public abstract double apply(double x, double y);

    @Override
    public String toString() { return symbol; }
}
```

```java
double x = 10, y = 3;
for (Operation op : Operation.values()) {
    System.out.printf("%.1f %s %.1f = %.1f%n", x, op, y, op.apply(x, y));
}
// 10.0 + 3.0 = 13.0
// 10.0 - 3.0 = 7.0
// 10.0 * 3.0 = 30.0
// 10.0 / 3.0 = 3.3
```

This pattern completely replaces long `if-else` chains — adding a new operation only requires a new constant, no existing logic changes.

---

## 8. EnumSet and EnumMap

Two collections optimized specifically for enums — faster than `HashSet` and `HashMap`.

```java
import java.util.EnumSet;
import java.util.EnumMap;

// EnumSet — a set of enum constants
EnumSet<Day> weekdays = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY,
                                    Day.THURSDAY, Day.FRIDAY);
EnumSet<Day> weekend  = EnumSet.complementOf(weekdays); // SATURDAY, SUNDAY

System.out.println(weekdays.contains(Day.MONDAY));  // true
System.out.println(weekend.contains(Day.SATURDAY)); // true
```

```java
// EnumMap — a map with enum constants as keys
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MONDAY,    "Standup meeting");
schedule.put(Day.WEDNESDAY, "Code review");
schedule.put(Day.FRIDAY,    "Demo");

System.out.println(schedule.get(Day.MONDAY)); // Standup meeting
```

!!! tip "Use EnumSet/EnumMap when the key is an enum"
    `EnumSet` uses a bit-vector internally — each bit corresponds to one constant by ordinal — making `contains()`, `add()`, `remove()` extremely fast. `EnumMap` uses an array indexed by ordinal. Both are significantly faster than their generic counterparts for enum keys.

---

## 9. Full Example

!!! info "Verified"
    Full compilable source: [`EnumDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/enums/EnumDemo.java)

```java linenums="1"
public class EnumDemo {

    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    enum HttpStatus {
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int    code;
        private final String reason;

        HttpStatus(int code, String reason) {
            this.code   = code;
            this.reason = reason;
        }

        public int    getCode()   { return code; }
        public String getReason() { return reason; }

        public boolean isError() { return code >= 400; }

        public static HttpStatus fromCode(int code) { // (1)
            for (HttpStatus s : values()) {
                if (s.code == code) return s;
            }
            throw new IllegalArgumentException("Unknown HTTP status: " + code);
        }
    }

    public static void main(String[] args) {
        // Basic usage
        Priority p = Priority.HIGH;
        System.out.println(p.name());    // HIGH
        System.out.println(p.ordinal()); // 2

        // Enhanced switch
        String label = switch (p) {
            case LOW, MEDIUM -> "Normal";
            case HIGH        -> "Urgent";
            case CRITICAL    -> "Emergency";
        };
        System.out.println(label); // Urgent

        // Enum with fields
        HttpStatus status = HttpStatus.NOT_FOUND;
        System.out.println(status.getCode());   // 404
        System.out.println(status.getReason()); // Not Found
        System.out.println(status.isError());   // true

        // Lookup by value
        HttpStatus found = HttpStatus.fromCode(200);
        System.out.println(found); // OK

        // Iterate all constants
        for (HttpStatus s : HttpStatus.values()) {
            System.out.printf("[%d] %s%n", s.getCode(), s.getReason());
        }
    }
}
```

1. Factory method `fromCode()` — a common pattern to look up an enum constant from a raw value (int, String). Frequently used when deserializing data from a database or external API.

---

## 10. Common Mistakes

### Mistake 1 — Using ordinal() for logic

```java
// ❌ ordinal() shifts when a new constant is inserted in the middle
public enum Status { PENDING, ACTIVE, INACTIVE }

int code = status.ordinal(); // saved to DB
// Later someone adds SUSPENDED between ACTIVE and INACTIVE
// → INACTIVE.ordinal() changes from 2 to 3, all old data maps incorrectly

// ✅ Use a dedicated field instead
public enum Status {
    PENDING(0), ACTIVE(1), INACTIVE(2);
    private final int code;
    Status(int code) { this.code = code; }
    public int getCode() { return code; }
}
```

### Mistake 2 — Not handling valueOf() exceptions

```java
// ❌ crashes on invalid input
String input = request.getParam("status"); // could be "pending" (lowercase)
OrderStatus s = OrderStatus.valueOf(input); // IllegalArgumentException

// ✅ Handle safely
try {
    OrderStatus s = OrderStatus.valueOf(input.toUpperCase());
} catch (IllegalArgumentException e) {
    // return a proper error response to the client
}
```

### Mistake 3 — Using .equals() instead of ==

```java
// ❌ .equals() works, but is redundant and throws NullPointerException if status is null
if (status.equals(OrderStatus.PENDING)) { ... }

// ✅ Use == — safe even if the left side is null when reversed
if (status == OrderStatus.PENDING) { ... }
```

### Mistake 4 — Using String constants in switch instead of Enum

```java
// ❌ compiler won't warn when a new case is added but not handled
String status = "PENDING";
if (status.equals("PENDING")) { ... }
else if (status.equals("ACTIVE")) { ... }
// Adding "CANCELLED" later and forgetting to handle it — no warning

// ✅ Enum + exhaustive switch — compiler errors on missing cases
OrderStatus status = OrderStatus.PENDING;
String result = switch (status) {
    case PENDING -> "...";
    case ACTIVE  -> "...";
    // Missing CANCELLED → compiler error when switch is an expression
};
```

---

## 11. Interview Questions

**Q1: Is a Java enum really a class?**

> Yes. The compiler translates an enum into a class that extends `java.lang.Enum`. Each constant is a `public static final` instance of that class. This is why enums can have fields, constructors, and methods — and why each constant is a real object.

**Q2: Why shouldn't you use ordinal() to persist to a database?**

> `ordinal()` depends on the **declaration order** in source code. If someone inserts a new constant in the middle later, all ordinals after it shift — causing old data in the database to map to the wrong constants. The correct approach is to add a dedicated `code` or `value` field to the enum and use that field for persistence.

**Q3: Can an enum implement an interface? Can it extend a class?**

> An enum **can implement interfaces** — this is how you add shared behavior across all constants. An enum **cannot extend a class** because it already implicitly extends `java.lang.Enum`, and Java does not support multiple class inheritance.

**Q4: Is Enum thread-safe?**

> Yes. Enum constants are `static final` and initialized exactly once by the class loader — a process that is thread-safe by the JVM specification. This makes Enum the safest way to implement the **Singleton pattern** in Java (immune to reflection attacks and serialization issues).

**Q5: How does EnumSet differ from a HashSet containing enums?**

> `EnumSet` uses a **bit-vector** internally (one bit per constant, indexed by ordinal), so `contains()`, `add()`, and `remove()` run in O(1) with minimal memory. `HashSet` is more general but has hashing and boxing overhead. For enum-typed elements, always prefer `EnumSet`.

---

## 12. References

| Resource | Content |
|----------|---------|
| [JLS §8.9 — Enum Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.9) | Official specification |
| [Oracle Tutorial — Enum Types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) | Official guide |
| *Effective Java* — Joshua Bloch | Item 34: Use enums instead of int constants · Item 38: Emulate extensible enums with interfaces |
| [Baeldung — Guide to Java Enums](https://www.baeldung.com/a-guide-to-java-enums) | Practical walkthrough |
