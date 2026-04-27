# Terminal cơ bản

## 1. Terminal là gì và tại sao cần học

**Terminal** (còn gọi là command line, command prompt, shell) là cách bạn ra lệnh cho máy tính bằng văn bản thay vì nhấn chuột vào icon.

Với lập trình viên Java, terminal cần thiết vì:

- Chạy và biên dịch Java mà không cần mở IDE
- Dùng Maven / Gradle để build dự án
- Chạy server, kiểm tra log, quản lý tiến trình
- Dùng Git để quản lý code

Bạn không cần ghi nhớ hàng trăm lệnh. Bài này tập trung vào **10 lệnh dùng nhiều nhất** mà bạn sẽ gõ hàng ngày.

---

## 2. Mở terminal

=== "Windows"

    Windows có hai terminal chính:

    **Command Prompt (CMD)** — terminal cổ điển:
    - Nhấn `Windows + R`, gõ `cmd`, nhấn Enter.
    - Hoặc: tìm kiếm "Command Prompt" trong Start Menu.

    **PowerShell** — terminal hiện đại hơn, dùng được hầu hết lệnh Unix:
    - Nhấn `Windows + X`, chọn **Windows PowerShell** hoặc **Terminal**.
    - Hoặc: tìm "PowerShell" trong Start Menu.

    !!! tip "Khuyến nghị: dùng Windows Terminal"
        Cài **Windows Terminal** từ Microsoft Store — hỗ trợ tab, giao diện đẹp, và tích hợp cả CMD lẫn PowerShell. Tìm "Windows Terminal" trong Microsoft Store và cài miễn phí.

