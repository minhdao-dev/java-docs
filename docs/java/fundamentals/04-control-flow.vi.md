# Rẽ nhánh — if / else / switch

## 1. Khái niệm

Rẽ nhánh (control flow) là cơ chế cho phép chương trình **chọn đường thực thi** dựa trên điều kiện. Thay vì chạy từng dòng theo thứ tự, chương trình có thể bỏ qua hoặc thực thi các khối code khác nhau tùy thuộc vào trạng thái dữ liệu.

Java cung cấp hai cấu trúc rẽ nhánh chính:

| Cấu trúc | Dùng khi |
| --- | --- |
| `if / else if / else` | Điều kiện phức tạp, khoảng giá trị, nhiều biến |
| `switch` | Một biến so sánh với nhiều giá trị cụ thể |

---

## 2. Tại sao quan trọng

Rẽ nhánh là nền tảng của mọi logic nghiệp vụ. Hiểu rõ chúng giúp:

- Viết code rõ ràng, tránh lồng điều kiện quá nhiều tầng
- Biết khi nào dùng `if` vs `switch` để tối ưu tính đọc được
- Tránh bug fall-through kinh điển trong `switch` truyền thống
- Tận dụng **switch expression** (Java 14+) và **pattern matching** (Java 21+) để code gọn, an toàn hơn

---

## 3. if / else if / else

```java
if (điều_kiện) {
    // thực thi khi điều_kiện là true
} else if (điều_kiện_khác) {
    // thực thi khi điều_kiện_khác là true
} else {
    // thực thi khi tất cả điều kiện trên là false
}
```

Biểu thức trong `if` bắt buộc là kiểu `boolean` — Java không tự chuyển số hay object sang boolean:

```java
int x = 5;

if (x) { ... }       // ❌ compile error — x là int, không phải boolean
if (x != 0) { ... }  // ✅
```

### Early return thay vì lồng if

Khi điều kiện lồng nhau nhiều tầng, **early return** giúp code đọc từ trên xuống như văn xuôi:

```java
// ❌ Pyramid of doom — khó đọc, dễ bỏ sót trường hợp
public String classify(int score) {
    if (score >= 0) {
        if (score <= 100) {
            if (score >= 90) {
                return "A";
            } else {
                return "B hoặc thấp hơn";
            }
        } else {
            return "Không hợp lệ";
        }
    } else {
        return "Không hợp lệ";
    }
}

// ✅ Early return — loại bỏ trường hợp biên trước, đọc tuần tự
public String classify(int score) {
    if (score < 0 || score > 100) return "Không hợp lệ";
    if (score >= 90) return "A";
    if (score >= 80) return "B";
    if (score >= 70) return "C";
    if (score >= 60) return "D";
    return "F";
}
```

---

## 4. switch statement

`switch` so sánh một biến với nhiều giá trị cụ thể. Phù hợp hơn chuỗi `if/else if` khi có từ 3–4 nhánh trở lên cho cùng một biến.

```java
int day = 3;

switch (day) {
    case 1:
        System.out.println("Thứ Hai");
        break;
    case 2:
        System.out.println("Thứ Ba");
        break;
    case 3:
        System.out.println("Thứ Tư");
        break;
    default:
        System.out.println("Ngày khác");
}
```

### Fall-through — tính năng thường là bug

Nếu thiếu `break`, thực thi **rơi xuống** (fall-through) các case tiếp theo:

```java
int x = 1;
switch (x) {
    case 1:
        System.out.println("one");   // được in
        // không có break!
    case 2:
        System.out.println("two");   // cũng được in — fall-through
        break;
    case 3:
        System.out.println("three"); // không được in
}
// Output: one
//         two
```

Fall-through **dùng có chủ ý** để gộp nhiều case có cùng hành vi:

```java
switch (month) {
    case 1: case 3: case 5:
    case 7: case 8: case 10: case 12:
        days = 31;
        break;
    case 4: case 6: case 9: case 11:
        days = 30;
        break;
    default:
        days = 28;
}
```

### Kiểu được hỗ trợ

`switch` truyền thống chỉ hoạt động với:

| Kiểu | Ghi chú |
| --- | --- |
| `byte`, `short`, `int`, `char` | Primitive |
| `Byte`, `Short`, `Integer`, `Character` | Wrapper |
| `String` | Từ Java 7 |
| `enum` | Từ Java 5 |

```java
switch (3.14) { ... }    // ❌ double — lỗi biên dịch
switch (longVar) { ... } // ❌ long — lỗi biên dịch
```

---

## 5. switch expression (Java 14+)

Java 14 giới thiệu **switch expression** — phiên bản hiện đại, gọn hơn và an toàn hơn:

```java
// Switch statement — nhiều break, dài dòng
String result;
switch (day) {
    case 1: result = "Thứ Hai"; break;
    case 2: result = "Thứ Ba";  break;
    default: result = "Khác";   break;
}

// Switch expression — gọn, không fall-through, trả về giá trị
String result = switch (day) {
    case 1  -> "Thứ Hai";
    case 2  -> "Thứ Ba";
    default -> "Khác";
};
```

