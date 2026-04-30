# instanceof và Type Casting

## 1. Khái niệm

**Type casting** là thao tác chuyển đổi một object từ kiểu này sang kiểu khác trong cùng một cây kế thừa.

**`instanceof`** là toán tử kiểm tra xem một object có thuộc một kiểu cụ thể hay không — trả về `true` hoặc `false`.

```java
Object obj = "Hello";

// instanceof — kiểm tra kiểu tại runtime
System.out.println(obj instanceof String);  // true
System.out.println(obj instanceof Integer); // false

// Type casting — chuyển kiểu
String s = (String) obj; // downcasting
System.out.println(s.length()); // 5
```

---

## 2. Upcasting — ngầm định, luôn an toàn

**Upcasting** là cast từ subclass lên superclass (hoặc interface). Java thực hiện tự động — không cần cú pháp cast tường minh.

```java
class Animal {
    void speak() { System.out.println("..."); }
}

class Dog extends Animal {
    @Override void speak() { System.out.println("Gâu gâu"); }
    void fetch() { System.out.println("Lấy đồ vật"); }
}
```

```java
Dog dog = new Dog();

// Upcasting — tự động, không cần (Animal)
Animal animal = dog; // ✅ Dog IS-A Animal

animal.speak(); // "Gâu gâu" — vẫn gọi method của Dog (runtime polymorphism)
// animal.fetch(); // ❌ compile error — Animal không có method fetch()
```

!!! tip "Upcasting không mất data — chỉ mất access"
    Object vẫn là `Dog` ở runtime. Việc upcasting chỉ giới hạn những method có thể gọi qua biến `animal` (chỉ method của `Animal`). Dữ liệu thực của `Dog` không mất đi.

---

## 3. Downcasting — tường minh, có thể lỗi

**Downcasting** là cast từ superclass xuống subclass. Phải viết tường minh và chỉ an toàn khi object thực sự là kiểu đó.

```java
Animal animal = new Dog(); // upcasting (ngầm)

// Downcasting — phải tường minh
Dog dog = (Dog) animal; // ✅ an toàn vì object thực sự là Dog
dog.fetch();            // "Lấy đồ vật" — giờ có thể gọi method của Dog
```

```java
Animal animal = new Animal(); // object thực sự là Animal

Dog dog = (Dog) animal; // ❌ ClassCastException tại runtime!
                        // Animal KHÔNG phải Dog
```

**`ClassCastException`** — lỗi runtime xảy ra khi downcasting sai kiểu. Đây là lý do phải kiểm tra `instanceof` trước khi downcast.

---

## 4. instanceof — kiểm tra trước khi downcast

### Cách cũ (trước Java 16)

```java
void processAnimal(Animal animal) {
    if (animal instanceof Dog) {
        Dog dog = (Dog) animal; // downcast sau khi kiểm tra
        dog.fetch();
    } else if (animal instanceof Cat) {
        Cat cat = (Cat) animal;
        cat.purr();
    }
}
```

Cách này hoạt động nhưng có hai bước thừa — kiểm tra rồi lại cast.

### Pattern matching instanceof (Java 16+) — nên dùng

```java
void processAnimal(Animal animal) {
    if (animal instanceof Dog dog) { // (1)
        dog.fetch(); // dùng ngay, không cần cast thêm
    } else if (animal instanceof Cat cat) {
        cat.purr();
    }
}
```

1. `instanceof Dog dog` — vừa kiểm tra kiểu vừa tạo biến `dog` đã được cast sẵn, chỉ có hiệu lực trong scope `if` đúng. Nếu `animal` là `null` thì trả về `false` — không bao giờ NPE.

```java
// Pattern matching còn dùng được trong điều kiện phức tạp
void greet(Object obj) {
    if (obj instanceof String s && s.length() > 3) { // (2)
        System.out.println("Chuỗi dài: " + s.toUpperCase());
    }
}
```

2. Biến `s` từ pattern matching có thể dùng ngay trong cùng điều kiện `&&` — rất gọn khi kết hợp thêm điều kiện phụ.

!!! tip "instanceof với null luôn trả về false"
    `null instanceof String` → `false`. Pattern matching `instanceof` cũng xử lý null an toàn — không cần kiểm tra null riêng trước khi dùng instanceof.

---

## 5. instanceof với Interface

`instanceof` hoạt động với cả interface — kiểm tra object có implement interface đó không:

```java
interface Flyable { void fly(); }
interface Swimable { void swim(); }

class Duck implements Flyable, Swimable {
    @Override public void fly()  { System.out.println("Vịt bay"); }
    @Override public void swim() { System.out.println("Vịt bơi"); }
}

class Eagle implements Flyable {
    @Override public void fly() { System.out.println("Đại bàng bay"); }
}
```

