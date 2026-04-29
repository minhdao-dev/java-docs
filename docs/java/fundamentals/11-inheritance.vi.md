# Kế thừa — Inheritance

## 1. Khái niệm

**Inheritance** (kế thừa) cho phép một class (**subclass** / lớp con) tự động có tất cả non-private fields và methods của một class khác (**superclass** / lớp cha), đồng thời mở rộng hoặc thay đổi behavior.

Mối quan hệ này gọi là **IS-A**: `Dog` IS-A `Animal`, `Manager` IS-A `Employee`.

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

public class Dog extends Animal {   // Dog kế thừa Animal
    public Dog(String name) {
        super(name);                // gọi constructor của Animal
    }

    public void bark() {
        System.out.println(name + " says: Woof!");
    }
}

Dog d = new Dog("Rex");
d.eat();   // kế thừa từ Animal — Rex is eating
d.bark();  // method riêng của Dog  — Rex says: Woof!
```

Từ khóa:

- `extends` — khai báo kế thừa
- **Superclass / Parent class / Base class** — class được kế thừa (`Animal`)
- **Subclass / Child class / Derived class** — class kế thừa (`Dog`)

!!! note "Java chỉ cho phép single inheritance"
    Một class chỉ có thể `extends` đúng một class. Không có multiple inheritance trong Java. Thay vào đó, dùng `interface` (bài 14) để đạt được hiệu quả tương tự.

---

## 2. Tại sao quan trọng

Không có inheritance, code giống nhau phải viết lại ở nhiều chỗ:

```java
// ❌ không dùng inheritance — lặp code hoàn toàn
public class Cat {
    private String name;
    public Cat(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); } // copy từ Dog
}

public class Dog {
    private String name;
    public Dog(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); } // lặp lại
}

// ✅ với inheritance — code chung đặt một chỗ
public class Animal {
    protected String name;
    public Animal(String name) { this.name = name; }
    public void eat() { System.out.println(name + " is eating"); }
}

public class Cat extends Animal { public Cat(String name) { super(name); } }
public class Dog extends Animal { public Dog(String name) { super(name); } }
```

Ngoài ra:

- **Tập trung behavior chung** — sửa `eat()` ở `Animal` là sửa cho toàn bộ subclass
- **Nền tảng cho Polymorphism** — `Animal a = new Dog("Rex")` (bài 12 sẽ đi sâu)
- **Mô hình hóa thực tế** — phản ánh đúng quan hệ IS-A trong domain

---

## 3. Cú pháp `extends`

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
        super(brand, year); // gọi trước khi truy cập bất kỳ field nào
        this.doors = doors;
    }

    public int getDoors() { return doors; }
}

public class ElectricCar extends Car {
    private int range; // km

    public ElectricCar(String brand, int year, int doors, int range) {
        super(brand, year, doors); // gọi constructor của Car
        this.range = range;
    }

    public int getRange() { return range; }
}

ElectricCar tesla = new ElectricCar("Tesla", 2024, 4, 580);
System.out.println(tesla.getInfo());  // kế thừa từ Vehicle — Tesla (2024)
System.out.println(tesla.getDoors()); // kế thừa từ Car      — 4
System.out.println(tesla.getRange()); // method riêng         — 580
```

Subclass **kế thừa** từ superclass:

- Tất cả `public` và `protected` fields và methods
- `default` (package-private) fields và methods nếu cùng package

Subclass **KHÔNG kế thừa**:

- `private` fields và methods — tồn tại trong object nhưng subclass không truy cập trực tiếp
- Constructor của superclass — phải gọi tường minh qua `super()`

---

## 4. Từ khóa `super`

`super` dùng để truy cập superclass từ bên trong subclass.

### `super()` — gọi constructor của cha

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
        super(color);       // phải là câu lệnh đầu tiên
        this.radius = radius;
    }
}
```

!!! warning "`super()` phải là câu lệnh đầu tiên trong constructor"
    Nếu superclass không có no-arg constructor, subclass **bắt buộc** phải gọi `super(args...)` tường minh. Quên điều này là lỗi compile phổ biến nhất khi mới học inheritance.

### Thứ tự khởi tạo

Khi gọi `new Circle("red", 5.0)`:

1. JVM cấp phát memory cho toàn bộ object (bao gồm cả fields của `Shape`)
2. Constructor `Circle` chạy → gặp `super(color)` → nhảy vào constructor `Shape`
3. Constructor `Shape` hoàn thành → `color` được set
4. Quay lại `Circle` → `this.radius = radius` chạy

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

### `super.method()` — gọi method của cha đã bị override

```java
public class Animal {
    public String describe() {
        return "I am an animal";
    }
}

