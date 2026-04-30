# Inheritance

## 1. What is it

**Inheritance** lets one class (**subclass** / child class) automatically gain all non-private fields and methods of another class (**superclass** / parent class), while also being able to extend or change its behavior.

This relationship is called **IS-A**: `Dog` IS-A `Animal`, `Manager` IS-A `Employee`.

```java
public class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void eat() {
        System.out.println(name + " is eating");
    }
}

public class Dog extends Animal {   // Dog inherits Animal
    public Dog(String name) {
        super(name);                // calls Animal's constructor
    }

    public void bark() {
        System.out.println(name + " says: Woof!");
    }
}

Dog d = new Dog("Rex");
d.eat();   // inherited from Animal — Rex is eating
d.bark();  // Dog's own method     — Rex says: Woof!
```

Key terms:

- `extends` — declares inheritance
- **Superclass / Parent class / Base class** — the class being inherited from (`Animal`)
- **Subclass / Child class / Derived class** — the class doing the inheriting (`Dog`)

!!! note "Java only allows single inheritance"
    A class can only `extends` one other class. There is no multiple inheritance in Java. Use `interface` (lesson 14) to achieve similar results.

---

## 2. Why it matters

Without inheritance, identical code must be duplicated everywhere:

```java
// ❌ no inheritance — full duplication
public class Cat {
    private String name;
    public Cat(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); } // copy of Dog
}

public class Dog {
    private String name;
    public Dog(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); } // repeated
}

// ✅ with inheritance — shared code lives in one place
public class Animal {
    protected String name;
    public Animal(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); }
}

public class Cat extends Animal { public Cat(String name) { super(name); } }
public class Dog extends Animal { public Dog(String name) { super(name); } }
```

Additionally:

- **Centralizes shared behavior** — fixing `eat()` in `Animal` fixes it for every subclass
- **Foundation for Polymorphism** — `Animal a = new Dog("Rex")` (covered in lesson 12)
- **Models the real world** — reflects IS-A relationships in the domain accurately

---

## 3. The `extends` keyword

```java
public class Vehicle {
    protected String brand;
    protected int    year;

    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year  = year;
    }

    public String getInfo() {
        return brand + " (" + year + ")";
    }
}

public class Car extends Vehicle {
    private int doors;

    public Car(String brand, int year, int doors) {
        super(brand, year); // must come before accessing any field
        this.doors = doors;
    }

    public int getDoors() { return doors; }
}

public class ElectricCar extends Car {
    private int range; // km

    public ElectricCar(String brand, int year, int doors, int range) {
        super(brand, year, doors); // calls Car's constructor
        this.range = range;
    }

    public int getRange() { return range; }
}

ElectricCar tesla = new ElectricCar("Tesla", 2024, 4, 580);
System.out.println(tesla.getInfo());  // inherited from Vehicle — Tesla (2024)
System.out.println(tesla.getDoors()); // inherited from Car      — 4
System.out.println(tesla.getRange()); // ElectricCar's own       — 580
```

What a subclass **does** inherit:

- All `public` and `protected` fields and methods
- `default` (package-private) fields and methods if in the same package

What a subclass **does NOT** inherit:

- `private` fields and methods — they exist in the object but are inaccessible to the subclass directly
- Constructors — must be invoked explicitly via `super()`

---

## 4. The `super` keyword

`super` is used to access the superclass from inside the subclass.

### `super()` — calling the parent's constructor

```java
public class Shape {
    private String color;

    public Shape(String color) {
        this.color = color;
    }

    public String getColor() { return color; }
}

public class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);       // must be the first statement
        this.radius = radius;
    }
}
```

!!! warning "`super()` must be the first statement in the constructor"
    If the superclass has no no-arg constructor, the subclass **must** call `super(args...)` explicitly. Forgetting this is the most common compile error when learning inheritance.

### Initialization order

When `new Circle("red", 5.0)` is called:

1. JVM allocates memory for the entire object (including `Shape`'s fields)
2. `Circle`'s constructor runs → hits `super(color)` → jumps into `Shape`'s constructor
3. `Shape`'s constructor completes → `color` is set
4. Returns to `Circle` → `this.radius = radius` runs

```java
class A {
    A() { System.out.println("A constructor"); }
}
class B extends A {
    B() { System.out.println("B constructor"); }
}
class C extends B {
    C() { System.out.println("C constructor"); }
}

new C();
// A constructor
// B constructor
// C constructor
```

### `super.method()` — calling an overridden parent method

```java
public class Animal {
    public String describe() {
        return "I am an animal";
    }
}

public class Dog extends Animal {
    @Override
    public String describe() {
        return super.describe() + ", specifically a dog"; // reuse parent logic
    }
}

new Dog().describe(); // "I am an animal, specifically a dog"
```

---

## 5. Method Overriding

Overriding is when a subclass provides a **different** implementation for a method that **already exists** in the superclass.

### Override rules

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    @Override                       // annotation required in practice
    public String sound() {         // same name, same parameters
        return "Woof";              // new implementation
    }
}

public class Cat extends Animal {
    @Override
    public String sound() { return "Meow"; }
}
```

| Criterion | Requirement |
| --- | --- |
| Method name | Must be identical |
| Parameter list | Must be identical |
| Return type | Same or **covariant** (a subtype of the parent's return type) |
| Access modifier | Cannot be narrowed (`public` → `protected` is a compile error) |
| Checked exceptions | Cannot be broader than the superclass declares |

### The `@Override` annotation

```java
public class Animal {
    public void eat() { System.out.println("eating"); }
}

public class Dog extends Animal {
    // Missing @Override — typo creates a new overload, not an override
    public void eat(String food) { System.out.println("eating " + food); } // overload

    @Override
    public void Eat() { } // ❌ compile error — no method Eat() in superclass
}
```

!!! tip "Always use `@Override`"
    `@Override` is not syntactically required but is essential in practice. It forces the compiler to verify you are actually overriding a real method — catching silent typo bugs before they reach runtime.

### Methods that cannot be overridden

```java
public class Base {
    public final void locked() { }    // final — cannot be overridden
    private void hidden() { }         // private — not inherited, not overridable
    public static void shared() { }   // static — this is method hiding, not overriding
}
```

---

## 6. Access control in inheritance

```java
public class Parent {
    private   int privateField   = 1; // Parent only
    int           defaultField   = 2; // same package + Child if same package
    protected int protectedField = 3; // same package + any Child
    public    int publicField    = 4; // everywhere
}

public class Child extends Parent {
    public void show() {
        // System.out.println(privateField); // ❌ compile error
        System.out.println(defaultField);    // ✅ if same package
        System.out.println(protectedField);  // ✅ always
        System.out.println(publicField);     // ✅ always
    }
}
```

!!! note "Private fields exist in the object but cannot be accessed directly"
    When `new Child()` is called, JVM still allocates memory for `privateField` — it exists in the object. The subclass can only read or write it through getters and setters the superclass exposes; it cannot touch the field directly.

### Best practice: keep fields `private`, expose via `protected` methods when needed

```java
public class Employee {
    private double salary;

    protected double getSalary() { return salary; } // subclass-only access

    public double calculatePay() { return salary; }
}

public class Manager extends Employee {
    private double bonus;

    @Override
    public double calculatePay() {
        return getSalary() + bonus; // use the getter, not the private field
    }
}
```

---

## 7. The `Object` class — root of everything

Every class in Java implicitly extends `Object`. If you don't write `extends`, Java adds `extends Object` for you.

```java
public class Dog { }
// equivalent to:
public class Dog extends Object { }
```

Three `Object` methods that most classes should override:

### `toString()`

```java
// Default — prints a memory address, which is useless
Dog d = new Dog("Rex");
System.out.println(d); // Dog@1b6d3586

// Override for meaningful output
public class Dog {
    private String name;
    public Dog(String name) { this.name = name; }

    @Override
    public String toString() { return "Dog{name='" + name + "'}"; }
}

System.out.println(new Dog("Rex")); // Dog{name='Rex'}
```

### `equals()` and `hashCode()`

```java
// Default — compares references (memory addresses)
Dog a = new Dog("Rex");
Dog b = new Dog("Rex");
System.out.println(a.equals(b)); // false — default equals() uses ==

// Override to compare by value
public class Dog {
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dog d)) return false;
        return name.equals(d.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }
}

System.out.println(a.equals(b)); // true — same name
```

!!! warning "Always override `hashCode()` when you override `equals()`"
    Java's contract: if `a.equals(b)` is `true` then `a.hashCode() == b.hashCode()` must also be true. `HashMap` and `HashSet` use `hashCode()` to locate the bucket first. Violating this contract makes objects vanish during lookups even after being put in — an extremely hard bug to track down.

---

## 8. `final` and inheritance

### `final class` — cannot be extended

```java
public final class String  { } // Java standard
public final class Integer { } // Java standard

