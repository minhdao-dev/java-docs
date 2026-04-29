# Abstract Class

## 1. What is it

An **abstract class** is a class that cannot be instantiated directly — you cannot call `new AbstractClass()`. It is designed to be inherited. It may contain:

- **Abstract methods** — declared but not implemented (no body); subclasses *must* provide the implementation
- **Concrete methods** — fully implemented; subclasses inherit them as-is

```java
public abstract class Shape {
    // abstract method — no body, subclass must implement
    public abstract double area();

    // concrete method — has a body, inherited by all subclasses
    public void describe() {
        System.out.println("I am a " + getClass().getSimpleName()
                           + " with area " + area());
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) { this.radius = radius; }

    @Override
    public double area() { return Math.PI * radius * radius; } // must implement
}

public class Rectangle extends Shape {
    private double w, h;

    public Rectangle(double w, double h) { this.w = w; this.h = h; }

    @Override
    public double area() { return w * h; }
}

// Shape s = new Shape(); // ❌ compile error — cannot instantiate abstract class

Shape c = new Circle(5);
c.describe(); // I am a Circle with area 78.53981633974483

Shape r = new Rectangle(4, 6);
r.describe(); // I am a Rectangle with area 24.0
```

---

## 2. Why it matters

Abstract classes solve the **incomplete parent problem**: the superclass knows the *structure* of a concept but not the *details* of how each subclass implements it.

```java
// ❌ Without abstract — parent has to guess or provide a stub
public class Shape {
    public double area() {
        return 0; // meaningless — every subclass will override this anyway
    }
}

// ✅ With abstract — compiler enforces that every subclass provides a real implementation
public abstract class Shape {
    public abstract double area(); // no guess needed — subclass is responsible
}
```

What abstract classes give you:

| Benefit | Explanation |
| --- | --- |
| **Enforced contract** | Subclasses that forget to implement an abstract method fail at compile time, not at runtime |
| **Shared code** | Concrete methods in the abstract class are written once and inherited by all subclasses |
| **Polymorphism** | `Shape s = new Circle(5)` works — you can use the abstract type as a reference |
| **Partial implementation** | Unlike an interface (lesson 14), an abstract class can carry state (fields) and concrete methods |

---

## 3. Declaring an abstract class

```java
public abstract class Vehicle {
    protected String brand;
    protected int    year;

    // constructor — abstract classes CAN have constructors
    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year  = year;
    }

    // abstract method — every concrete subclass must implement this
    public abstract double fuelCost(int km);

    // concrete method — shared by all subclasses
    public String getInfo() {
        return brand + " (" + year + ")";
    }
}
```

Key rules:

- The `abstract` keyword goes before `class`
- A class with **any** abstract method must be declared `abstract` — the compiler enforces this
- An `abstract` class may have zero abstract methods (unusual, but valid — it just blocks direct instantiation)

---

## 4. Abstract methods

```java
public abstract class Animal {
    protected String name;

    public Animal(String name) { this.name = name; }

    // abstract — subclass is responsible for defining "how this animal moves"
    public abstract void move();

    // abstract — subclass defines "what sound this animal makes"
    public abstract String sound();

    // concrete — the same logic for everyone
    public void introduce() {
        System.out.println("I am " + name + " and I go: " + sound());
    }
}

public class Fish extends Animal {
    public Fish(String name) { super(name); }

    @Override
    public void move()       { System.out.println(name + " swims"); }

    @Override
    public String sound()    { return "...blub"; }
}

public class Bird extends Animal {
    public Bird(String name) { super(name); }

    @Override
    public void move()       { System.out.println(name + " flies"); }

    @Override
    public String sound()    { return "Tweet"; }
}

Animal nemo  = new Fish("Nemo");
Animal tweety = new Bird("Tweety");

nemo.introduce();   // I am Nemo and I go: ...blub
tweety.introduce(); // I am Tweety and I go: Tweet

nemo.move();   // Nemo swims
tweety.move(); // Tweety flies
```

!!! warning "Forgetting to implement an abstract method is a compile error"
    If a subclass does not implement all abstract methods from its parent, the subclass itself must also be declared `abstract` — otherwise the compiler will refuse to compile it.

    ```java
    public class Dog extends Animal {
        public Dog(String name) { super(name); }

        @Override
        public void move() { System.out.println(name + " runs"); }

        // sound() is not implemented!
        // ❌ compile error: Dog is not abstract and does not override abstract method sound()
    }
    ```

---

## 5. Constructors in abstract classes

Abstract classes **can** have constructors. They are called by the subclass via `super()`.

