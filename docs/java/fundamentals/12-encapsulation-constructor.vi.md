# Encapsulation và Constructor

## 1. Khái niệm

**Encapsulation** (đóng gói) là nguyên tắc che giấu trạng thái nội bộ của object, chỉ cho phép truy cập và thay đổi qua interface được kiểm soát.

**Constructor** là method đặc biệt chạy một lần duy nhất khi object được tạo bằng `new` — dùng để khởi tạo giá trị ban đầu cho fields.

```java
public class BankAccount {
    private double balance; // encapsulation: field bị ẩn

    public BankAccount(double initialBalance) { // constructor
        if (initialBalance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        this.balance = initialBalance;
    }

    public double getBalance() { return balance; } // truy cập được kiểm soát
}
```

---

## 2. Tại sao quan trọng

Không có encapsulation, bất kỳ code nào cũng có thể set field thành giá trị vô nghĩa:

```java
// Không có encapsulation
account.balance = -99999; // không ai ngăn được điều này

// Có encapsulation
account.setBalance(-99999); // ném IllegalArgumentException — object tự bảo vệ
```

Hai nguyên tắc này cùng bảo vệ **object invariants** — tập hợp các điều kiện phải luôn đúng trong suốt vòng đời của object. Ví dụ: `balance >= 0`, `name != null`, `age > 0`.

Ngoài ra:
- **Tách biệt API khỏi implementation** — thay đổi cách lưu dữ liệu bên trong mà không ảnh hưởng code bên ngoài
- **Dễ test** — validate logic tập trung ở một chỗ thay vì rải rác khắp nơi
- **Dễ debug** — object sai trạng thái? Chỉ cần kiểm tra setter và constructor

---

## 3. Access Modifiers

Access modifier kiểm soát ai được phép truy cập một field, method, hoặc class.

| Modifier | Trong class | Cùng package | Subclass | Mọi nơi |
| --- | :---: | :---: | :---: | :---: |
| `private` | ✓ | ✗ | ✗ | ✗ |
| _(default)_ | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

### Quy tắc mặc định

```java
public class Product {
    private String name;      // chỉ trong class này
    private double price;     // chỉ trong class này
    int quantity;             // default: cùng package (hạn chế dùng)
    protected String sku;     // package + subclass (dùng khi kế thừa cần)
    public static final int MAX_STOCK = 1000; // hằng số — public là hợp lý
}
```

!!! tip "Nguyên tắc: luôn bắt đầu với `private`"
    Với fields: luôn `private`. Với methods: `private` trừ khi cần expose ra ngoài. Mở rộng access modifier sau dễ hơn thu hẹp — code đang dùng `public` field không thể đổi thành `private` mà không break.

!!! warning "`protected` không có nghĩa là \"an toàn hơn `public`\""
    `protected` vẫn bị truy cập từ bất kỳ subclass nào — kể cả subclass ở package khác. Đừng nhầm tưởng nó là "semi-private". Chỉ dùng `protected` khi thiết kế inheritance hierarchy có chủ đích.

---

## 4. Encapsulation — Getters và Setters

### Getters

Trả về giá trị của private field.

```java
public class Person {
    private String name;
    private int age;
    private boolean active;

    public String getName()  { return name; }   // get + FieldName
    public int    getAge()   { return age; }
    public boolean isActive(){ return active; }  // is + FieldName cho boolean
}
```

!!! note "Convention đặt tên getter"
    `getFieldName()` cho mọi kiểu. Riêng `boolean` dùng `isFieldName()`. Spring, JPA, Jackson đều dựa vào convention này để tự động bind/serialize object — sai tên là bug ngay.

### Setters

Cho phép thay đổi field và kiểm tra tính hợp lệ.

```java
public class Person {
    private String name;
    private int age;

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name must not be blank");
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150)
            throw new IllegalArgumentException("Age out of range: " + age);
        this.age = age;
    }
}
```

### Khi nào không cần setter

Nếu field không bao giờ thay đổi sau khi tạo — đừng viết setter. Tạo ra setter không cần thiết là mở cửa cho lỗi.

