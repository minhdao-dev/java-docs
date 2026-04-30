# Method và Pass-by-value

## 1. Khái niệm

**Method** là một khối code được đặt tên, đóng gói một tác vụ cụ thể và có thể tái sử dụng nhiều lần.

```java
public static int add(int a, int b) {
    return a + b;
}
```

- **`public`** — phạm vi truy cập (access modifier)
- **`static`** — gắn với class, gọi không cần tạo object
- **`int`** — kiểu trả về (return type)
- **`add`** — tên method (camelCase)
- **`(int a, int b)`** — danh sách tham số (kiểu + tên mỗi cặp)
- Thân method: `return` gửi kết quả về nơi gọi.

Gọi method:

```java
int result = add(3, 5); // result = 8
```

---

## 2. Tại sao quan trọng

Method là đơn vị cơ bản để **tổ chức code** trong Java:

- **DRY** (Don't Repeat Yourself) — viết logic một lần, gọi nhiều nơi
- **Testability** — mỗi method nhỏ dễ test độc lập
- **Readability** — tên method tốt là tài liệu tự mô tả
- **Pass-by-value** là khái niệm nền tảng để hiểu tại sao code hoạt động như vậy — phỏng vấn hỏi rất nhiều

---

## 3. Khai báo method

### Cú pháp đầy đủ

```java
[access modifier] [static] [return type] methodName([parameters]) {
    // thân method
    [return value;]
}
```

### Return type

```java
// void — không trả về gì
static void printHello() {
    System.out.println("Hello");
}

// Trả về giá trị
static int square(int n) {
    return n * n;
}

// Early return — thoát ngay khi có kết quả
static boolean isEven(int n) {
    return n % 2 == 0; // không cần if/else
}
```

!!! tip "Một method chỉ làm một việc"
    Nếu tên method cần chữ "và" (ví dụ: `validateAndSave`) thì đó là dấu hiệu nên tách thành hai method riêng.

### Không có giá trị trả về mặc định

```java
static int divide(int a, int b) {
    if (b != 0) return a / b;
    // ❌ lỗi compile — không có return ở đây
}

static int divide(int a, int b) {
    if (b != 0) return a / b;
    return 0; // ✅ mọi nhánh đều có return
}
```

---

## 4. Pass-by-value

**Java luôn luôn truyền tham số theo giá trị (pass-by-value).** Không có ngoại lệ.

Điều này có nghĩa là: method nhận một **bản sao** của giá trị được truyền vào — không phải bản thân biến gốc.

![Pass-by-value — primitive vs reference](../../assets/diagrams/pass-by-value.svg)

### Với Primitive — copy giá trị

```java
static void addFive(int n) {
    n += 5; // sửa bản sao, không ảnh hưởng đến biến gốc
}

int x = 10;
addFive(x);
System.out.println(x); // 10 — x không đổi
```

### Với Object / Array — copy địa chỉ (reference)

```java
static void changeFirst(int[] arr) {
    arr[0] = 99; // sửa nội dung object qua địa chỉ — caller thấy được
}

static void reassign(int[] arr) {
    arr = new int[]{7, 8, 9}; // chỉ sửa biến cục bộ — caller KHÔNG thấy
}

int[] scores = {1, 2, 3};
changeFirst(scores);
System.out.println(scores[0]); // 99 — nội dung object đã thay đổi

reassign(scores);
System.out.println(scores[0]); // vẫn 99 — biến scores của caller không đổi
```

!!! warning "\"Pass-by-reference\" là cách nói sai với Java"
    Java truyền **bản sao của địa chỉ**, không truyền bản thân biến. Vì vậy bạn có thể **thay đổi nội dung** của object qua bản sao địa chỉ, nhưng **không thể reassign** biến của caller.

---

## 5. Method Overloading

Cùng tên method, khác danh sách tham số — Java chọn đúng method dựa trên kiểu và số lượng tham số lúc compile.

```java
static int    add(int a, int b)       { return a + b; }
static double add(double a, double b) { return a + b; }
static int    add(int a, int b, int c){ return a + b + c; }

add(1, 2);        // gọi add(int, int)
add(1.0, 2.5);    // gọi add(double, double)
add(1, 2, 3);     // gọi add(int, int, int)
```

!!! warning "Return type không phân biệt overload"
    ```java
    static int  getValue() { return 1; }
    static long getValue() { return 1L; } // ❌ lỗi compile — ambiguous
    ```
    Java chọn overload dựa hoàn toàn vào **danh sách tham số**, không phải return type.

---

## 6. Varargs — tham số có số lượng linh hoạt

Cho phép truyền bất kỳ số lượng argument nào — bên trong method được xử lý như một array.

```java
static int sum(int... numbers) { // numbers là int[]
    int total = 0;
    for (int n : numbers) total += n;
    return total;
}

sum();           // 0
sum(1);          // 1
sum(1, 2, 3);    // 6
sum(1, 2, 3, 4); // 10
```

```java
// Varargs phải là tham số cuối cùng
static String format(String template, Object... args) {
    return String.format(template, args);
}
```

---

## 7. Static và Instance method

```java
public class MathUtils {

    // Static method — không cần object, gọi qua tên class
    public static int abs(int n) {
        return n < 0 ? -n : n;
    }
}

// Gọi static method
int result = MathUtils.abs(-5); // 5
```

```java
public class Counter {
    private int count = 0; // instance field

    // Instance method — cần object, có thể truy cập instance field
    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

Counter c = new Counter();
c.increment();
c.increment();
System.out.println(c.getCount()); // 2
```

| | Static method | Instance method |
| --- | --- | --- |
| Gọi qua | Tên class | Object |
| Truy cập instance field | Không | Có |
| Dùng khi | Utility, không cần state | Cần state của object |

---

## 8. Đệ quy

Method tự gọi lại chính mình. Mọi đệ quy cần hai thành phần: **base case** (điều kiện dừng) và **recursive case** (gọi lại với bài toán nhỏ hơn).

```java
static int factorial(int n) {
    if (n <= 1) return 1;          // base case
    return n * factorial(n - 1);   // recursive case
}
```

### Call stack trace — factorial(4)

Mỗi lần gọi đệ quy, JVM đẩy một **stack frame** mới vào call stack. Khi đạt base case, các frame được pop theo thứ tự LIFO và trả kết quả ngược về:

```
factorial(4)           ← frame 0: n=4, đang chờ factorial(3)
  factorial(3)         ← frame 1: n=3, đang chờ factorial(2)
    factorial(2)       ← frame 2: n=2, đang chờ factorial(1)
      factorial(1)     ← frame 3: n=1, base case → trả về 1

      ← 1              pop frame 3
    ← 2 * 1 = 2        pop frame 2
  ← 3 * 2 = 6          pop frame 1
← 4 * 6 = 24           pop frame 0 → kết quả cuối cùng
```

### Fibonacci — cây đệ quy

```java
static int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2); // mỗi lần gọi tạo 2 nhánh con
}
```

Mỗi lần gọi tạo **hai nhánh** — cây phát triển theo hàm mũ. Các node màu cam là những lần tính lại không cần thiết:

![Fibonacci call tree — duplicate computations](../../assets/diagrams/fibonacci-call-tree.svg)

`fib(30)` ≈ 1 triệu lần gọi. Cách tối ưu (memoization, dynamic programming) sẽ được học ở Phase 2.

### Ví dụ đơn giản — tổng các chữ số

```java
// sumDigits(1234) = 4 + sumDigits(123) = 4 + 3 + sumDigits(12) = ... = 10
static int sumDigits(int n) {
    if (n < 10) return n;                 // base case: còn 1 chữ số
    return n % 10 + sumDigits(n / 10);   // chữ số cuối + phần còn lại
}
```

!!! tip "Khi nào nên dùng đệ quy?"
    Dùng đệ quy khi bài toán **tự nhiên phân rã** thành bài toán con cùng dạng: duyệt cây, binary search, quick sort, merge sort. Nếu có thể dùng vòng lặp — **dùng vòng lặp**.

!!! warning "Đệ quy không có base case → StackOverflowError"
    Mỗi lần gọi đệ quy tốn một stack frame. JVM mặc định giới hạn khoảng 500–1 000 lần gọi lồng nhau. Đệ quy sâu (n > 10 000) nên đổi sang vòng lặp.

---

## 9. Code ví dụ

!!! info "Verified"
    Bản đầy đủ có thể compile: [`MethodsDemo.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/methods/MethodsDemo.java)

```java linenums="1"
import java.util.Arrays;

public class MethodsDemo {

    // Overloading — tìm giá trị lớn nhất
    static int    max(int a, int b)       { return a > b ? a : b; }
    static double max(double a, double b) { return a > b ? a : b; }

    // Varargs — tính trung bình
    static double average(double... nums) { // (1)
        if (nums.length == 0) return 0;
        double sum = 0;
        for (double n : nums) sum += n;
        return sum / nums.length;
    }

    // Pass-by-value: primitive
    static void tryDouble(int n) {
        n *= 2; // chỉ sửa bản sao
    }

    // Pass-by-value: reference — CÓ THỂ thay đổi nội dung
    static void doubleAll(int[] arr) { // (2)
        for (int i = 0; i < arr.length; i++) arr[i] *= 2;
    }

    // Đệ quy
    static int gcd(int a, int b) { // (3)
        return b == 0 ? a : gcd(b, a % b);
    }

    // Early return thay vì nested if
    static String classify(int score) {
        if (score >= 90) return "Xuất sắc";
        if (score >= 75) return "Giỏi";
        if (score >= 50) return "Trung bình";
        return "Yếu";
    }

    public static void main(String[] args) {
        System.out.println(max(3, 7));            // 7
        System.out.println(max(3.5, 2.8));        // 3.5
        System.out.println(average(80, 90, 70));  // 80.0

        int x = 10;
        tryDouble(x);
        System.out.println(x);                    // 10 — không đổi

        int[] arr = {1, 2, 3};
        doubleAll(arr);
        System.out.println(Arrays.toString(arr)); // [2, 4, 6] — đã đổi

        System.out.println(gcd(48, 18));          // 6
        System.out.println(classify(85));         // Giỏi
    }
}
```

1. Varargs `double... nums` — có thể gọi với bất kỳ số lượng argument nào, kể cả 0. Bên trong method nó là một `double[]`.
2. `doubleAll` nhận bản sao của địa chỉ `arr`, nhưng hai bản sao cùng trỏ vào một array trên Heap — nên thay đổi qua `arr[i]` caller đều thấy.
3. Thuật toán Euclid tìm ước chung lớn nhất: đệ quy sạch, dễ đọc, và chỉ cần ~log(min(a,b)) bước.

---

## 10. Lỗi thường gặp

### Lỗi 1 — Nhầm pass-by-value với primitive

```java
static void reset(int n) { n = 0; }

int count = 5;
reset(count);
System.out.println(count); // ❌ kỳ vọng 0 nhưng in ra 5

// ✅ Phải trả về giá trị mới
static int reset() { return 0; }
count = reset();
```

### Lỗi 2 — Nhầm reassign reference bên trong method

```java
static void clear(int[] arr) {
    arr = new int[arr.length]; // ❌ chỉ sửa biến cục bộ, caller không thấy
}

static void clear2(int[] arr) {
    Arrays.fill(arr, 0);        // ✅ sửa nội dung object, caller thấy được
}
```

### Lỗi 3 — Bỏ sót `return` trong một nhánh

```java
static String grade(int score) {
    if (score >= 50) return "Pass";
    // ❌ lỗi compile: missing return — nếu score < 50 thì trả về gì?
}

static String grade(int score) {
    if (score >= 50) return "Pass";
    return "Fail"; // ✅
}
```

### Lỗi 4 — Overload bị mơ hồ

```java
static void print(int n)    { System.out.println("int: " + n); }
static void print(long n)   { System.out.println("long: " + n); }

print(42);   // ✅ gọi print(int)
print(42L);  // ✅ gọi print(long)

// Nhưng:
static void process(int a, double b) { }
static void process(double a, int b) { }
process(1, 2); // ❌ ambiguous — Java không biết chọn cái nào
```

### Lỗi 5 — Đệ quy thiếu base case

```java
static int sum(int n) {
    return n + sum(n - 1); // ❌ không bao giờ dừng → StackOverflowError
}

static int sum(int n) {
    if (n <= 0) return 0;      // ✅ base case
    return n + sum(n - 1);
}
```

---

## 11. Câu hỏi phỏng vấn

**Q1: Java là pass-by-value hay pass-by-reference?**

> Java **luôn là pass-by-value**. Với primitive, giá trị được copy. Với object, **địa chỉ (reference)** được copy — nên có thể thay đổi nội dung object bên trong method, nhưng không thể reassign biến của caller. Nhiều người nhầm gọi đây là "pass-by-reference" nhưng về mặt kỹ thuật là sai.

**Q2: Method overloading khác method overriding như thế nào?**

> **Overloading** (nạp chồng) — cùng tên, khác tham số, xảy ra trong **cùng một class**, được quyết định lúc **compile time**. **Overriding** (ghi đè) — subclass định nghĩa lại method của superclass với cùng signature, được quyết định lúc **runtime** (dynamic dispatch). Đây là nền tảng của Polymorphism — sẽ học ở Phase OOP.

**Q3: Varargs là gì? Có hạn chế gì?**

> Varargs (`Type... name`) cho phép truyền 0 hoặc nhiều argument của cùng kiểu — bên trong method là một array. Hạn chế: (1) chỉ có một varargs per method, (2) phải là tham số cuối cùng, (3) không phân biệt với cách gọi truyền array thẳng, (4) overloading với varargs dễ gây ambiguous.

**Q4: Khi nào dùng static method, khi nào dùng instance method?**

> **Static** khi logic không phụ thuộc vào state của object — utility methods, factory methods, helper functions (ví dụ: `Math.abs()`, `Arrays.sort()`). **Instance** khi logic cần truy cập hoặc thay đổi field của object. Nếu method không dùng bất kỳ instance field nào, đó thường là dấu hiệu nên là static.

**Q5: Tại sao đệ quy có thể gây `StackOverflowError`?**

> Mỗi lần gọi method, JVM đẩy một **stack frame** mới vào call stack — chứa tham số, biến cục bộ, địa chỉ trở về. Call stack có kích thước giới hạn (thường 512KB–1MB). Đệ quy sâu không có base case, hoặc base case sai, sẽ tạo hàng nghìn frame cho đến khi stack tràn. Giải pháp: đổi sang loop, hoặc dùng **tail recursion** (Java không tối ưu tự động, nhưng có thể tự viết dạng iterative bằng stack tường minh).

---

## 12. Tài liệu tham khảo

| Tài liệu | Nội dung |
| --- | --- |
| [JLS §8.4 — Method Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4) | Đặc tả chính thức |
| [Oracle Tutorial — Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html) | Hướng dẫn chính thức |
| [Oracle Tutorial — Passing Info to Methods](https://docs.oracle.com/javase/tutorial/java/javaOO/arguments.html) | Pass-by-value chi tiết |
| *Clean Code* — Robert C. Martin | Chapter 3: Functions — small, do one thing, no side effects |
| *Effective Java* — Joshua Bloch | Item 53: Use varargs judiciously |
