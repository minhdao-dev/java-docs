# File I/O

## 1. Khái niệm

**File I/O** (Input/Output) là khả năng đọc dữ liệu từ file và ghi dữ liệu vào file trên ổ đĩa. Đây là kỹ năng bắt buộc cho bất kỳ ứng dụng nào cần **lưu trữ dữ liệu lâu dài** — khi tắt chương trình đi, dữ liệu vẫn còn.

Java có hai thế hệ API để làm việc với file:

| | `java.io` (cũ) | `java.nio.file` (mới — nên dùng) |
|---|---|---|
| Phiên bản | Java 1.0 | Java 7+ |
| Core class | `File`, `FileReader`, `FileWriter` | `Path`, `Files` |
| API | Cồng kềnh, nhiều checked exception | Gọn, expressive, dễ đọc |
| Charset mặc định | Platform-dependent | UTF-8 tường minh |
| Tính năng | Cơ bản | Đầy đủ hơn, hỗ trợ atomic ops, symbolic links |

**Bài này tập trung vào `java.nio.file` — API hiện đại, được dùng trong mọi dự án Java mới.**

---

## 2. Tại sao quan trọng

Không có File I/O, dữ liệu chỉ tồn tại trong RAM — mất hết khi tắt chương trình. Mọi ứng dụng thực tế đều cần persistence:

- Ứng dụng console: lưu config, lưu dữ liệu người dùng vào `.txt` / `.csv`
- Backend: đọc file config (`application.yml`), xử lý file upload, export báo cáo
- Log: ghi log ra file để debug sau
- Project Student Grade Manager: lưu danh sách sinh viên vào file, đọc lại khi khởi động

---

## 3. Path — đường dẫn file

`Path` là đại diện cho một đường dẫn file hoặc thư mục. Dùng `Path.of()` (Java 11+) hoặc `Paths.get()` (Java 7+) để tạo:

```java
import java.nio.file.Path;

Path p1 = Path.of("data/students.txt");          // đường dẫn tương đối
Path p2 = Path.of("C:/projects/data/file.txt");  // đường dẫn tuyệt đối (Windows)
Path p3 = Path.of("/home/user/data/file.txt");   // đường dẫn tuyệt đối (Linux/Mac)

// Nối đường dẫn — dùng resolve(), không dùng string concatenation
Path dir  = Path.of("data");
Path file = dir.resolve("students.txt"); // data/students.txt
```

### Các method hữu ích của Path

```java
Path p = Path.of("data/reports/2026/students.txt");

System.out.println(p.getFileName());  // students.txt
System.out.println(p.getParent());    // data/reports/2026
System.out.println(p.toAbsolutePath()); // /home/user/project/data/reports/2026/students.txt
System.out.println(p.toString());     // data/reports/2026/students.txt
```