```java
Object[] animals = { new Duck(), new Eagle() };

for (Object a : animals) {
    if (a instanceof Flyable f)  f.fly();
    if (a instanceof Swimable s) s.swim();
}
// Vịt bay
// Vịt bơi
// Đại bàng bay
```

---

## 6. switch Pattern Matching (Java 21)

Java 21 mở rộng pattern matching vào `switch` — thay thế hoàn toàn chuỗi `if-else instanceof`:

```java
sealed interface Shape permits Circle, Rectangle, Triangle { }
record Circle(double radius)         implements Shape { }
record Rectangle(double w, double h) implements Shape { }
record Triangle(double base, double h) implements Shape { }
```

```java
double area(Shape shape) {
    return switch (shape) {
        case Circle    c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.w() * r.h();
        case Triangle  t -> 0.5 * t.base() * t.h();
    }; // compiler biết đã cover hết — không cần default
}
```

!!! tip "Sealed interface + switch pattern matching = exhaustiveness check"
    **Sealed interface** (Java 17+) giới hạn những class nào được phép implement. Kết hợp với `switch` pattern matching, compiler biết chính xác tất cả các case có thể xảy ra và báo lỗi nếu thiếu — không cần `default` và không sợ bỏ sót case mới.

---

## 7. Type Casting với Primitive

Ngoài object casting, Java còn có **primitive casting** — đã đề cập ở bài Variables & Data Types, nhắc lại ngắn gọn:

```java
// Widening (implicit) — không mất dữ liệu
int    i = 100;
long   l = i;    // int → long, tự động
double d = i;    // int → double, tự động

// Narrowing (explicit) — có thể mất dữ liệu
double pi    = 3.14159;
int    piInt = (int) pi;   // 3 — phần thập phân bị cắt
System.out.println(piInt); // 3

long   big  = 1_000_000_000_000L;
int    cut  = (int) big;   // overflow — giá trị sai
System.out.println(cut);   // -727379968
```

!!! warning "Primitive narrowing không throw exception"
    Khác với object casting (throw `ClassCastException`), primitive narrowing im lặng cắt bỏ bits thừa. Kiểm tra range trước khi cast nếu không chắc giá trị có fit hay không.

---

## 8. Code ví dụ đầy đủ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`CastingDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/casting/CastingDemo.java)

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

    // Upcasting — nhận mọi Shape
    static void printArea(Shape s) {
        System.out.printf("%s: area = %.2f%n", s.type(), s.area());
    }

    // Downcasting với pattern matching
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

    // instanceof với null
    static void safeCheck(Object obj) {
        if (obj instanceof String s && !s.isBlank()) { // (2)
            System.out.println("String hợp lệ: " + s);
        } else {
            System.out.println("Không phải String hoặc trống: " + obj);
        }
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8)
        };

        // Upcasting — gọi method qua kiểu Shape
        System.out.println("=== Areas ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Describe & Mutate ===");
        for (Shape s : shapes) describe(s);

        // Sau khi mutate — area đã thay đổi
        System.out.println("\n=== Areas after mutation ===");
        for (Shape s : shapes) printArea(s);

        System.out.println("\n=== Null safety ===");
        safeCheck("Xin chào");
        safeCheck(null);
        safeCheck("   ");
        safeCheck(42);
    }
}
```

1. Pattern matching `instanceof Circle c` — vừa kiểm tra vừa tạo biến `c` đã downcast, không cần `(Circle) s` thêm.
2. `obj instanceof String s && !s.isBlank()` — biến `s` có thể dùng ngay trong cùng điều kiện `&&`. Nếu `obj` là `null`, vế đầu trả về `false` và `&&` short-circuit — không bao giờ NPE.

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
String hợp lệ: Xin chào
Không phải String hoặc trống: null
Không phải String hoặc trống:    
Không phải String hoặc trống: 42
```

---

## 9. Lỗi thường gặp

### Lỗi 1 — Downcast không kiểm tra instanceof trước

```java
Animal animal = new Animal();

// ❌ ClassCastException tại runtime — không có báo trước lúc compile
Dog dog = (Dog) animal;
dog.fetch();

// ✅ Luôn kiểm tra trước
if (animal instanceof Dog dog) {
    dog.fetch();
}
```

### Lỗi 2 — Nhầm compile-time type với runtime type

```java
Animal animal = new Dog(); // compile-time type: Animal; runtime type: Dog

// ❌ Nhầm nghĩ animal không phải Dog vì biến khai báo là Animal
System.out.println(animal instanceof Dog); // true! Runtime type là Dog

// ❌ Nhầm nghĩ có thể gọi fetch() trực tiếp
// animal.fetch(); // compile error — Animal không có fetch()

// ✅ Phải downcast để gọi method của Dog
if (animal instanceof Dog dog) dog.fetch();
```

