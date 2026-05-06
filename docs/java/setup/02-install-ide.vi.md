# Cài đặt IDE

## 1. IDE là gì

**IDE** (Integrated Development Environment) là môi trường lập trình tích hợp — một phần mềm duy nhất gộp chung:

- **Trình soạn thảo code** với tô màu cú pháp, gợi ý tự động (autocomplete)
- **Trình biên dịch/chạy** tích hợp — nhấn một nút để build và chạy chương trình
- **Debugger** — dừng chương trình tại bất kỳ dòng nào để kiểm tra giá trị biến
- **Quản lý project** — tổ chức file, thư viện, cấu hình build

Viết Java không có IDE là hoàn toàn có thể, nhưng IDE giúp bạn phát hiện lỗi ngay khi gõ (trước khi chạy), tiết kiệm hàng giờ mỗi ngày.

---

## 2. Chọn IDE nào?

| IDE | Phù hợp cho | Miễn phí? |
|-----|-------------|-----------|
| **IntelliJ IDEA Community** | Người mới học và cả lập trình viên chuyên nghiệp | ✓ |
| **Eclipse** | Môi trường doanh nghiệp, quen thuộc với dự án cũ | ✓ |
| **VS Code + Java Extension Pack** | Ai đã dùng VS Code, thích gọn nhẹ | ✓ |
| **Apache NetBeans** | Người mới, sinh viên — giao diện thân thiện, Maven tích hợp sẵn | ✓ |

!!! tip "Khuyến nghị cho người mới"
    Chọn **IntelliJ IDEA Community Edition**. Đây là IDE Java phổ biến nhất, giao diện thân thiện nhất, và hầu hết tài liệu học Java hiện nay đều sử dụng IntelliJ. Bạn có thể cài thêm IDE khác sau khi đã quen.

---

## 3. Cài đặt IntelliJ IDEA Community Edition

