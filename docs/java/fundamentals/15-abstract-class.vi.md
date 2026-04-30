# Abstract Class

## 1. Khái niệm

**Abstract class** (lớp trừu tượng) là class không thể khởi tạo trực tiếp — bạn không thể gọi `new AbstractClass()`. Nó được thiết kế để kế thừa. Nó có thể chứa:

- **Abstract method** — khai báo nhưng không có phần thân (no body); subclass *bắt buộc* phải cài đặt
- **Concrete method** — cài đặt đầy đủ; subclass kế thừa và dùng ngay

```java
public abstract class Shape {
    // abstract method — không có body, subclass phải implement
    public abstract double area();

    // concrete method — có body, mọi subclass đều kế thừa
    public void describe() {
        System.out.println("Tôi là " + getClass().getSimpleName()
                           + " với diện tích " + area());
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) { this.radius = radius; }

    @Override
    public double area() { return Math.PI * radius * radius; } // bắt buộc implement
}

public class Rectangle extends Shape {
    private double w, h;

    public Rectangle(double w, double h) { this.w = w; this.h = h; }

    @Override
    public double area() { return w * h; }
}

// Shape s = new Shape(); // ❌ compile error — không thể tạo instance abstract class

Shape c = new Circle(5);
c.describe(); // Tôi là Circle với diện tích 78.53981633974483

Shape r = new Rectangle(4, 6);
r.describe(); // Tôi là Rectangle với diện tích 24.0
```

---

## 2. Tại sao cần abstract class

Abstract class giải quyết bài toán **lớp cha chưa hoàn chỉnh**: superclass biết *cấu trúc* của một khái niệm nhưng không biết *chi tiết* triển khai của từng subclass.

```java
// ❌ Không dùng abstract — lớp cha phải "đoán" hoặc trả về giá trị vô nghĩa
public class Shape {
    public double area() {
        return 0; // vô nghĩa — mọi subclass đều sẽ override cái này
    }
}

// ✅ Dùng abstract — compiler bắt buộc mỗi subclass phải có implementation thật sự
public abstract class Shape {
    public abstract double area(); // không cần đoán — subclass chịu trách nhiệm
}
```

Lợi ích của abstract class:

| Lợi ích | Giải thích |
| --- | --- |
| **Hợp đồng bắt buộc** | Subclass quên implement abstract method → compile error ngay, không phải runtime error |
| **Code dùng chung** | Concrete method viết một lần trong abstract class, mọi subclass đều kế thừa |
| **Đa hình** | `Shape s = new Circle(5)` hoạt động — dùng abstract type làm reference |
| **Cài đặt một phần** | Khác interface (bài 14), abstract class có thể chứa state (fields) và concrete methods |

---

## 3. Khai báo abstract class

```java
public abstract class Vehicle {
    protected String brand;
    protected int    year;

    // constructor — abstract class CÓ THỂ có constructor
    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year  = year;
    }

    // abstract method — mọi concrete subclass phải implement
    public abstract double fuelCost(int km);

    // concrete method — dùng chung cho mọi subclass
    public String getInfo() {
        return brand + " (" + year + ")";
    }
}
```

Các quy tắc:

- Từ khóa `abstract` đứng trước `class`
- Class có **bất kỳ** abstract method nào phải được khai báo `abstract` — compiler bắt buộc điều này
- Abstract class có thể có không có abstract method nào (hiếm gặp nhưng hợp lệ — chỉ block việc tạo instance trực tiếp)

---

## 4. Abstract method

```java
public abstract class Animal {
    protected String name;

    public Animal(String name) { this.name = name; }

    // abstract — subclass định nghĩa "con vật này di chuyển thế nào"
    public abstract void move();

    // abstract — subclass định nghĩa "con vật này kêu gì"
    public abstract String sound();

    // concrete — logic giống nhau cho tất cả
    public void introduce() {
        System.out.println("Tôi là " + name + " và tôi kêu: " + sound());
    }
}

public class Fish extends Animal {
    public Fish(String name) { super(name); }

    @Override
    public void move()    { System.out.println(name + " bơi"); }

    @Override
    public String sound() { return "...blup"; }
}

public class Bird extends Animal {
    public Bird(String name) { super(name); }

    @Override
    public void move()    { System.out.println(name + " bay"); }

    @Override
    public String sound() { return "Chíp chíp"; }
}

Animal nemo   = new Fish("Nemo");
Animal tweety = new Bird("Tweety");

nemo.introduce();   // Tôi là Nemo và tôi kêu: ...blup
tweety.introduce(); // Tôi là Tweety và tôi kêu: Chíp chíp

nemo.move();   // Nemo bơi
tweety.move(); // Tweety bay
```