public class MyString extends String { } // ❌ compile error — cannot inherit from final 'String'
```

Use `final class` when the class is immutable and you do not want subclasses breaking that guarantee.

### `final method` — cannot be overridden

```java
public class Template {
    public final void run() {  // subclasses cannot override this
        setup();
        execute();             // subclasses override this instead
        teardown();
    }

    protected void setup()    { }
    protected void execute()  { }
    protected void teardown() { }
}

public class ConcreteTask extends Template {
    @Override
    protected void execute() { System.out.println("Doing work..."); }

    // @Override public void run() { } // ❌ compile error
}
```

!!! note "`final method` vs `final class`"
    `final class` blocks all inheritance. `final method` only locks that one method — subclasses can still extend the class and add new methods. The `run()` pattern above is the **Template Method Pattern**, a widely used design pattern.

---

## 9. Complete example

```java
import java.util.Objects;

public class Employee {

    private final String id;
    private final String name;
    private double baseSalary;

    // validates all input — every subclass calling super() gets this for free
    public Employee(String id, String name, double baseSalary) {
        if (id == null || id.isBlank())     throw new IllegalArgumentException("ID required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (baseSalary < 0)                 throw new IllegalArgumentException("Salary cannot be negative");
        this.id         = id;
        this.name       = name;
        this.baseSalary = baseSalary;
    }

    public String getId()         { return id; }
    public String getName()       { return name; }
    public double getBaseSalary() { return baseSalary; }

    protected void setBaseSalary(double salary) { // protected: only subclasses can adjust salary
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.baseSalary = salary;
    }

    // extension point: each subclass overrides; toString() calls this via dynamic dispatch
    public double calculatePay() { return baseSalary; }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "{id='" + id + "', name='" + name + "', pay=" + calculatePay() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee e)) return false;
        return id.equals(e.id); // identity by id only, not name or salary
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

public class Manager extends Employee {

    private final String department;
    private double       bonus;

    public Manager(String id, String name, double baseSalary, String department, double bonus) {
        super(id, name, baseSalary); // runs Employee's validation first, then Manager adds its own
        if (department == null || department.isBlank())
            throw new IllegalArgumentException("Department required");
        this.department = department;
        this.bonus      = Math.max(0, bonus);
    }

    public String getDepartment() { return department; }
    public double getBonus()      { return bonus; }

    public void setBonus(double bonus) { this.bonus = Math.max(0, bonus); }

    // baseSalary (via super) + bonus — no direct access to private baseSalary needed
    @Override
    public double calculatePay() {
        return super.calculatePay() + bonus;
    }
}

public class Intern extends Employee {

    private final int durationMonths;

    public Intern(String id, String name, double baseSalary, int durationMonths) {
        super(id, name, baseSalary);
        if (durationMonths <= 0) throw new IllegalArgumentException("Duration must be positive");
        this.durationMonths = durationMonths;
    }

    public int getDurationMonths() { return durationMonths; }

    @Override
    public double calculatePay() { return super.calculatePay() * 0.8; } // interns receive 80% of base salary
}

// Demo
Employee alice   = new Manager("E001", "Alice",   8000, "Engineering", 2000);
Employee bob     = new Employee("E002", "Bob",     4000);
Employee charlie = new Intern("E003",  "Charlie",  2000, 6);

System.out.println(alice);   // Manager{id='E001', name='Alice', pay=10000.0}
System.out.println(bob);     // Employee{id='E002', name='Bob', pay=4000.0}
System.out.println(charlie); // Intern{id='E003', name='Charlie', pay=1600.0}

Employee alice2 = new Manager("E001", "Alice", 9000, "Engineering", 3000);
System.out.println(alice.equals(alice2)); // true — same id
```

---

## 10. Common mistakes

### Mistake 1 — Forgetting `super()` when the superclass has no no-arg constructor

```java
public class Animal {
    private String name;
    public Animal(String name) { this.name = name; } // no no-arg constructor
}

public class Dog extends Animal {
    public Dog() {
        // ❌ compile error: There is no default constructor available in 'Animal'
        // Java implicitly calls super() — but Animal has no no-arg constructor
    }