```java
public abstract class BankAccount {
    private final String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number required");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Balance cannot be negative");
        this.accountNumber = accountNumber;
        this.balance       = initialBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance()       { return balance; }

    protected void setBalance(double balance) { this.balance = balance; }

    // Each account type computes interest differently
    public abstract double calculateInterest();

    public void applyInterest() {
        double interest = calculateInterest();
        setBalance(balance + interest);
        System.out.printf("Applied %.2f interest → new balance %.2f%n", interest, balance);
    }
}

public class SavingsAccount extends BankAccount {
    private final double annualRate;

    public SavingsAccount(String number, double balance, double annualRate) {
        super(number, balance); // runs the validation in BankAccount
        this.annualRate = annualRate;
    }

    @Override
    public double calculateInterest() {
        return getBalance() * annualRate / 12; // monthly interest
    }
}

public class CheckingAccount extends BankAccount {
    public CheckingAccount(String number, double balance) {
        super(number, balance);
    }

    @Override
    public double calculateInterest() { return 0; } // no interest on checking
}

BankAccount savings  = new SavingsAccount("SA-001", 10_000, 0.06);
BankAccount checking = new CheckingAccount("CA-001", 5_000);

savings.applyInterest();  // Applied 50.00 interest → new balance 10050.00
checking.applyInterest(); // Applied 0.00 interest → new balance 5000.00
```

!!! note "Abstract class constructors are never called directly"
    `new BankAccount(...)` is illegal. The constructor only runs through `super()` inside a concrete subclass's constructor. It is used for shared initialization and validation — exactly the same as any superclass constructor.

---

## 6. The Template Method Pattern

This is the most important design pattern enabled by abstract classes. The abstract class defines the *algorithm skeleton*, and subclasses fill in specific steps.

```java
public abstract class DataProcessor {

    // template method — final so subclasses cannot change the overall flow
    public final void process() {
        readData();
        processData();
        writeData();
    }

    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
}

public class CsvProcessor extends DataProcessor {
    @Override
    protected void readData()    { System.out.println("Reading CSV file"); }
    @Override
    protected void processData() { System.out.println("Parsing CSV rows"); }
    @Override
    protected void writeData()   { System.out.println("Writing to database"); }
}

public class JsonProcessor extends DataProcessor {
    @Override
    protected void readData()    { System.out.println("Reading JSON file"); }
    @Override
    protected void processData() { System.out.println("Parsing JSON objects"); }
    @Override
    protected void writeData()   { System.out.println("Writing to API"); }
}

DataProcessor csv  = new CsvProcessor();
DataProcessor json = new JsonProcessor();

csv.process();
// Reading CSV file
// Parsing CSV rows
// Writing to database

json.process();
// Reading JSON file
// Parsing JSON objects
// Writing to API
```

The overall sequence `readData → processData → writeData` is *fixed* in the abstract class. Each subclass varies only the specific steps it owns.

---

## 7. Abstract class vs regular class vs interface

|  | Regular class | Abstract class | Interface |
| --- | --- | --- | --- |
| Instantiation | `new MyClass()` ✅ | `new AbstractClass()` ❌ | `new MyInterface()` ❌ |
| Fields | Any type | Any type | `public static final` only |
| Constructors | ✅ | ✅ (called via `super()`) | ❌ |
| Abstract methods | ❌ | ✅ (mix with concrete) | All methods are abstract by default (unless `default`) |
| Concrete methods | ✅ | ✅ | `default` methods only (Java 8+) |
| Multiple inheritance | ❌ (one `extends`) | ❌ (one `extends`) | ✅ (`implements` multiple) |
| Use case | General class | Partial implementation + shared state | Pure contract / capability |

**When to choose abstract class:**

- Subclasses share real state (fields) and concrete helper methods
- You want to enforce a partial implementation pattern (Template Method)
- The inheritance tree represents a clear IS-A family hierarchy

**When to choose interface (lesson 14):**

- You only need to define a contract (what classes *can do*), with no shared state
- You need a class to implement multiple capabilities
- You want maximum flexibility and decoupling

---

## 8. Complete example

```java
public abstract class PaymentProcessor {

    private final String processorName;
    private int successCount = 0;
    private int failCount    = 0;

    public PaymentProcessor(String processorName) {
        this.processorName = processorName;
    }

    // Template method — final: subclasses cannot change the flow
    public final boolean processPayment(double amount) {
        if (amount <= 0) {
            System.out.println("[" + processorName + "] Invalid amount: " + amount);
            return false;
        }

        System.out.println("[" + processorName + "] Processing $" + amount + " via " + getMethodName());

        boolean result = executePayment(amount); // dynamic dispatch calls the right subclass implementation

        if (result) {
            successCount++;
            System.out.println("[" + processorName + "] ✓ Payment successful");
        } else {
            failCount++;
            System.out.println("[" + processorName + "] ✗ Payment failed");
        }

        return result;
    }

    // protected: visible to subclasses, hidden from external callers
    protected abstract boolean executePayment(double amount);
    public    abstract String  getMethodName();

    public String getStats() {
        return processorName + " — success: " + successCount + ", failed: " + failCount;
    }
}

public class CreditCardProcessor extends PaymentProcessor {
    private final String cardNumber;

    public CreditCardProcessor(String cardNumber) {
        super("CreditCard");
        this.cardNumber = cardNumber;
    }

    @Override
    protected boolean executePayment(double amount) {
        // Simulate: amounts divisible by 7 are "declined"
        return amount % 7 != 0;
    }

    @Override
    public String getMethodName() {
        return "Card *" + cardNumber.substring(cardNumber.length() - 4);
    }
}

public class PayPalProcessor extends PaymentProcessor {
    private final String email;

    public PayPalProcessor(String email) {
        super("PayPal");
        this.email = email;
    }

    @Override
    protected boolean executePayment(double amount) {
        return amount < 10_000; // PayPal blocks large amounts
    }

    @Override
    public String getMethodName() { return "PayPal(" + email + ")"; }
}

// Demo
PaymentProcessor card   = new CreditCardProcessor("4111111111111234");
PaymentProcessor paypal = new PayPalProcessor("user@example.com");

card.processPayment(100);
card.processPayment(49);   // 49 % 7 == 0 → declined
card.processPayment(-5);   // invalid amount

paypal.processPayment(500);
paypal.processPayment(15_000); // too large → failed

System.out.println(card.getStats());
System.out.println(paypal.getStats());
```

