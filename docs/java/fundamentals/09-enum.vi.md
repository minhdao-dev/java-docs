# Enum

## 1. Khái niệm

**Enum** (enumeration — kiểu liệt kê) là một kiểu đặc biệt trong Java dùng để biểu diễn một **tập hợp hằng số cố định, có tên, và có ý nghĩa rõ ràng**.

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

Mỗi giá trị trong enum (`MONDAY`, `TUESDAY`, ...) gọi là một **enum constant**. Đây là các object, không phải số nguyên hay chuỗi thuần.

```java
Day today = Day.WEDNESDAY;
System.out.println(today); // WEDNESDAY
```

---

## 2. Tại sao quan trọng

Trước khi có enum, lập trình viên dùng hằng số `int` hoặc `String` để biểu diễn tập hợp cố định:

```java
// ❌ Cách cũ — dùng int constant
public static final int STATUS_PENDING  = 0;
public static final int STATUS_ACTIVE   = 1;
public static final int STATUS_INACTIVE = 2;

void setStatus(int status) { ... } // nhận int — không an toàn kiểu
setStatus(99); // compiler không báo lỗi, nhưng 99 không hợp lệ
```

```java
// ❌ Cách cũ — dùng String constant
public static final String ROLE_ADMIN = "ADMIN";
public static final String ROLE_USER  = "USER";

void setRole(String role) { ... } // lỗi typo không bị phát hiện
setRole("admin"); // khác "ADMIN" — logic sai, compiler không bắt được
```

**Enum giải quyết tất cả vấn đề này:**

```java
// ✅ Enum — type-safe, rõ ràng, không thể truyền giá trị sai
public enum OrderStatus { PENDING, ACTIVE, INACTIVE }
public enum Role { ADMIN, USER }

void setStatus(OrderStatus status) { ... }
setStatus(OrderStatus.PENDING); // ✅
setStatus(99);                  // ❌ lỗi compile ngay lập tức
```

**Lợi ích cụ thể:**

- **Type-safe** — compiler bắt lỗi ngay, không thể truyền giá trị ngoài tập hợp
- **Readable** — code tự mô tả ý nghĩa, không cần comment giải thích
- **Maintainable** — thêm/xóa constant ở một chỗ, IDE tự báo chỗ cần cập nhật
- **Switchable** — switch/pattern matching hỗ trợ enum trực tiếp
- **Singleton đảm bảo** — mỗi constant chỉ tồn tại một instance duy nhất trong JVM

---

## 3. Enum cơ bản

### Khai báo

```java
public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER
}
```

Quy ước: tên enum viết **PascalCase**, tên constant viết **SCREAMING_SNAKE_CASE**.

### Dùng trong code

```java
Season current = Season.SUMMER;

// So sánh bằng ==, không cần .equals()
if (current == Season.SUMMER) {
    System.out.println("Mùa hè!");
}
```

### name() và ordinal()

Mỗi enum constant có sẵn hai thuộc tính:

```java
Season s = Season.AUTUMN;

System.out.println(s.name());    // "AUTUMN" — tên dưới dạng String
System.out.println(s.ordinal()); // 2 — vị trí trong khai báo (bắt đầu từ 0)
```

!!! warning "Đừng phụ thuộc vào ordinal()"
    `ordinal()` trả về vị trí khai báo trong enum — nếu sau này thêm constant vào giữa, toàn bộ ordinal phía sau dịch chuyển và logic cũ bị sai. Dùng `ordinal()` cho serialization hoặc logic là nguy hiểm.

### values() và valueOf()

```java
// values() — trả về mảng tất cả constant theo thứ tự khai báo
for (Season s : Season.values()) {
    System.out.println(s); // SPRING, SUMMER, AUTUMN, WINTER
}

// valueOf() — chuyển String thành enum constant (phân biệt hoa thường)
Season s = Season.valueOf("WINTER"); // Season.WINTER
Season x = Season.valueOf("winter"); // ❌ IllegalArgumentException
```

---

## 4. Enum với Field và Constructor

Enum là một class đặc biệt — có thể có **field**, **constructor**, và **method**.

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS  (4.869e+24, 6.0518e6),
    EARTH  (5.976e+24, 6.37814e6),
    MARS   (6.421e+23, 3.3972e6);

    private final double mass;   // kg
    private final double radius; // m

    Planet(double mass, double radius) { // constructor luôn là private/package
        this.mass   = mass;
        this.radius = radius;
    }

    static final double G = 6.67300E-11;

    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}
```

```java
double earthWeight = 75.0;
double mass = earthWeight / Planet.EARTH.surfaceGravity();

