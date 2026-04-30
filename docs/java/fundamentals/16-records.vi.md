# Records

## 1. Khái niệm

**Record** (Java 16+) là một loại class đặc biệt được thiết kế để làm **data carrier** — đối tượng chỉ dùng để chứa dữ liệu, không có hành vi phức tạp. Record tự động sinh ra toàn bộ boilerplate code mà lập trình viên thường phải viết tay.

```java
// Khai báo record — chỉ một dòng
public record Point(int x, int y) { }
```

Compiler tự động sinh ra:
- **Constructor** nhận tất cả field
- **Accessor method** cho mỗi field (tên giống field, không có tiền tố `get`)
- **`equals()`** — so sánh từng field
- **`hashCode()`** — tính từ tất cả field
- **`toString()`** — in tên class và tất cả field

```java
Point p = new Point(3, 4);

System.out.println(p.x());          // 3   ← accessor
System.out.println(p.y());          // 4
System.out.println(p);              // Point[x=3, y=4]  ← toString()
System.out.println(p.equals(new Point(3, 4))); // true  ← equals()
```

---

## 2. Tại sao quan trọng

Trước khi có Record, để tạo một class dữ liệu đơn giản phải viết rất nhiều code:

```java
// ❌ Class truyền thống — 40+ dòng chỉ để chứa 2 field
public final class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

```java
// ✅ Record — 1 dòng, tương đương 100%
public record Point(int x, int y) { }
```

Record giải quyết:

- **Boilerplate** — không còn viết constructor, getter, equals, hashCode, toString tay
- **Immutability mặc định** — tất cả field là `final`, không thể thay đổi sau khi tạo
- **Intent rõ ràng** — ai đọc code cũng hiểu ngay đây là data class, không có side effect
- **Less bugs** — equals/hashCode được sinh đúng, không bị quên field

---

## 3. Record cơ bản

### Khai báo

```java
public record Person(String name, int age) { }

public record Point(double x, double y) { }

public record Range(int min, int max) { }
```

### Tạo và dùng

```java
Person p = new Person("Alice", 30);

// Accessor — tên giống field, không có "get"
System.out.println(p.name()); // Alice
System.out.println(p.age());  // 30

// toString() tự động
System.out.println(p); // Person[name=Alice, age=30]

// equals() tự động — so sánh theo giá trị
Person p2 = new Person("Alice", 30);
System.out.println(p.equals(p2)); // true

// hashCode() nhất quán với equals()
System.out.println(p.hashCode() == p2.hashCode()); // true
```

### Immutability — field luôn là final

```java
Person p = new Person("Alice", 30);
// p.name = "Bob"; // ❌ không compile — field là final
// p.age  = 31;    // ❌ không compile
```

!!! tip "Record là immutable by design"
    Immutable object an toàn hơn trong môi trường multi-thread, dễ test và dễ debug hơn vì state không thay đổi sau khi tạo. Record ép buộc điều này mà không cần thêm code.

---

## 4. Compact Constructor — validate dữ liệu

Dùng **compact constructor** để validate input trước khi lưu vào field. Compact constructor không khai báo parameter (chúng được kế thừa từ record header) và không cần gán `this.field = value` (compiler tự làm sau khi constructor kết thúc).

```java
public record Range(int min, int max) {

    // Compact constructor — chỉ cần viết validation
    Range {
        if (min > max) {
            throw new IllegalArgumentException(
                "min (%d) phải <= max (%d)".formatted(min, max));
        }
    }
}
```

```java
Range r1 = new Range(1, 10);  // ✅
Range r2 = new Range(10, 1);  // ❌ IllegalArgumentException: min (10) phải <= max (1)
```

```java
// Ví dụ khác — normalize String
public record Email(String address) {

    Email {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Email không hợp lệ: " + address);
        }
        address = address.toLowerCase().trim(); // (1)
    }
}
```

1. Trong compact constructor, có thể gán lại giá trị cho component — compiler sẽ dùng giá trị mới này để gán vào field. Đây là cách duy nhất để transform input trong record.

```java
Email e = new Email("  Alice@Example.COM  ");
System.out.println(e.address()); // alice@example.com — đã normalize
```

---

## 5. Custom Method trong Record

Record có thể có instance method và static method thông thường:

```java
public record Point(double x, double y) {

    // Instance method
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Static factory method — pattern phổ biến
    public static Point origin() {
        return new Point(0, 0);
    }
}
```

```java
Point a = new Point(0, 0);
Point b = new Point(3, 4);

System.out.println(a.distanceTo(b)); // 5.0
System.out.println(b.magnitude());  // 5.0
System.out.println(Point.origin()); // Point[x=0.0, y=0.0]
```

---

## 6. Record implements Interface

Record có thể implements interface nhưng **không thể extends class** (vì đã ngầm extends `java.lang.Record`).

```java
public interface Describable {
    String describe();
}

public record Product(String name, double price) implements Describable {

