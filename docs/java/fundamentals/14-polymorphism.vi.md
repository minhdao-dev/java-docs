# Đa hình — Polymorphism

## 1. Khái niệm

**Polymorphism** (đa hình) cho phép một biến, tham số, hoặc method hoạt động khác nhau tùy thuộc vào **type thực tế** của object lúc runtime — không phải type được khai báo.

Tên gọi từ tiếng Hy Lạp: *poly* (nhiều) + *morphe* (hình dạng). Một interface, nhiều implementation.

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

Animal a = new Dog(); // type khai báo: Animal — type thực tế: Dog
System.out.println(a.sound()); // "Woof" — JVM gọi Dog.sound(), không phải Animal.sound()

a = new Cat();        // cùng biến, giờ trỏ đến Cat
System.out.println(a.sound()); // "Meow"
```

Java có hai loại polymorphism:

| Loại | Tên khác | Quyết định lúc | Cơ chế |
| --- | --- | --- | --- |
| **Runtime** | Dynamic polymorphism | Runtime | Method overriding + dynamic dispatch |
| **Compile-time** | Static polymorphism | Compile time | Method overloading |

---

## 2. Tại sao quan trọng

Không có polymorphism, code phải kiểm tra type thủ công ở mọi nơi:

```java
// ❌ không có polymorphism — phải sửa code mỗi khi thêm loại mới
void makeSound(Object animal) {
    if (animal instanceof Dog) {
        System.out.println("Woof");
    } else if (animal instanceof Cat) {
        System.out.println("Meow");
    }
    // thêm Bird? phải vào đây sửa tiếp
}

// ✅ với polymorphism — thêm loại mới không cần động vào code này
void makeSound(Animal animal) {
    System.out.println(animal.sound()); // tự gọi đúng implementation
}
```

Đây chính là **Open/Closed Principle**: code *mở* để thêm tính năng mới (thêm subclass), nhưng *đóng* để tránh phải sửa code đang chạy tốt.

Ngoài ra:
- **Giảm coupling** — code chỉ phụ thuộc vào supertype, không quan tâm implementation cụ thể
- **Dễ test** — thay thế implementation thật bằng mock/fake cùng interface
- **Nền tảng cho Design Patterns** — Strategy, Factory, Template Method đều dựa vào polymorphism

---

## 3. Runtime Polymorphism — Dynamic Dispatch

**Dynamic dispatch** là cơ chế JVM dùng để quyết định method nào được gọi lúc runtime, dựa trên type thực tế của object — không phải type của biến.

```java
Animal a;

a = new Dog();
a.sound(); // JVM thấy object thực tế là Dog → gọi Dog.sound() → "Woof"

a = new Cat();
a.sound(); // JVM thấy object thực tế là Cat → gọi Cat.sound() → "Meow"
```

### Upcasting — nền tảng của runtime polymorphism

**Upcasting** là gán object của subclass vào biến của superclass. Tự động, luôn an toàn.

```java
Dog dog = new Dog("Rex");
Animal animal = dog;   // upcasting — ngầm định, không cần ép kiểu

// Qua biến Animal, chỉ thấy được interface của Animal
animal.sound();  // ✅ — method của Animal
animal.bark();   // ❌ lỗi compile — Animal không có bark()
```

!!! note "Upcasting không thay đổi object"
    `animal` và `dog` cùng trỏ đến một object trên Heap. Upcasting chỉ thay đổi góc nhìn của compiler — object thực tế không bị ảnh hưởng. JVM vẫn gọi đúng method của `Dog` khi runtime.

### Dynamic dispatch trong thực tế

```java
Animal[] animals = {
    new Dog(),
    new Cat(),
    new Dog(),
    new Cat()
};

for (Animal a : animals) {
    System.out.println(a.sound()); // mỗi lần gọi khác nhau — JVM tự xử lý
}
// Woof
// Meow
// Woof
// Meow
```

---

## 4. Compile-time Polymorphism — Method Overloading

**Overloading** là nhiều method cùng tên nhưng khác tham số trong cùng một class. Compiler chọn method phù hợp tại thời điểm build — không liên quan đến type runtime.

```java
public class Printer {
    public void print(String text)  { System.out.println("String: " + text); }
    public void print(int number)   { System.out.println("Int: " + number); }
    public void print(double value) { System.out.println("Double: " + value); }
}

Printer p = new Printer();
p.print("hello"); // compiler chọn print(String) tại build time
p.print(42);      // compiler chọn print(int)
p.print(3.14);    // compiler chọn print(double)
```

!!! note "Overloading đã được học ở bài 08"
    Compile-time polymorphism không liên quan đến inheritance. Điểm khác biệt cốt lõi: overloading resolved tại **compile time**, overriding resolved tại **runtime**.

---

## 5. Upcasting và Downcasting

### Upcasting — luôn an toàn

```java
Animal animal = new Dog("Rex"); // upcasting — tự động
```

### Downcasting — cần ép kiểu, có rủi ro

**Downcasting** là ép kiểu từ supertype về subtype để truy cập method đặc trưng của subclass.

```java
Animal animal = new Dog("Rex");