    // ✅
    public Dog(String name) {
        super(name); // explicit call
    }
}
```

### Mistake 2 — Missing `@Override` → overload instead of override

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    // Typo (capital S) — creates a new method, overrides nothing
    public String Sound() { return "Woof"; } // ❌ @Override would catch this immediately
}

Animal a = new Dog();
a.sound(); // returns "..." — Dog.Sound() is never called through an Animal reference
```

### Mistake 3 — Narrowing the access modifier when overriding

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    @Override
    protected String sound() { return "Woof"; } // ❌ compile error — public → protected is narrowing
}
```

### Mistake 4 — Overriding `equals()` but forgetting `hashCode()`

```java
public class Point {
    private int x, y;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point p)) return false;
        return x == p.x && y == p.y;
    }
    // ❌ hashCode() not overridden
}

Set<Point> set = new HashSet<>();
set.add(new Point(1, 2));
set.contains(new Point(1, 2)); // false — same values but different hashCode → wrong bucket
```

### Mistake 5 — Calling an overridable method from a constructor

```java
public class Base {
    public Base() {
        print(); // ❌ dangerous — this method may be overridden
    }
    public void print() { System.out.println("Base"); }
}

public class Child extends Base {
    private String value = "hello";

    @Override
    public void print() {
        System.out.println(value); // value is not initialized yet!
    }
}

new Child();
// Output: null — not "hello"
// Reason: super() runs first, calls Child.print(), but Child.value has not been assigned yet
```

---

## 11. Interview questions

**Q1: What is inheritance? What does IS-A mean?**

> Inheritance is the mechanism by which a subclass automatically gains the non-private fields and methods of its superclass and can extend or override them. IS-A is the test for whether inheritance makes sense: "Dog IS-A Animal" is true → inheritance is appropriate. "Car IS-A Engine" is false → use composition instead. Misusing inheritance where IS-A does not hold creates rigid, hard-to-maintain hierarchies.

**Q2: What is the difference between overriding and overloading?**

> Overriding: a subclass provides a new implementation for a method that already exists in the superclass — same name, same parameters, resolved at **runtime** (dynamic dispatch). Overloading: same method name but different parameters within the same class — resolved at **compile time**. `@Override` marks overriding; there is no equivalent annotation for overloading.

**Q3: When should you use inheritance vs. composition?**

> Use inheritance when the IS-A relationship is clear and stable (`Dog IS-A Animal`). Use composition when the relationship is HAS-A (`Car HAS-A Engine`) or when you need the flexibility to swap behavior at runtime. Prefer composition over inheritance — inheritance creates tight coupling between superclass and subclass, and changes to the superclass can break the entire hierarchy.

**Q4: Why does Java not support multiple inheritance?**

> Because of the **Diamond Problem**: if `C extends A, B` and both `A` and `B` override the same method `m()` from a common ancestor, `C` has no way to know which implementation to use. Java's solution is to allow only one `extends` but multiple `implements`. When `default` method conflicts arise in interfaces, they must be resolved explicitly — a controlled, deterministic rule rather than an ambiguous one.

**Q5: What is the difference between `super()` and `this()` in a constructor?**

> `super()` calls the superclass constructor — must be the first statement, Java adds it implicitly when the superclass has a no-arg constructor. `this()` calls another constructor in the same class — also must be the first statement. Both cannot appear in the same constructor because only one statement can be first.

**Q6: Why must you always override `hashCode()` when you override `equals()`?**

> Java's contract: if `a.equals(b)` is `true` then `a.hashCode() == b.hashCode()` must also hold. `HashMap` and `HashSet` use `hashCode()` to find the right bucket first, then use `equals()` to confirm the match. If two equal objects have different hash codes, `HashMap.get()` looks in the wrong bucket and returns `null` even though the key was put in — an extremely subtle and hard-to-trace bug.

---

## 12. References

| Resource | What to read |
| --- | --- |
| [Oracle Tutorial — Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | Official inheritance guide |
| [Oracle Tutorial — Overriding Methods](https://docs.oracle.com/javase/tutorial/java/IandI/override.html) | Detailed override rules |
| *Effective Java* — Joshua Bloch | Item 18: Favor composition over inheritance · Item 19: Design for inheritance or prohibit it |
| *Clean Code* — Robert C. Martin | Chapter 6: Objects and Data Structures |