---

## 9. Common mistakes

### Mistake 1 — Trying to instantiate an abstract class

```java
public abstract class Shape {
    public abstract double area();
}

Shape s = new Shape(); // ❌ compile error — Shape is abstract and cannot be instantiated
Shape s = new Shape() { // ✅ anonymous class — provides the missing implementation inline
    @Override
    public double area() { return 0; }
};
```

### Mistake 2 — Declaring a concrete subclass without implementing all abstract methods

```java
public abstract class Animal {
    public abstract void move();
    public abstract String sound();
}

public class Dog extends Animal {        // ❌ compile error
    @Override
    public void move() { System.out.println("runs"); }
    // sound() is missing → Dog must also be abstract, or implement sound()
}

// Fix 1 — implement all abstract methods
public class Dog extends Animal {
    @Override public void move()       { System.out.println("runs"); }
    @Override public String sound()    { return "Woof"; }
}

// Fix 2 — declare Dog abstract too (defer to Dog's subclasses)
public abstract class Dog extends Animal {
    @Override public void move() { System.out.println("runs"); }
    // sound() is still abstract — Dog's subclasses must implement it
}
```

### Mistake 3 — Calling an overridable method from the abstract class constructor

```java
public abstract class Base {
    public Base() {
        init(); // ❌ dangerous — init() may be overridden by a subclass
    }
    public abstract void init();
}

public class Child extends Base {
    private String value = "hello";

    @Override
    public void init() {
        System.out.println(value); // prints null — value not yet initialized!
    }
}

new Child(); // prints null, not "hello"
```

The fix: do not call overridable methods in constructors. Use a separate `initialize()` method that callers invoke after construction, or use a factory method.

### Mistake 4 — Using abstract class when an interface suffices

```java
// ❌ abstract class — forces a single inheritance chain
public abstract class Serializable {
    public abstract String serialize();
}

// ✅ interface — any class can implement it regardless of its superclass
public interface Serializable {
    String serialize();
}
```

If the abstract class has no state (no fields) and no concrete method logic that genuinely needs to be shared, prefer an interface.

---

## 10. Interview questions

**Q1: What is the difference between an abstract class and a regular class?**

> An abstract class cannot be instantiated directly. It may declare abstract methods (no body) that subclasses must implement. A regular class can be instantiated and cannot contain abstract methods. Use an abstract class when you want to provide a partial implementation and enforce that subclasses fill in the rest.

**Q2: Can an abstract class have a constructor? Why?**

> Yes. The constructor is called via `super()` from the subclass constructor. It is used for shared initialization (setting common fields, validating input) that would otherwise be duplicated in every subclass. You cannot call `new AbstractClass()` directly, but the constructor still runs every time a concrete subclass is instantiated.

**Q3: Can an abstract class have no abstract methods?**

> Yes — a class with `abstract` but no abstract methods simply cannot be instantiated. This is occasionally useful when the class is meant to be a base type and direct instantiation would always be wrong, even though all methods have implementations.

**Q4: What happens if a subclass does not implement all abstract methods?**

> It must also be declared `abstract`. The chain of abstraction continues until a concrete class provides all implementations. The compiler enforces this — there is no way to silently ignore an unimplemented abstract method.

**Q5: What is the Template Method Pattern and how does it use abstract classes?**

> The Template Method Pattern defines an algorithm skeleton in a `final` method of the abstract class. The steps that vary between subclasses are declared `abstract` — each subclass fills them in. The abstract class controls the *sequence*; the subclasses control the *details*. This avoids duplicating the orchestration code while still allowing customization.

**Q6: Abstract class vs interface — when do you choose each?**

> Choose **abstract class** when subclasses share real state (instance fields) or substantial concrete behavior, or when the Template Method Pattern is the right fit. Choose **interface** when you only need a contract (no shared state), when a class needs to fulfill multiple roles, or when you want maximum decoupling. In modern Java, if the choice is ambiguous, lean toward interface — you can always convert later, but removing abstract class hierarchy is harder.

---

## 11. References

| Resource | What to read |
| --- | --- |
| [Oracle Tutorial — Abstract Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) | Official guide with examples |
| *Effective Java* — Joshua Bloch | Item 20: Prefer interfaces to abstract classes |
| *Design Patterns* — Gang of Four | Template Method Pattern (pp. 325–330) |
| *Head First Design Patterns* | Chapter 8: Template Method Pattern |