    @Override
    public String describe() {
        return "%s — %.2f VND".formatted(name, price);
    }
}
```

```java
Product p = new Product("Laptop", 25_000_000.0);
System.out.println(p.describe()); // Laptop — 25000000.00 VND
```

---

## 7. Record làm DTO — use case phổ biến nhất

**DTO (Data Transfer Object)** là pattern cực phổ biến trong Spring Boot — dùng để nhận request từ client hoặc trả response về client mà không expose entity trực tiếp. Record là lựa chọn hoàn hảo:

```java
// Request DTO — nhận dữ liệu từ client
public record CreateUserRequest(
    String username,
    String email,
    String password
) {
    CreateUserRequest {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username không được trống");
        if (!email.contains("@"))
            throw new IllegalArgumentException("Email không hợp lệ");
    }
}

// Response DTO — trả về cho client (không expose password)
public record UserResponse(
    Long   id,
    String username,
    String email
) { }
```

```java
// Sẽ học cách dùng với Spring Boot ở Phase 05
// CreateUserRequest req = new CreateUserRequest("alice", "alice@mail.com", "secret");
// UserResponse res = userService.create(req);
// return ResponseEntity.ok(res);
```

---

## 8. Record Generic

Record hỗ trợ type parameter:

```java
public record Pair<A, B>(A first, B second) {

    public Pair<B, A> swap() {
        return new Pair<>(second, first);
    }
}
```

```java
Pair<String, Integer> p = new Pair<>("hello", 42);
System.out.println(p.first());  // hello
System.out.println(p.second()); // 42

Pair<Integer, String> swapped = p.swap();
System.out.println(swapped); // Pair[first=42, second=hello]
```

---

## 9. So sánh: Record vs Class vs Lombok

| | Truyền thống | Lombok `@Data` | Record (Java 16+) |
|---|---|---|---|
| Boilerplate | Nhiều | Ít (annotation) | Không có |
| Immutable | Tùy (phải thêm `final`) | Không (setter được sinh) | Luôn luôn |
| Kế thừa | Có | Có | Không extends được |
| Dependency ngoài | Không | Cần Lombok | Không |
| Phiên bản Java | Mọi | Mọi (với Lombok) | Java 16+ |
| Dùng cho | Mọi loại object | Mọi loại object | Data-only, DTO, value object |

!!! tip "Khi nào dùng Record, khi nào dùng Class thường?"
    **Dùng Record** khi object chỉ chứa dữ liệu, không cần thay đổi sau khi tạo, không cần kế thừa — DTO, response object, value object, config tuple.
    **Dùng Class** khi cần mutable state, kế thừa, logic phức tạp, hoặc framework yêu cầu (JPA Entity bắt buộc phải có default constructor và setter).

---

## 10. Giới hạn của Record

```java
// ❌ Không thể extends class
public record Point(int x, int y) extends Shape { } // lỗi compile

// ❌ Không thể có instance field ngoài record component
public record Point(int x, int y) {
    private int z; // ❌ lỗi compile — chỉ được có static field
    private static int count = 0; // ✅ static field OK
}

// ❌ Không thể là abstract
public abstract record Shape(double area) { } // lỗi compile

// ❌ JPA Entity không thể là Record
// @Entity
// public record Student(...) { } // JPA cần no-arg constructor và setter — record không có
```

---

## 11. Code ví dụ đầy đủ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`RecordDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/records/RecordDemo.java)

```java linenums="1"
public class RecordDemo {

    // Record cơ bản
    record Point(double x, double y) {
        // Compact constructor — validate
        Point {
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("Tọa độ không được là NaN");
        }

        double distanceTo(Point other) {
            double dx = x - other.x, dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        static Point origin() { return new Point(0, 0); }
    }

    // Record generic
    record Pair<A, B>(A first, B second) {
        Pair<B, A> swap() { return new Pair<>(second, first); }
    }

    // Record DTO
    record StudentDTO(String name, double gpa) {
        StudentDTO {
            if (gpa < 0 || gpa > 10)
                throw new IllegalArgumentException("GPA phải trong [0, 10]");
            name = name.strip(); // normalize
        }

        String grade() { // (1)
            if (gpa >= 9.0) return "Xuất sắc";
            if (gpa >= 8.0) return "Giỏi";
            if (gpa >= 6.5) return "Khá";
            if (gpa >= 5.0) return "Trung bình";
            return "Yếu";
        }
    }

    public static void main(String[] args) {
        // Point
        Point a = Point.origin();
        Point b = new Point(3, 4);
        System.out.println(b);                  // Point[x=3.0, y=4.0]
        System.out.println(a.distanceTo(b));    // 5.0

        // equals dựa theo giá trị
        System.out.println(b.equals(new Point(3, 4))); // true

        // Pair
        Pair<String, Integer> pair = new Pair<>("score", 95);
        System.out.println(pair);               // Pair[first=score, second=95]
        System.out.println(pair.swap());        // Pair[first=95, second=score]

        // StudentDTO
        StudentDTO s = new StudentDTO("  Nguyen Van A  ", 8.5);
        System.out.println(s.name());   // Nguyen Van A — đã strip
        System.out.println(s.grade());  // Giỏi
        System.out.println(s);          // StudentDTO[name=Nguyen Van A, gpa=8.5]

        // Immutability — record không thể thay đổi sau khi tạo
        // s.name = "other"; // ❌ không compile
    }
}
```