!!! tip "Luôn dùng Path.of(), không dùng string ghép đường dẫn"
    `Path.of("data", "reports", "file.txt")` tự động dùng dấu phân cách đúng cho từng OS (Windows: `\`, Unix: `/`). Ghép chuỗi thủ công (`"data" + "/" + "file.txt"`) dễ gây lỗi khi chạy trên OS khác.

---

## 4. Files — thao tác với file

`java.nio.file.Files` là utility class chứa toàn bộ các method để đọc, ghi, tạo, xóa, copy file. Tất cả method đều là `static`.

### Kiểm tra tồn tại và tạo thư mục

```java
import java.nio.file.Files;
import java.nio.file.Path;

Path dir  = Path.of("data");
Path file = dir.resolve("students.txt");

// Kiểm tra
System.out.println(Files.exists(file));       // true/false
System.out.println(Files.isDirectory(dir));   // true/false
System.out.println(Files.isRegularFile(file)); // true/false

// Tạo thư mục (tạo cả các thư mục cha nếu chưa có)
Files.createDirectories(dir); // không lỗi nếu đã tồn tại
```

---

## 5. Đọc file

### readString() — đọc toàn bộ file thành một String (Java 11+)

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

Path path = Path.of("data/note.txt");

try {
    String content = Files.readString(path, StandardCharsets.UTF_8);
    System.out.println(content);
} catch (IOException e) {
    System.err.println("Không đọc được file: " + e.getMessage());
}
```

### readAllLines() — đọc từng dòng thành List\<String\>

```java
import java.util.List;

try {
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    for (String line : lines) {
        System.out.println(line);
    }
} catch (IOException e) {
    System.err.println("Lỗi: " + e.getMessage());
}
```

### lines() — Stream\<String\> cho file lớn (lazy loading)

```java
import java.util.stream.Stream;

try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) { // (1)
    stream.filter(line -> !line.isBlank())
          .forEach(System.out::println);
} catch (IOException e) {
    System.err.println("Lỗi: " + e.getMessage());
}
```

1. `Files.lines()` trả về Stream — phải đóng bằng try-with-resources để tránh resource leak. Đây là cách đúng để đọc file lớn vì nó load từng dòng theo yêu cầu, không load toàn bộ vào RAM.

!!! tip "Khi nào dùng cái nào?"
    - File nhỏ (< vài MB): `readString()` hoặc `readAllLines()` — đơn giản nhất
    - File lớn (log, CSV lớn): `Files.lines()` + Stream — lazy, tiết kiệm RAM

---

## 6. Ghi file

### writeString() — ghi String vào file (Java 11+)

```java
import java.nio.file.StandardOpenOption;

Path path = Path.of("data/output.txt");
String content = "Hello, File I/O!";

try {
    // Ghi đè (mặc định) — tạo file nếu chưa có, xóa nội dung cũ nếu có
    Files.writeString(path, content, StandardCharsets.UTF_8);

    // Append — thêm vào cuối file
    Files.writeString(path, "\nDòng mới", StandardCharsets.UTF_8,
                      StandardOpenOption.APPEND);
} catch (IOException e) {
    System.err.println("Không ghi được file: " + e.getMessage());
}
```

### write() — ghi List\<String\> (mỗi phần tử một dòng)

```java
import java.util.List;

List<String> lines = List.of("Dòng 1", "Dòng 2", "Dòng 3");

try {
    Files.write(path, lines, StandardCharsets.UTF_8);
} catch (IOException e) {
    System.err.println("Lỗi: " + e.getMessage());
}
```

### StandardOpenOption — điều khiển cách ghi

```java
// Tạo file mới — lỗi nếu file đã tồn tại
Files.writeString(path, content, StandardOpenOption.CREATE_NEW);

// Ghi đè (mặc định khi không chỉ định)
Files.writeString(path, content, StandardOpenOption.TRUNCATE_EXISTING);

// Thêm vào cuối
Files.writeString(path, content, StandardOpenOption.APPEND);

// Tạo nếu chưa có, ghi đè nếu đã có
Files.writeString(path, content,
    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
```

---

## 7. Copy, Move, Xóa

```java
Path src  = Path.of("data/students.txt");
Path dst  = Path.of("backup/students_backup.txt");

// Copy
Files.copy(src, dst);
// Copy và ghi đè nếu dst đã tồn tại
Files.copy(src, dst, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

// Move (rename hoặc di chuyển)
Files.move(src, Path.of("data/students_old.txt"));

// Xóa
Files.delete(path);               // lỗi nếu file không tồn tại
Files.deleteIfExists(path);       // không lỗi nếu không tồn tại
```

---

## 8. BufferedReader / BufferedWriter — cho file lớn

Khi cần xử lý file lớn với logic phức tạp hơn, dùng `BufferedReader`/`BufferedWriter` kết hợp try-with-resources:

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

// Đọc từng dòng với BufferedReader
Path input = Path.of("data/large-file.txt");

try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
    String line;
    while ((line = reader.readLine()) != null) {
        // xử lý từng dòng
        System.out.println(line);
    }
} catch (IOException e) {
    System.err.println("Lỗi đọc: " + e.getMessage());
}
```

```java
// Ghi từng dòng với BufferedWriter
Path output = Path.of("data/result.txt");

try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
    writer.write("Dòng đầu tiên");
    writer.newLine(); // xuống dòng cross-platform (\r\n hay \n tùy OS)
    writer.write("Dòng thứ hai");
} catch (IOException e) {
    System.err.println("Lỗi ghi: " + e.getMessage());
}
```

---

## 9. Try-with-resources — bắt buộc với I/O

Mọi resource I/O (`BufferedReader`, `BufferedWriter`, `Stream<String>`, ...) **phải được đóng** sau khi dùng để tránh resource leak. Try-with-resources tự động đóng resource ngay cả khi có exception:

```java
// ❌ Không đóng resource — file descriptor leak
BufferedReader reader = Files.newBufferedReader(path);
String content = reader.readLine();
// Nếu có exception ở đây, reader không được đóng