public class Dog extends Animal {
    @Override
    public String describe() {
        return super.describe() + ", specifically a dog"; // tái sử dụng logic của cha
    }
}

new Dog().describe(); // "I am an animal, specifically a dog"
```

---

## 5. Method Overriding

Override là khi subclass cung cấp implementation **khác** cho một method **đã có** ở superclass.

### Quy tắc override

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    @Override                        // annotation bắt buộc trên thực tế
    public String sound() {          // cùng tên, cùng parameters
        return "Woof";               // implementation mới
    }
}

public class Cat extends Animal {
    @Override
    public String sound() { return "Meow"; }
}
```

| Tiêu chí | Yêu cầu |
| --- | --- |
| Tên method | Phải giống hệt |
| Danh sách tham số | Phải giống hệt |
| Return type | Giống hệt hoặc **covariant** (subtype của return type cha) |
| Access modifier | Không được thu hẹp (`public` → `protected` là lỗi compile) |
| Checked exception | Không được throw rộng hơn superclass |

### `@Override` annotation

```java
public class Animal {
    public void eat() { System.out.println("eating"); }
}

public class Dog extends Animal {
    // Thiếu @Override — lỗi typo tạo ra overload mới, không phải override
    public void eat(String food) { System.out.println("eating " + food); } // overload

    @Override
    public void Eat() { } // ❌ lỗi compile ngay — không có method Eat() ở superclass
}
```

!!! tip "Luôn dùng `@Override`"
    `@Override` không bắt buộc về ngữ pháp nhưng cần thiết trên thực tế. Nó bắt compiler verify rằng bạn đang override một method thực sự tồn tại — phòng tránh lỗi typo thầm lặng.

### Method không thể override

```java
public class Base {
    public final void locked() { }    // final — không override được
    private void hidden() { }         // private — không kế thừa, không override được
    public static void shared() { }   // static — là method hiding, không phải override
}
```

---

## 6. Access control trong inheritance

```java
public class Parent {
    private   int privateField   = 1; // chỉ trong Parent
    int           defaultField   = 2; // package + Child nếu cùng package
    protected int protectedField = 3; // package + mọi Child
    public    int publicField    = 4; // mọi nơi
}

public class Child extends Parent {
    public void show() {
        // System.out.println(privateField); // ❌ lỗi compile
        System.out.println(defaultField);    // ✅ nếu cùng package
        System.out.println(protectedField);  // ✅ luôn được
        System.out.println(publicField);     // ✅ luôn được
    }
}
```

!!! note "Private field tồn tại nhưng không truy cập được trực tiếp"
    Khi tạo `new Child()`, JVM vẫn cấp phát memory cho `privateField` — field đó tồn tại trong object. Subclass chỉ đọc/ghi nó qua getter/setter mà superclass cung cấp, không thể đụng trực tiếp.

### Best practice: fields `private`, dùng `protected` method khi cần

```java
public class Employee {
    private double salary;

    protected double getSalary() { return salary; } // chỉ subclass dùng

    public double calculatePay() { return salary; }
}

public class Manager extends Employee {
    private double bonus;

    @Override
    public double calculatePay() {
        return getSalary() + bonus; // dùng getter, không truy cập field trực tiếp
    }
}
```

---

## 7. `Object` class — gốc của mọi class

Mọi class trong Java đều ngầm extend `Object`. Nếu không viết `extends`, Java tự thêm `extends Object`.

```java
public class Dog { }
// tương đương:
public class Dog extends Object { }
```

Ba method của `Object` mà hầu hết class nên override:

### `toString()`

```java
// Mặc định — in địa chỉ memory, vô nghĩa
Dog d = new Dog("Rex");
System.out.println(d); // Dog@1b6d3586

// Override để có output có ý nghĩa
public class Dog {
    private String name;
    public Dog(String name) { this.name = name; }

    @Override
    public String toString() { return "Dog{name='" + name + "'}"; }
}

System.out.println(new Dog("Rex")); // Dog{name='Rex'}
```

### `equals()` và `hashCode()`

```java
// Mặc định — so sánh reference (địa chỉ memory)
Dog a = new Dog("Rex");
Dog b = new Dog("Rex");
System.out.println(a.equals(b)); // false — mặc định equals() dùng ==

// Override để so sánh theo giá trị
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

System.out.println(a.equals(b)); // true — cùng name
```