!!! warning "Quên implement abstract method là compile error"
    Nếu subclass không implement tất cả abstract methods từ lớp cha, subclass đó bắt buộc phải được khai báo `abstract` — nếu không compiler sẽ từ chối compile.

    ```java
    public class Dog extends Animal {
        public Dog(String name) { super(name); }

        @Override
        public void move() { System.out.println(name + " chạy"); }

        // sound() không được implement!
        // ❌ compile error: Dog is not abstract and does not override abstract method sound()
    }
    ```

---

## 5. Constructor trong abstract class

Abstract class **có thể** có constructor. Constructor được gọi bởi subclass thông qua `super()`.

```java
public abstract class BankAccount {
    private final String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double initialBalance) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Số tài khoản không được trống");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Số dư không được âm");
        this.accountNumber = accountNumber;
        this.balance       = initialBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance()       { return balance; }

    protected void setBalance(double balance) { this.balance = balance; }

    // Mỗi loại tài khoản tính lãi khác nhau
    public abstract double calculateInterest();

    public void applyInterest() {
        double interest = calculateInterest();
        setBalance(balance + interest);
        System.out.printf("Áp dụng lãi %.2f → số dư mới %.2f%n", interest, balance);
    }
}

public class SavingsAccount extends BankAccount {
    private final double annualRate;

    public SavingsAccount(String number, double balance, double annualRate) {
        super(number, balance); // chạy validation trong BankAccount
        this.annualRate = annualRate;
    }

    @Override
    public double calculateInterest() {
        return getBalance() * annualRate / 12; // lãi hàng tháng
    }
}

public class CheckingAccount extends BankAccount {
    public CheckingAccount(String number, double balance) {
        super(number, balance);
    }

    @Override
    public double calculateInterest() { return 0; } // tài khoản thanh toán không có lãi
}

BankAccount savings  = new SavingsAccount("SA-001", 10_000, 0.06);
BankAccount checking = new CheckingAccount("CA-001", 5_000);

savings.applyInterest();  // Áp dụng lãi 50.00 → số dư mới 10050.00
checking.applyInterest(); // Áp dụng lãi 0.00 → số dư mới 5000.00
```

!!! note "Constructor của abstract class không bao giờ được gọi trực tiếp"
    `new BankAccount(...)` là bất hợp lệ. Constructor chỉ chạy thông qua `super()` bên trong constructor của concrete subclass. Mục đích là khởi tạo chung và validate — giống hệt bất kỳ superclass constructor nào.

---

## 6. Template Method Pattern

Đây là design pattern quan trọng nhất sử dụng abstract class. Abstract class định nghĩa *bộ khung thuật toán*, subclass điền vào các bước cụ thể.

```java
public abstract class DataProcessor {

    // template method — final để subclass không thể thay đổi luồng tổng thể
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
    protected void readData()    { System.out.println("Đọc file CSV"); }
    @Override
    protected void processData() { System.out.println("Parse các dòng CSV"); }
    @Override
    protected void writeData()   { System.out.println("Ghi vào database"); }
}

public class JsonProcessor extends DataProcessor {
    @Override
    protected void readData()    { System.out.println("Đọc file JSON"); }
    @Override
    protected void processData() { System.out.println("Parse JSON objects"); }
    @Override
    protected void writeData()   { System.out.println("Ghi lên API"); }
}

DataProcessor csv  = new CsvProcessor();
DataProcessor json = new JsonProcessor();

csv.process();
// Đọc file CSV
// Parse các dòng CSV
// Ghi vào database

json.process();
// Đọc file JSON
// Parse JSON objects
// Ghi lên API
```

Trình tự tổng thể `readData → processData → writeData` được *cố định* trong abstract class. Mỗi subclass chỉ thay đổi các bước nó sở hữu.

---

## 7. Abstract class vs Regular class vs Interface