1. Record có thể có business method — nhưng nếu logic quá phức tạp, hãy xem xét dùng class thường thay vì nhét vào record.

---

## 12. Lỗi thường gặp

### Lỗi 1 — Gọi getter với tiền tố `get`

```java
record Person(String name, int age) { }

Person p = new Person("Alice", 30);

// ❌ NoSuchMethodError — record KHÔNG sinh ra getName()
p.getName();

// ✅ Accessor của record không có tiền tố "get"
p.name();
p.age();
```

### Lỗi 2 — Dùng Record cho JPA Entity

```java
// ❌ JPA cần no-arg constructor và setter — record không cung cấp
@Entity
public record Student(Long id, String name) { }

// ✅ JPA Entity phải là class thường
@Entity
public class Student {
    @Id Long id;
    String name;
    // getters, setters, no-arg constructor...
}
```

### Lỗi 3 — Nhầm compact constructor với canonical constructor

```java
record Range(int min, int max) {

    // ❌ Đây là canonical constructor — khai báo lại thừa, dễ bị thừa thiếu gán
    Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // ✅ Compact constructor — không cần tham số, không cần gán this.field
    Range {
        if (min > max) throw new IllegalArgumentException("min > max");
    }
}
```

### Lỗi 4 — Thêm instance field vào record

```java
// ❌ Instance field ngoài component không được phép
record Point(int x, int y) {
    private String label; // lỗi compile
}

// ✅ Nếu cần thêm dữ liệu, thêm vào component
record Point(int x, int y, String label) { }

// ✅ Hoặc tính toán từ component hiện có
record Point(int x, int y) {
    double magnitude() { return Math.sqrt(x * x + y * y); }
}
```

---

## 13. Câu hỏi phỏng vấn

**Q1: Record trong Java là gì và khi nào nên dùng?**

> Record (Java 16+) là một loại class đặc biệt để biểu diễn **immutable data carriers**. Compiler tự động sinh ra canonical constructor, accessor methods, `equals()`, `hashCode()`, `toString()`. Dùng Record khi object chỉ chứa dữ liệu và không cần thay đổi sau khi tạo — DTO, value object, response payload, config tuple. Không dùng cho JPA entity (cần mutable) hay class cần kế thừa.

**Q2: Record khác class thông thường ở những điểm nào?**

> Điểm khác biệt chính: (1) tất cả component là `private final` — immutable; (2) không thể extends class khác (đã ngầm extends `java.lang.Record`); (3) không thể có instance field ngoài component; (4) accessor method không có tiền tố `get`; (5) `equals()`/`hashCode()`/`toString()` được tự động sinh dựa trên tất cả component. Class thường linh hoạt hơn nhưng cần viết nhiều boilerplate.

**Q3: Compact constructor là gì? Dùng khi nào?**

> Compact constructor là cú pháp đặc biệt trong record — khai báo không có danh sách tham số (kế thừa từ record header) và không cần `this.field = value` (compiler tự thêm sau khi constructor body kết thúc). Dùng để validate input, normalize dữ liệu (ví dụ: trim String, chuyển lowercase). Nếu muốn transform một component, gán lại giá trị trong compact constructor — compiler sẽ dùng giá trị mới.

**Q4: Record có thể implements interface không?**

> Có. Record có thể implements bất kỳ interface nào và phải implement tất cả abstract method của interface đó. Nhưng record không thể extends class vì đã ngầm kế thừa `java.lang.Record`. Đây là cách thêm behavior chung cho record — ví dụ implements `Comparable`, `Serializable`, hoặc interface tự định nghĩa.

**Q5: Tại sao JPA Entity không thể là Record?**

> JPA (Hibernate) yêu cầu entity phải có **no-argument constructor** (để Hibernate tạo object khi load từ database qua reflection) và **setter method** (để Hibernate gán giá trị vào từng field). Record không cung cấp cả hai — canonical constructor luôn cần tất cả tham số, và tất cả field là `final` nên không có setter. Đây là giới hạn thiết kế của record khi kết hợp với framework yêu cầu mutable object.

---

## 14. Tài liệu tham khảo

| Tài liệu | Nội dung |
|----------|---------|
| [JEP 395 — Records](https://openjdk.org/jeps/395) | Đề xuất thiết kế chính thức |
| [Oracle Docs — Record Classes](https://docs.oracle.com/en/java/docs/api/java.base/java/lang/Record.html) | Javadoc |
| [Oracle Tutorial — Record Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/records.html) | Hướng dẫn chính thức |
| [Baeldung — Java Records](https://www.baeldung.com/java-record-keyword) | Bài viết thực hành |