!!! warning "Luôn override `hashCode()` khi override `equals()`"
    Hợp đồng Java: nếu `a.equals(b)` là `true` thì `a.hashCode() == b.hashCode()` phải đúng. `HashMap`, `HashSet` dùng `hashCode()` để tìm bucket trước. Vi phạm quy tắc này khiến object biến mất khi tìm kiếm dù đã `put` vào — bug cực kỳ khó tìm.

---

## 8. `final` với inheritance

### `final class` — không thể extend

```java
public final class String  { } // Java chuẩn
public final class Integer { } // Java chuẩn

public class MyString extends String { } // ❌ lỗi compile — cannot inherit from final 'String'
```

Dùng `final class` khi class là immutable và không muốn subclass phá vỡ tính chất đó.

### `final method` — không thể override

```java
public class Template {
    public final void run() {  // subclass không thể override cái này
        setup();
        execute();             // subclass override cái này
        teardown();
    }

    protected void setup()    { }
    protected void execute()  { }
    protected void teardown() { }
}

public class ConcreteTask extends Template {
    @Override
    protected void execute() { System.out.println("Doing work..."); }

    // @Override public void run() { } // ❌ lỗi compile
}
```

!!! note "`final method` vs `final class`"
    `final class` ngăn toàn bộ inheritance. `final method` chỉ khóa một method — subclass vẫn có thể extend class và thêm method mới. Pattern `run()` ở trên là **Template Method Pattern**, một design pattern phổ biến.

---

## 9. Code ví dụ tổng hợp

```java
import java.util.Objects;

public class Employee {

    private final String id;
    private final String name;
    private double baseSalary;

    // validate toàn bộ input — mọi subclass gọi super() đều được validate miễn phí
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

    protected void setBaseSalary(double salary) { // protected: chỉ subclass được điều chỉnh lương
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.baseSalary = salary;
    }

    // điểm mở rộng: mỗi subclass override; toString() gọi cái này qua dynamic dispatch
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
        return id.equals(e.id); // identity theo id, không phải name hay salary
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

public class Manager extends Employee {

    private final String department;
    private double       bonus;

    public Manager(String id, String name, double baseSalary, String department, double bonus) {
        super(id, name, baseSalary); // chạy validation của Employee trước, Manager validate thêm department
        if (department == null || department.isBlank())
            throw new IllegalArgumentException("Department required");
        this.department = department;
        this.bonus      = Math.max(0, bonus);
    }

    public String getDepartment() { return department; }
    public double getBonus()      { return bonus; }

    public void setBonus(double bonus) { this.bonus = Math.max(0, bonus); }

    // baseSalary (qua super) + bonus — không cần truy cập trực tiếp private baseSalary
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
    public double calculatePay() { return super.calculatePay() * 0.8; } // intern nhận 80% lương cơ bản
}

// Demo
Employee alice   = new Manager("E001", "Alice",   8000, "Engineering", 2000);
Employee bob     = new Employee("E002", "Bob",     4000);
Employee charlie = new Intern("E003",  "Charlie",  2000, 6);

System.out.println(alice);   // Manager{id='E001', name='Alice', pay=10000.0}
System.out.println(bob);     // Employee{id='E002', name='Bob', pay=4000.0}
System.out.println(charlie); // Intern{id='E003', name='Charlie', pay=1600.0}

Employee alice2 = new Manager("E001", "Alice", 9000, "Engineering", 3000);
System.out.println(alice.equals(alice2)); // true — cùng id
```

---

## 10. Lỗi thường gặp

### Lỗi 1 — Quên gọi `super()` khi superclass không có no-arg constructor

```java
public class Animal {
    private String name;
    public Animal(String name) { this.name = name; } // không có no-arg constructor
}

public class Dog extends Animal {
    public Dog() {
        // ❌ lỗi compile: There is no default constructor available in 'Animal'
        // Java ngầm gọi super() nhưng Animal không có no-arg constructor
    }

    // ✅
    public Dog(String name) {
        super(name); // gọi tường minh
    }
}
```

### Lỗi 2 — Thiếu `@Override` → overload thay vì override

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    // Lỗi typo (S hoa) — tạo method mới, không override
    public String Sound() { return "Woof"; } // ❌ @Override sẽ bắt lỗi này ngay
}

