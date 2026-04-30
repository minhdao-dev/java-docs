# Interface

## 1. Khái niệm

**Interface** là một "hợp đồng" — nó định nghĩa *những gì* một class phải làm mà không quan tâm *làm thế nào*. Class nào cam kết thực thi interface thì phải cài đặt tất cả các method mà interface yêu cầu.

```java
public interface Drawable {
    void draw(); // abstract method — không có body
}

public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Vẽ hình tròn");
    }
}

public class Square implements Drawable {
    @Override
    public void draw() {
        System.out.println("Vẽ hình vuông");
    }
}
```

```java
Drawable d = new Circle();
d.draw(); // Vẽ hình tròn

d = new Square();
d.draw(); // Vẽ hình vuông
```

Code gọi `d.draw()` không cần biết `d` là `Circle` hay `Square` — chỉ cần biết `d` là `Drawable`. Đây là **loose coupling** — điểm mạnh cốt lõi của interface.

---

## 2. Tại sao quan trọng

Interface là nền tảng của rất nhiều pattern và feature trong Java:

- **Polymorphism** — một biến có thể trỏ đến nhiều loại object khác nhau
- **Loose coupling** — code phụ thuộc vào abstraction, không phụ thuộc vào implementation cụ thể
- **Dependency Injection** — Spring Boot inject implementation dựa trên interface (sẽ học Phase 05)
- **Testing** — dễ mock interface để test độc lập từng component
- **Multiple behavior** — một class có thể implement nhiều interface (Java không cho extends nhiều class)

Hầu hết JDK đều xây dựng trên interface: `List`, `Map`, `Set`, `Comparable`, `Runnable`, `Iterable`...

---

## 3. Khai báo interface

```java
public interface Flyable {
    // Constant — mặc định là public static final
    double MAX_ALTITUDE = 12_000; // tương đương: public static final double MAX_ALTITUDE = 12_000;

    // Abstract method — mặc định là public abstract
    void fly();                   // tương đương: public abstract void fly();
    void land(String location);
}
```

!!! tip "Quy ước đặt tên interface"
    Interface thường đặt tên theo khả năng (ability): `Runnable`, `Comparable`, `Serializable`, `Drawable`, `Flyable`. Đây là cách phân biệt rõ với class (danh từ: `Dog`, `Circle`). Tuy nhiên cũng có interface tên danh từ: `List`, `Map`, `Collection`.

---

## 4. Implements interface

```java
public class Airplane implements Flyable {

    @Override
    public void fly() {
        System.out.println("Máy bay cất cánh");
    }

    @Override
    public void land(String location) {
        System.out.println("Hạ cánh tại " + location);
    }
}
```

Class **phải implement tất cả abstract method** của interface — nếu thiếu, compiler báo lỗi ngay.

---

## 5. Multiple interface — lợi thế lớn nhất

Một class chỉ được `extends` **một** superclass, nhưng có thể `implements` **nhiều** interface:

```java
public interface Swimable {
    void swim();
}

public interface Flyable {
    void fly();
}

public interface Runnable {
    void run();
}

// Duck có thể làm được cả ba
public class Duck implements Flyable, Swimable, Runnable {

    @Override public void fly()  { System.out.println("Vịt bay"); }
    @Override public void swim() { System.out.println("Vịt bơi"); }
    @Override public void run()  { System.out.println("Vịt chạy"); }
}
```

```java
Duck duck = new Duck();

Flyable  f = duck; f.fly();  // Vịt bay
Swimable s = duck; s.swim(); // Vịt bơi
Runnable r = duck; r.run();  // Vịt chạy
```

Điều này cho phép một object được nhìn từ nhiều "góc độ" khác nhau tùy context — không thể làm được với `extends`.

---

## 6. Default method (Java 8+)

Trước Java 8, interface chỉ được có abstract method. Từ Java 8, interface có thể có **default method** — method có sẵn body, class implement có thể dùng ngay hoặc override.

```java
public interface Greeting {
    String getName();

    // Default method — có body, implement sẵn
    default String greet() {
        return "Xin chào, " + getName() + "!";
    }

    default String greetFormal() {
        return "Kính chào " + getName() + ", rất vui được gặp bạn.";
    }
}

public class User implements Greeting {
    private String name;

    User(String name) { this.name = name; }

    @Override
    public String getName() { return name; }

    // greet() và greetFormal() dùng default — không cần override
}

public class VIPUser implements Greeting {
    private String name;

    VIPUser(String name) { this.name = name; }

    @Override
    public String getName() { return name; }

    @Override
    public String greet() { // override default nếu cần hành vi khác
        return "✨ Xin chào đặc biệt, " + getName() + "!";
    }
}
```