Ưu điểm:

- **Không fall-through**: mỗi `case ->` độc lập, không cần `break`
- **Trả về giá trị** trực tiếp — dùng được trong assignment, return, argument
- **Exhaustiveness**: compiler báo lỗi nếu không cover hết case (với `enum`)
- **Gộp nhiều case**: `case 1, 2, 3 ->`

```java
int daysInMonth = switch (month) {
    case 1, 3, 5, 7, 8, 10, 12 -> 31;
    case 4, 6, 9, 11            -> 30;
    case 2                      -> 28;
    default -> throw new IllegalArgumentException("Tháng không hợp lệ: " + month);
};
```

### yield — trả về giá trị từ block nhiều dòng

Khi một nhánh cần nhiều dòng, dùng block `{}` và `yield`:

```java
int statusCode = switch (status) {
    case "SUCCESS"   -> 200;
    case "NOT_FOUND" -> 404;
    case "ERROR" -> {
        System.out.println("Ghi log lỗi server...");
        yield 500; // trả về giá trị từ block
    }
    default -> throw new IllegalArgumentException("Status không xác định: " + status);
};
```

!!! note "`yield` vs `return`"
    `return` thoát khỏi **method**. `yield` trả về giá trị khỏi **switch expression** và tiếp tục thực thi method.

---

## 6. Pattern matching trong switch (Java 21+)

Java 21 mở rộng switch để hỗ trợ **type pattern** — kiểm tra kiểu và binding biến trong một bước:

```java
// Trước Java 21 — chuỗi if/instanceof
static String describe(Object obj) {
    if (obj instanceof Integer i) return "Integer: " + i;
    if (obj instanceof String s)  return "String dài " + s.length() + " ký tự";
    if (obj instanceof Double d)  return "Double: " + d;
    return "Kiểu khác";
}

// Java 21 — switch pattern matching
static String describe(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s  -> "String dài " + s.length() + " ký tự";
        case Double d  -> "Double: " + d;
        default        -> "Kiểu khác";
    };
}
```

### Guarded patterns với `when`

Thêm điều kiện bổ sung vào từng case:

```java
static String classify(Object obj) {
    return switch (obj) {
        case Integer i when i < 0  -> "Số nguyên âm";
        case Integer i when i == 0 -> "Số không";
        case Integer i             -> "Số nguyên dương: " + i;
        case String s when s.isEmpty() -> "Chuỗi rỗng";
        case String s              -> "Chuỗi: " + s;
        default                    -> "Kiểu khác";
    };
}
```

### Xử lý null trong switch

Switch truyền thống ném `NullPointerException` khi biến là `null`. Pattern matching switch xử lý được:

```java
static String handleNull(String s) {
    return switch (s) {
        case null -> "Giá trị null";
        case ""   -> "Chuỗi rỗng";
        default   -> "Chuỗi: " + s;
    };
}
```

---

## 7. Code ví dụ

```java linenums="1"
public class ControlFlowDemo {

    // Early return — phân loại điểm
    static String gradeOf(int score) {
        if (score < 0 || score > 100) return "Không hợp lệ";
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    // switch expression — tên tháng
    static String monthName(int month) {
        return switch (month) {
            case 1  -> "Tháng Một";
            case 2  -> "Tháng Hai";
            case 3  -> "Tháng Ba";
            case 4  -> "Tháng Tư";
            case 5  -> "Tháng Năm";
            case 6  -> "Tháng Sáu";
            case 7  -> "Tháng Bảy";
            case 8  -> "Tháng Tám";
            case 9  -> "Tháng Chín";
            case 10 -> "Tháng Mười";
            case 11 -> "Tháng Mười Một";
            case 12 -> "Tháng Mười Hai";
            default -> throw new IllegalArgumentException("Tháng không hợp lệ: " + month);
        };
    }

    // switch expression với yield
    static int httpStatus(String code) {
        return switch (code) {
            case "OK"        -> 200;
            case "NOT_FOUND" -> 404;
            case "ERROR" -> {
                System.out.println("Ghi log lỗi server...");
                yield 500;
            }
            default -> throw new IllegalArgumentException("Code không xác định: " + code);
        };
    }

    // Pattern matching (Java 21+)
    static String describe(Object obj) {
        return switch (obj) {
            case null                      -> "null";
            case Integer i when i < 0      -> "Số âm: " + i;
            case Integer i                 -> "Số nguyên: " + i;
            case String s when s.isEmpty() -> "Chuỗi rỗng";
            case String s                  -> "Chuỗi: \"" + s + "\"";
            default -> "Kiểu: " + obj.getClass().getSimpleName();
        };
    }

    public static void main(String[] args) {
        System.out.println(gradeOf(85));             // B
        System.out.println(monthName(7));            // Tháng Bảy
        System.out.println(httpStatus("NOT_FOUND")); // 404
        System.out.println(describe(-42));           // Số âm: -42
        System.out.println(describe("Hello"));       // Chuỗi: "Hello"
        System.out.println(describe(""));            // Chuỗi rỗng
        System.out.println(describe(null));          // null
    }
}
```