```java
public class Point {
    private final int x; // final — không thể thay đổi sau constructor
    private final int y;

    public Point(int x, int y) { this.x = x; this.y = y; }

    public int getX() { return x; }
    public int getY() { return y; }
    // không có setX(), setY()
}
```

---

## 5. Constructor

### Cú pháp

```java
public class Temperature {
    private double celsius;

    // Constructor: tên giống class, không có return type
    public Temperature(double celsius) {
        this.celsius = celsius;
    }
}

Temperature t = new Temperature(100.0); // gọi constructor
```

### Default constructor

Khi không khai báo constructor nào, Java tự sinh ra một **default constructor** không tham số, không làm gì ngoài gọi `super()`.

```java
class Empty {}

Empty e = new Empty(); // hoạt động — Java tự sinh constructor
```

!!! warning "Định nghĩa constructor → default bị xóa"
    Ngay khi bạn khai báo bất kỳ constructor nào, Java **không còn** tự sinh default constructor.
    ```java
    class Box {
        int width;
        Box(int width) { this.width = width; }
    }

    Box b = new Box();    // ❌ lỗi compile — không có no-arg constructor
    Box b = new Box(10);  // ✅
    ```
    Nếu cần cả hai: khai báo tường minh no-arg constructor.

### Thứ tự khởi tạo

Khi `new` chạy, ba việc xảy ra theo thứ tự:

1. Cấp phát Heap — JVM tìm chỗ trống
2. Gán giá trị mặc định cho tất cả fields (`0`, `null`, `false`)
3. Chạy constructor body — ghi đè các giá trị mặc định

```java
class Demo {
    int x;      // bước 2: x = 0
    String s;   // bước 2: s = null

    Demo() {
        x = 42;       // bước 3: x = 42
        s = "hello";  // bước 3: s = "hello"
    }
}
```

---

## 6. Constructor Overloading và `this()`

### Overloading

Nhiều constructor với danh sách tham số khác nhau — Java chọn đúng cái khi gọi `new`.

```java
public class Rectangle {
    private final int width;
    private final int height;

    public Rectangle(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    public Rectangle(int side) {          // hình vuông
        this.width  = side;
        this.height = side;
    }

    public Rectangle() {                  // mặc định 1×1
        this.width  = 1;
        this.height = 1;
    }
}

new Rectangle(4, 6); // w=4 h=6
new Rectangle(5);    // w=5 h=5
new Rectangle();     // w=1 h=1
```

### `this()` — constructor chaining

Gọi constructor khác trong cùng class. Phải là **câu lệnh đầu tiên** trong constructor.

```java
public class Rectangle {
    private final int width;
    private final int height;

    public Rectangle(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    public Rectangle(int side) {
        this(side, side);   // gọi Rectangle(int, int) — tránh lặp code
    }

    public Rectangle() {
        this(1, 1);         // gọi Rectangle(int, int)
    }
}
```

![Constructor Chaining](../../assets/diagrams/constructor-chain.svg)

!!! tip "Dùng `this()` để tránh lặp logic khởi tạo"
    Thay vì copy-paste validation/assignment vào từng constructor, hãy để tất cả chạy qua constructor đầy đủ nhất. Khi logic thay đổi chỉ cần sửa một chỗ.

---

## 7. Immutability với `final`

Object **immutable** không thể thay đổi sau khi tạo. Toàn bộ Java standard library dùng pattern này: `String`, `Integer`, `LocalDate`.

```java
public final class Money {             // final class — không thể extend
    private final String currency;     // final field — chỉ gán một lần
    private final long   amount;       // tính theo đơn vị nhỏ nhất (cents)

    public Money(String currency, long amount) {
        if (currency == null || currency.isBlank())
            throw new IllegalArgumentException("Currency required");
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");
        this.currency = currency;
        this.amount   = amount;
    }

    public String getCurrency() { return currency; }
    public long   getAmount()   { return amount; }

    // Thay vì mutate, trả về object mới
    public Money add(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException("Currency mismatch");
        return new Money(currency, this.amount + other.amount);
    }

    @Override public String toString() {
        return currency + " " + (amount / 100.0);
    }
}
```