### Lỗi 3 — Bỏ qua pattern matching, dùng cách cũ

```java
// ❌ Cách cũ — dư thừa, dễ nhầm
if (obj instanceof String) {
    String s = (String) obj; // cast thêm lần nữa — thừa
    System.out.println(s.length());
}

// ✅ Pattern matching — gọn, an toàn, không cast thừa
if (obj instanceof String s) {
    System.out.println(s.length());
}
```

### Lỗi 4 — Dùng biến pattern matching ngoài scope

```java
if (animal instanceof Dog dog) {
    dog.fetch(); // ✅ trong scope
}
// dog.fetch(); // ❌ compile error — dog chỉ tồn tại trong block if
```

### Lỗi 5 — Primitive narrowing im lặng mất dữ liệu

```java
long revenue = 5_000_000_000L; // 5 tỷ
int  wrong   = (int) revenue;  // ❌ overflow — compile không báo, ra số sai

// ✅ Kiểm tra range trước
if (revenue >= Integer.MIN_VALUE && revenue <= Integer.MAX_VALUE) {
    int safe = (int) revenue;
}
// Hoặc giữ nguyên kiểu long
```

---

## 10. Câu hỏi phỏng vấn

**Q1: `instanceof` kiểm tra điều gì chính xác?**

> `instanceof` kiểm tra **runtime type** của object — không phải compile-time type của biến. `Animal a = new Dog(); a instanceof Dog` trả về `true` vì object thực sự là `Dog`. Với `null`: `null instanceof AnyType` luôn trả về `false` — không throw NPE. `instanceof` cũng trả về `true` cho tất cả superclass và interface mà object implement: một `Dog` object là `instanceof Dog`, `instanceof Animal`, và `instanceof Object`.

**Q2: Pattern matching instanceof (Java 16+) khác cách cũ thế nào?**

> Cách cũ cần hai bước: `if (obj instanceof String)` rồi `String s = (String) obj`. Pattern matching `if (obj instanceof String s)` gộp lại một bước — vừa kiểm tra vừa tạo biến đã cast, chỉ có hiệu lực trong scope đúng. Ngoài gọn hơn, còn tránh lỗi logic khi quên cast hoặc cast sai tên biến. Biến pattern còn dùng được trong cùng điều kiện `&&`.

**Q3: Upcasting và Downcasting khác nhau thế nào?**

> **Upcasting** (subclass → superclass) luôn an toàn, Java thực hiện ngầm định — không cần cú pháp cast. Giới hạn access xuống còn method của superclass, nhưng runtime polymorphism vẫn gọi đúng method của subclass. **Downcasting** (superclass → subclass) phải tường minh bằng `(Type)` và có thể throw `ClassCastException` nếu object thực sự không phải kiểu đó. Luôn kiểm tra `instanceof` trước khi downcast.

**Q4: Tại sao không nên downcast nhiều trong code thực tế?**

> Downcast nhiều thường là dấu hiệu **vi phạm Open/Closed Principle** — phải mở code cũ mỗi khi thêm subclass mới. Giải pháp tốt hơn là **polymorphism**: thêm method vào superclass/interface (hoặc dùng Visitor pattern) để mỗi subclass tự xử lý theo cách riêng mà code gọi không cần biết kiểu cụ thể. Trong Java 21+, switch pattern matching với sealed class giải quyết bài toán này gọn hơn và exhaustive hơn.

**Q5: `ClassCastException` xảy ra lúc nào? Làm sao tránh?**

> `ClassCastException` xảy ra tại **runtime** khi downcast một object sang kiểu không tương thích — compiler không phát hiện được vì biến có kiểu superclass hợp lệ. Tránh bằng cách: (1) luôn kiểm tra `instanceof` trước khi downcast; (2) dùng pattern matching `instanceof` để kết hợp kiểm tra và cast; (3) thiết kế tốt để hạn chế nhu cầu downcast — dùng polymorphism và generics thay thế.

---

## 11. Tài liệu tham khảo

| Tài liệu | Nội dung |
|----------|---------|
| [JLS §15.20.2 — instanceof](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.20.2) | Đặc tả chính thức |
| [JEP 394 — Pattern Matching for instanceof](https://openjdk.org/jeps/394) | Java 16 feature |
| [JEP 441 — Pattern Matching for switch](https://openjdk.org/jeps/441) | Java 21 feature |
| [Oracle Tutorial — Casting Objects](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | Hướng dẫn chính thức |
| [Baeldung — instanceof](https://www.baeldung.com/java-instanceof) | Bài viết thực hành |
