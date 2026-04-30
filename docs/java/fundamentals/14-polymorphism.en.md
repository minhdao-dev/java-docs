# Polymorphism

## 1. What is it

**Polymorphism** allows a variable, parameter, or method to behave differently depending on the **actual type** of the object at runtime — not the declared type.

The name comes from Greek: *poly* (many) + *morphe* (form). One interface, many implementations.

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    @Override public String sound() { return "Woof"; }
}

public class Cat extends Animal {
    @Override public String sound() { return "Meow"; }
}

Animal a = new Dog(); // declared type: Animal — actual type: Dog
System.out.println(a.sound()); // "Woof" — JVM calls Dog.sound(), not Animal.sound()

a = new Cat();        // same variable, now points to Cat
System.out.println(a.sound()); // "Meow"
```

Java has two types of polymorphism:

| Type | Also called | Resolved at | Mechanism |
| --- | --- | --- | --- |
| **Runtime** | Dynamic polymorphism | Runtime | Method overriding + dynamic dispatch |
| **Compile-time** | Static polymorphism | Compile time | Method overloading |

---

## 2. Why it matters

Without polymorphism, code must check types manually everywhere:

```java
// ❌ no polymorphism — must modify this every time a new type is added
void makeSound(Object animal) {
    if (animal instanceof Dog) {
        System.out.println("Woof");
    } else if (animal instanceof Cat) {
        System.out.println("Meow");
    }
    // adding Bird? must come back here and edit
}

// ✅ with polymorphism — adding a new type requires no change here
void makeSound(Animal animal) {
    System.out.println(animal.sound()); // calls the right implementation automatically
}
```

This is the **Open/Closed Principle** in action: code is *open* for extension (add new subclasses) but *closed* for modification (don't touch working code).

Additionally:

- **Reduces coupling** — code depends only on the supertype, not on specific implementations
- **Easier to test** — swap real implementations with mocks/fakes sharing the same interface
- **Foundation for Design Patterns** — Strategy, Factory, Template Method all rely on polymorphism

---

## 3. Runtime Polymorphism — Dynamic Dispatch

**Dynamic dispatch** is the mechanism JVM uses to decide which method to call at runtime, based on the actual type of the object — not the type of the variable.

```java
Animal a;

a = new Dog();
a.sound(); // JVM sees actual object is Dog → calls Dog.sound() → "Woof"

a = new Cat();
a.sound(); // JVM sees actual object is Cat → calls Cat.sound() → "Meow"
```

### Upcasting — the foundation of runtime polymorphism

**Upcasting** assigns a subclass object to a superclass variable. Automatic and always safe.

```java
Dog dog = new Dog("Rex");
Animal animal = dog;   // upcasting — implicit, no cast needed

// Through the Animal variable, only Animal's interface is visible
animal.sound();  // ✅ — method defined on Animal
animal.bark();   // ❌ compile error — Animal has no bark()
```

!!! note "Upcasting does not change the object"
    `animal` and `dog` point to the same object on the Heap. Upcasting only changes the compiler's view of the reference — the actual object is untouched. JVM still calls `Dog`'s method at runtime.

### Dynamic dispatch in practice

```java
Animal[] animals = {
    new Dog(),
    new Cat(),
    new Dog(),
    new Cat()
};

for (Animal a : animals) {
    System.out.println(a.sound()); // different method called each time — JVM handles it
}
// Woof
// Meow
// Woof
// Meow
```

---

## 4. Compile-time Polymorphism — Method Overloading

**Overloading** is multiple methods with the same name but different parameters in the same class. The compiler selects the right method at build time — unrelated to runtime types.

```java
public class Printer {
    public void print(String text)  { System.out.println("String: " + text); }
    public void print(int number)   { System.out.println("Int: " + number); }
    public void print(double value) { System.out.println("Double: " + value); }
}

Printer p = new Printer();
p.print("hello"); // compiler selects print(String) at build time
p.print(42);      // compiler selects print(int)
p.print(3.14);    // compiler selects print(double)
```

!!! note "Overloading was covered in lesson 08"
    Compile-time polymorphism is unrelated to inheritance. The core distinction: overloading is resolved at **compile time**, overriding is resolved at **runtime**.

---

## 5. Upcasting and Downcasting

### Upcasting — always safe

```java
Animal animal = new Dog("Rex"); // upcasting — automatic
```

### Downcasting — requires an explicit cast, carries risk

**Downcasting** casts from a supertype back to a subtype to access subclass-specific methods.

```java
Animal animal = new Dog("Rex");

// Downcast to use Dog's method
Dog dog = (Dog) animal;  // explicit cast
dog.bark();              // ✅ — Dog.bark() is now accessible