for (Planet p : Planet.values()) {
    System.out.printf("Cân nặng trên %s: %.2f%n", p, p.surfaceWeight(mass));
}
// Cân nặng trên MERCURY: 28.33
// Cân nặng trên EARTH: 75.00
// ...
```

!!! tip "Constructor của Enum luôn là private"
    Dù không khai báo `private` tường minh, constructor của enum luôn là private — không thể tạo instance enum từ bên ngoài. Đây là cách Java đảm bảo tính singleton cho mỗi constant.

---

## 5. Ví dụ thực tế — OrderStatus

Đây là pattern xuất hiện trong hầu hết các ứng dụng backend:

```java
public enum OrderStatus {
    PENDING("Đang chờ xử lý"),
    CONFIRMED("Đã xác nhận"),
    SHIPPING("Đang giao hàng"),
    DELIVERED("Đã giao"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }
}
```

```java
OrderStatus status = OrderStatus.SHIPPING;
System.out.println(status.getDisplayName()); // Đang giao hàng
System.out.println(status.isFinal());        // false
```

---

## 6. Enum trong switch

Enum kết hợp rất tốt với switch — compiler kiểm tra tất cả các case nếu dùng enhanced switch.

### Traditional switch

```java
Day day = Day.SATURDAY;

switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println("Ngày làm việc");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println("Cuối tuần");
        break;
}
```

### Enhanced switch (Java 14+ — nên dùng)

```java
String type = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Ngày làm việc";
    case SATURDAY, SUNDAY                              -> "Cuối tuần";
};
System.out.println(type); // Cuối tuần
```

!!! tip "Enhanced switch với enum — compiler kiểm tra exhaustiveness"
    Nếu switch không cover hết tất cả constant của enum, compiler cảnh báo hoặc báo lỗi (tùy cách dùng). Đây là một trong những lý do enum + switch là combo mạnh nhất để xử lý tập hợp cố định.

---

## 7. Enum với Abstract Method

Mỗi constant có thể override một abstract method — mỗi constant có hành vi riêng.

```java
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) { this.symbol = symbol; }

    public abstract double apply(double x, double y);

    @Override
    public String toString() { return symbol; }
}
```

```java
double x = 10, y = 3;
for (Operation op : Operation.values()) {
    System.out.printf("%.1f %s %.1f = %.1f%n", x, op, y, op.apply(x, y));
}
// 10.0 + 3.0 = 13.0
// 10.0 - 3.0 = 7.0
// 10.0 * 3.0 = 30.0
// 10.0 / 3.0 = 3.3
```

Pattern này thay thế hoàn toàn `if-else` dài dòng — thêm operation mới chỉ cần thêm constant, không sửa logic hiện có.

---

## 8. EnumSet và EnumMap

Hai collection được tối ưu riêng cho enum — nhanh hơn `HashSet` và `HashMap`.

```java
import java.util.EnumSet;
import java.util.EnumMap;

// EnumSet — tập hợp các enum constant
EnumSet<Day> weekdays = EnumSet.of(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY,
                                    Day.THURSDAY, Day.FRIDAY);
EnumSet<Day> weekend  = EnumSet.complementOf(weekdays); // SATURDAY, SUNDAY

System.out.println(weekdays.contains(Day.MONDAY));  // true
System.out.println(weekend.contains(Day.SATURDAY)); // true
```

```java
// EnumMap — map với key là enum constant
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MONDAY,    "Standup meeting");
schedule.put(Day.WEDNESDAY, "Code review");
schedule.put(Day.FRIDAY,    "Demo");

System.out.println(schedule.get(Day.MONDAY)); // Standup meeting
```

!!! tip "Dùng EnumSet/EnumMap khi key là enum"
    `EnumSet` dùng bit-vector bên trong — cực kỳ nhanh và compact. `EnumMap` dùng array indexed theo ordinal. Cả hai nhanh hơn hẳn `HashSet`/`HashMap` với enum key.

---

## 9. Code ví dụ đầy đủ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`EnumDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/enums/EnumDemo.java)

```java linenums="1"
public class EnumDemo {

    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    enum HttpStatus {
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int    code;
        private final String reason;

        HttpStatus(int code, String reason) {
            this.code   = code;
            this.reason = reason;
        }

        public int    getCode()   { return code; }
        public String getReason() { return reason; }

        public boolean isError() { return code >= 400; }

        public static HttpStatus fromCode(int code) { // (1)
            for (HttpStatus s : values()) {
                if (s.code == code) return s;
            }
            throw new IllegalArgumentException("Unknown HTTP status: " + code);
        }
    }

    public static void main(String[] args) {
        // Basic usage
        Priority p = Priority.HIGH;
        System.out.println(p.name());    // HIGH
        System.out.println(p.ordinal()); // 2

        // Enhanced switch
        String label = switch (p) {
            case LOW, MEDIUM -> "Normal";
            case HIGH        -> "Urgent";
            case CRITICAL    -> "Emergency";
        };
        System.out.println(label); // Urgent

        // Enum với field
        HttpStatus status = HttpStatus.NOT_FOUND;
        System.out.println(status.getCode());   // 404
        System.out.println(status.getReason()); // Not Found
        System.out.println(status.isError());   // true

        // Lookup từ value
        HttpStatus found = HttpStatus.fromCode(200);
        System.out.println(found); // OK

        // Duyệt tất cả constant
        for (HttpStatus s : HttpStatus.values()) {
            System.out.printf("[%d] %s%n", s.getCode(), s.getReason());
        }
    }
}
```