Animal a = new Dog();
a.sound(); // trả về "..." — Dog.Sound() không bao giờ được gọi qua Animal reference
```

### Lỗi 3 — Thu hẹp access modifier khi override

```java
public class Animal {
    public String sound() { return "..."; }
}

public class Dog extends Animal {
    @Override
    protected String sound() { return "Woof"; } // ❌ lỗi compile — public → protected là thu hẹp
}
```

### Lỗi 4 — Override `equals()` nhưng quên `hashCode()`

```java
public class Point {
    private int x, y;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point p)) return false;
        return x == p.x && y == p.y;
    }
    // ❌ không override hashCode()
}

Set<Point> set = new HashSet<>();
set.add(new Point(1, 2));
set.contains(new Point(1, 2)); // false — cùng giá trị nhưng hashCode khác → tìm sai bucket
```

### Lỗi 5 — Gọi overridable method trong constructor

```java
public class Base {
    public Base() {
        print(); // ❌ nguy hiểm — method này có thể bị override
    }
    public void print() { System.out.println("Base"); }
}

public class Child extends Base {
    private String value = "hello";

    @Override
    public void print() {
        System.out.println(value); // value chưa được khởi tạo lúc này!
    }
}

new Child();
// Output: null — không phải "hello"
// Lý do: super() chạy trước, gọi Child.print(), nhưng Child.value chưa được gán
```

---

## 11. Câu hỏi phỏng vấn

**Q1: Inheritance là gì? IS-A relationship có nghĩa gì?**

> Inheritance là cơ chế cho phép subclass tự động có non-private fields và methods của superclass và có thể mở rộng hoặc override chúng. IS-A là cách kiểm tra inheritance có hợp lý không: "Dog IS-A Animal" đúng → nên dùng inheritance. "Car IS-A Engine" sai → nên dùng composition. Dùng inheritance sai chỗ tạo ra hierarchy cứng nhắc và khó maintain.

**Q2: Overriding khác Overloading như thế nào?**

> Overriding: subclass cung cấp implementation mới cho method đã có ở superclass — cùng tên, cùng parameters, được resolved tại **runtime** (dynamic dispatch). Overloading: cùng tên nhưng khác parameters trong cùng class — được resolved tại **compile time**. `@Override` đánh dấu overriding; không có annotation tương đương cho overloading.

**Q3: Khi nào dùng inheritance, khi nào dùng composition?**

> Inheritance khi quan hệ IS-A rõ ràng và bền vững (`Dog IS-A Animal`). Composition khi quan hệ là HAS-A (`Car HAS-A Engine`) hoặc khi cần linh hoạt thay đổi behavior lúc runtime. Nguyên tắc: ưu tiên composition hơn inheritance — inheritance tạo coupling chặt, thay đổi superclass có thể break toàn bộ hierarchy.

**Q4: Tại sao Java không có multiple inheritance?**

> Vì **Diamond Problem**: nếu `C extends A, B` và cả `A`, `B` đều override cùng method `m()` từ class cha chung, `C` không biết dùng implementation nào. Java giải quyết bằng cách chỉ cho `extends` một class nhưng `implements` nhiều interface — Diamond Problem được xử lý bằng quy tắc tường minh khi có `default` method conflict.

**Q5: `super()` và `this()` khác nhau thế nào?**

> `super()` gọi constructor của superclass — phải là câu lệnh đầu tiên, Java ngầm thêm nếu superclass có no-arg constructor. `this()` gọi constructor khác trong cùng class — cũng phải là câu lệnh đầu tiên. Không thể dùng cả hai trong cùng một constructor vì cả hai đều phải đứng đầu.

**Q6: Tại sao nên luôn override `hashCode()` khi override `equals()`?**

> Java quy định: `a.equals(b) == true` → `a.hashCode() == b.hashCode()` phải đúng. `HashMap` và `HashSet` dùng `hashCode()` để tìm bucket trước, sau đó dùng `equals()` để xác nhận. Nếu hai object equal nhưng hashCode khác, `HashMap.get()` sẽ tìm sai bucket và trả về `null` dù key đã được `put` vào — bug cực kỳ khó tìm.

---

## 12. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [Oracle Tutorial — Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | Inheritance chính thức |
| [Oracle Tutorial — Overriding Methods](https://docs.oracle.com/javase/tutorial/java/IandI/override.html) | Quy tắc override chi tiết |
| *Effective Java* — Joshua Bloch | Item 18: Favor composition over inheritance · Item 19: Design for inheritance or prohibit it |
| *Clean Code* — Robert C. Martin | Chapter 6: Objects and Data Structures |