// Downcast to the wrong type → ClassCastException at runtime
Animal cat = new Cat();
Dog wrongCast = (Dog) cat; // ❌ ClassCastException — Cat is not a Dog
```

!!! warning "A bad downcast crashes at runtime, not compile time"
    The compiler cannot catch this error because the type is only known at runtime. Always use `instanceof` to check before downcasting.

---

## 6. `instanceof` for safe downcasting

### Old style (before Java 16)

```java
Animal animal = getAnimal(); // actual type unknown

if (animal instanceof Dog) {
    Dog dog = (Dog) animal; // check first, then cast — safe
    dog.bark();
} else if (animal instanceof Cat) {
    Cat cat = (Cat) animal;
    cat.purr();
}
```

### Pattern matching (Java 16+) — the modern way

```java
Animal animal = getAnimal();

if (animal instanceof Dog dog) {   // check + cast + variable declaration in one line
    dog.bark();
} else if (animal instanceof Cat cat) {
    cat.purr();
}
```

!!! tip "Prefer pattern matching `instanceof` from Java 16+"
    More concise, and eliminates the risk of forgetting to check before casting. Lesson 18 will cover `instanceof` and type casting in depth.
    (Feature history: preview Java 14–15, JEP 305/375; finalized Java 16, JEP 394.)

---

## 7. Polymorphism with Collections

The real power of polymorphism is most visible when working with collections that hold multiple subtypes.

```java
List<Animal> animals = new ArrayList<>();
animals.add(new Dog("Rex"));
animals.add(new Cat("Whiskers"));
animals.add(new Dog("Buddy"));

// One loop handles every type — no if/else needed
for (Animal a : animals) {
    System.out.println(a.sound());
}
// Woof
// Meow
// Woof
```

### Adding a new type requires no changes to existing code

```java
// Adding Bird to the system
public class Bird extends Animal {
    @Override public String sound() { return "Tweet"; }
}

// The processing code does NOT change
animals.add(new Bird("Tweety"));

for (Animal a : animals) {
    System.out.println(a.sound()); // automatically calls Bird.sound() — "Tweet"
}
```

This is **Open/Closed Principle** in action: adding `Bird` leaves the loop untouched.

---

## 8. Complete example

Extending the hierarchy from lesson 11 — adding `Contractor` and `PayrollProcessor` that calculates payroll polymorphically.

```java
import java.util.List;
import java.util.ArrayList;

// new subclass — not a single line in Employee, Manager, or Intern was changed
public class Contractor extends Employee {

    private double hourlyRate;
    private int    hoursWorked;

    public Contractor(String id, String name, double hourlyRate, int hoursWorked) {
        super(id, name, 0);
        if (hourlyRate <= 0)  throw new IllegalArgumentException("Rate must be positive");
        if (hoursWorked < 0)  throw new IllegalArgumentException("Hours cannot be negative");
        this.hourlyRate  = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    // hourlyRate × hoursWorked — PayrollProcessor knows nothing of this detail
    @Override
    public double calculatePay() {
        return hourlyRate * hoursWorked;
    }
}

// PayrollProcessor knows nothing about specific subclasses
public class PayrollProcessor {

    // List<Employee> holds Manager/Intern/Contractor — polymorphism handles the rest
    public static double totalPayroll(List<Employee> staff) {
        double total = 0;
        for (Employee e : staff) {
            total += e.calculatePay(); // dynamic dispatch — JVM calls the right implementation
        }
        return total;
    }