=== "macOS"

    **Terminal** tích hợp sẵn:
    - Nhấn `Cmd + Space`, gõ `Terminal`, nhấn Enter.
    - Hoặc: **Finder → Applications → Utilities → Terminal**.

    Shell mặc định trên macOS là **Zsh** (từ macOS Catalina trở lên).

    !!! tip "iTerm2 — terminal tốt hơn cho macOS"
        [iTerm2](https://iterm2.com/) cung cấp nhiều tính năng hơn Terminal mặc định: tìm kiếm, split pane, màu sắc phong phú. Tải miễn phí tại iterm2.com.

=== "Linux"

    - **Ubuntu:** `Ctrl + Alt + T`
    - **GNOME:** tìm "Terminal" trong launcher
    - **KDE:** tìm "Konsole"

---

## 3. Hiểu dấu nhắc lệnh (prompt)

Khi mở terminal, bạn sẽ thấy một dòng tương tự:

=== "Windows CMD"

    ```
    C:\Users\TenBan>_
    ```

    - `C:\Users\TenBan` — thư mục hiện tại bạn đang đứng
    - `>` — dấu nhắc, chờ bạn gõ lệnh
    - `_` — con trỏ

=== "macOS / Linux"

    ```
    tenbandung@MacBook ~ %_
    ```

    - `tenbandung` — tên người dùng
    - `MacBook` — tên máy
    - `~` — thư mục home (viết tắt của `/Users/TenBan` trên macOS)
    - `%` hoặc `$` — dấu nhắc

---

## 4. Điều hướng thư mục

### Xem thư mục hiện tại

=== "Windows CMD"

    ```cmd
    cd
    ```

=== "macOS / Linux / PowerShell"

    ```bash
    pwd
    ```

    Kết quả ví dụ: `/Users/tenbandung` — đây là thư mục bạn đang đứng.

---

### Liệt kê file và thư mục

=== "Windows CMD"

    ```cmd
    dir
    ```

=== "macOS / Linux / PowerShell"

    ```bash
    ls
    ```

    Xem chi tiết hơn (kích thước, quyền, ngày tạo):

    ```bash
    ls -l
    ```

    Xem cả file ẩn:

    ```bash
    ls -la
    ```

---

### Di chuyển vào thư mục khác

```bash
cd ten-thu-muc
```

Ví dụ — vào thư mục `Documents`:

```bash
cd Documents
```

Vào thư mục lồng nhau:

```bash
cd Documents/java-projects
```

Di chuyển lên thư mục cha (lên một cấp):

```bash
cd ..
```

Về thư mục home:

=== "macOS / Linux"

    ```bash
    cd ~
    ```

=== "Windows CMD"

    ```cmd
    cd %USERPROFILE%
    ```

---

### Đường dẫn tuyệt đối và tương đối

| Loại | Ví dụ | Ý nghĩa |
|------|-------|---------|
| **Tuyệt đối** | `cd /Users/tenbandung/Documents` | Bắt đầu từ gốc hệ thống, không phụ thuộc vị trí hiện tại |
| **Tương đối** | `cd Documents` | Tính từ thư mục hiện tại |

---

## 5. Quản lý file và thư mục

### Tạo thư mục mới

```bash
mkdir ten-thu-muc
```

Tạo nhiều cấp cùng lúc:

=== "macOS / Linux"

    ```bash
    mkdir -p projects/java/hello-world
    ```

=== "Windows CMD"

    ```cmd
    mkdir projects\java\hello-world
    ```

---

### Xóa file

=== "macOS / Linux"

    ```bash
    rm ten-file.txt
    ```

    Xóa thư mục và toàn bộ nội dung bên trong:

    ```bash
    rm -rf ten-thu-muc/
    ```

    !!! warning "Cẩn thận với `rm -rf`"
        Lệnh này xóa vĩnh viễn, không vào Thùng rác. Kiểm tra kỹ tên thư mục trước khi chạy.

=== "Windows CMD"

    ```cmd
    del ten-file.txt
    rmdir /s /q ten-thu-muc
    ```

---

### Copy và di chuyển file

=== "macOS / Linux"

    ```bash
    cp nguon.txt dich.txt          # copy file
    cp -r thu-muc-nguon/ thu-muc-dich/  # copy thư mục
    mv nguon.txt dich.txt          # di chuyển (hoặc đổi tên)
    ```

=== "Windows CMD"

    ```cmd
    copy nguon.txt dich.txt
    xcopy thu-muc-nguon thu-muc-dich /E /I
    move nguon.txt dich.txt
    ```

---

## 6. Xem nội dung file

=== "macOS / Linux"

    ```bash
    cat ten-file.txt        # in toàn bộ nội dung
    head -20 ten-file.txt   # 20 dòng đầu
    tail -20 ten-file.txt   # 20 dòng cuối
    ```

=== "Windows CMD"

    ```cmd
    type ten-file.txt
    ```

---

## 7. Biên dịch và chạy Java từ terminal

Đây là phần quan trọng nhất của bài này.

### Cấu trúc thư mục ví dụ

```
hello-java/
└── HelloWorld.java
```

### Tạo file Java

Tạo thư mục và file:

```bash
mkdir hello-java
cd hello-java
```

Dùng trình soạn thảo để tạo file `HelloWorld.java` với nội dung:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

!!! note "Tên file phải khớp với tên class"
    File có tên `HelloWorld.java` phải chứa `public class HelloWorld`. Nếu không khớp, `javac` sẽ báo lỗi.

### Biên dịch

```bash
javac HelloWorld.java
```

Nếu không có lỗi, lệnh này tạo ra file `HelloWorld.class` trong cùng thư mục.

### Chạy

```bash
java HelloWorld
```

!!! warning "Không thêm `.class` khi chạy"
    Lệnh đúng là `java HelloWorld`, không phải `java HelloWorld.class`.

Kết quả:

```
Hello, World!
```

### Quy trình tóm gọn

```
HelloWorld.java  →  javac  →  HelloWorld.class  →  java  →  Output
(source code)       (biên dịch)  (bytecode)          (chạy)
```

---

## 8. Lệnh hữu ích khác

### Xóa màn hình

=== "macOS / Linux"

    ```bash
    clear
    ```

=== "Windows CMD"

    ```cmd
    cls
    ```

### Xem lịch sử lệnh vừa gõ

=== "macOS / Linux"

    ```bash
    history
    ```

Nhấn phím **↑** để lặp lại lệnh trước đó — không cần gõ lại.

### Dừng chương trình đang chạy

Nhấn `Ctrl + C` để dừng bất kỳ lệnh nào đang thực thi (rất hữu ích khi chạy server).

### Tìm kiếm trong file

=== "macOS / Linux"

    ```bash
    grep "tu-can-tim" ten-file.txt
    grep -r "tu-can-tim" ten-thu-muc/   # tìm đệ quy trong thư mục
    ```

=== "Windows CMD"

    ```cmd
    findstr "tu-can-tim" ten-file.txt
    ```

---

## 9. Bảng tổng hợp lệnh thiết yếu

| Lệnh | Windows CMD | macOS / Linux |
|------|------------|---------------|
| Xem thư mục hiện tại | `cd` | `pwd` |
| Liệt kê nội dung | `dir` | `ls` |
| Vào thư mục | `cd ten-thu-muc` | `cd ten-thu-muc` |
| Lên thư mục cha | `cd ..` | `cd ..` |
| Tạo thư mục | `mkdir ten` | `mkdir ten` |
| Xóa file | `del file.txt` | `rm file.txt` |
| Xóa thư mục | `rmdir /s /q ten` | `rm -rf ten/` |
| Copy file | `copy a.txt b.txt` | `cp a.txt b.txt` |
| Di chuyển/đổi tên | `move a.txt b.txt` | `mv a.txt b.txt` |
| Xem nội dung file | `type file.txt` | `cat file.txt` |
| Xóa màn hình | `cls` | `clear` |
| Dừng chương trình | `Ctrl + C` | `Ctrl + C` |
| Biên dịch Java | `javac File.java` | `javac File.java` |
| Chạy Java | `java ClassName` | `java ClassName` |

---

## 10. Mẹo dùng terminal hiệu quả

- **Tab để tự hoàn thành:** gõ vài chữ đầu tên file/thư mục rồi nhấn `Tab` — terminal tự điền phần còn lại.
- **↑ ↓ để duyệt lịch sử:** không cần gõ lại lệnh cũ.
- **Ctrl + C để thoát:** bất kỳ lệnh nào bị kẹt hay chạy lâu — nhấn `Ctrl + C` để dừng ngay.
- **Ctrl + L để xóa màn hình:** thay thế cho `clear` / `cls`.
- **Drag & drop đường dẫn:** kéo file/thư mục từ Finder (macOS) hoặc Explorer (Windows) thả vào terminal — terminal tự điền đường dẫn đầy đủ.

---

Tiếp theo: [Git và GitHub](04-git-github.md) — học cách lưu lịch sử code và làm việc nhóm.