|  | Regular class | Abstract class | Interface |
| --- | --- | --- | --- |
| Khởi tạo | `new MyClass()` ✅ | `new AbstractClass()` ❌ | `new MyInterface()` ❌ |
| Fields | Bất kỳ | Bất kỳ | Chỉ `public static final` |
| Constructor | ✅ | ✅ (gọi qua `super()`) | ❌ |
| Abstract method | ❌ | ✅ (kết hợp với concrete) | Mặc định đều abstract (trừ `default`) |
| Concrete method | ✅ | ✅ | Chỉ `default` method (Java 8+) |
| Đa kế thừa | ❌ (một `extends`) | ❌ (một `extends`) | ✅ (`implements` nhiều) |
| Trường hợp dùng | Class thông thường | Cài đặt một phần + state chung | Hợp đồng thuần túy / khả năng |

**Khi nào dùng abstract class:**

- Các subclass chia sẻ state thật sự (instance fields) và concrete helper methods
- Muốn áp dụng Template Method Pattern
- Cây kế thừa thể hiện quan hệ IS-A rõ ràng trong cùng một họ

**Khi nào dùng interface (bài 14):**

- Chỉ cần định nghĩa hợp đồng (class *có thể làm gì*), không cần state chung
- Class cần thực hiện nhiều vai trò khác nhau
- Muốn tối đa hóa tính linh hoạt và decoupling

---

## 8. Ví dụ hoàn chỉnh

```java
public abstract class PaymentProcessor {

    private final String processorName;
    private int successCount = 0;
    private int failCount    = 0;

    public PaymentProcessor(String processorName) {
        this.processorName = processorName;
    }

    // Template method — final: subclass không thể thay đổi luồng
    public final boolean processPayment(double amount) {
        if (amount <= 0) {
            System.out.println("[" + processorName + "] Số tiền không hợp lệ: " + amount);
            return false;
        }

        System.out.println("[" + processorName + "] Xử lý " + amount
                           + " VND qua " + getMethodName());

        boolean result = executePayment(amount); // dynamic dispatch gọi đúng implementation của subclass

        if (result) {
            successCount++;
            System.out.println("[" + processorName + "] ✓ Thanh toán thành công");
        } else {
            failCount++;
            System.out.println("[" + processorName + "] ✗ Thanh toán thất bại");
        }

        return result;
    }

    // protected: visible với subclass, ẩn với caller bên ngoài
    protected abstract boolean executePayment(double amount);
    public    abstract String  getMethodName();

    public String getStats() {
        return processorName + " — thành công: " + successCount + ", thất bại: " + failCount;
    }
}

public class CreditCardProcessor extends PaymentProcessor {
    private final String cardNumber;

    public CreditCardProcessor(String cardNumber) {
        super("ThẻTín Dụng");
        this.cardNumber = cardNumber;
    }

    @Override
    protected boolean executePayment(double amount) {
        // Giả lập: số tiền chia hết cho 7 bị từ chối
        return amount % 7 != 0;
    }

    @Override
    public String getMethodName() {
        return "Thẻ *" + cardNumber.substring(cardNumber.length() - 4);
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
        return amount < 50_000_000; // PayPal chặn số tiền quá lớn
    }

    @Override
    public String getMethodName() { return "PayPal(" + email + ")"; }
}

// Demo
PaymentProcessor card   = new CreditCardProcessor("4111111111111234");
PaymentProcessor paypal = new PayPalProcessor("user@example.com");

card.processPayment(100_000);
card.processPayment(49_000);       // 49000 % 7 == 0 → bị từ chối
card.processPayment(-5_000);       // số tiền không hợp lệ

paypal.processPayment(500_000);
paypal.processPayment(80_000_000); // quá lớn → thất bại

System.out.println(card.getStats());
System.out.println(paypal.getStats());
```

---

## 9. Các lỗi thường gặp

### Lỗi 1 — Cố gắng tạo instance của abstract class

```java
public abstract class Shape {
    public abstract double area();
}

Shape s = new Shape(); // ❌ compile error — Shape là abstract, không thể tạo instance
Shape s = new Shape() { // ✅ anonymous class — cung cấp implementation thiếu ngay chỗ này
    @Override
    public double area() { return 0; }
};
```

### Lỗi 2 — Khai báo concrete subclass mà không implement đủ abstract methods

```java
public abstract class Animal {
    public abstract void move();
    public abstract String sound();
}

public class Dog extends Animal {        // ❌ compile error
    @Override
    public void move() { System.out.println("chạy"); }
    // sound() bị thiếu → Dog phải là abstract, hoặc implement sound()
}

// Fix 1 — implement đủ tất cả abstract methods
public class Dog extends Animal {
    @Override public void move()    { System.out.println("chạy"); }
    @Override public String sound() { return "Woof"; }
}

// Fix 2 — khai báo Dog là abstract (để các subclass của Dog implement)
public abstract class Dog extends Animal {
    @Override public void move() { System.out.println("chạy"); }
    // sound() vẫn abstract — subclass của Dog phải implement
}
```