```java
User    u = new User("Alice");
VIPUser v = new VIPUser("Bob");

System.out.println(u.greet());        // Xin chào, Alice!
System.out.println(u.greetFormal());  // Kính chào Alice, rất vui được gặp bạn.
System.out.println(v.greet());        // ✨ Xin chào đặc biệt, Bob!
```

!!! tip "Default method dùng để làm gì?"
    Default method giải quyết bài toán **backward compatibility**: khi muốn thêm method vào interface đã có sẵn nhiều class implement, nếu thêm abstract method thì toàn bộ class cũ bị lỗi compile. Default method cho phép thêm method mới mà không phá vỡ code cũ. Ví dụ điển hình: `List.sort()`, `Map.getOrDefault()` được thêm vào Java 8 qua default method.

---

## 7. Static method trong interface (Java 8+)

Interface có thể có static method — utility method liên quan đến interface, không gắn với instance.

```java
public interface Validator<T> {
    boolean validate(T value);

    // Static factory method — tạo Validator từ lambda
    static <T> Validator<T> of(java.util.function.Predicate<T> predicate) {
        return predicate::test;
    }
}

public interface MathUtils {
    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
```

```java
System.out.println(MathUtils.clamp(150, 0, 100)); // 100
System.out.println(MathUtils.clamp(-5,  0, 100)); // 0
```

---

## 8. Private method trong interface (Java 9+)

Interface có thể có private method — dùng để tái sử dụng code giữa các default method, không bị expose ra ngoài.

```java
public interface Logger {

    default void logInfo(String msg) {
        log("INFO", msg);
    }

    default void logError(String msg) {
        log("ERROR", msg);
    }

    // Private — chỉ dùng nội bộ trong interface, không visible với class implement
    private void log(String level, String msg) {
        System.out.printf("[%s] %s%n", level, msg);
    }
}
```

---

## 9. Interface vs Abstract Class

![Interface vs Abstract Class](../../assets/diagrams/interface-vs-abstract.svg)

| | Interface | Abstract Class |
|---|---|---|
| Keyword | `implements` | `extends` |
| Số lượng | Nhiều interface | Chỉ một class |
| Constructor | Không có | Có |
| Field | Chỉ `public static final` | Mọi loại field |
| Method | abstract, default, static, private | Mọi loại method |
| State | Không có instance state | Có instance state |
| Dùng khi | Định nghĩa **khả năng** (can-do) | Chia sẻ **code chung** + **is-a** |

**Quy tắc chọn:**

```
Cần chia sẻ code (field, constructor, concrete method)?
    → Abstract Class

Cần implement nhiều "nguồn" khác nhau? Hoặc đang định nghĩa hành vi, không phải bản chất?
    → Interface

Không chắc?
    → Interface — linh hoạt hơn, ít ràng buộc hơn
```

---

## 10. Các interface quan trọng trong JDK

### Comparable\<T\> — so sánh tự nhiên

```java
public class Student implements Comparable<Student> {
    String name;
    double gpa;

    Student(String name, double gpa) {
        this.name = name;
        this.gpa  = gpa;
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa); // sort giảm dần theo GPA
    }
}
```

```java
List<Student> list = new ArrayList<>();
list.add(new Student("Alice", 8.5));
list.add(new Student("Bob",   9.2));
list.add(new Student("Carol", 7.8));

Collections.sort(list); // dùng compareTo()
list.forEach(s -> System.out.println(s.name + ": " + s.gpa));
// Bob:   9.2
// Alice: 8.5
// Carol: 7.8
```

### Iterable\<T\> — hỗ trợ for-each

```java
public class NumberRange implements Iterable<Integer> {
    private final int start, end;

    NumberRange(int start, int end) {
        this.start = start;
        this.end   = end;
    }

    @Override
    public java.util.Iterator<Integer> iterator() {
        return new java.util.Iterator<>() {
            int current = start;

            @Override public boolean hasNext() { return current <= end; }
            @Override public Integer next()    { return current++; }
        };
    }
}
```

```java
for (int n : new NumberRange(1, 5)) {
    System.out.print(n + " "); // 1 2 3 4 5
}
```

### AutoCloseable — dùng với try-with-resources

```java
public class DatabaseConnection implements AutoCloseable {
    DatabaseConnection() { System.out.println("Mở kết nối DB"); }

    public void query(String sql) { System.out.println("Query: " + sql); }

    @Override
    public void close() { System.out.println("Đóng kết nối DB"); }
}
```

```java
try (DatabaseConnection conn = new DatabaseConnection()) {
    conn.query("SELECT * FROM users");
} // close() tự động được gọi
// Output:
// Mở kết nối DB
// Query: SELECT * FROM users
// Đóng kết nối DB
```

