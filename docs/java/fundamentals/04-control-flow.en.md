# Control Flow — if / else / switch

## 1. What is it

Control flow is the mechanism that lets a program **choose which path to execute** based on conditions. Instead of running line by line in order, a program can skip or execute different code blocks depending on the state of the data.

Java provides two main branching constructs:

| Construct | Use when |
| --- | --- |
| `if / else if / else` | Complex conditions, value ranges, multiple variables |
| `switch` | One variable compared against multiple specific values |

---

## 2. Why it matters

Branching is the foundation of all business logic. Understanding it helps you:

- Write clear code and avoid deeply nested conditions
- Know when to use `if` vs `switch` for maximum readability
- Avoid the classic fall-through bug in traditional `switch`
- Leverage **switch expressions** (Java 14+) and **pattern matching** (Java 21+) for cleaner, safer code

---

## 3. if / else if / else

```java
if (condition) {
    // executes when condition is true
} else if (otherCondition) {
    // executes when otherCondition is true
} else {
    // executes when all conditions above are false
}
```

The expression in `if` must be of type `boolean` — Java does not implicitly convert numbers or objects to boolean:

```java
int x = 5;

if (x) { ... }       // ❌ compile error — x is int, not boolean
if (x != 0) { ... }  // ✅
```

### Early return instead of nested ifs

When conditions nest deeply, **early return** makes code read top-to-bottom like prose:

```java
// ❌ Pyramid of doom — hard to read, easy to miss cases
public String classify(int score) {
    if (score >= 0) {
        if (score <= 100) {
            if (score >= 90) {
                return "A";
            } else {
                return "B or lower";
            }
        } else {
            return "Invalid";
        }
    } else {
        return "Invalid";
    }
}

// ✅ Early return — handle edge cases first, read sequentially
public String classify(int score) {
    if (score < 0 || score > 100) return "Invalid";
    if (score >= 90) return "A";
    if (score >= 80) return "B";
    if (score >= 70) return "C";
    if (score >= 60) return "D";
    return "F";
}
```

---

## 4. switch statement

`switch` compares one variable against multiple specific values. It reads cleaner than a long `if/else if` chain when you have 3 or more branches for the same variable.

```java
int day = 3;

switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Other day");
}
```

### Fall-through — a feature that is usually a bug

Without `break`, execution **falls through** to the next case:

```java
int x = 1;
switch (x) {
    case 1:
        System.out.println("one");   // printed
        // no break!
    case 2:
        System.out.println("two");   // also printed — fall-through
        break;
    case 3:
        System.out.println("three"); // not printed
}
// Output: one
//         two
```

Fall-through used **intentionally** to group cases with the same behavior:

```java
switch (month) {
    case 1: case 3: case 5:
    case 7: case 8: case 10: case 12:
        days = 31;
        break;
    case 4: case 6: case 9: case 11:
        days = 30;
        break;
    default:
        days = 28;
}
```

### Supported types

Traditional `switch` only works with:

| Type | Note |
| --- | --- |
| `byte`, `short`, `int`, `char` | Primitives |
| `Byte`, `Short`, `Integer`, `Character` | Wrappers |
| `String` | Since Java 7 |
| `enum` | Since Java 5 |

```java
switch (3.14) { ... }    // ❌ double — compile error
switch (longVar) { ... } // ❌ long — compile error
```

---

## 5. switch expression (Java 14+)

Java 14 introduced **switch expressions** — a modern, concise, and safer form:

```java
// switch statement — verbose, multiple breaks
String result;
switch (day) {
    case 1: result = "Monday";    break;
    case 2: result = "Tuesday";   break;
    default: result = "Other day"; break;
}

// switch expression — concise, no fall-through, returns a value
String result = switch (day) {
    case 1  -> "Monday";
    case 2  -> "Tuesday";
    default -> "Other day";
};
```

Key advantages:

- **No fall-through**: each `case ->` is independent, no `break` needed
- **Returns a value** directly — usable in assignments, returns, arguments
- **Exhaustiveness check**: compiler error if cases are missing (with `enum`)
- **Multi-case labels**: `case 1, 2, 3 ->`

```java
int daysInMonth = switch (month) {
    case 1, 3, 5, 7, 8, 10, 12 -> 31;
    case 4, 6, 9, 11            -> 30;
    case 2                      -> 28;
    default -> throw new IllegalArgumentException("Invalid month: " + month);
};
```

### yield — returning a value from a multi-line block

When a branch needs multiple statements, use a `{}` block with `yield`:

```java
int statusCode = switch (status) {
    case "SUCCESS"   -> 200;
    case "NOT_FOUND" -> 404;
    case "ERROR" -> {
        System.out.println("Logging server error...");
        yield 500; // returns a value from the block
    }
    default -> throw new IllegalArgumentException("Unknown status: " + status);
};
```

!!! note "`yield` vs `return`"
    `return` exits the **method**. `yield` returns a value from the **switch expression** and continues executing the method.

---

## 6. Pattern matching in switch (Java 21+)

Java 21 extends switch to support **type patterns** — type checking and variable binding in one step:

```java
// Before Java 21 — chained if/instanceof
static String describe(Object obj) {
    if (obj instanceof Integer i) return "Integer: " + i;
    if (obj instanceof String s)  return "String of length " + s.length();
    if (obj instanceof Double d)  return "Double: " + d;
    return "Other type";
}

// Java 21 — switch pattern matching
static String describe(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s  -> "String of length " + s.length();
        case Double d  -> "Double: " + d;
        default        -> "Other type";
    };
}
```