    public static void printPayslips(List<Employee> staff) {
        for (Employee e : staff) {
            System.out.printf("%-12s | %-10s | %,.0f VND%n",
                e.getClass().getSimpleName(), e.getName(), e.calculatePay());
        }
    }
}

// Demo
List<Employee> staff = new ArrayList<>();
staff.add(new Employee("E001",   "Alice",   5_000_000));
staff.add(new Manager("E002",    "Bob",     7_000_000, "Engineering", 2_000_000));
staff.add(new Intern("E003",     "Charlie", 3_000_000, 6));
staff.add(new Contractor("C001", "Diana",   500_000,   20)); // adding Contractor needs no changes to PayrollProcessor

PayrollProcessor.printPayslips(staff);
System.out.println("Total: " + String.format("%,.0f", PayrollProcessor.totalPayroll(staff)) + " VND");

// Employee     | Alice      | 5,000,000 VND
// Manager      | Bob        | 9,000,000 VND
// Intern       | Charlie    | 2,400,000 VND
// Contractor   | Diana      | 10,000,000 VND
// Total: 26,400,000 VND
```

---

## 9. Common mistakes

### Mistake 1 — Downcast without checking type → ClassCastException

```java
Animal animal = new Cat();
Dog dog = (Dog) animal; // ❌ ClassCastException at runtime — not caught at compile time

// ✅
if (animal instanceof Dog dog) {
    dog.bark();
}
```

### Mistake 2 — Static methods are not dynamically dispatched

```java
public class Animal {
    public static String type() { return "Animal"; }
    public String sound()       { return "..."; }
}

public class Dog extends Animal {
    public static String type() { return "Dog"; }   // method hiding, not overriding
    @Override public String sound() { return "Woof"; }
}

Animal a = new Dog();
System.out.println(a.type());  // "Animal" — static: compile-time binding
System.out.println(a.sound()); // "Woof"   — instance: runtime binding
```

!!! warning "Static methods are not polymorphic"
    `static` methods are resolved at compile time based on the variable's declared type, not the object's runtime type. Calling `a.type()` where `a` is declared as `Animal` always returns `"Animal"` — even if the object is a `Dog`.

### Mistake 3 — Fields are not dynamically dispatched

```java
public class Animal {
    public String name = "Animal";
}

public class Dog extends Animal {
    public String name = "Dog"; // field hiding, not overriding
}

Animal a = new Dog();
System.out.println(a.name); // "Animal" — field: compile-time binding based on declared type
```

### Mistake 4 — Confusing overloading with overriding

```java
public class Animal {
    public void eat(Food food) { System.out.println("eating"); }
}

public class Dog extends Animal {
    public void eat(DogFood food) { // ❌ new overload, not an override
        System.out.println("eating dog food");
    }
    // eat(Food) is still inherited from Animal — it is not replaced
}

Animal a = new Dog();
a.eat(new DogFood()); // calls Animal.eat(Food) — not Dog.eat(DogFood)
// Reason: compiler resolves eat(DogFood) against type Animal — Animal has no such method,
// so DogFood is upcast to Food and Animal.eat(Food) is called
```

### Mistake 5 — Expecting subclass-specific methods through a supertype variable

```java
Animal animal = new Dog("Rex");
animal.bark(); // ❌ compile error — Animal has no bark()

// To call bark(), downcast first
if (animal instanceof Dog dog) {
    dog.bark(); // ✅
}
```

---

## 10. Interview questions

**Q1: What is polymorphism? How many types does Java have?**

> Polymorphism is the ability for a method to behave differently depending on the actual type of the object. Java has two kinds: (1) **Runtime polymorphism** — method overriding plus dynamic dispatch; JVM selects the implementation at runtime based on the object's actual type. (2) **Compile-time polymorphism** — method overloading; the compiler selects the method at build time based on the argument types.

**Q2: How does dynamic dispatch work?**

> When an instance method is called through a supertype variable, JVM ignores the variable's declared type and looks at the actual type of the object on the Heap, then calls that class's implementation. The mechanism is the **vtable** (virtual method table) — each class holds a table of pointers to its method implementations. When an object is created, its class's vtable is attached. JVM looks up this table at every virtual method call.

**Q3: Why are static methods not polymorphic?**

> Static methods belong to the class, not to an object — there is no object for JVM to look up a vtable on. The compiler resolves static method calls at build time based on the declared type of the variable. So `a.type()` where `a` is declared as `Animal` always calls `Animal.type()`, regardless of the runtime type. This is called **method hiding**, not overriding.

**Q4: What is the difference between upcasting and downcasting?**

> **Upcasting**: assigning a subtype object to a supertype variable — implicit, always safe, no cast syntax needed. Loses access to subclass-specific methods. **Downcasting**: explicitly casting from a supertype back to a subtype to regain access to subclass-specific methods — requires `(SubType) ref` syntax, throws `ClassCastException` at runtime if the actual type doesn't match. Always use `instanceof` to verify before downcasting.

**Q5: How does polymorphism relate to the Open/Closed Principle?**

> The Open/Closed Principle states that a module should be *open* for extension but *closed* for modification. Polymorphism enables this by allowing new behavior to be added (new subclasses with overridden methods) without modifying code that processes the supertype. Example: `PayrollProcessor.totalPayroll(List<Employee>)` needs no changes when `Contractor` is added — the new class just overrides `calculatePay()` correctly.

**Q6: Are fields dynamically dispatched? Why not?**

> No. Field access is resolved at compile time based on the declared type of the variable — not the runtime type. Only instance methods have dynamic dispatch. This is precisely why Java convention keeps fields `private` and exposes them only through methods — methods are polymorphic, fields are not.

---

## 11. References

| Resource | What to read |
| --- | --- |
| [Oracle Tutorial — Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html) | Official polymorphism guide |
| [Oracle Tutorial — instanceof](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html) | The instanceof operator |
| *Effective Java* — Joshua Bloch | Item 52: Use overloading judiciously · Item 41: Use marker interfaces |
| *Clean Code* — Robert C. Martin | Chapter 6: Objects and Data Structures — polymorphism over switch/if-else |
