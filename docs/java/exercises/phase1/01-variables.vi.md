# 01. Biến, Kiểu dữ liệu, Toán tử

Bài tập thực hành cho các bài lý thuyết:
[Kiểu dữ liệu và Biến](../../java/fundamentals/02-variables-datatypes.md) ·
[Toán tử](../../java/fundamentals/03-operators.md)

---

## Bài 1 — Dự đoán output: String Pool · 🟢 Dễ

Cho đoạn code sau, dự đoán output của từng câu lệnh `println` mà **không chạy chương trình**:

```java
String a = "hello";
String b = "hello";
String c = new String("hello");

System.out.println(a == b);       // (1)
System.out.println(a == c);       // (2)
System.out.println(a.equals(c));  // (3)
System.out.println(c.equals(b));  // (4)
```

??? tip "Gợi ý"
    - `==` so sánh **địa chỉ** trên Stack, không phải nội dung
    - String literal dùng **String Pool** — cùng nội dung → cùng object
    - `new String(...)` tạo object mới trên Heap, bỏ qua Pool
    - `.equals()` so sánh **nội dung** ký tự

??? success "Lời giải"
    | Dòng | Output | Giải thích |
    |------|--------|-----------|
    | (1) | `true` | `a` và `b` cùng trỏ vào một object trong String Pool |
    | (2) | `false` | `c` là object riêng trên Heap — địa chỉ khác `a` |
    | (3) | `true` | `.equals()` so sánh nội dung — đều là `"hello"` |
    | (4) | `true` | `.equals()` so sánh nội dung — đều là `"hello"` |

---

## Bài 2 — Sửa lỗi compile · 🟢 Dễ

Đoạn code dưới đây có **3 lỗi compile**. Tìm và sửa tất cả:

```java
public class Main {
    public static void main(String[] args) {
        var total;                       // (A)
        total = 100.5;

        long population = 8_000_000_000; // (B)

        float price = 19.99;             // (C)

        System.out.println(total + " / " + population + " / " + price);
    }
}
```

??? tip "Gợi ý"
    - `var` yêu cầu gì ở phía phải?
    - Literal số nguyên lớn hơn `int` cần hậu tố gì?
    - Literal số thực mặc định là kiểu gì trong Java?

??? success "Lời giải"
    | Vị trí | Lỗi | Sửa |
    |--------|-----|-----|
    | (A) | `var` không thể khai báo thiếu initializer — compiler không có gì để suy kiểu | Thay bằng `var total = 100.5;` hoặc `double total;` |
    | (B) | `8_000_000_000` vượt quá giới hạn `int` — compiler đọc nó là `int` trước khi gán | Thêm hậu tố `L`: `8_000_000_000L` |
    | (C) | `19.99` là `double` literal — gán vào `float` bị mất độ chính xác, compiler từ chối | Thêm hậu tố `f`: `19.99f`, hoặc đổi kiểu thành `double` |

    ```java
    public class Main {
        public static void main(String[] args) {
            var total = 100.5;

            long population = 8_000_000_000L;

            float price = 19.99f;

            System.out.println(total + " / " + population + " / " + price);
        }
    }
    ```

---

## Bài 3 — Viết code: Student Card · 🟡 Trung bình

Viết class `StudentCard` với method `main` thực hiện các yêu cầu sau:

1. Khai báo các biến thể hiện thông tin sinh viên:
    - Họ tên (`String`), Tuổi (`int`), GPA (`double`), Đang học (`boolean`), Mã sinh viên (`long`)
2. Gán dữ liệu mẫu.
3. In ra màn hình theo đúng định dạng:

    ```
    ╔══════════════════════════╗
    │      STUDENT CARD        │
    ╠══════════════════════════╣
    │ Name   : Nguyen Van An   │
    │ Age    : 20              │
    │ GPA    : 3.75            │
    │ Status : Enrolled        │
    │ ID     : 20240001        │
    ╚══════════════════════════╝
    ```

4. Dùng `var` cho ít nhất 2 biến.

??? tip "Gợi ý"
    - `System.out.printf("%-16s", value)` — căn trái, đủ 16 ký tự
    - `boolean` → `"Enrolled"` / `"Not Enrolled"` dùng ternary: `isEnrolled ? "Enrolled" : "Not Enrolled"`

??? success "Lời giải"
    ```java
    public class StudentCard {
        public static void main(String[] args) {
            var name           = "Nguyen Van An";
            int age            = 20;
            var gpa            = 3.75;
            boolean isEnrolled = true;
            long studentId     = 20240001L;

            String status = isEnrolled ? "Enrolled" : "Not Enrolled";

            System.out.println("╔══════════════════════════╗");
            System.out.println("│      STUDENT CARD        │");
            System.out.println("╠══════════════════════════╣");
            System.out.printf( "│ Name   : %-16s│%n", name);
            System.out.printf( "│ Age    : %-16d│%n", age);
            System.out.printf( "│ GPA    : %-16.2f│%n", gpa);
            System.out.printf( "│ Status : %-16s│%n", status);
            System.out.printf( "│ ID     : %-16d│%n", studentId);
            System.out.println("╚══════════════════════════╝");
        }
    }
    ```
