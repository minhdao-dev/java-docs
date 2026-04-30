# instanceof and Type Casting

## 1. What They Are

**Type casting** is the operation of converting an object from one type to another within the same inheritance hierarchy.

**`instanceof`** is an operator that checks whether an object belongs to a specific type — returning `true` or `false`.

```java
Object obj = "Hello";

// instanceof — runtime type check
System.out.println(obj instanceof String);  // true
System.out.println(obj instanceof Integer); // false

// Type casting — convert the type
String s = (String) obj; // downcasting
System.out.println(s.length()); // 5
```

---

## 2. Upcasting — Implicit, Always Safe

**Upcasting** is casting from a subclass up to a superclass (or interface). Java does this automatically — no explicit cast syntax required.

```java
class Animal {
    void speak() { System.out.println("..."); }
}

class Dog extends Animal {
    @Override void speak() { System.out.println("Woof"); }
    void fetch() { System.out.println("Fetching"); }
}
```

```java
Dog dog = new Dog();

// Upcasting — automatic, no (Animal) needed
Animal animal = dog; // ✅ Dog IS-A Animal

animal.speak(); // "Woof" — still calls Dog's method (runtime polymorphism)
// animal.fetch(); // ❌ compile error — Animal has no fetch() method
```

!!! tip "Upcasting doesn't lose data — it restricts access"
    The object is still a `Dog` at runtime. Upcasting only limits which methods are accessible through the `animal` variable (only `Animal` methods). The actual `Dog` data is not lost.

---

## 3. Downcasting — Explicit, Can Fail

**Downcasting** is casting from a superclass down to a subclass. It must be written explicitly and is only safe when the object actually is that type.

```java
Animal animal = new Dog(); // upcasting (implicit)

// Downcasting — must be explicit
Dog dog = (Dog) animal; // ✅ safe because the object really is a Dog
dog.fetch();            // "Fetching" — Dog-specific method is now accessible
```

```java
Animal animal = new Animal(); // object is actually just an Animal

Dog dog = (Dog) animal; // ❌ ClassCastException at runtime!
                        // Animal is NOT a Dog
```

**`ClassCastException`** — a runtime error thrown when downcasting to an incompatible type. The compiler can't catch it, which is why you must check `instanceof` before downcasting.

---

## 4. instanceof — Check Before Downcasting

### Old way (before Java 16)

```java
void processAnimal(Animal animal) {
    if (animal instanceof Dog) {
        Dog dog = (Dog) animal; // cast after checking
        dog.fetch();
    } else if (animal instanceof Cat) {
        Cat cat = (Cat) animal;
        cat.purr();
    }
}
```

This works but requires two steps — check then cast.

### Pattern matching instanceof (Java 16+) — preferred

```java
void processAnimal(Animal animal) {
    if (animal instanceof Dog dog) { // (1)
        dog.fetch(); // use directly — no extra cast
    } else if (animal instanceof Cat cat) {
        cat.purr();
    }
}
```

1. `instanceof Dog dog` — simultaneously checks the type and declares a pre-cast variable `dog`, scoped to the `if` block. If `animal` is `null`, it returns `false` — never throws NPE.

```java
// Pattern variable can be used in the same condition
void greet(Object obj) {
    if (obj instanceof String s && s.length() > 3) { // (2)
        System.out.println("Long string: " + s.toUpperCase());
    }
}
```

2. The pattern variable `s` is immediately available in the same `&&` condition — concise when combining with additional checks.

!!! tip "instanceof with null always returns false"
    `null instanceof String` → `false`. Pattern matching instanceof handles null safely — no separate null check needed before using instanceof.

---

## 5. instanceof with Interfaces

`instanceof` works with interfaces too — checking whether an object implements a given interface:

```java
interface Flyable { void fly(); }
interface Swimable { void swim(); }

class Duck implements Flyable, Swimable {
    @Override public void fly()  { System.out.println("Duck flies"); }
    @Override public void swim() { System.out.println("Duck swims"); }
}

class Eagle implements Flyable {
    @Override public void fly() { System.out.println("Eagle flies"); }
}
```

```java
Object[] animals = { new Duck(), new Eagle() };

for (Object a : animals) {
    if (a instanceof Flyable f)  f.fly();
    if (a instanceof Swimable s) s.swim();
}
// Duck flies
// Duck swims
// Eagle flies
```

---

## 6. switch Pattern Matching (Java 21)