=== "Windows"

    ### Bước 1 — Tải IntelliJ IDEA

    1. Mở trình duyệt, vào trang tải:  
       [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Cuộn xuống tìm phần **Community Edition** (phần bên phải, có nhãn "For JVM and Android development").
    3. Nhấn nút **Download** màu đen bên dưới **Community Edition** (không phải Ultimate).
    4. File `.exe` được tải về (~850 MB).

    ### Bước 2 — Chạy installer

    1. Tìm file `.exe` trong thư mục Downloads, nhấn đúp để mở.
    2. Nhấn **Yes** khi được hỏi cho phép thay đổi máy tính.
    3. Màn hình chào mừng → nhấn **Next**.
    4. Chọn thư mục cài đặt (giữ mặc định) → nhấn **Next**.
    5. Màn hình **Installation Options** — tick vào:
        - ✅ **Create Desktop Shortcut** — tạo icon ngoài màn hình
        - ✅ **Add "Open Folder as Project"** — chuột phải vào thư mục để mở trong IntelliJ
        - ✅ **Add launchers dir to the PATH** — chạy IntelliJ từ terminal nếu cần
        - `.java` — có thể tick hoặc không (dùng IntelliJ mở file .java khi nhấn đúp)
    6. Nhấn **Next** → **Install** → chờ 1–2 phút → nhấn **Finish**.

    ### Bước 3 — Mở IntelliJ lần đầu

    1. Nhấn đúp icon **IntelliJ IDEA** trên màn hình (hoặc tìm trong Start Menu).
    2. Màn hình **Import Settings** hỏi muốn nhập cài đặt cũ không — chọn **Do not import settings** → **OK**.
    3. Màn hình **Welcome to IntelliJ IDEA** xuất hiện — đây là màn hình chính.

=== "macOS"

    ### Cách 1 — Tải từ trang JetBrains (khuyến nghị)

    1. Vào [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Cuộn xuống phần **Community Edition**, chọn đúng chip:
        - **Apple Silicon (M1/M2/M3):** chọn `.dmg (Apple Silicon)`
        - **Intel:** chọn `.dmg (Intel)`
    3. Tải file `.dmg` về.
    4. Mở file `.dmg`, kéo icon **IntelliJ IDEA CE** vào thư mục **Applications**.
    5. Mở **Finder → Applications → IntelliJ IDEA CE** để khởi động.

    ### Cách 2 — Cài qua Homebrew

    ```bash
    brew install --cask intellij-idea-ce
    ```

=== "Linux"

    ### Cách 1 — Tải file .tar.gz

    1. Vào [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
    2. Cuộn xuống **Community Edition**, tải file `.tar.gz`.
    3. Giải nén và chạy:

        ```bash
        tar -xzf ideaIC-*.tar.gz -C ~/opt/
        ~/opt/idea-IC-*/bin/idea.sh
        ```

    4. Trong IntelliJ, vào **Tools → Create Desktop Entry** để tạo shortcut trong menu ứng dụng.

    ### Cách 2 — Snap (Ubuntu)

    ```bash
    sudo snap install intellij-idea-community --classic
    ```

### Kiểm tra IntelliJ hoạt động

Sau khi mở IntelliJ IDEA lần đầu:

1. Nhấn **New Project** ở màn hình Welcome.
2. Chọn **Java** ở cột trái, đảm bảo **JDK** hiển thị `21` (nếu không thấy, nhấn vào dropdown và chọn **Add JDK** → trỏ đến thư mục JDK 21).
3. Nhấn **Create** → IntelliJ tạo project mẫu.
4. Mở file `Main.java` trong cây thư mục bên trái → nhấn nút **▶ Run** màu xanh → chương trình chạy và hiển thị kết quả ở cửa sổ dưới.

!!! success "Thành công nếu"
    Cửa sổ **Run** phía dưới hiển thị `Hello, World!` (hoặc output của chương trình mẫu). IntelliJ đã kết nối được với JDK 21.

---

## 4. Cài đặt Eclipse IDE

Eclipse là IDE lâu đời và phổ biến trong môi trường doanh nghiệp Java.

=== "Windows"

    ### Bước 1 — Tải Eclipse Installer

    1. Vào [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)
    2. Nhấn **Download x86_64** để tải **Eclipse Installer** (~70 MB).

    ### Bước 2 — Chạy Eclipse Installer

    1. Mở file `.exe` vừa tải.
    2. Màn hình chọn loại Eclipse xuất hiện → chọn **Eclipse IDE for Java Developers**.
    3. Kiểm tra ô **JVM** — Eclipse sẽ tự phát hiện JDK 21. Nếu không thấy, nhấn vào ô đó và chỉ đến thư mục JDK 21.
    4. Nhấn **Install** → đồng ý License → chờ tải (~400 MB) → nhấn **Launch**.

=== "macOS"

    1. Vào [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/), tải Eclipse Installer cho macOS.
    2. Mở file `.dmg`, chạy installer, chọn **Eclipse IDE for Java Developers**.
    3. Hoặc dùng Homebrew: `brew install --cask eclipse-java`

=== "Linux"

    ```bash
    # Ubuntu/Debian
    sudo snap install eclipse --classic

    # Hoặc tải thủ công từ eclipse.org → giải nén và chạy eclipse/eclipse
    ```

### Tạo project đầu tiên trong Eclipse

1. Mở Eclipse, chọn **Workspace** (thư mục lưu project) → nhấn **Launch**.
2. Vào **File → New → Java Project**.
3. Đặt tên project (ví dụ `HelloWorld`) → đảm bảo **JRE** chọn `JavaSE-21` → nhấn **Finish**.
4. Chuột phải vào thư mục `src` → **New → Class**.
5. Đặt tên `Main`, tick **public static void main(String[] args)** → nhấn **Finish**.
6. Nhấn nút **▶ Run** (hoặc `Ctrl + F11`) để chạy.

---

## 5. Cài đặt VS Code + Java Extension Pack

VS Code là trình soạn thảo gọn nhẹ — không nặng bằng IntelliJ hay Eclipse. Để code Java trong VS Code cần cài thêm bộ extension.

=== "Windows / macOS"

    ### Bước 1 — Cài VS Code

    1. Vào [https://code.visualstudio.com/](https://code.visualstudio.com/), nhấn **Download**.
    2. Chạy installer, làm theo hướng dẫn.

    ### Bước 2 — Cài Java Extension Pack

    1. Mở VS Code.
    2. Nhấn tổ hợp phím `Ctrl + Shift + X` (Windows) hoặc `Cmd + Shift + X` (macOS) để mở **Extensions**.
    3. Gõ vào ô tìm kiếm: `Extension Pack for Java`
    4. Tìm extension của **Microsoft** (có logo màu xanh) → nhấn **Install**.

    Bộ extension này bao gồm 6 thành phần:
    - **Language Support for Java** — tô màu cú pháp, autocomplete, phát hiện lỗi
    - **Debugger for Java** — debug tích hợp
    - **Test Runner for Java** — chạy JUnit test
    - **Maven for Java** — quản lý Maven project
    - **Project Manager for Java** — điều hướng project
    - **IntelliCode** — AI gợi ý code

    ### Bước 3 — Mở project Java

    1. Vào **File → Open Folder**, chọn thư mục project.
    2. Mở file `.java` — VS Code tự nhận diện và kích hoạt Java extension.
    3. Nhấn nút **▶ Run** xuất hiện phía trên hàm `main` để chạy chương trình.

=== "Linux"

    ```bash
    # Ubuntu/Debian — tải .deb từ trang VS Code hoặc:
    sudo snap install code --classic
    ```

    Sau đó cài **Extension Pack for Java** như trên.

!!! warning "VS Code cần JDK đã cài trước"
    VS Code không tự quản lý JDK — nó đọc biến `JAVA_HOME` hoặc tự phát hiện JDK trong `PATH`. Đảm bảo JDK 21 đã cài và `JAVA_HOME` được đặt đúng trước khi mở VS Code.

---

## 6. Cài đặt Apache NetBeans

NetBeans là IDE mã nguồn mở, được phát triển bởi **Apache Software Foundation**. Đây là IDE quen thuộc ở nhiều trường đại học nhờ giao diện trực quan, tích hợp sẵn Maven và không cần cấu hình phức tạp để bắt đầu.

=== "Windows"

    ### Bước 1 — Tải NetBeans

    1. Mở trình duyệt, vào trang tải:  
       [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/)
    2. Chọn phiên bản mới nhất (ví dụ **Apache NetBeans 25**).
    3. Tải file **Windows Installer** (`.exe`, ~600 MB).

    ### Bước 2 — Chạy installer

    1. Mở file `.exe` vừa tải.
    2. Nhấn **Next** ở màn hình chào.
    3. Chọn thư mục cài đặt (giữ mặc định) → **Next**.
    4. Kiểm tra ô **JDK** trỏ đúng đến JDK 21 đã cài → **Next**.
    5. Nhấn **Install** → chờ cài xong → nhấn **Finish**.

    ### Bước 3 — Mở NetBeans lần đầu

    1. Tìm **Apache NetBeans IDE** trong Start Menu hoặc nhấn đúp icon trên màn hình.
    2. Lần đầu khởi động có thể mất 30–60 giây.

=== "macOS"

    ### Cách 1 — Tải file cài đặt

    1. Vào [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/), tải file **macOS Installer** (`.dmg`).
    2. Mở file `.dmg`, kéo **Apache NetBeans** vào thư mục **Applications**.

    ### Cách 2 — Cài qua Homebrew

    ```bash
    brew install --cask netbeans
    ```

=== "Linux"

    ### Cách 1 — Tải file .sh

    1. Vào [https://netbeans.apache.org/front/main/download/](https://netbeans.apache.org/front/main/download/), tải file **Linux Installer** (`.sh`).
    2. Mở terminal, cấp quyền thực thi và chạy:

        ```bash
        chmod +x apache-netbeans-*.sh
        ./apache-netbeans-*.sh
        ```

    3. Làm theo hướng dẫn trên màn hình.

    ### Cách 2 — Snap (Ubuntu)

    ```bash
    sudo snap install netbeans --classic
    ```

### Tạo project đầu tiên trong NetBeans

1. Nhấn **New Project** (hoặc **File → New Project**).
2. Chọn **Java with Maven → Java Application** → nhấn **Next**.
3. Điền tên project (ví dụ `HelloWorld`) → nhấn **Finish**.
4. NetBeans tạo file `App.java` trong thư mục `src`.
5. Nhấn nút **▶ Run Project** (hoặc phím `F6`) để chạy.

!!! success "Thành công nếu"
    Cửa sổ **Output** phía dưới hiển thị output của chương trình. NetBeans đã kết nối với JDK 21.

---

## 7. So sánh nhanh

| Tính năng | IntelliJ IDEA CE | Eclipse | VS Code | NetBeans |
|-----------|-----------------|---------|---------|---------|
| Tốc độ khởi động | Chậm hơn | Trung bình | Nhanh | Trung bình |
| RAM sử dụng | ~500–800 MB | ~400–600 MB | ~200–400 MB | ~300–500 MB |
| Autocomplete thông minh | ★★★★★ | ★★★ | ★★★★ | ★★★★ |
| Tích hợp Maven/Gradle | Xuất sắc | Tốt | Tốt | Tốt (Maven sẵn) |
| Phổ biến trong cộng đồng | Rất cao | Cao (doanh nghiệp) | Cao (frontend) | Trung bình (học thuật) |
| Plugin/Extension | Phong phú | Phong phú | Phong phú | Trung bình |

---

## 8. Phím tắt cần nhớ ngay

=== "IntelliJ IDEA"

    | Tác dụng | Windows / Linux | macOS |
    |----------|-----------------|-------|
    | Chạy chương trình | `Shift + F10` | `Ctrl + R` |
    | Debug chương trình | `Shift + F9` | `Ctrl + D` |
    | Gợi ý autocomplete | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment dòng | `Ctrl + /` | `Cmd + /` |
    | Tìm kiếm mọi thứ | `Shift Shift` | `Shift Shift` |
    | Fix lỗi nhanh (quick fix) | `Alt + Enter` | `Option + Enter` |
    | Format code tự động | `Ctrl + Alt + L` | `Cmd + Option + L` |

=== "Eclipse"

    | Tác dụng | Windows / Linux | macOS |
    |----------|-----------------|-------|
    | Chạy chương trình | `Ctrl + F11` | `Cmd + F11` |
    | Debug chương trình | `F11` | `F11` |
    | Gợi ý autocomplete | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment dòng | `Ctrl + /` | `Cmd + /` |
    | Format code tự động | `Ctrl + Shift + F` | `Cmd + Shift + F` |
    | Tự động import | `Ctrl + Shift + O` | `Cmd + Shift + O` |

=== "VS Code"

    | Tác dụng | Windows / Linux | macOS |
    |----------|-----------------|-------|
    | Chạy/Debug chương trình | `F5` | `F5` |
    | Gợi ý autocomplete | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment dòng | `Ctrl + /` | `Cmd + /` |
    | Format code tự động | `Shift + Alt + F` | `Shift + Option + F` |
    | Mở Command Palette | `Ctrl + Shift + P` | `Cmd + Shift + P` |

=== "NetBeans"

    | Tác dụng | Windows / Linux | macOS |
    |----------|-----------------|-------|
    | Chạy project | `F6` | `F6` |
    | Debug project | `F5` | `F5` |
    | Gợi ý autocomplete | `Ctrl + Space` | `Ctrl + Space` |
    | Comment/uncomment dòng | `Ctrl + /` | `Cmd + /` |
    | Format code tự động | `Alt + Shift + F` | `Option + Shift + F` |
    | Fix import tự động | `Ctrl + Shift + I` | `Cmd + Shift + I` |
    | Build project | `F11` | `F11` |

---

## 9. Xử lý lỗi thường gặp

### IntelliJ không nhận ra JDK

**Triệu chứng:** IntelliJ báo "No JDK configured" hoặc code Java bị gạch đỏ toàn bộ.

**Cách sửa:**
1. Vào **File → Project Structure** (`Ctrl + Alt + Shift + S`).
2. Chọn **Project** ở cột trái.
3. Ở ô **SDK**, nhấn **Add SDK → JDK** → trỏ đến thư mục cài JDK 21.

### VS Code không tìm thấy Java

**Triệu chứng:** Báo "Java runtime could not be located".

**Cách sửa:**  
Mở VS Code Settings (`Ctrl + ,`), tìm `java.jdt.ls.java.home`, đặt giá trị là đường dẫn đến JDK 21 (ví dụ: `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot`).

### Eclipse chạy rất chậm (Windows)

**Nguyên nhân:** Eclipse cần thêm bộ nhớ.

**Cách sửa:** Mở file `eclipse.ini` trong thư mục cài Eclipse, tìm dòng `-Xmx` và tăng lên `-Xmx2g` (tối đa 2 GB RAM).

### NetBeans không nhận ra JDK

**Triệu chứng:** NetBeans báo "No Java Platform defined" hoặc không thể build project.

**Cách sửa:**
1. Vào **Tools → Java Platforms**.
2. Nhấn **Add Platform** → chọn **Java Standard Edition** → **Next**.
3. Trỏ đến thư mục JDK 21 → **Next** → **Finish**.

### NetBeans khởi động rất chậm

**Nguyên nhân:** Bộ nhớ mặc định của NetBeans khá thấp.

**Cách sửa:** Mở file `netbeans.conf` trong thư mục cài đặt (thường ở `C:\Program Files\NetBeans-25\netbeans\etc\`), tìm dòng `netbeans_default_options` và thêm `-J-Xmx2g` vào cuối chuỗi options.

---

Sau khi IDE đã hoạt động ổn định, bước tiếp theo là [Terminal cơ bản](03-terminal-basics.md) — học cách thao tác với dòng lệnh để chạy Java mà không cần IDE.