---

## 8. Lỗi thường gặp

### Lỗi 1 — Quên break trong switch statement

```java
int x = 1;
switch (x) {
    case 1:
        System.out.println("one"); // ❌ fall-through — in cả "two"
    case 2:
        System.out.println("two");
        break;
}
// Output: one
//         two

// ✅ Thêm break hoặc dùng switch expression
String result = switch (x) {
    case 1  -> "one";
    case 2  -> "two";
    default -> "other";
};
```

### Lỗi 2 — Lồng if quá nhiều tầng

```java
// ❌ Pyramid of doom
public double calculateDiscount(User user, Order order) {
    if (user != null) {
        if (order != null) {
            if (order.getTotal() > 100) {
                if (user.isPremium()) {
                    return 0.2;
                } else {
                    return 0.1;
                }
            }
        }
    }
    return 0;
}

// ✅ Guard clause + early return
public double calculateDiscount(User user, Order order) {
    if (user == null || order == null) return 0;
    if (order.getTotal() <= 100) return 0;
    return user.isPremium() ? 0.2 : 0.1;
}
```

### Lỗi 3 — Thiếu default trong switch statement

```java
// ❌ Nếu status không khớp case nào, result không được gán
String result;
switch (status) {
    case "OK":    result = "success"; break;
    case "ERROR": result = "failed";  break;
    // thiếu default
}
System.out.println(result); // ❌ compile error: variable result might not have been initialized

// ✅
switch (status) {
    case "OK":    result = "success"; break;
    case "ERROR": result = "failed";  break;
    default:      result = "unknown"; break;
}
```

### Lỗi 4 — Dùng switch statement khi switch expression phù hợp hơn

```java
// ❌ Dài dòng — 3 break thừa
String label;
switch (code) {
    case 1:  label = "One";   break;
    case 2:  label = "Two";   break;
    default: label = "Other"; break;
}

// ✅ Gọn hơn nhiều
String label = switch (code) {
    case 1  -> "One";
    case 2  -> "Two";
    default -> "Other";
};
```

### Lỗi 5 — So sánh String bằng == trong if

```java
String input = new String("admin");

if (input == "admin") { ... }      // ❌ false — so sánh địa chỉ, không phải nội dung

if ("admin".equals(input)) { ... } // ✅
```

---

## 9. Câu hỏi phỏng vấn

**Q1: Fall-through trong switch là gì? Khi nào dùng có chủ ý?**

> Fall-through xảy ra khi thiếu `break` — thực thi tiếp tục sang case tiếp theo. Thường là bug. Dùng có chủ ý khi muốn gộp nhiều case có cùng hành vi: `case 1: case 2: case 3: doSomething(); break;`

**Q2: switch expression (Java 14+) khác switch statement thế nào?**

> Ba điểm khác biệt chính: (1) Arrow syntax `->` không có fall-through — không cần `break`. (2) Có thể trả về giá trị trực tiếp, dùng được trong assignment. (3) Compiler kiểm tra exhaustiveness với `enum` — báo lỗi nếu thiếu case.

**Q3: Khi nào dùng if/else, khi nào dùng switch?**

> Dùng `switch` khi so sánh **một biến** với nhiều **giá trị cụ thể** (từ 3–4 case trở lên). Dùng `if/else` khi điều kiện phức tạp: khoảng giá trị (`score >= 90`), nhiều biến, hoặc biểu thức logic tùy ý.

**Q4: `yield` trong switch expression dùng để làm gì?**

> `yield` trả về giá trị từ một **block** `{}` bên trong switch expression. Cần thiết khi nhánh có nhiều dòng lệnh trước khi trả về. `return` không dùng được ở đây vì nó sẽ thoát khỏi method, không phải khỏi switch expression.

**Q5: Pattern matching trong switch (Java 21) cho phép gì?**

> Cho phép dùng **type pattern** làm case — kiểm tra kiểu và binding biến trong một bước. Có thể thêm `when` để lọc thêm điều kiện (guarded pattern). Cũng hỗ trợ `case null` để xử lý null an toàn thay vì ném `NullPointerException`.

---

## 10. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [JLS §14.9 — The if Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.9) | Đặc tả chính thức câu lệnh if |
| [JEP 361 — Switch Expressions](https://openjdk.org/jeps/361) | Switch expression (Java 14) |
| [JEP 441 — Pattern Matching for switch](https://openjdk.org/jeps/441) | Pattern matching trong switch (Java 21) |
| [Oracle Java Tutorial — Control Flow](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/flow.html) | Hướng dẫn chính thức |
| *Effective Java* — Joshua Bloch | Item 57: Minimize the scope of local variables |