Java 21 extends pattern matching to `switch` — completely replacing chains of `if-else instanceof`:

```java
sealed interface Shape permits Circle, Rectangle, Triangle { }
record Circle(double radius)           implements Shape { }
record Rectangle(double w, double h)   implements Shape { }
record Triangle(double base, double h) implements Shape { }
```

```java
double area(Shape shape) {
    return switch (shape) {
        case Circle    c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.w() * r.h();
        case Triangle  t -> 0.5 * t.base() * t.h();
    }; // compiler knows all cases are covered — no default needed
}
```

!!! tip "Sealed interface + switch pattern matching = exhaustiveness check"
    A **sealed interface** (Java 17+) restricts which classes may implement it. Combined with `switch` pattern matching, the compiler knows all possible cases at compile time and raises an error if any are missing — no `default` needed and no risk of forgetting a new case.

---

## 7. Primitive Type Casting

In addition to object casting, Java has **primitive casting** — briefly revisited from Variables & Data Types:

```java
// Widening (implicit) — no data loss
int    i = 100;
long   l = i;    // int → long, automatic
double d = i;    // int → double, automatic

// Narrowing (explicit) — data may be lost
double pi    = 3.14159;
int    piInt = (int) pi;   // 3 — decimal part is truncated
System.out.println(piInt); // 3

long big = 1_000_000_000_000L;
int  cut = (int) big;      // overflow — wrong value
System.out.println(cut);   // -727379968
```

!!! warning "Primitive narrowing does not throw an exception"
    Unlike object casting (which throws `ClassCastException`), primitive narrowing silently truncates excess bits. Check the range before casting if you're not sure the value fits.

---

## 8. Full Example