// Downcast để dùng method của Dog
Dog dog = (Dog) animal;  // ép kiểu tường minh
dog.bark();              // ✅ — giờ truy cập được Dog.bark()

// Downcast sai type → ClassCastException lúc runtime
Animal cat = new Cat();
Dog wrongCast = (Dog) cat; // ❌ ClassCastException — Cat không phải Dog
```

!!! warning "Downcast sai type sẽ crash lúc runtime, không phải compile time"
    Compiler không phát hiện được lỗi này vì type chỉ biết lúc runtime. Luôn dùng `instanceof` để kiểm tra trước khi downcast.

---

## 6. `instanceof` để downcast an toàn

### Cách cũ (trước Java 16)

```java
Animal animal = getAnimal(); // không biết type thực tế

if (animal instanceof Dog) {
    Dog dog = (Dog) animal; // check xong mới cast — an toàn
    dog.bark();
} else if (animal instanceof Cat) {
    Cat cat = (Cat) animal;
    cat.purr();
}
```

### Pattern matching (Java 16+) — cách hiện đại

```java
Animal animal = getAnimal();

if (animal instanceof Dog dog) {   // check + cast + khai báo biến trong một dòng
    dog.bark();
} else if (animal instanceof Cat cat) {
    cat.purr();
}
```

!!! tip "Ưu tiên dùng pattern matching `instanceof` từ Java 16+"
    Gọn hơn, không có nguy cơ quên check trước khi cast. Bài 15 sẽ đi sâu hơn vào `instanceof` và type casting.
    (Lịch sử: preview Java 14–15, JEP 305/375; chính thức Java 16, JEP 394.)

---

## 7. Polymorphism với Collections

Sức mạnh thực sự của polymorphism thể hiện rõ nhất khi làm việc với collections chứa nhiều subtype.

```java
List<Animal> animals = new ArrayList<>();
animals.add(new Dog("Rex"));
animals.add(new Cat("Whiskers"));
animals.add(new Dog("Buddy"));

// Một vòng lặp xử lý được mọi loại — không cần if/else
for (Animal a : animals) {
    System.out.println(a.sound());
}
// Woof
// Meow
// Woof
```

### Thêm loại mới không cần sửa code cũ

```java
// Thêm Bird vào hệ thống
public class Bird extends Animal {
    @Override public String sound() { return "Tweet"; }
}

// Code xử lý KHÔNG CẦN thay đổi gì
animals.add(new Bird("Tweety"));

for (Animal a : animals) {
    System.out.println(a.sound()); // tự động gọi Bird.sound() — "Tweet"
}
```

Đây chính là **Open/Closed Principle** trong thực tế: thêm `Bird` mà không động vào vòng lặp.

---

## 8. Code ví dụ tổng hợp

Mở rộng hierarchy từ bài 11 — thêm `Contractor` và `PayrollProcessor` tính lương đa hình.

```java
import java.util.List;
import java.util.ArrayList;

// subclass mới hoàn toàn — không sửa một dòng nào của Employee, Manager, hay Intern
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

    // hourlyRate × hoursWorked — PayrollProcessor không cần biết điều này
    @Override
    public double calculatePay() {
        return hourlyRate * hoursWorked;
    }
}

// PayrollProcessor không biết gì về subclass cụ thể
public class PayrollProcessor {