---

## 11. Functional Interface — giới thiệu sơ bộ

**Functional interface** là interface chỉ có **đúng một abstract method**. Nó là nền tảng để dùng Lambda expression (sẽ học chi tiết ở Phase 02).

```java
@FunctionalInterface // annotation tùy chọn, giúp compiler kiểm tra
public interface Calculator {
    int calculate(int a, int b);
}
```

```java
// Cách truyền thống — anonymous class
Calculator add = new Calculator() {
    @Override
    public int calculate(int a, int b) { return a + b; }
};

// Cách modern — Lambda (Phase 02)
Calculator add = (a, b) -> a + b;
Calculator mul = (a, b) -> a * b;

System.out.println(add.calculate(3, 4)); // 7
System.out.println(mul.calculate(3, 4)); // 12
```

Các functional interface có sẵn trong JDK: `Runnable`, `Callable`, `Comparator`, `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, `Supplier<T>` — sẽ được học kỹ ở Phase 02 cùng Stream API.

---

## 12. Code ví dụ đầy đủ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`InterfaceDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/interfaces/InterfaceDemo.java)

```java linenums="1"
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterfaceDemo {

    // Interface — hợp đồng
    interface Shape {
        double area();
        double perimeter();

        default String describe() { // (1)
            return "%s | Diện tích: %.2f | Chu vi: %.2f"
                .formatted(getClass().getSimpleName(), area(), perimeter());
        }
    }

    interface Resizable {
        void resize(double factor);
    }

    // Implements 2 interface
    static class Circle implements Shape, Resizable {
        private double radius;

        Circle(double radius) { this.radius = radius; }

        @Override public double area()      { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
        @Override public void resize(double factor) { radius *= factor; }
    }

    static class Rectangle implements Shape, Comparable<Rectangle> {
        private double w, h;

        Rectangle(double w, double h) { this.w = w; this.h = h; }

        @Override public double area()      { return w * h; }
        @Override public double perimeter() { return 2 * (w + h); }

        @Override
        public int compareTo(Rectangle other) { // (2)
            return Double.compare(this.area(), other.area());
        }
    }

    public static void main(String[] args) {
        // Polymorphism qua interface
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5));
        shapes.add(new Rectangle(4, 6));
        shapes.add(new Circle(3));

        for (Shape s : shapes) {
            System.out.println(s.describe()); // gọi cùng method, behavior khác nhau
        }

        System.out.println();

        // Resize — chỉ Circle implement Resizable
        Circle c = new Circle(5);
        System.out.println("Trước: " + c.describe());
        c.resize(2.0);
        System.out.println("Sau:   " + c.describe());

        System.out.println();

        // Sort Rectangle theo area — dùng Comparable
        List<Rectangle> rects = List.of(
            new Rectangle(3, 4),
            new Rectangle(1, 2),
            new Rectangle(5, 6)
        );
        List<Rectangle> sorted = new ArrayList<>(rects);
        Collections.sort(sorted);
        sorted.forEach(r -> System.out.printf("%.0fx%.0f = %.0f%n", r.w, r.h, r.area()));
    }
}
```

1. Default method `describe()` được định nghĩa một lần trong interface, tất cả class implement (`Circle`, `Rectangle`) đều dùng được ngay mà không cần override — đây là sức mạnh của default method.
2. `Rectangle implements Comparable<Rectangle>` cho phép dùng `Collections.sort()` mà không cần truyền `Comparator` riêng — Java biết cách so sánh hai `Rectangle` thông qua `compareTo()`.

**Output:**
```
Circle | Diện tích: 78.54 | Chu vi: 31.42
Rectangle | Diện tích: 24.00 | Chu vi: 20.00
Circle | Diện tích: 28.27 | Chu vi: 18.85

Trước: Circle | Diện tích: 78.54 | Chu vi: 31.42
Sau:   Circle | Diện tích: 314.16 | Chu vi: 62.83

1x2 = 2
3x4 = 12
5x6 = 30
```

---

## 13. Lỗi thường gặp

### Lỗi 1 — Quên implement method của interface

```java
interface Animal {
    void eat();
    void sleep();
}

// ❌ lỗi compile: Dog chưa implement sleep()
class Dog implements Animal {
    @Override public void eat() { System.out.println("Chó ăn"); }
    // sleep() bị quên — compiler báo lỗi ngay
}

// ✅ Implement đủ tất cả method
class Dog implements Animal {
    @Override public void eat()   { System.out.println("Chó ăn"); }
    @Override public void sleep() { System.out.println("Chó ngủ"); }
}
```