Lợi ích của immutability:
- **Thread-safe** — không cần synchronize, không thể có race condition
- **Dễ lý luận** — giá trị không bao giờ bất ngờ thay đổi
- **An toàn để cache và share** — không ai modify được bản mà người khác đang dùng

!!! note "Record (Java 16+) là cách nhanh nhất để tạo immutable class"
    ```java
    record Money(String currency, long amount) {
        Money {  // compact constructor — validate
            if (currency == null || currency.isBlank())
                throw new IllegalArgumentException("Currency required");
            if (amount < 0)
                throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
    ```
    Record tự có `final` fields, getter theo tên field, `toString()`, `equals()`, `hashCode()`.

---

## 8. Code ví dụ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`UserAccount.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/encapsulation/UserAccount.java)

```java
import java.util.Objects;

public final class UserAccount {

    private final String username;  // final: gán một lần trong constructor — identity không thể thay đổi
    private String       email;
    private int          loginCount;
    private boolean      active;

    // không có no-arg constructor — mọi UserAccount cần username + email hợp lệ ngay từ đầu
    public UserAccount(String username, String email) {
        this.username   = validateUsername(username);
        this.email      = validateEmail(email);
        this.loginCount = 0;
        this.active     = true;
    }

    // Private helper — validation tập trung, không lặp lại
    private static String validateUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username must not be blank");
        if (username.length() < 3)
            throw new IllegalArgumentException("Username too short: " + username);
        return username.toLowerCase().trim();
    }

    private static String validateEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email: " + email);
        return email.toLowerCase().trim();
    }

    // Getters
    public String  getUsername()   { return username; }
    public String  getEmail()      { return email; }
    public int     getLoginCount() { return loginCount; }
    public boolean isActive()      { return active; }

    // Setter với validation
    public void setEmail(String email) {
        this.email = validateEmail(email); // tái sử dụng validation — chỉ một chỗ cần sửa khi đổi rule
    }

    // Behavior method — thay đổi state theo logic nghiệp vụ
    public void recordLogin() {
        if (!active) throw new IllegalStateException("Account is deactivated");
        loginCount++;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "UserAccount{username='" + username + "', email='" + email
               + "', logins=" + loginCount + ", active=" + active + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount u)) return false;
        return username.equals(u.username); // bằng nhau theo username, không phải email hay loginCount
    }

    @Override
    public int hashCode() { return Objects.hash(username); }

    public static void main(String[] args) {
        UserAccount alice = new UserAccount("Alice_Dev", "alice@example.com");
        System.out.println(alice);       // UserAccount{username='alice_dev', ...}

        alice.recordLogin();
        alice.recordLogin();
        System.out.println(alice.getLoginCount()); // 2

        alice.setEmail("alice@newdomain.com");

        // alice.username = "hacker"; // ❌ lỗi compile — private final

        alice.deactivate();
        try {
            alice.recordLogin();         // ❌ IllegalStateException
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage()); // Account is deactivated
        }
    }
}
```

---

## 9. Lỗi thường gặp

### Lỗi 1 — Public fields, không encapsulation

```java
// ❌ bất kỳ code nào cũng viết trực tiếp vào field
public class Circle {
    public double radius;
}

Circle c = new Circle();
c.radius = -5; // vô nghĩa, không ai ngăn được

// ✅
public class Circle {
    private double radius;
    public Circle(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }
    public double getRadius() { return radius; }
}
```

### Lỗi 2 — Quên rằng khai báo constructor xóa mất default

```java
class Config {
    String host;
    int port;

    Config(String host, int port) {
        this.host = host;
        this.port = port;
    }
}

Config c = new Config(); // ❌ lỗi compile — không còn no-arg constructor

// ✅ khai báo tường minh nếu cần
Config() { this("localhost", 8080); }
```

### Lỗi 3 — Getter trả về reference đến mutable object

```java
// ❌ caller có thể modify list nội bộ
public class Team {
    private List<String> members = new ArrayList<>();

    public List<String> getMembers() {
        return members; // nguy hiểm — caller gọi .clear() là mất hết
    }
}

// ✅ trả về bản sao bất biến
public List<String> getMembers() {
    return Collections.unmodifiableList(members);
}
```