// ✅ Try-with-resources — đảm bảo đóng trong mọi trường hợp
try (BufferedReader reader = Files.newBufferedReader(path)) {
    String content = reader.readLine();
    // reader.close() được gọi tự động khi ra khỏi block này
}

// Nhiều resource — đóng theo thứ tự ngược (writer trước, reader sau)
try (BufferedReader reader = Files.newBufferedReader(src);
     BufferedWriter writer = Files.newBufferedWriter(dst)) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.newLine();
    }
}
```

---

## 10. Code ví dụ đầy đủ — Student Data Persistence

Ví dụ thực tế, liên quan trực tiếp đến project **Student Grade Manager**:

!!! info "Verified"
    Bản đầy đủ có thể compile: [`StudentStorage.java`](https://github.com/minhdao-dev/java-docs/blob/main/examples/src/main/java/fundamentals/fileio/StudentStorage.java)

```java linenums="1"
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StudentStorage {

    private static final Path DATA_FILE = Path.of("data/students.txt");

    public static void main(String[] args) throws IOException {
        // Đảm bảo thư mục tồn tại
        Files.createDirectories(DATA_FILE.getParent());

        // Lưu danh sách sinh viên
        List<String> students = List.of(
            "Nguyen Van A,8.5,9.0,7.5",
            "Tran Thi B,9.0,8.5,9.5",
            "Le Van C,6.5,7.0,7.5"
        );
        saveStudents(students);
        System.out.println("Đã lưu " + students.size() + " sinh viên.");

        // Đọc lại từ file
        List<String[]> loaded = loadStudents();
        System.out.println("\nDanh sách sinh viên:");
        for (String[] parts : loaded) {
            String name   = parts[0];
            double avg    = calcAverage(parts);
            System.out.printf("  %-15s GPA: %.2f%n", name, avg);
        }

        // Thêm một sinh viên mới (append)
        appendStudent("Pham Thi D,8.0,8.0,9.0");
        System.out.println("\nĐã thêm sinh viên mới.");

        // Đọc lại để xác nhận
        System.out.println("Tổng số: " + loadStudents().size() + " sinh viên.");
    }

    static void saveStudents(List<String> records) throws IOException { // (1)
        Files.write(DATA_FILE, records, StandardCharsets.UTF_8);
    }

    static List<String[]> loadStudents() throws IOException { // (2)
        if (!Files.exists(DATA_FILE)) return new ArrayList<>();

        List<String[]> result = new ArrayList<>();
        for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                result.add(line.split(","));
            }
        }
        return result;
    }

    static void appendStudent(String record) throws IOException {
        Files.writeString(DATA_FILE, record + System.lineSeparator(),
                          StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }

    static double calcAverage(String[] parts) {
        double sum = 0;
        for (int i = 1; i < parts.length; i++) {
            sum += Double.parseDouble(parts[i].trim());
        }
        return sum / (parts.length - 1);
    }
}
```

1. `Files.write()` với `List<String>` ghi mỗi phần tử ra một dòng và tự thêm newline. Mặc định ghi đè toàn bộ file.
2. Luôn kiểm tra `Files.exists()` trước khi đọc để tránh `NoSuchFileException` — lần đầu chạy chương trình thì file chưa có.

**Output:**
```
Đã lưu 3 sinh viên.

Danh sách sinh viên:
  Nguyen Van A    GPA: 8.33
  Tran Thi B      GPA: 9.00
  Le Van C        GPA: 7.00

Đã thêm sinh viên mới.
Tổng số: 4 sinh viên.
```

---

## 11. Lỗi thường gặp

### Lỗi 1 — Không chỉ định Charset

```java
// ❌ Charset phụ thuộc OS — chạy trên Windows có thể ra ký tự lạ
Files.readString(path); // dùng Charset mặc định của platform

// ✅ Luôn chỉ định UTF-8 tường minh
Files.readString(path, StandardCharsets.UTF_8);
```

### Lỗi 2 — Quên tạo thư mục cha

```java
Path file = Path.of("data/reports/2026/output.txt");

// ❌ NoSuchFileException nếu thư mục data/reports/2026 chưa tồn tại
Files.writeString(file, "content");

// ✅ Tạo tất cả thư mục cha trước
Files.createDirectories(file.getParent());
Files.writeString(file, "content");
```

### Lỗi 3 — Không đóng Stream từ Files.lines()

```java
// ❌ Resource leak — Stream không được đóng
Stream<String> stream = Files.lines(path);
stream.forEach(System.out::println);
// stream.close() không được gọi nếu có exception