    // List<Employee> chứa Manager/Intern/Contractor — polymorphism xử lý phần còn lại
    public static double totalPayroll(List<Employee> staff) {
        double total = 0;
        for (Employee e : staff) {
            total += e.calculatePay(); // dynamic dispatch — JVM gọi đúng implementation
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
staff.add(new Employee("E001",    "Alice",   5_000_000));
staff.add(new Manager("E002",     "Bob",     7_000_000, "Engineering", 2_000_000));
staff.add(new Intern("E003",      "Charlie", 3_000_000, 6));
staff.add(new Contractor("C001",  "Diana",   500_000,   20)); // thêm Contractor không cần sửa PayrollProcessor

PayrollProcessor.printPayslips(staff);
System.out.println("Total: " + String.format("%,.0f", PayrollProcessor.totalPayroll(staff)) + " VND");

// Employee     | Alice      | 5,000,000 VND
// Manager      | Bob        | 9,000,000 VND
// Intern       | Charlie    | 2,400,000 VND
// Contractor   | Diana      | 10,000,000 VND
// Total: 26,400,000 VND
```

---

## 9. Lỗi thường gặp

### Lỗi 1 — Downcast không kiểm tra type → ClassCastException

```java
Animal animal = new Cat();
Dog dog = (Dog) animal; // ❌ ClassCastException lúc runtime — không phải compile time

// ✅
if (animal instanceof Dog dog) {
    dog.bark();
}
```

### Lỗi 2 — Static method không bị dynamic dispatch

```java
public class Animal {
    public static String type() { return "Animal"; }
    public String sound()       { return "..."; }
}

public class Dog extends Animal {
    public static String type() { return "Dog"; }   // method hiding, không phải override
    @Override public String sound() { return "Woof"; }
}

Animal a = new Dog();
System.out.println(a.type());  // "Animal" — static method: compile-time binding
System.out.println(a.sound()); // "Woof"   — instance method: runtime binding
```

!!! warning "Static method không bị polymorphism"
    `static` method được resolve tại compile time dựa trên type của biến, không phải type của object. Gọi `a.type()` với `a` kiểu `Animal` luôn cho kết quả `"Animal"` dù object thực tế là `Dog`.

### Lỗi 3 — Field không bị dynamic dispatch

```java
public class Animal {
    public String name = "Animal";
}

public class Dog extends Animal {
    public String name = "Dog"; // field hiding, không phải override
}

Animal a = new Dog();
System.out.println(a.name); // "Animal" — field: compile-time binding theo type biến
```

### Lỗi 4 — Nhầm overloading với overriding

```java
public class Animal {
    public void eat(Food food) { System.out.println("eating"); }
}

public class Dog extends Animal {
    public void eat(DogFood food) { // ❌ overload mới, không phải override
        System.out.println("eating dog food");
    }
    // eat(Food) vẫn được kế thừa từ Animal — không bị thay thế
}

Animal a = new Dog();
a.eat(new DogFood()); // gọi Animal.eat(Food) — không phải Dog.eat(DogFood)
// Lý do: compiler resolve eat(DogFood) theo type Animal — Animal không có method này nên upcast DogFood → Food
```

### Lỗi 5 — Dùng biến supertype rồi bất ngờ khi method đặc thù không có

```java
Animal animal = new Dog("Rex");
animal.bark(); // ❌ lỗi compile — Animal không có bark()

// Muốn gọi bark() phải downcast
if (animal instanceof Dog dog) {
    dog.bark(); // ✅
}
```

---

## 10. Câu hỏi phỏng vấn

**Q1: Polymorphism là gì? Có mấy loại trong Java?**

> Polymorphism là khả năng một method hoạt động khác nhau tùy type thực tế của object. Java có hai loại: (1) **Runtime polymorphism** — method overriding + dynamic dispatch, JVM chọn implementation lúc runtime dựa trên type thực tế. (2) **Compile-time polymorphism** — method overloading, compiler chọn method lúc build dựa trên tham số.

**Q2: Dynamic dispatch hoạt động thế nào?**

> Khi gọi một instance method qua biến supertype, JVM không nhìn vào type của biến — nó nhìn vào type thực tế của object trên Heap và gọi implementation tương ứng. Cơ chế này hoạt động qua **vtable** (virtual method table) — mỗi class có một bảng trỏ đến implementation method của mình. Khi object được tạo, vtable của class thực tế được gắn vào — JVM tra bảng này để gọi đúng method.

**Q3: Tại sao static method không bị polymorphism?**

> Static method thuộc về class, không thuộc về object — không có object nào để JVM tra vtable. Compiler resolve static method call tại build time dựa trên type của biến khai báo, không phải type runtime. Vì vậy `Animal.type()` luôn gọi `Animal.type()` dù biến đang trỏ đến `Dog`. Đây gọi là **method hiding**, không phải overriding.

**Q4: Sự khác biệt giữa upcasting và downcasting?**

> **Upcasting**: gán subtype vào biến supertype — tự động, luôn an toàn, không cần ép kiểu. Mất đi khả năng gọi method đặc thù của subclass. **Downcasting**: ép kiểu từ supertype về subtype để lấy lại method đặc thù — phải tường minh `(Dog) animal`, có thể ném `ClassCastException` nếu type thực tế sai. Luôn dùng `instanceof` để kiểm tra trước khi downcast.

**Q5: Polymorphism liên quan đến Open/Closed Principle thế nào?**

> Open/Closed Principle: module phải *mở* để mở rộng nhưng *đóng* để sửa đổi. Polymorphism thực hiện điều này bằng cách cho phép thêm behavior mới (thêm subclass mới) mà không cần sửa code đang xử lý supertype. Ví dụ: `PayrollProcessor.totalPayroll(List<Employee>)` không cần thay đổi khi thêm `Contractor` — chỉ cần override `calculatePay()` đúng cách.

**Q6: Field có bị dynamic dispatch không? Tại sao?**

> Không. Field access được resolve tại compile time dựa trên type của biến khai báo — không phải type runtime. Chỉ instance method mới có dynamic dispatch. Đây là lý do convention Java khuyến nghị field phải `private` và chỉ expose qua method — method có polymorphism, field thì không.

---

## 11. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [Oracle Tutorial — Polymorphism](https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html) | Polymorphism chính thức |
| [Oracle Tutorial — instanceof](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html) | Toán tử instanceof |
| *Effective Java* — Joshua Bloch | Item 52: Use overloading judiciously · Item 41: Use marker interfaces |
| *Clean Code* — Robert C. Martin | Chapter 6: Objects and Data Structures — polymorphism thay thế switch/if-else |