### Lỗi 4 — `this()` không phải câu lệnh đầu tiên

```java
public Rectangle(int side) {
    System.out.println("Creating square"); // ❌ lỗi compile
    this(side, side); // this() phải là dòng đầu tiên
}

// ✅
public Rectangle(int side) {
    this(side, side); // dòng đầu tiên
}
```

### Lỗi 5 — Setter không validate, getter expose internal state

```java
public void setAge(int age) {
    this.age = age; // ❌ age = -999 cũng chấp nhận
}

public Date getBirthDate() {
    return birthDate; // ❌ Date là mutable — caller thay đổi được
}

// ✅
public void setAge(int age) {
    if (age < 0 || age > 150) throw new IllegalArgumentException("Invalid age: " + age);
    this.age = age;
}

public Date getBirthDate() {
    return new Date(birthDate.getTime()); // trả về defensive copy
}
```

---

## 10. Câu hỏi phỏng vấn

**Q1: Encapsulation là gì và tại sao quan trọng?**

> Encapsulation là nguyên tắc che giấu implementation của object, chỉ expose những gì cần thiết qua public interface. Quan trọng vì: (1) bảo vệ object invariants — field không thể bị set thành giá trị vô nghĩa, (2) tách biệt API khỏi implementation — thay đổi bên trong mà không break caller, (3) tập trung validation vào một chỗ thay vì rải rác toàn codebase.

**Q2: Sự khác nhau giữa `private`, `protected`, `default` và `public`?**

> Phạm vi truy cập tăng dần: `private` (chỉ trong class) → `default`/package-private (cùng package) → `protected` (package + subclass bất kỳ đâu) → `public` (mọi nơi). Best practice: fields luôn `private`, chỉ mở rộng khi thực sự cần và có lý do rõ ràng.

**Q3: Default constructor là gì? Khi nào nó bị xóa?**

> Default constructor là no-arg constructor (`ClassName() {}`) mà Java tự sinh ra khi class không khai báo constructor nào. Nó bị xóa ngay khi bạn khai báo bất kỳ constructor nào — kể cả constructor có tham số. Nếu cần no-arg constructor kết hợp với constructor có tham số, phải khai báo tường minh cả hai.

**Q4: `this()` khác `this` như thế nào?**

> `this` là reference đến object hiện tại — dùng để truy cập field/method của instance. `this()` là lời gọi đến constructor khác trong cùng class — phải là câu lệnh đầu tiên trong constructor, dùng để tránh lặp logic khởi tạo (constructor chaining). Không thể dùng cả hai trong cùng một câu lệnh.

**Q5: Immutable class là gì? Cách tạo?**

> Immutable class là class mà object của nó không thể thay đổi sau khi tạo. Cách tạo: (1) khai báo class là `final` để ngăn subclass phá vỡ immutability, (2) tất cả fields là `private final`, (3) không có setters, (4) constructor validate toàn bộ input, (5) nếu field là mutable object, trả về defensive copy trong getter. Lợi ích: thread-safe mà không cần synchronization.

**Q6: Tại sao getter trả về `List` mutable lại nguy hiểm?**

> Vì caller có thể gọi `.add()`, `.remove()`, `.clear()` trực tiếp trên list nội bộ của object — phá vỡ encapsulation hoàn toàn mà không cần setter nào. Fix: trả về `Collections.unmodifiableList(list)` hoặc `List.copyOf(list)` để caller chỉ đọc được, không sửa được.

---

## 11. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [Oracle Tutorial — Controlling Access](https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html) | Access modifiers chính thức |
| [JEP 395 — Records](https://openjdk.org/jeps/395) | Cách nhanh nhất tạo immutable class |
| *Effective Java* — Joshua Bloch | Item 15: Minimize mutability · Item 16: Favor composition · Item 17: Design for inheritance |
| *Clean Code* — Robert C. Martin | Chapter 6: Objects and Data Structures — Law of Demeter |