### Guarded patterns with `when`

Add extra conditions to individual cases:

```java
static String classify(Object obj) {
    return switch (obj) {
        case Integer i when i < 0  -> "Negative integer";
        case Integer i when i == 0 -> "Zero";
        case Integer i             -> "Positive integer: " + i;
        case String s when s.isEmpty() -> "Empty string";
        case String s              -> "String: " + s;
        default                    -> "Other type";
    };
}
```

### Handling null in switch

Traditional switch throws `NullPointerException` when the variable is `null`. Pattern matching switch handles it safely:

```java
static String handleNull(String s) {
    return switch (s) {
        case null -> "Null value";
        case ""   -> "Empty string";
        default   -> "String: " + s;
    };
}
```

---

## 7. Code example

```java linenums="1"
public class ControlFlowDemo {

    // Early return — grade classification
    static String gradeOf(int score) {
        if (score < 0 || score > 100) return "Invalid";
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    // switch expression — month name
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

    // switch expression with yield
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
```

---

## 8. Common mistakes

### Mistake 1 — Forgetting break in switch statement

```java
int x = 1;
switch (x) {
    case 1:
        System.out.println("one"); // ❌ fall-through — prints "two" as well
    case 2:
        System.out.println("two");
        break;
}
// Output: one
//         two

// ✅ Use switch expression to eliminate fall-through entirely
String result = switch (x) {
    case 1  -> "one";
    case 2  -> "two";
    default -> "other";
};
```

### Mistake 2 — Deeply nested ifs

```java
// ❌ Pyramid of doom
public double calculateDiscount(User user, Order order) {
    if (user != null) {
        if (order != null) {
            if (order.getTotal() > 100) {
                if (user.isPremium()) {
                    return 0.2;
                } else {
                    return 0.1;
                }
            }
        }
    }
    return 0;
}

// ✅ Guard clauses + early return
public double calculateDiscount(User user, Order order) {
    if (user == null || order == null) return 0;
    if (order.getTotal() <= 100) return 0;
    return user.isPremium() ? 0.2 : 0.1;
}
```

### Mistake 3 — Missing default in switch statement

```java
// ❌ If status matches no case, result is never assigned
String result;
switch (status) {
    case "OK":    result = "success"; break;
    case "ERROR": result = "failed";  break;
    // no default
}
System.out.println(result); // ❌ compile error: variable result might not have been initialized

// ✅ Always provide a default
switch (status) {
    case "OK":    result = "success"; break;
    case "ERROR": result = "failed";  break;
    default:      result = "unknown"; break;
}
```

### Mistake 4 — Using switch statement when switch expression fits better

```java
// ❌ Verbose — 3 unnecessary breaks
String label;
switch (code) {
    case 1:  label = "One";   break;
    case 2:  label = "Two";   break;
    default: label = "Other"; break;
}

// ✅ Much cleaner
String label = switch (code) {
    case 1  -> "One";
    case 2  -> "Two";
    default -> "Other";
};
```

### Mistake 5 — Comparing Strings with == in if

```java
String input = new String("admin");

if (input == "admin") { ... }      // ❌ false — compares addresses, not content

if ("admin".equals(input)) { ... } // ✅
```

---

## 9. Interview questions

**Q1: What is fall-through in switch? When is it used intentionally?**

> Fall-through occurs when a `break` is missing — execution continues into the next case. Usually a bug. Used intentionally to group multiple cases with the same behavior: `case 1: case 2: case 3: doSomething(); break;`

**Q2: How does switch expression (Java 14+) differ from switch statement?**

> Three key differences: (1) Arrow syntax `->` has no fall-through — no `break` needed. (2) Can return a value directly — usable in assignments. (3) Compiler enforces exhaustiveness with `enum` — error if cases are missing.

**Q3: When should you use if/else vs switch?**

> Use `switch` when comparing **one variable** against multiple **specific values** (3+ cases) — it reads cleaner and the compiler may optimize it with a jump table. Use `if/else` for complex conditions: value ranges (`score >= 90`), multiple variables, or arbitrary boolean expressions.

**Q4: What is `yield` used for in switch expressions?**

> `yield` returns a value from a `{}` **block** inside a switch expression. It is needed when a branch requires multiple statements before producing a value. `return` cannot be used here — it would exit the entire method, not just the switch expression.

**Q5: What does pattern matching in switch (Java 21) enable?**

> It allows **type patterns** as cases — type checking and variable binding in one step. You can add `when` clauses for additional filtering (guarded patterns). It also supports `case null` for safe null handling instead of throwing `NullPointerException`.

---

## 10. Further reading

| Resource | What to read |
| --- | --- |
| [JLS §14.9 — The if Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.9) | Language specification |
| [JEP 361 — Switch Expressions](https://openjdk.org/jeps/361) | Switch expression (Java 14) |
| [JEP 441 — Pattern Matching for switch](https://openjdk.org/jeps/441) | Pattern matching in switch (Java 21) |
| [Oracle Java Tutorial — Control Flow](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/flow.html) | Official tutorial |
| *Effective Java* — Joshua Bloch | Item 57: Minimize the scope of local variables |