!!! info "Verified"
    Full compilable source: [`CastingDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/casting/CastingDemo.java)

```java linenums="1"
public class CastingDemo {

    abstract static class Shape {
        abstract double area();
        String type() { return getClass().getSimpleName(); }
    }

    static class Circle    extends Shape {
        double radius;
        Circle(double r)         { this.radius = r; }
        @Override double area()  { return Math.PI * radius * radius; }
        void   scale(double f)   { radius *= f; }
    }

    static class Rectangle extends Shape {
        double w, h;
        Rectangle(double w, double h) { this.w = w; this.h = h; }
        @Override double area()       { return w * h; }
        void rotate()                 { double t = w; w = h; h = t; }
    }

    static class Triangle  extends Shape {
        double base, height;
        Triangle(double b, double h)  { this.base = b; this.height = h; }
        @Override double area()       { return 0.5 * base * height; }
    }

    // Upcasting — accepts any Shape
    static void printArea(Shape s) {
        System.out.printf("%s: area = %.2f%n", s.type(), s.area());
    }

    // Downcasting with pattern matching
    static void describe(Shape s) {
        if (s instanceof Circle c) { // (1)
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

    // instanceof with null
    static void safeCheck(Object obj) {
        if (obj instanceof String s && !s.isBlank()) { // (2)
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

        // Upcasting — call methods through Shape reference
        System.out.println("=== Areas ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Describe & Mutate ===");
        for (Shape s : shapes) describe(s);

        // After mutation — areas have changed
        System.out.println("\n=== Areas after mutation ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Null safety ===");
        safeCheck("Hello");
        safeCheck(null);
        safeCheck("   ");
        safeCheck(42);
    }
}
```

1. Pattern matching `instanceof Circle c` — checks the type and creates a pre-cast variable `c` in one step, no `(Circle) s` needed.
2. `obj instanceof String s && !s.isBlank()` — pattern variable `s` is usable immediately in the same `&&` condition. If `obj` is `null`, the first operand returns `false` and short-circuit evaluation prevents NPE.

**Output:**
```
=== Areas ===
Circle: area = 78.54
Rectangle: area = 24.00
Triangle: area = 12.00

=== Describe & Mutate ===
Circle — radius=5.0, scale x2 → radius=10.0
Rectangle — 4.0x6.0 → rotated: 6.0x4.0
Triangle — base=3.0, h=8.0

=== Areas after mutation ===
Circle: area = 314.16
Rectangle: area = 24.00
Triangle: area = 12.00

=== Null safety ===
Valid string: Hello
Not a string or blank: null
Not a string or blank:    
Not a string or blank: 42
```

---

## 9. Common Mistakes

### Mistake 1 — Downcasting without checking instanceof first

```java
Animal animal = new Animal();

// ❌ ClassCastException at runtime — no compile-time warning
Dog dog = (Dog) animal;
dog.fetch();

// ✅ Always check first
if (animal instanceof Dog dog) {
    dog.fetch();
}
```

### Mistake 2 — Confusing compile-time type with runtime type

```java
Animal animal = new Dog(); // compile-time type: Animal; runtime type: Dog

// ❌ Wrong assumption: "animal isn't a Dog because it's declared as Animal"
System.out.println(animal instanceof Dog); // true! Runtime type is Dog

// ❌ Still can't call fetch() directly
// animal.fetch(); // compile error — Animal has no fetch()

// ✅ Downcast to access Dog-specific methods
if (animal instanceof Dog dog) dog.fetch();
```

### Mistake 3 — Using the old pattern instead of pattern matching

```java
// ❌ Old way — redundant, easy to mistype the variable name
if (obj instanceof String) {
    String s = (String) obj; // casting again — unnecessary
    System.out.println(s.length());
}

// ✅ Pattern matching — concise, safe, no redundant cast
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### Mistake 4 — Using the pattern variable outside its scope

```java
if (animal instanceof Dog dog) {
    dog.fetch(); // ✅ inside scope
}
// dog.fetch(); // ❌ compile error — dog only exists inside the if block
```

### Mistake 5 — Silent data loss with primitive narrowing

```java
long revenue = 5_000_000_000L; // 5 billion
int  wrong   = (int) revenue;  // ❌ overflow — compiles fine, produces wrong value

// ✅ Check range first
if (revenue >= Integer.MIN_VALUE && revenue <= Integer.MAX_VALUE) {
    int safe = (int) revenue;
}
// Or just keep the long
```

---

## 10. Interview Questions

**Q1: What exactly does `instanceof` check?**

> `instanceof` checks the **runtime type** of the object — not the compile-time type of the variable. `Animal a = new Dog(); a instanceof Dog` returns `true` because the object is actually a `Dog`. With `null`: `null instanceof AnyType` always returns `false` — no NPE. `instanceof` also returns `true` for all superclasses and interfaces: a `Dog` object is `instanceof Dog`, `instanceof Animal`, and `instanceof Object`.

**Q2: How does pattern matching instanceof (Java 16+) differ from the old approach?**

> The old approach needed two steps: `if (obj instanceof String)` then `String s = (String) obj`. Pattern matching `if (obj instanceof String s)` merges them — checks the type and declares a pre-cast variable in one expression, scoped to the matching branch. Beyond being shorter, it eliminates logical errors from forgetting the cast or mistyping the variable name. The pattern variable is also available within the same `&&` condition.

**Q3: What is the difference between upcasting and downcasting?**

> **Upcasting** (subclass → superclass) is always safe and done implicitly by Java — no cast syntax needed. It restricts accessible methods to those of the superclass, but runtime polymorphism still dispatches to the correct subclass method. **Downcasting** (superclass → subclass) must be written explicitly with `(Type)` and can throw `ClassCastException` if the object is not actually that type. Always check `instanceof` before downcasting.

**Q4: Why should you avoid excessive downcasting in real code?**

> Heavy downcasting often signals a violation of the **Open/Closed Principle** — you have to modify existing code every time a new subclass is added. Better alternatives: **polymorphism** (add a method to the superclass/interface so each subclass handles its own behavior), the **Visitor pattern**, or in Java 21+, switch pattern matching with sealed classes which provides exhaustiveness checking and makes adding new cases explicit.

**Q5: When does `ClassCastException` occur, and how do you prevent it?**

> `ClassCastException` is thrown at **runtime** when downcasting an object to an incompatible type — the compiler can't catch it because the variable's declared type is a valid supertype. Prevention: (1) always check `instanceof` before downcasting; (2) use pattern matching `instanceof` to combine the check and cast; (3) minimize the need for downcasting through better design — prefer polymorphism and generics.

---

## 11. References

| Resource | Content |
|----------|---------|
| [JLS §15.20.2 — instanceof](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.20.2) | Official specification |
| [JEP 394 — Pattern Matching for instanceof](https://openjdk.org/jeps/394) | Java 16 feature |
| [JEP 441 — Pattern Matching for switch](https://openjdk.org/jeps/441) | Java 21 feature |
| [Oracle Tutorial — Casting Objects](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | Official guide |
| [Baeldung — instanceof](https://www.baeldung.com/java-instanceof) | Practical walkthrough |