### Lỗi 3 — Gọi overridable method từ constructor của abstract class

```java
public abstract class Base {
    public Base() {
        init(); // ❌ nguy hiểm — init() có thể bị override bởi subclass
    }
    public abstract void init();
}

public class Child extends Base {
    private String value = "hello";

    @Override
    public void init() {
        System.out.println(value); // in ra null — value chưa được khởi tạo!
    }
}

new Child(); // in ra null, không phải "hello"
```

Cách fix: không gọi overridable methods trong constructor. Dùng factory method hoặc method `initialize()` riêng để người dùng gọi sau khi tạo object.

### Lỗi 4 — Dùng abstract class khi interface là đủ

```java
// ❌ abstract class — ép buộc vào một chuỗi kế thừa duy nhất
public abstract class Serializable {
    public abstract String serialize();
}

// ✅ interface — bất kỳ class nào cũng có thể implement dù superclass của nó là gì
public interface Serializable {
    String serialize();
}
```

Nếu abstract class không có state (không có instance field) và không có concrete method logic thật sự cần chia sẻ, hãy dùng interface.

---

## 10. Câu hỏi phỏng vấn

**Q1: Sự khác nhau giữa abstract class và regular class là gì?**

> Abstract class không thể khởi tạo trực tiếp. Nó có thể khai báo abstract methods (không có body) mà subclass bắt buộc phải implement. Regular class có thể khởi tạo và không chứa abstract methods. Dùng abstract class khi muốn cung cấp partial implementation và bắt buộc subclass hoàn thiện phần còn lại.

**Q2: Abstract class có thể có constructor không? Tại sao?**

> Có. Constructor được gọi qua `super()` từ constructor của subclass. Nó dùng để khởi tạo chung (set fields chung, validate input) mà nếu không sẽ phải duplicate trong mỗi subclass. Không thể gọi `new AbstractClass()` trực tiếp, nhưng constructor vẫn chạy mỗi lần một concrete subclass được khởi tạo.

**Q3: Abstract class có thể không có abstract method nào không?**

> Có — class có `abstract` nhưng không có abstract method chỉ đơn giản là không thể khởi tạo. Đôi khi hữu ích khi class được thiết kế làm base type và việc tạo instance trực tiếp luôn sai về mặt logic, dù tất cả methods đã có implementation.

**Q4: Điều gì xảy ra nếu subclass không implement đủ abstract methods?**

> Subclass đó cũng phải được khai báo `abstract`. Chuỗi abstract tiếp tục cho đến khi một concrete class cung cấp đủ tất cả implementations. Compiler bắt buộc điều này — không có cách nào để im lặng bỏ qua một abstract method chưa implement.

**Q5: Template Method Pattern là gì và dùng abstract class như thế nào?**

> Template Method Pattern định nghĩa bộ khung thuật toán trong một method `final` của abstract class. Các bước thay đổi giữa các subclass được khai báo `abstract` — mỗi subclass điền vào. Abstract class kiểm soát *trình tự*; subclass kiểm soát *chi tiết*. Pattern này tránh duplicate code điều phối trong khi vẫn cho phép tùy chỉnh.

**Q6: Abstract class vs interface — khi nào chọn cái nào?**

> Chọn **abstract class** khi subclass chia sẻ state thật sự (instance fields) hoặc concrete behavior đáng kể, hoặc khi Template Method Pattern phù hợp. Chọn **interface** khi chỉ cần định nghĩa hợp đồng (không cần state chung), khi class cần thực hiện nhiều vai trò, hoặc muốn tối đa hóa decoupling. Trong Java hiện đại, nếu không chắc, nghiêng về interface — chuyển đổi sau dễ hơn là tháo dỡ abstract class hierarchy.

---

## 11. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [Oracle Tutorial — Abstract Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) | Hướng dẫn chính thức kèm ví dụ |
| *Effective Java* — Joshua Bloch | Item 20: Prefer interfaces to abstract classes |
| *Design Patterns* — Gang of Four | Template Method Pattern (pp. 325–330) |
| *Head First Design Patterns* | Chương 8: Template Method Pattern |