1. Factory method `fromCode()` — pattern phổ biến để lookup enum constant từ một giá trị thô (int, String). Thường dùng khi deserialize dữ liệu từ database hoặc API.

---

## 10. Lỗi thường gặp

### Lỗi 1 — Dùng ordinal() cho logic

```java
// ❌ ordinal() thay đổi nếu thêm constant mới vào giữa
public enum Status { PENDING, ACTIVE, INACTIVE }

int code = status.ordinal(); // lưu vào DB
// Sau này thêm SUSPENDED vào giữa ACTIVE và INACTIVE
// → INACTIVE.ordinal() đổi từ 2 → 3, dữ liệu cũ bị sai hết

// ✅ Dùng field riêng cho code
public enum Status {
    PENDING(0), ACTIVE(1), INACTIVE(2);
    private final int code;
    Status(int code) { this.code = code; }
    public int getCode() { return code; }
}
```

### Lỗi 2 — valueOf() không handle exception

```java
// ❌ crash nếu input không hợp lệ
String input = request.getParam("status"); // có thể là "pending" (lowercase)
OrderStatus s = OrderStatus.valueOf(input); // IllegalArgumentException

// ✅ Handle an toàn
try {
    OrderStatus s = OrderStatus.valueOf(input.toUpperCase());
} catch (IllegalArgumentException e) {
    // trả về lỗi cho client
}
```

### Lỗi 3 — So sánh enum bằng .equals() thay vì ==

```java
// ❌ .equals() không sai, nhưng dư thừa và dễ gây NullPointerException
if (status.equals(OrderStatus.PENDING)) { ... }

// ✅ Dùng == — an toàn hơn, không bị NPE khi status là null so sánh ngược
if (status == OrderStatus.PENDING) { ... }
```

### Lỗi 4 — Dùng String constant thay vì Enum trong switch

```java
// ❌ compiler không bắt được nếu thêm case mới mà quên xử lý
String status = "PENDING";
if (status.equals("PENDING")) { ... }
else if (status.equals("ACTIVE")) { ... }
// Thêm "CANCELLED" mà quên — không báo lỗi gì

// ✅ Enum + exhaustive switch — compiler cảnh báo case thiếu
OrderStatus status = OrderStatus.PENDING;
String result = switch (status) {
    case PENDING   -> "...";
    case ACTIVE    -> "...";
    // Thiếu CANCELLED → compiler báo lỗi nếu switch là expression
};
```

---

## 11. Câu hỏi phỏng vấn

**Q1: Enum trong Java có thực sự là class không?**

> Có. Compiler biên dịch enum thành một class kế thừa từ `java.lang.Enum`. Mỗi constant là một `public static final` instance của chính class đó. Vì vậy enum có thể có field, constructor, method — và mỗi constant là một object thực sự.

**Q2: Tại sao không nên dùng `ordinal()` để lưu vào database?**

> `ordinal()` phụ thuộc vào **thứ tự khai báo** trong source code. Nếu sau này thêm constant mới vào giữa, `ordinal()` của các constant phía sau đều thay đổi, khiến dữ liệu cũ trong database bị map sai. Cách đúng là thêm một field `code` hoặc `value` cố định vào enum và dùng field đó để lưu.

**Q3: Enum có thể implements interface không? Có thể extends class không?**

> Enum **có thể implements interface** — đây là cách thêm hành vi chung cho tất cả constant. Enum **không thể extends class** vì nó đã ngầm kế thừa `java.lang.Enum`, và Java không hỗ trợ đa kế thừa class.

**Q4: Enum có thread-safe không?**

> Có. Các enum constant là `static final` và được khởi tạo một lần bởi class loader — quá trình này thread-safe theo đặc tả JVM. Đây là lý do Enum là cách implement **Singleton pattern** an toàn nhất trong Java (không bị break bởi reflection hay serialization).

**Q5: EnumSet khác gì với HashSet chứa enum?**

> `EnumSet` dùng **bit-vector** bên trong (mỗi bit ứng với một constant theo ordinal) nên các phép toán như `contains()`, `add()`, `remove()` chạy O(1) và cực nhanh. Bộ nhớ dùng cũng nhỏ hơn nhiều. `HashSet` tổng quát hơn nhưng tốn overhead của hashing và boxing.

---

## 12. Tài liệu tham khảo

| Tài liệu | Nội dung |
|----------|---------|
| [JLS §8.9 — Enum Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.9) | Đặc tả chính thức |
| [Oracle Tutorial — Enum Types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) | Hướng dẫn chính thức |
| *Effective Java* — Joshua Bloch | Item 34: Use enums instead of int constants · Item 38: Emulate extensible enums with interfaces |
| [Baeldung — Guide to Java Enums](https://www.baeldung.com/a-guide-to-java-enums) | Bài viết thực hành |