### Lỗi 2 — Conflict khi implement hai interface có default method cùng tên

```java
interface A { default void hello() { System.out.println("A"); } }
interface B { default void hello() { System.out.println("B"); } }

// ❌ lỗi compile: class phải chỉ định dùng default method của interface nào
class C implements A, B { }

// ✅ Override và chỉ định rõ
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // gọi default method của A
    }
}
```

### Lỗi 3 — Thêm state (instance field) vào interface

```java
// ❌ interface không có instance field — đây là compile error
interface Counter {
    int count = 0;      // thực ra là public static final — KHÔNG phải instance field
    void increment();
}

// ✅ State phải ở class implement
class SimpleCounter implements Counter {
    private int count = 0; // instance field ở class

    @Override
    public void increment() { count++; }

    public int getCount() { return count; }
}
```

### Lỗi 4 — Nhầm interface với abstract class khi cần constructor hay shared state

```java
// ❌ Interface không có constructor — không thể khởi tạo shared logic
interface Vehicle {
    // Muốn viết: Vehicle(String brand) { this.brand = brand; } — KHÔNG THỂ
}

// ✅ Dùng abstract class khi cần constructor hoặc shared instance field
abstract class Vehicle {
    protected final String brand;

    Vehicle(String brand) { this.brand = brand; }

    abstract void start();
}
```

---

## 14. Câu hỏi phỏng vấn

**Q1: Interface và Abstract Class khác nhau thế nào? Khi nào chọn cái nào?**

> **Interface** định nghĩa *khả năng* (can-do) — một class có thể implement nhiều interface, không có constructor, không có instance state, field mặc định là `public static final`. **Abstract class** chia sẻ *code và state* giữa các subclass — có constructor, instance field, concrete method, nhưng chỉ được extends một lần. **Chọn interface** khi định nghĩa contract hoặc behavior độc lập, cần multiple implementation. **Chọn abstract class** khi có code chung thực sự cần tái sử dụng và các subclass thể hiện quan hệ is-a chặt chẽ.

**Q2: Default method trong interface giải quyết vấn đề gì?**

> Default method (Java 8+) giải quyết **backward compatibility**: trước Java 8, thêm một abstract method vào interface đã có sẵn sẽ làm hỏng tất cả class đã implement interface đó. Default method cho phép thêm method mới vào interface mà không phá vỡ code cũ — class cũ kế thừa implementation mặc định, class mới có thể override nếu cần. Ví dụ: `List.sort()`, `Map.forEach()` được thêm vào Java 8 theo cách này.

**Q3: Functional interface là gì? Tại sao quan trọng?**

> Functional interface là interface chỉ có **đúng một abstract method** (có thể có nhiều default/static method). Nó là nền tảng để Java hỗ trợ **Lambda expression** — thay vì phải tạo anonymous class, có thể truyền hành vi dưới dạng biểu thức ngắn gọn. Annotation `@FunctionalInterface` là tùy chọn nhưng giúp compiler kiểm tra ràng buộc này. Ví dụ: `Runnable`, `Comparator`, `Predicate`, `Function` đều là functional interface.

**Q4: Nếu hai interface có default method cùng tên, class implement phải làm gì?**

> Class phải **override method đó** và chỉ định rõ muốn dùng default method của interface nào bằng cú pháp `InterfaceName.super.methodName()`. Nếu không override, compiler báo lỗi vì không biết dùng default nào. Đây là quy tắc "explicit wins" — xung đột phải được giải quyết tường minh.

**Q5: Tại sao Spring Boot thường inject interface thay vì concrete class?**

> Đây là nguyên tắc **Dependency Inversion** (D trong SOLID): code nên phụ thuộc vào abstraction, không phụ thuộc vào implementation cụ thể. Khi Spring inject `UserService` (interface) thay vì `UserServiceImpl` (class), code gọi không bị ràng buộc vào một implementation — dễ swap implementation, dễ mock trong test, dễ mở rộng mà không sửa code hiện có. Đây cũng là lý do Spring AOP (transaction, security, caching) hoạt động được — Spring tạo proxy implement cùng interface và "bọc" bên ngoài implementation thực.

---

## 15. Tài liệu tham khảo

| Tài liệu | Nội dung |
|----------|---------|
| [JLS §9 — Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html) | Đặc tả chính thức |
| [Oracle Tutorial — Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html) | Hướng dẫn chính thức |
| [Baeldung — Java Interface](https://www.baeldung.com/java-interfaces) | Bài viết thực hành |
| *Effective Java* — Joshua Bloch | Item 20: Prefer interfaces to abstract classes · Item 21: Design interfaces for posterity |