// ✅ Try-with-resources đảm bảo đóng luôn
try (Stream<String> stream = Files.lines(path)) {
    stream.forEach(System.out::println);
}
```

### Lỗi 4 — Dùng string concatenation cho đường dẫn

```java
// ❌ Dùng "/" cứng — sai trên Windows
String path = "data" + "/" + "students.txt";

// ✅ Path.of() tự xử lý dấu phân cách theo OS
Path path = Path.of("data", "students.txt");
```

### Lỗi 5 — Ghi đè file thay vì append

```java
// ❌ Mỗi lần gọi xóa sạch nội dung cũ
Files.writeString(logFile, newEntry); // mất hết log cũ!

// ✅ Dùng APPEND option
Files.writeString(logFile, newEntry, StandardOpenOption.APPEND);
```

---

## 12. Câu hỏi phỏng vấn

**Q1: `java.io.File` khác `java.nio.file.Path` như thế nào?**

> `java.io.File` là API cũ từ Java 1.0 — nhiều method trả về `boolean` thay vì throw exception khi thất bại, không hỗ trợ symbolic link, charset mặc định phụ thuộc OS. `java.nio.file.Path` (Java 7+) xử lý exception đúng cách, hỗ trợ đầy đủ các tính năng của OS hiện đại, và kết hợp với `Files` utility class để có API sạch hơn nhiều. Dự án mới luôn dùng NIO.

**Q2: Tại sao phải dùng UTF-8 tường minh khi đọc/ghi file?**

> Nếu không chỉ định charset, Java dùng charset mặc định của nền tảng (`Charset.defaultCharset()`). Trên Windows thường là `windows-1252`, trên Linux/Mac là `UTF-8`. File được ghi trên Windows đọc trên Linux sẽ bị lỗi ký tự nếu có tiếng Việt hay ký tự đặc biệt. Chỉ định `StandardCharsets.UTF_8` tường minh đảm bảo hành vi nhất quán trên mọi nền tảng.

**Q3: Khi nào dùng `readAllLines()`, khi nào dùng `Files.lines()`?**

> `readAllLines()` load toàn bộ file vào `List<String>` trong RAM — đơn giản và phù hợp với file nhỏ (dưới vài MB). `Files.lines()` trả về `Stream<String>` và đọc lazy từng dòng theo yêu cầu — phù hợp với file lớn (log, CSV hàng triệu dòng) vì không load toàn bộ vào RAM. Quan trọng: `Files.lines()` phải được đóng bằng try-with-resources vì nó giữ file handle.

**Q4: Try-with-resources hoạt động như thế nào với I/O?**

> Try-with-resources (Java 7+) tự động gọi `.close()` trên bất kỳ object nào implements `AutoCloseable` khi ra khỏi block — kể cả khi có exception. Không có try-with-resources, phải viết `finally { reader.close(); }` thủ công và quản lý exception trong `finally` phức tạp. Mọi resource I/O (`BufferedReader`, `BufferedWriter`, `Stream<String>`, ...) đều phải đóng để tránh file descriptor leak.

**Q5: `Files.write()` và `Files.writeString()` khác nhau thế nào?**

> `Files.write(path, List<String>)` nhận `List<String>` và ghi mỗi phần tử ra một dòng, tự thêm newline — tiện khi có sẵn danh sách dòng. `Files.writeString(path, String)` nhận một `String` đơn và ghi nguyên vẹn — tiện khi đã có nội dung đầy đủ hoặc khi xử lý format tự định nghĩa. Cả hai đều hỗ trợ `StandardOpenOption`.

---

## 13. Tài liệu tham khảo

| Tài liệu | Nội dung |
|----------|---------|
| [Oracle Docs — java.nio.file.Files](https://docs.oracle.com/en/java/docs/api/java.base/java/nio/file/Files.html) | Javadoc đầy đủ |
| [Oracle Tutorial — Basic I/O](https://docs.oracle.com/javase/tutorial/essential/io/index.html) | Hướng dẫn chính thức |
| [Baeldung — Java NIO2 File API](https://www.baeldung.com/java-nio-2-file-api) | Bài viết thực hành |
| *Effective Java* — Joshua Bloch | Item 9: Prefer try-with-resources to try-finally |
